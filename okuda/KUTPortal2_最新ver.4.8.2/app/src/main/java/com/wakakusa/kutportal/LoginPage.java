package com.wakakusa.kutportal;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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


    //ユーザ名とパスワードを入力するための変数
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
        LoadingPage.actFlag.setFlagState(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        //テキストエディットの宣言
        actflag1 = new EFlag();
        userName = (EditText) findViewById(R.id.userNameEdit);
        password = (EditText) findViewById(R.id.PasswordEdit);

        // button = (Button)findViewById(R.id.loginButton);

    }

    @Override
    protected void onPause() {

        // このあとActivityが破棄される可能性があるので非同期処理をキャンセルしておく
        String url = "https://wakakusa.info.kochi-tech.ac.jp/test/main.php";

        name = userName.getText().toString();
        pass = password.getText().toString();


        if (mAsyncTask != null) mAsyncTask.cancelLoad();
        super.onPause();
    }

    public void onClick(View view) throws Exception {
        //URL,ユーザ名パスワードの設定
        String url = "https://wakakusa.info.kochi-tech.ac.jp/test/main.php";
        name = userName.getText().toString();
        pass = password.getText().toString();
        uri = new URI("http://wakakusa.info.kochi-tech.ac.jp");


        // 直前の非同期処理が終わってないこともあるのでキャンセルしておく
        if (mAsyncTask != null) mAsyncTask.cancelLoad();
        // UIスレッドで通信してはならないので、AsyncTaskによりワーカースレッドで通信する
        getLoaderManager().restartLoader(1, null, this);
    }

    //intent.setClassName("com.wakakusa.kutportal","com.wakakusa.kutportal.LoadingPage");
    //startActivity(intent);
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {


        try {

            //String urlText = "http://animemap.net/api/table/tokyo.json";
            //String urlText = "http://192.168.33.15:8000/posts.json";
            String urlText = "https://wakakusa.info.kochi-tech.ac.jp/test/main.php";
            //String urlText = "http://weather.livedoor.com/forecast/webservice/json/v1?city=270000";
            //String urlText = "http://www.ajaxtower.jp/googlemaps/gdownloadurl/data.json";

            com.wakakusa.kutportal.JsonLoader2 jsonLoader = new com.wakakusa.kutportal.JsonLoader2(this, urlText);
            jsonLoader.forceLoad();
            return jsonLoader;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }


    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {


        final String[] tableName = {"student", "course", "score", "test", "news"};

        DatabaseWriter[] dbWriter = new DatabaseWriter[5];
        for (int i = 0; i < 5; i++) dbWriter[i] = new DatabaseWriter(this, tableName[i]);


        if (data != null) { //dataがnull？の状態
            System.out.println(data.toString());
            JSONArray[] tableData = new JSONArray[5];

            try {

                JSONObject object = new JSONObject();
                object.put("check", "offline");
                JSONObject object2 = new JSONObject();
                object2.put("check", "firstlogin");
                System.out.println(object2.toString() + object.toString());
                if (data.toString().equals(object.toString())) {

                    intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.UserPage");
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    System.out.println("usodaro");

                } else if (data.toString().equals(object2.toString())) {
                    System.out.println("onegai");
                    Toast.makeText(LoginPage.this, "ユーザ名かパスワードが違います", Toast.LENGTH_LONG).show();

                } else {


                    for (int i = 0; i < 5; i++) dbWriter[i].deleteDB();
                    //tableData[0から4]でそれぞれテーブルデータをとってくる
                    for (int i = 0; i < 5; i++)
                        tableData[i] = data.getJSONArray(tableName[i]);


                    //データを格納する
                    for (int j = 0; j < 5; j++) {
                        for (int i = 0; i < tableData[j].length(); i++) {
                            System.out.println(tableData[j].toString());
                            JSONObject jsonObject = tableData[j].getJSONObject(i);
                            dbWriter[j].writeDB(jsonObject);
                        }
                    }



                    intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                    startActivity(intent);
                    overridePendingTransition(0, 0);


                    //moveTaskToBack(true);

                }

                //textView.setText(dbReader[0].readDB(dbWriter[0].property,0)); //データの入力

            } catch (JSONException e) {
                Log.d("onLoadFinished", "JSONのパースに失敗しました。 JSONException=" + e);
                intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        } else {
            //nullが返されているから、これがログとして出力されている？

            Log.d("onLoadFinished", "onLoadFinished error!");

            intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.LoginPage");
            startActivity(intent);
            overridePendingTransition(0,0);

        }

    }

    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {
        // 処理なし
    }
    @Override
    public void onRestart(){
        super.onRestart();


        this.finish();


    }
}

class JsonLoader2 extends AsyncTaskLoader<JSONObject> {
    private String urlText;

    JSONObject object;
    JSONObject object2;
    private Context mContext;
    private CookieStore store;
    private CookieManager manager;
    private Map map = new HashMap();
    private HttpCookie cookie;
    //private CookieManager cookieManager =CookieManager.getInstance();
    public MyCookieStore cookiestore;
    private ByteArrayOutputStream outputStream=null;
    private long cookietime;
    private URI uri;
    private JSONObject json;
    private HttpsURLConnection response;
    private String limit;

    public JsonLoader2(Context context, String urlText)throws Exception {
        super(context);
        this.urlText = urlText;
        mContext = context;
        URL url = new URL(urlText);


        uri = new URI("http://wakakusa.info.kochi-tech.ac.jp");

        cookiestore =new MyCookieStore(mContext);

        manager = new CookieManager();
//以下をコメントにするとセッションは働きません
        buildCookieManager();

        CookieSyncManager.createInstance(mContext);
    }
    private void buildCookieManager() {

        manager = new CookieManager();
        CookieHandler.setDefault(manager);


    }
    public void showCookie() {

        store = manager.getCookieStore();


        List<HttpCookie> cookies = store.getCookies();


        List<URI> cookieuri = store.getURIs();


        cookie = cookies.get(0);

        uri = cookieuri.get(0);
        System.out.println(cookieuri.get(0));

        cookietime = cookie.getMaxAge();

        store.add(uri, cookie);
        store.get(uri);

        System.out.println(cookie);
        Log.i("lightbox", "Cookies[" + 0 + "]: " + cookie + "/" + cookietime);


    }

    @Override
    public JSONObject loadInBackground() {
        //HttpURLConnection connection = null;
        TrustManagerFactory trustManager;
        BufferedInputStream inputStream = null;
        response =null;

        byte[] buffer = new byte[1024];
        int length;
        int checkpoint = 0;
        map.put("display", "環境");
        try {
            URL url = new URL(urlText);




            object = new JSONObject();
            object.put("check", "offline");
            object2 =new JSONObject();
            object2.put("check", "firstlogin");



            String USERNAME;
            String PASSWORD;

            String Cookiedata = null;
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
            String real = dbReader.readDB(str2, 0);

            String ara = dbReader.readDB(str3, 0);
            String sessionID = dbReader.readDB(str4,0);

            System.out.println("Check ara:" + ara);
            System.out.println("Check real:" + real);
            System.out.println("Check limit:" + limit);
            System.out.println(ara.length());


            limit = dbReader.readDB(str1, 0);
            real = dbReader.readDB(str2, 0);
            ara = dbReader.readDB(str3, 0);
            System.out.println(ara.length());
            System.out.println(limit + ":" + real + ":" + ara);
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

            // ★ポイント2★ URIはhttps://で始める
            // ★ポイント3★ 送信データにセンシティブな情報を含めてよい
            trustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManager.init(ks);
            SSLContext sslCon = SSLContext.getInstance("TLS");
            sslCon.init(null, trustManager.getTrustManagers(), new SecureRandom());


            //↓Basic認証
            response = (HttpsURLConnection)url.openConnection();
            response.setConnectTimeout(30000);
            response.setReadTimeout(30000);
            response.setChunkedStreamingMode(0);

            response.setDefaultSSLSocketFactory(sslCon.getSocketFactory());
            response.setSSLSocketFactory(sslCon.getSocketFactory());

            //response.connect();
            //ここまで
            response.setUseCaches(false);
            response.setChunkedStreamingMode(0);
            response.setRequestMethod("POST");
            response.setInstanceFollowRedirects(true);
            response.setRequestProperty("Accept-Language", "jp");
            //response.setRequestProperty("Connection", "Keep-Alive");


            //System.setProperty("http.keepAlive", "false");

            Log.i("OSA030", "setConnection -> close");
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
            Object cookiesave;
            while (it.hasNext()) {
                key = it.next().toString();
                value = map.get(key);
                if (!data.equals("")) {
                    data += "&";
                }
                //String型 <- Object型
                String valueS = value.toString();

                data += key + "=" + URLEncoder.encode(valueS, "utf-8");
            }

            Log.i("lightbox", "送信内容 : " + data);



            //時間の保存
            String str = "&tokenID=" + URLEncoder.encode(sessionID, "utf-8");
            System.out.println(sessionID);





            //ara = dbReader.readDB(str3,0);
            /*

            System.out.println(ara);
            System.out.println(ara.substring(0,4));
            System.out.println(checkpoint);
            */

            System.out.println(ara);



            USERNAME = LoginPage.name;
            PASSWORD = LoginPage.pass;

            String sendData = "userid=" + URLEncoder.encode(USERNAME, "utf-8") +
                    "&password=" + URLEncoder.encode(PASSWORD, "utf-8") +
                    "&submit=" + URLEncoder.encode("login", "utf-8")+
                    "&reday=" + URLEncoder.encode(real.substring(0, 14), "utf-8");
            bw.write(sendData + data+str);
            bw.close();
            osw.close();
            System.out.println(sendData + data);
            System.out.println("normal Login");

            checkpoint = 3;

            bw.close();

            osw.close();



            checkResponse(response);    //エラーをキャッチする

            inputStream = new BufferedInputStream(response.getInputStream());
            outputStream = new ByteArrayOutputStream();
            //byte[] buffer = new byte[1024];


            //int length;
            while ((length = inputStream.read(buffer)) != -1) {
                if (length > 0) {

                    outputStream.write(buffer, 0, length);
                    outputStream.close();




                }
            }







            System.out.println(checkpoint);
            if (checkpoint == 3) {

                showCookie();


                System.out.println("get cookie");

                System.out.println("umakuitte");
            } else {
                System.out.println("Don't get cookie");


                return object2;
            }

            System.out.println("date:" + sdf1.format(date));
            //cal.set(Time[0], Time[1], Time[2], Time[3], Time[4], Time[5]);
            cal.add(Calendar.SECOND, (int) cookietime);
            //System.out.println(sdf1.format(cal.getTime()));
            System.out.println("cal2:" + sdf1.format(cal.getTime()));
            //データベース書き込み


            dbWriter.update("realtime", "realtime", real.substring(0, 14), sdf1.format(date));
            dbWriter.update("limittime", "limittime", limit.substring(0, 14), sdf1.format(cal.getTime()));
            limit = dbReader.readDB(str1, 0);
            real = dbReader.readDB(str2, 0);
            System.out.println(limit + ":" + real);
            cookiestore.add(uri, cookie);
            System.out.println(cookiestore.get(uri));
            //ここら辺で止まってる？
            System.out.println(new String(outputStream.toByteArray()));
            String j =new String(outputStream.toByteArray());
            json = new JSONObject(j);

            System.out.println(json.toString());
            outputStream.close();
            inputStream.close();
            response.disconnect();
            System.out.println("login 完了");

            return json;

        } catch (SecurityException e) {
            System.out.println("1check Exception");

            e.printStackTrace();
        } catch (MalformedURLException exception) {
            // 処理なし
            System.out.println("2check Exception");

        } catch (IOException exception) {
            System.out.println("3check Exception");
            if(limit.substring(0, 14).equals("00000000000001")){
                return null;
            }

            return object;

            // 処理なし

        } catch (JSONException e) {
            if(checkpoint==3){



            }
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
        int statusCode = response.getResponseCode();
        if (HttpURLConnection.HTTP_OK != statusCode) {
            throw new IOException("HttpStatus: " + statusCode);
        }

    }


}
