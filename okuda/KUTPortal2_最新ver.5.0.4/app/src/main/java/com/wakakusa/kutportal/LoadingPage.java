package com.wakakusa.kutportal;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieSyncManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/*
 *  起動画面クラス
 *  自動ログインか手動ログインの判定を行い、データを取得するクラス
 *  自動ログイン処理
 */


public class LoadingPage extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    Intent intent = new Intent();
    public static boolean b;
    public static boolean a;
    public static int help;

    public static EFlag actFlag;
    public static EFlag actFlag2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_loader);
        actFlag =new EFlag();
        actFlag2=new EFlag();
        help =0;
        a=false;
        b = true;
        getLoaderManager().restartLoader(1, null, this);
    }
    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        try {
            //JsonLoaderを実行
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            String urlText = "https://wakakusa.info.kochi-tech.ac.jp/test/main.php";
            JsonLoader jsonLoader = new JsonLoader(this, urlText);
            jsonLoader.forceLoad();
            return jsonLoader;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
        //JsonLoader終了時の処理
        final String[] tableName = {"student", "course", "score", "test", "news"};
        DatabaseWriter[] dbWriter = new DatabaseWriter[5];
        for (int i = 0; i < 5; i++) dbWriter[i] = new DatabaseWriter(this, tableName[i]);

        if (data != null) {
            JSONArray[] tableData = new JSONArray[5];
            try {

                JSONObject object = new JSONObject();
                object.put("check", "offline");
                if (data.toString().equals(object.toString())) {
                    //オフライン時の処理（トップ画面への遷移と更新ボタンの使用不可）
                    b=false;
                    Intent intent2 = new Intent();
                    intent2.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                    startActivity(intent2);
                    overridePendingTransition(0, 0);
                }
                for (int i = 0; i < 5; i++)
                    tableData[i] = data.getJSONArray(tableName[i]);
                //新規データを保存する処理
                if (!tableData[0].toString().equals("[]") || !tableData[2].toString().equals("[]") || !tableData[3].toString().equals("[]") && !tableData[4].toString().equals("[]")) {
                    for (int j = 1; j < 5; j++) {
                        for (int i = 1; i < tableData[j].length(); i++) {
                           if (!tableData[i].toString().equals("[]")) {
                            JSONObject jsonObject = tableData[j].getJSONObject(i);
                            dbWriter[j].writeDB(jsonObject);
                            }
                        }
                    }
                    //ログイン成功時トップ画面に遷移
                    intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                } else {
                    for (int j = 0; j < 5; j++) {
                        for (int i = 0; i < tableData[j].length(); i++) {
                            JSONObject jsonObject = tableData[j].getJSONObject(i);
                            //もしもともと同じidの値が入っていれば消す
                            String ext = jsonObject.getString(dbWriter[j].property[0]);
                            dbWriter[j].deleteDB2(dbWriter[j].property[0], ext);
                            //格納する
                            dbWriter[j].writeDB(jsonObject);
                        }
                    }
                    intent = getIntent();
                    //更新ボタンを押したかどうか判定する
                    if (intent.getStringExtra("name").equals("update")) {
                        System.out.println("koko");
                        actFlag.setFlagState(true);
                        Intent update = new Intent();

                        update.setClassName("com.wakakusa.kutportal", intent.getStringExtra("ClassName"));
                        update.addFlags(update.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(update);
                        overridePendingTransition(0, 0);

                    } else {
                        intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                        startActivity(intent);
                        overridePendingTransition(0, 0);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                startActivity(intent);
                overridePendingTransition(0, 0);

            } catch (NullPointerException e) {
                //自動ログインで更新処理に飛んだ場合判定でNullPointerExceptionが発生するのでここで保存する
                intent = new Intent();
                intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                try {
                    for (int j = 1; j < 5; j++) {

                        for (int i = 1; i < tableData[j].length(); i++) {
                                JSONObject jsonObject = tableData[j].getJSONObject(i);
                                String ext = jsonObject.getString(dbWriter[j].property[0]);
                                dbWriter[j].deleteDB2(dbWriter[j].property[0], ext);

                                dbWriter[j].writeDB(jsonObject);
                        }
                    }
                    startActivity(intent);

                } catch (JSONException ex) {

                    ex.printStackTrace();
                    intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                }
            }
        }
        else {
            intent =new Intent();
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
        if(this.actFlag.getFlagState() == true){
            this.finish();
            overridePendingTransition(0, 0);
        }
    }


}
@SuppressWarnings("deprecation")
class JsonLoader extends AsyncTaskLoader<JSONObject> {
    /*
*サーバと通信を行うクラス
*Cookie・更新日時・tokenID送信
*Cookie取得・保存・削除などを行う
*/
    private String urlText;
    JSONObject object;
    private Context mContext;
    private CookieStore store;
    private static CookieManager manager;
    private Map map = new HashMap();
    private HttpCookie cookie;
    public MyCookieStore cookiestore;
    private ByteArrayOutputStream outputStream=null;
    private long cookietime;
    private URI uri;
    private JSONObject json;
    private HttpsURLConnection response;
    private String limit;
    public static int help;

    public JsonLoader(Context context, String urlText)throws Exception {
        super(context);
        //アプリを終了した際にCookieが消えてしまうので非推奨クラスを使用
        this.urlText = urlText;
        mContext = context;
        URL url = new URL(urlText);
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
        int checkpoint = 0;
        map.put("display", "環境");
        try {
            URL url = new URL(urlText);
            object = new JSONObject();
            object.put("check", "offline");
            DatabaseWriter dbWriter = new DatabaseWriter(mContext, "loginData");
            DatabaseReader dbReader = new DatabaseReader(mContext, "loginData");
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();

            String[] str1 = {"limittime"};
            String[] str2 = {"realtime"};
            String[] str3 = {"ara"};
            String[] str4 = {"tokenID"};
            String ara = dbReader.readDB(str3, 0);
            String sessionID = dbReader.readDB(str4, 0);
            ContentValues cvalue = new ContentValues();

            if(ara.length()==0){
                cvalue.put("ara", "option");
                cvalue.put("limittime", "00000000000001");
                cvalue.put("realtime", "00000000000000");
                cvalue.put("response","1111");
                dbWriter.write.insert(dbWriter.Table_name, null, cvalue);
            }

            String limit = dbReader.readDB(str1, 0);
            String real = dbReader.readDB(str2, 0);
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
            //通信の設定を決定
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

            String[] limit2 =limit.split("\n",0);
            String[] ara2= ara.split("\n",0);
            help=0;
            if (ara2[0].equals("option")) {
                dbWriter.update("ara", "ara", ara.substring(0, 6), "true");
                help=1;
                ara = dbReader.readDB(str3, 0);
                ara2= ara.split("\n",0);
            }
            //Cookieをヘッダーに設定する処理
            if (!limit2[0].equals("00000000000001") && ara2[0].equals("true")) {

                response.setRequestProperty("Cookie", cookiestore.get(uri).get(0).toString());
            }
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

            String[] token=sessionID.split("\n",0);
            String str = "&tokenID=" + URLEncoder.encode(token[0],"utf-8");

            //Cookieの削除判定
            if (Long.parseLong(sdf1.format(date)) >= Long.parseLong(limit2[0]) && !limit2[0].equals(("00000000000001"))) {

                cookiestore.remove(uri, cookiestore.get(uri).get(0));
                dbWriter.update("realtime", "realtime", real.substring(0, 14), "00000000000000");
                dbWriter.update("limittime", "limittime", limit.substring(0, 14), "00000000000001");

                return null;
            }

            //通信処理
            if (ara2[0].equals("true") && !limit2[0].equals("00000000000001")) {

                String reday = "&reday=" + URLEncoder.encode(real.substring(0, 14), "utf-8");
                response.connect();

                bw.write(data+reday+str);
                bw.close();
                osw.close();
                checkpoint = 2;


            }

            bw.close();
            osw.close();

            checkResponse(response);

            inputStream = new BufferedInputStream(response.getInputStream());
            outputStream = new ByteArrayOutputStream();

            while ((length = inputStream.read(buffer)) != -1) {
                if (length > 0) {
                    outputStream.write(buffer, 0, length);
                    outputStream.close();
                }
            }

            if (checkpoint == 2) {
                showCookie();
            } else {
                return null;
            }
            //更新日時とCookieとCookieの使用期限を保存する処理
            cal.add(Calendar.SECOND, (int) cookietime);
            dbWriter.update("realtime", "realtime", real.substring(0, 14), sdf1.format(date));
            dbWriter.update("limittime", "limittime", limit.substring(0, 14), sdf1.format(cal.getTime()));

            cookiestore.add(uri, cookie);
            json = new JSONObject(new String(outputStream.toByteArray()));
            outputStream.close();
            inputStream.close();
            response.disconnect();

            return json;

        } catch (SecurityException e) {

            e.printStackTrace();
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            String str[] = {"limittime"};
            DatabaseReader dbReader = new DatabaseReader(mContext, "loginData");
            limit = dbReader.readDB(str, 0);


            if(limit.split("\n", 0)[0].equals("00000000000001")){return null;}
            return object;
        } catch (JSONException e) {

            e.printStackTrace();
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            return null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    //通信が成功しているかどうか判定する
    private void checkResponse(HttpURLConnection response) throws IOException {
        int statusCode = response.getResponseCode();
        if (HttpURLConnection.HTTP_OK != statusCode) {
            throw new IOException("HttpStatus: " + statusCode);
        }

    }


}


class KeyStoreUtil {
    //assets内の証明書を取得するクラス
    public static KeyStore getEmptyKeyStore() throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance("BKS");
        ks.load(null);
        return ks;

    }
    public static void loadX509Certificate(KeyStore ks,InputStream is)throws CertificateException,KeyStoreException{
        try{
            CertificateFactory factory=
                    CertificateFactory.getInstance("X509");
            X509Certificate x509 = (X509Certificate) factory.generateCertificate(is);
            String alias  = x509.getSubjectDN().getName();
            ks.setCertificateEntry(alias,x509);

        }finally{
            try {
                is.close();
            }
            catch (IOException e) { e.printStackTrace(); }
        }
    }
}
class MyCookieStore implements CookieStore{

 /*
    *Cookieの保存、削除を行うクラス
    */

    private Map<URI, List<HttpCookie>> mapCookies = new HashMap<URI, List<HttpCookie>>();


    private final SharedPreferences spePreferences;

    //Cookieを保存するメソッド
    public void add(URI uri, HttpCookie cookie) {

        List<HttpCookie> cookies = mapCookies.get(uri);
       cookies = new ArrayList<HttpCookie>();
        mapCookies.put(uri, cookies);
        cookies.add(cookie);

        SharedPreferences.Editor ediWriter = spePreferences.edit();
        HashSet<String> setCookies = new HashSet<String>();
        setCookies.add(cookie.toString());
        ediWriter.putStringSet(uri.toString(), setCookies);
        ediWriter.apply();


    }




    @SuppressWarnings("unchecked")
    public MyCookieStore(Context ctxContext) {

        spePreferences = ctxContext.getSharedPreferences("CookiePrefsFile", 0);
        Map<String, ?> prefsMap = spePreferences.getAll();


        for(Map.Entry<String, ?> entry : prefsMap.entrySet()) {

            for (String strCookie : (HashSet<String>) entry.getValue()) {


                if (!mapCookies.containsKey(entry.getKey())) {

                    List<HttpCookie> lstCookies = new ArrayList<HttpCookie>();
                    lstCookies.addAll(HttpCookie.parse(strCookie));

                    try {

                        mapCookies.put(new URI(entry.getKey()), lstCookies);

                    } catch (URISyntaxException e) {

                        e.printStackTrace();

                    }

                } else {

                    List<HttpCookie> lstCookies = mapCookies.get(entry.getKey());
                    lstCookies.addAll(HttpCookie.parse(strCookie));

                    try {

                        mapCookies.put(new URI(entry.getKey()), lstCookies);

                    } catch (URISyntaxException e) {

                        e.printStackTrace();

                    }

                }
            }

        }

    }
    //保存されたCookieを一つ取得するメソッド
    public List<HttpCookie> get(URI uri) {

        List<HttpCookie> lstCookies = mapCookies.get(uri);


        if (lstCookies == null )
            mapCookies.put(uri, new ArrayList<HttpCookie>());

        return mapCookies.get(uri);

    }
    //保存したCookieを全て削除するメソッド
    public boolean removeAll() {

        mapCookies.clear();
        return true;

    }

    //保存されたCookieを全て取得するメソッド
    public List<HttpCookie> getCookies() {

        Collection<List<HttpCookie>> values = mapCookies.values();

        List<HttpCookie> result = new ArrayList<HttpCookie>();
        for (List<HttpCookie> value : values) {
            result.addAll(value);
        }

        return result;

    }

    //保存されたCookieのURIを取得するメソッド
    public List<URI> getURIs() {

        Set<URI> keys = mapCookies.keySet();
        return new ArrayList<URI>(keys);

    }

    //保存されたCookieを指定して削除するメソッド
    public boolean remove(URI uri, HttpCookie cookie) {

        List<HttpCookie> lstCookies = mapCookies.get(uri);
        if (lstCookies == null && cookie.getMaxAge()!=0)
            return false;

        return lstCookies.remove(cookie);

    }

}

