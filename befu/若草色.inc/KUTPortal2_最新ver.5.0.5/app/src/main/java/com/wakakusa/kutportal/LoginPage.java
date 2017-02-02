package com.wakakusa.kutportal;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class LoginPage extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    /*
     * ログイン画面の処理を行うクラス
     *
     * IDとパスワードの入力
     * 受信データの保存
     * トップ画面への遷移を行う
     */


    static EFlag actflag1;
    static EditText userName;
    static EditText password;
    static String name;
    static String pass;


    Intent intent = new Intent();
    private URI uri;
    private AsyncTaskLoader<JSONObject> mAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //LoadingPageを閉じる
        LoadingPage.actFlag.setFlagState(true);
        super.onCreate(savedInstanceState);
        //Login画面の生成
        setContentView(R.layout.activity_login_page);
        actflag1 = new EFlag();
        userName = (EditText) findViewById(R.id.userNameEdit);
        password = (EditText) findViewById(R.id.PasswordEdit);
    }

    @Override
    protected void onPause() {
        name = userName.getText().toString();
        pass = password.getText().toString();
        //すでに起動していた場合に終了する
        if (mAsyncTask != null) mAsyncTask.cancelLoad();
        super.onPause();
    }

    public void onClick(View view) throws Exception {
        //ログインボタンの処理
        name = userName.getText().toString();
        pass = password.getText().toString();
        uri = new URI("http://wakakusa.info.kochi-tech.ac.jp");
        if (mAsyncTask != null) mAsyncTask.cancelLoad();
        getLoaderManager().restartLoader(1, null, this);
    }


    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        try {
            //JsonLoader2を実行
            String urlText = "https://wakakusa.info.kochi-tech.ac.jp/test/main.php";
            com.wakakusa.kutportal.JsonLoader2 jsonLoader = new com.wakakusa.kutportal.JsonLoader2(this, urlText);
            jsonLoader.forceLoad();
            return jsonLoader;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }


    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
        //JsonLoader2終了時の処理
        final String[] tableName = {"student", "course", "score", "test", "news"};
        DatabaseWriter[] dbWriter = new DatabaseWriter[5];
        for (int i = 0; i < 5; i++) dbWriter[i] = new DatabaseWriter(this, tableName[i]);
        if (data != null) {
            JSONArray[] tableData = new JSONArray[5];

            try {
                //オフライン時の判定に使用するオブジェクト
                JSONObject object = new JSONObject();
                object.put("check", "offline");
                JSONObject object2 = new JSONObject();
                object2.put("check", "firstlogin");
                if (data.toString().equals(object.toString())) {
                    Toast.makeText(LoginPage.this, "オフラインです", Toast.LENGTH_LONG).show();
                } else if (data.toString().equals(object2.toString())) {

                    Toast.makeText(LoginPage.this, "ユーザ名かパスワードが違います", Toast.LENGTH_LONG).show();

                } else {
                    //データを保存する処理
                    for (int i = 0; i < 5; i++) dbWriter[i].deleteDB();

                    for (int i = 0; i < 5; i++)
                        tableData[i] = data.getJSONArray(tableName[i]);

                    for (int j = 0; j < 5; j++) {
                        for (int i = 0; i < tableData[j].length(); i++) {
                            JSONObject jsonObject = tableData[j].getJSONObject(i);
                            dbWriter[j].writeDB(jsonObject);
                        }
                    }

                    //ログイン成功時にトップ画面に遷移する処理
                    intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            } catch (JSONException e) {
                //e.printStackTrace();
                intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        } else {
            //IDとパスワードが違う場合
            Toast.makeText(LoginPage.this, "ユーザ名かパスワードが違います", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {

    }
    @Override
    public void onRestart(){
        //Backボタン(画面左下のボタン)を押した時にこの画面を表示しないようにする
        super.onRestart();
        this.finish();
    }
}


class JsonLoader2 extends AsyncTaskLoader<JSONObject> {
    /*
 *サーバと通信を行うクラス
 *ID・パスワード・更新日時・tokenID送信
 *Cookie取得・保存・削除などを行う
 */
    private String urlText;

    JSONObject object;
    JSONObject object2;
    private Context mContext;
    private CookieStore store;
    private CookieManager manager;
    private Map map = new HashMap();
    private HttpCookie cookie;

    public MyCookieStore cookiestore;
    private ByteArrayOutputStream outputStream=null;
    private long cookietime;
    private URI uri;
    private JSONObject json;
    private HttpsURLConnection response;
    private String limit;

    public JsonLoader2(Context context, String urlText)throws Exception {
        super(context);
        //アプリを終了した際にCookieが消えてしまうので非推奨クラスを使用
        this.urlText = urlText;
        mContext = context;
        uri = new URI("http://wakakusa.info.kochi-tech.ac.jp");
        cookiestore =new MyCookieStore(mContext);
        manager = new CookieManager();
        buildCookieManager();
        CookieSyncManager.createInstance(mContext);
    }
    private void buildCookieManager() {
        manager = new CookieManager();
        CookieHandler.setDefault(manager);


    }
    public void showCookie() {
        //Cookieを取得する処理
        store = manager.getCookieStore();


        List<HttpCookie> cookies = store.getCookies();


        List<URI> cookieuri = store.getURIs();


        cookie = cookies.get(0);

        uri = cookieuri.get(0);

        cookietime = cookie.getMaxAge();

        store.add(uri, cookie);
        store.get(uri);
    }

    @Override
    public JSONObject loadInBackground() {

        TrustManagerFactory trustManager;
        BufferedInputStream inputStream = null;
        response =null;

        byte[] buffer = new byte[1024];
        int length;
        map.put("display", "環境");
        try {
            URL url = new URL(urlText);

            object = new JSONObject();
            object.put("check", "offline");
            object2 =new JSONObject();
            object2.put("check", "firstlogin");
            String USERNAME;
            String PASSWORD;


            DatabaseWriter dbWriter = new DatabaseWriter(mContext, "loginData");
            DatabaseReader dbReader = new DatabaseReader(mContext, "loginData");

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();

            String[] str1 = {"limittime"};
            String[] str2 = {"realtime"};
            String[] str3 = {"ara"};
            String[] str4 = {"tokenID"};
            limit = dbReader.readDB(str1, 0);
            String real;

            String ara;
            String sessionID = dbReader.readDB(str4,0);

            limit = dbReader.readDB(str1, 0);
            real = dbReader.readDB(str2, 0);
            ara = dbReader.readDB(str3, 0);

            //assetsディレクトリ内にあるサーバの証明書を取得する処理
            KeyStore ks = KeyStoreUtil.getEmptyKeyStore();
            KeyStoreUtil.loadX509Certificate(ks,
                    mContext.getResources().getAssets().open("server.crt"));
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    if (!hostname.equals(session.getPeerHost())) {
                        return false;
                    }
                    return true;
                }
            });
            trustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManager.init(ks);
            SSLContext sslCon = SSLContext.getInstance("TLS");
            sslCon.init(null, trustManager.getTrustManagers(), new SecureRandom());

            //通信を行っている処理
            response = (HttpsURLConnection)url.openConnection();
            response.setConnectTimeout(30000);
            response.setReadTimeout(30000);
            response.setChunkedStreamingMode(0);
            response.setDefaultSSLSocketFactory(sslCon.getSocketFactory());
            response.setSSLSocketFactory(sslCon.getSocketFactory());
            response.setUseCaches(false);
            response.setChunkedStreamingMode(0);
            response.setRequestMethod("POST");
            response.setInstanceFollowRedirects(true);
            response.setRequestProperty("Accept-Language", "jp");
            response.setRequestProperty("Connection", "close");
            response.setDoInput(true);
            response.setDoOutput(true);
            response.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream());
            BufferedWriter bw = new BufferedWriter(osw);

            Iterator<Object> it = map.keySet().iterator();
            String key = null;
            Object value = null;
            String data = "";
            while (it.hasNext()) {
                key = it.next().toString();
                value = map.get(key);
                if (!data.equals("")) {
                    data += "&";
                }
                String valueS = value.toString();

                data += key + "=" + URLEncoder.encode(valueS, "utf-8");
            }
            String str = "&tokenID=" + URLEncoder.encode(sessionID, "utf-8");

            USERNAME = LoginPage.name;
            PASSWORD = LoginPage.pass;
            String sendData = "userid=" + URLEncoder.encode(USERNAME, "utf-8") +
                    "&password=" + URLEncoder.encode(PASSWORD, "utf-8") +
                    "&submit=" + URLEncoder.encode("login", "utf-8")+
                    "&reday=" + URLEncoder.encode(real.substring(0, 14), "utf-8");
            //ID・パスワード・更新日時・tokenIDを送信する処理
            bw.write(sendData + data+str);
            bw.close();
            osw.close();
            bw.close();
            osw.close();
            checkResponse(response);
            //通信先のデータを取得する処理
            inputStream = new BufferedInputStream(response.getInputStream());
            outputStream = new ByteArrayOutputStream();
            while ((length = inputStream.read(buffer)) != -1) {
                if (length > 0) {

                    outputStream.write(buffer, 0, length);
                    outputStream.close();
                }
            }

            showCookie();
            //更新日時とCookieとCookieの使用期限を保存する処理
            cal.add(Calendar.SECOND, (int) cookietime);
            dbWriter.update("realtime", "realtime", real.substring(0, 14), sdf1.format(date));
            dbWriter.update("limittime", "limittime", limit.substring(0, 14), sdf1.format(cal.getTime()));
            limit = dbReader.readDB(str1, 0);
            cookiestore.add(uri, cookie);

            String j =new String(outputStream.toByteArray());
            json = new JSONObject(j);
            outputStream.close();
            inputStream.close();
            response.disconnect();
            return json;

        } catch (SecurityException e) {

            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException exception) {
            return object;
        } catch (JSONException e) {
            e.printStackTrace();


        }
        catch (IndexOutOfBoundsException exception) {
            return object2;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    private void checkResponse(HttpURLConnection response) throws IOException {
        //通信が成功しているか確認する処理
        int statusCode = response.getResponseCode();
        if (HttpURLConnection.HTTP_OK != statusCode) {
            throw new IOException("HttpStatus: " + statusCode);
        }

    }


}
