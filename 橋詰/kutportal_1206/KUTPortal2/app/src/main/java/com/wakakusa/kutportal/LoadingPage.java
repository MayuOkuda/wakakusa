package com.wakakusa.kutportal;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.io.WriteAbortedException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;




public class LoadingPage extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_loader);


        // TextViewを取得
        textView = (TextView) findViewById(R.id.jsontest);

        // JSONの取得
        getLoaderManager().restartLoader(1, null, this);
    }



    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        //String urlText = "http://animemap.net/api/table/tokyo.json";
        //String urlText = "http://192.168.33.15:8000/posts.json";
        String urlText = "https://wakakusa.info.kochi-tech.ac.jp/test/main.php";
        //String urlText = "http://weather.livedoor.com/forecast/webservice/json/v1?city=270000";
        //String urlText = "http://www.ajaxtower.jp/googlemaps/gdownloadurl/data.json";
        JsonLoader jsonLoader = new JsonLoader(this, urlText);
        jsonLoader.forceLoad();
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
        final String[] tableName = {"student", "course","score", "test", "news","sessionID"};



        DatabaseWriter[] dbWriter = new DatabaseWriter[6];
               for(int i = 0; i < 5; i++)dbWriter[i] = new DatabaseWriter(this, tableName[i]);
        dbWriter[5] = new DatabaseWriter(this,tableName[5]);
        DatabaseReader[] dbReader = new DatabaseReader[6];
               for(int i = 0; i < 5; i++) dbReader[i] = new DatabaseReader(tableName[i]);
        dbReader[5] = new DatabaseReader(tableName[5]);


        //データベースの削除
        dbWriter[0].deleteDB();

        if (data != null) { //dataがnull？の状態

            try {
                //tableData[0から4]でそれぞれテーブルデータをとってくる
                JSONArray[] tableData = new JSONArray[5];
                for(int i = 0; i < 5; i++)
                    tableData[i] = data.getJSONArray(tableName[i]);

                    //データを格納する
                for(int j = 0; j < 5; j++) {
                    for (int i = 0; i < tableData[j].length(); i++) {
                        JSONObject jsonObject = tableData[j].getJSONObject(i);
                        dbWriter[j].writeDB(jsonObject);
                    }
                }
                dbWriter[5].LoginWrite("sessionID","000000");

                    textView.setText(dbReader[0].readDB(dbWriter[0].property)); //データの入力


            } catch (JSONException e) {
                Log.d("onLoadFinished", "JSONのパースに失敗しました。 JSONException=" + e);
            }
        } else {
            //nullが返されているから、これがログとして出力されている？
            Log.d("onLoadFinished", "onLoadFinished error!");
        }
    }

    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {
        // 処理なし
    }
}


class JsonLoader extends AsyncTaskLoader<JSONObject> {
    private String urlText;
    private Context mContext;
    private static CookieManager manager = null;
    private Map map=new HashMap();


    public JsonLoader(Context context, String urlText) {
        super(context);
        this.urlText = urlText;
        mContext = context;
        if (manager == null) {
            manager = new CookieManager();
// 以下をコメントにするとセッションは働きません
            buildCookieManager();
        }

    }
    // **********************************************
// CookieManager 作成
// **********************************************
    private void buildCookieManager() {
        manager = new CookieManager();
        CookieHandler.setDefault(manager);
    }

    // **********************************************
// Cookie の表示テスト
// **********************************************
    public void showCookie() {

        CookieStore store = manager.getCookieStore();
        List<HttpCookie> cookies = store.getCookies();
        for (int i = 0; i < cookies.size(); i++) {
            HttpCookie cookie = cookies.get(i);

            Log.i("lightbox", "Cookie[" + i + "]: " + cookie);

        }

    }



    @Override
    public JSONObject loadInBackground() {
        //HttpURLConnection connection = null;
        TrustManagerFactory trustManager;
        BufferedInputStream inputStream = null;
        ByteArrayOutputStream responseArray = null;
        byte[] buffer = new byte[1024];
        int length;


        try{
            String sendData = "userid=" + URLEncoder.encode("1X001a","utf-8")+
                    "&password=" + URLEncoder.encode("audayo","utf-8")+
                    "&submit=" + URLEncoder.encode("login","utf-8");
            map.put("display", "環境");
            URL url = new URL(urlText);
           // connection = (HttpURLConnection)url.openConnection();
           // connection.setRequestMethod("GET");
           // connection.setDoOutput(true);
           // connection.setInstanceFollowRedirects(false);
           // connection.connect();

            KeyStore ks = KeyStoreUtil.getEmptyKeyStore();
            KeyStoreUtil.loadX509Certificate(ks,
                    mContext.getResources().getAssets().open("server.crt"));

            // ホスト名の検証を行う
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
            HttpsURLConnection response = (HttpsURLConnection)url.openConnection();

            response.setConnectTimeout(30000);
            response.setReadTimeout(30000);
            response.setChunkedStreamingMode(0);

            response.setDefaultSSLSocketFactory(sslCon.getSocketFactory());
            response.setSSLSocketFactory(sslCon.getSocketFactory());


            response.setUseCaches(false);
            response.setChunkedStreamingMode(0);
            response.setRequestMethod("POST");
            response.setInstanceFollowRedirects(false);
            response.setRequestProperty("Accept-Language", "jp");
            response.setRequestProperty("Connection", "Keep-Alive");
            //response.setDoInput(true);
            response.setDoOutput(true);
            response.setRequestProperty("Content-Type","application/x-www-form-urlencoded");


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
                //String型 <- Object型
                String valueS = value.toString();

                data += key + "=" + URLEncoder.encode(valueS, "utf-8");
            }

            Log.i("lightbox", "送信内容 : " + data);
            System.out.println("OKじゃないじゃないか!");
            //czw.write(sendData+data);
            bw.write(sendData +data);

            bw.close();
            osw.close();
            //ここまで

            checkResponse(response);    //エラーをキャッチする
            inputStream = new BufferedInputStream(response.getInputStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //byte[] buffer = new byte[1024];


            //int length;
            while ((length = inputStream.read(buffer)) != -1){
                if (length > 0){

                    outputStream.write(buffer, 0, length);
                }
            }
            showCookie();
            //ここら辺で止まってる？
            JSONObject json = new JSONObject(new String(outputStream.toByteArray()));
            return json;


        }
        catch (MalformedURLException exception){
            // 処理なし
        }
        catch (IOException exception){
            // 処理なし
        }
        catch (Exception e){
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

