package com.wakakusa.kutportal;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieSyncManager;
import android.widget.TextView;
import android.os.Build;



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
import java.lang.Exception;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;


public class LoadingPage extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    Intent intent = new Intent();
    public static boolean b;
    public static boolean a;

    public static EFlag actFlag;
    public static EFlag actFlag2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_loader);
        actFlag =new EFlag();
        actFlag2=new EFlag();
        a=false;
        b = true;
        // TextViewを取得
        // JSONの取得
        getLoaderManager().restartLoader(1, null, this);
    }
    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        try {
            //String urlText = "http://animemap.net/api/table/tokyo.json";
            //String urlText = "http://192.168.33.15:8000/posts.json";
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            String urlText = "https://wakakusa.info.kochi-tech.ac.jp/test/main.php";
            //String urlText = "http://weather.livedoor.com/forecast/webservice/json/v1?city=270000";
            //String urlText = "http://www.ajaxtower.jp/googlemaps/gdownloadurl/data.json";

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
        final String[] tableName = {"student", "course", "score", "test", "news"};
        //intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.LoadingPage");
        //intent.putExtra("name","no");

        DatabaseWriter[] dbWriter = new DatabaseWriter[5];
        for (int i = 0; i < 5; i++) dbWriter[i] = new DatabaseWriter(this, tableName[i]);
        //データベースの削除
        if (data != null) { //dataがnull？の状態
            JSONArray[] tableData = new JSONArray[5];
            try {

                JSONObject object = new JSONObject();
                object.put("check", "offline");
                if (data.toString().equals(object.toString())) {
                    System.out.println("offline");
                    b=false;
                    Intent intent2 = new Intent();
                    intent2.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                    //intent2.putExtra("name","offline");
                    startActivity(intent2);
                    overridePendingTransition(0, 0);
                    ActivityStack.stackHistory(this);
                    System.out.println("usodaro");
                }
                for (int i = 0; i < 5; i++)
                    tableData[i] = data.getJSONArray(tableName[i]);


                if (!tableData[0].toString().equals("[]") || !tableData[2].toString().equals("[]") || !tableData[3].toString().equals("[]") && !tableData[4].toString().equals("[]")) {

                    System.out.println(tableData[0].get(0).toString());
                    //dbWriter[0].deleteDB();
                    //tableData[0から4]でそれぞれテーブルデータをとってくる

                    for (int j = 1; j < 5; j++) {
                        for (int i = 1; i < tableData[j].length(); i++) {
                            //空の時入れない
                           /* if (!tableData[i].toString().equals("[]")) {
                           */
                            JSONObject jsonObject = tableData[j].getJSONObject(i);
                            dbWriter[j].writeDB(jsonObject);
                            /*}*/
                        }
                    }
                    //データを格納する
                    intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    ActivityStack.stackHistory(this);
                    //moveTaskToBack(true);
                } else {
                    System.out.println("uwaaaaaa");


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
                    if (intent.getStringExtra("name").equals("update")) {
                        System.out.println("koko");
                        actFlag.setFlagState(true);
                        finish();
                        overridePendingTransition(0, 0);
                        ActivityStack.stackHistory(this);
                    } else {
                        intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        ActivityStack.stackHistory(this);
                    }
                }
                //textView.setText(dbReader[0].readDB(dbWriter[0].property,0)); //データの入力
            } catch (JSONException e) {
                Log.d("onLoadFinished", "JSONのパースに失敗しました。 JSONException=" + e);
                intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                startActivity(intent);
                overridePendingTransition(0, 0);
                ActivityStack.stackHistory(this);
            } catch (NullPointerException e) {
                System.out.println("rr!?");
                intent = new Intent();
                intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");


                try {
                    for (int j = 1; j < 5; j++) {

                        for (int i = 1; i < tableData[j].length(); i++) {
                            //空の時入れない
                            if (!tableData[i].toString().equals("[]")) {
                                JSONObject jsonObject = tableData[j].getJSONObject(i);
                                //もしもともと同じidの値が入っていれば消す
                                String ext = jsonObject.getString(dbWriter[j].property[0]);
                                dbWriter[j].deleteDB2(dbWriter[j].property[0], ext);
                                //格納する
                                dbWriter[j].writeDB(jsonObject);
                            }
                        }
                    }
                    startActivity(intent);

                } catch (JSONException ex) {

                    Log.d("onLoadFinished", "JSONのパースに失敗しました。 JSONException=" + e);
                    intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    ActivityStack.stackHistory(this);
                }
            }
        }
        else {
            //nullが返されているから、これがログとして出力されている？
            Log.d("onLoadFinished", "onLoadFinished error!");
            intent =new Intent();
            intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.LoginPage");
            startActivity(intent);
            overridePendingTransition(0,0);
            ActivityStack.stackHistory(this);
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
            //this.moveTaskToBack(true);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            overridePendingTransition(0,0);
            return true;

        }
        return false;
    }

}
@SuppressWarnings("deprecation")
class JsonLoader extends AsyncTaskLoader<JSONObject> {
    private String urlText;
    JSONObject object;
    private Context mContext;
    private CookieStore store;
    private static CookieManager manager;
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
    public JsonLoader(Context context, String urlText)throws Exception {
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

            String USERNAME;
            String PASSWORD;

            String Cookiedata = null;
            DatabaseWriter dbWriter = new DatabaseWriter(mContext, "loginData");
            DatabaseReader dbReader = new DatabaseReader(mContext, "loginData");

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();

            //long in = Long.parseLong(t);

            //dbWriter.deleteDB();
            String[] str1 = {"limittime"};
            String[] str2 = {"realtime"};
            String[] str3 = {"ara"};
            String[] str4 = {"sessionID"};


            limit = dbReader.readDB(str1, 0);
            String real = dbReader.readDB(str2, 0);

            String ara = dbReader.readDB(str3, 0);
            String sessionID = dbReader.readDB(str4,0);

            System.out.println("Check ara:" + ara);
            System.out.println("Check real:" + real);
            System.out.println("Check limit:" + limit);
            System.out.println(ara.length());
            ContentValues cvalue = new ContentValues();
            if (ara.length() == 0) {
                cvalue.put("ara", "null");
                cvalue.put("limittime", "00000000000001");
                cvalue.put("realtime", "00000000000000");
                cvalue.put("response","1111");

                dbWriter.write.insert(dbWriter.Table_name, null, cvalue);
            }
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
            if (!limit.substring(0, 14).equals("00000000000001") && ara.substring(0, 4).equals("true")) {
                System.out.println("setCookie"+cookiestore.get(uri).get(0).toString());
                response.setRequestProperty("Cookie", cookiestore.get(uri).get(0).toString());
            }
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
            String str = "&token=" + URLEncoder.encode(sessionID, "utf-8");
            System.out.println(sessionID);


            if (Long.parseLong(sdf1.format(date)) >= Long.parseLong(limit.substring(0, 14)) && !limit.substring(0, 14).equals(("00000000000001"))) {
                System.out.println("b");
                cookiestore.remove(uri, cookiestore.get(uri).get(0));
                dbWriter.update("realtime", "realtime", real.substring(0, 14), "00000000000000");
                dbWriter.update("limittime", "limittime", limit.substring(0, 14), "00000000000001");

                return null;
            }


            if (ara.substring(0, 4).equals("null")) {
                System.out.println("c");
                dbWriter.update("ara", "ara", "null", "true");
                System.out.println("update database");

                checkpoint = 1;
            } else if (ara.substring(0, 4).equals("true") && !limit.substring(0, 14).equals("00000000000001")) {
                // ホスト名の検証を行う
                String[] cookie_v;
                cookie_v = cookiestore.get(uri).get(0).toString().split("=", 3);
                System.out.println(cookiestore.get(uri).get(0));
                Cookiedata = "reday=" + URLEncoder.encode(real.substring(0, 14), "utf-8");
                //Cookiedata = "reday=" + URLEncoder.encode("20170116180000", "utf-8");
                //Cookiedata = "a=1&"+cookiestore.get(uri).get(0);
                System.out.println(Cookiedata);

                response.connect();

                bw.write(Cookiedata + data);
                bw.close();
                osw.close();

                System.out.println("auto Login");
                checkpoint = 2;


            }
            //ara = dbReader.readDB(str3,0);
            /*

            System.out.println(ara);
            System.out.println(ara.substring(0,4));
            System.out.println(checkpoint);
            */

            System.out.println(ara);
            if (checkpoint == 0 && ara.substring(0, 4).equals("true")) {
                checkpoint = 4;
                System.out.println("move normal login");
            }


            if (ara.equals("false") || checkpoint == 4) {

                LoginPage.actflag1.setFlagState(true);
                USERNAME = LoginPage.name;
                PASSWORD = LoginPage.pass;

                String sendData = "userid=" + URLEncoder.encode(USERNAME, "utf-8") +
                        "&password=" + URLEncoder.encode(PASSWORD, "utf-8") +
                        "&submit=" + URLEncoder.encode("login", "utf-8")+
                        "&reday=" + URLEncoder.encode(real.substring(0, 14), "utf-8");
                bw.write(sendData + data);
                bw.close();
                osw.close();
                System.out.println(sendData + data);
                System.out.println("normal Login");

                checkpoint = 3;
            }
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
            if (checkpoint == 2 || checkpoint == 3) {

                showCookie();


                System.out.println("get cookie");

                System.out.println("umakuitte");
            } else {
                System.out.println("Don't get cookie");
                return null;
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
            json = new JSONObject(new String(outputStream.toByteArray()));
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
            if(limit.substring(0, 14).equals("00000000000001")){return null;}

            return object;

            // 処理なし

        } catch (JSONException e) {
            if(new String(outputStream.toByteArray()).substring(0,1).equals("<")){

            }
            e.printStackTrace();
        } catch (NullPointerException exception) {
            return null;

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


class KeyStoreUtil {
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
            catch (IOException e) { /* 例外処理は割愛 */ }
        }
    }
}
class MyCookieStore implements CookieStore{


