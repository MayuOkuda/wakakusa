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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.WriteAbortedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;

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
        String urlText = "https://wakakusa.info.kochi-tech.ac.jp/json.php";
        //String urlText = "http://weather.livedoor.com/forecast/webservice/json/v1?city=270000";
        //String urlText = "http://www.ajaxtower.jp/googlemaps/gdownloadurl/data.json";
        JsonLoader jsonLoader = new JsonLoader(this, urlText);
        jsonLoader.forceLoad();
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
        final String[] tableName = {"student", "course","score", "test", "news"};

        DatabaseWriter[] dbWriter = new DatabaseWriter[5];
               for(int i = 0; i < 5; i++)dbWriter[i] = new DatabaseWriter(this, tableName[i]);
        DatabaseReader[] dbReader = new DatabaseReader[5];
               for(int i = 0; i < 5; i++) dbReader[i] = new DatabaseReader(this,tableName[i]);

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

                    textView.setText(dbReader[0].readDB(dbWriter[0].property,0)); //データの入力


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

    public JsonLoader(Context context, String urlText) {
        super(context);
        this.urlText = urlText;
        mContext = context;
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

            response.setDefaultSSLSocketFactory(sslCon.getSocketFactory());
            response.setSSLSocketFactory(sslCon.getSocketFactory());

            response.connect();
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