    /*
     * The memory storage of the cookies
     */
    private Map<URI, List<HttpCookie>> mapCookies = new HashMap<URI, List<HttpCookie>>();

    /*
     * The instance of the shared preferences
     */
    private final SharedPreferences spePreferences;

    /*
     * @see java.net.CookieStore#add(java.net.URI, java.net.HttpCookie)
     */
    public void add(URI uri, HttpCookie cookie) {

        System.out.println("add");
        System.out.println(cookie.toString());

        List<HttpCookie> cookies = mapCookies.get(uri);
        //if (cookies == null) {*/
        cookies = new ArrayList<HttpCookie>();
        mapCookies.put(uri, cookies);
        /*}*/
        cookies.add(cookie);

        SharedPreferences.Editor ediWriter = spePreferences.edit();
        HashSet<String> setCookies = new HashSet<String>();
        setCookies.add(cookie.toString());
        ediWriter.putStringSet(uri.toString(), setCookies);
        System.out.println(setCookies);
        ediWriter.apply();


    }



    /*
     * Constructor
     *
     * @param  ctxContext the context of the Activity
     */
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

                System.out.println(entry.getKey() + ": " + strCookie);

            }

        }

    }

    /*
     * @see java.net.CookieStore#get(java.net.URI)
     */
    public List<HttpCookie> get(URI uri) {

        List<HttpCookie> lstCookies = mapCookies.get(uri);


        if (lstCookies == null )
            mapCookies.put(uri, new ArrayList<HttpCookie>());

        return mapCookies.get(uri);

    }

    /*
     * @see java.net.CookieStore#removeAll()
     */
    public boolean removeAll() {

        mapCookies.clear();
        return true;

    }

    /*
     * @see java.net.CookieStore#getCookies()
     */
    public List<HttpCookie> getCookies() {

        Collection<List<HttpCookie>> values = mapCookies.values();

        List<HttpCookie> result = new ArrayList<HttpCookie>();
        for (List<HttpCookie> value : values) {
            result.addAll(value);
        }

        return result;

    }

    /*
     * @see java.net.CookieStore#getURIs()
     */
    public List<URI> getURIs() {

        Set<URI> keys = mapCookies.keySet();
        return new ArrayList<URI>(keys);

    }

    /*
     * @see java.net.CookieStore#remove(java.net.URI, java.net.HttpCookie)
     */
    public boolean remove(URI uri, HttpCookie cookie) {

        List<HttpCookie> lstCookies = mapCookies.get(uri);

        System.out.println(cookie.getMaxAge());


        if (lstCookies == null && cookie.getMaxAge()!=0)
            return false;

        return lstCookies.remove(cookie);

    }

}

