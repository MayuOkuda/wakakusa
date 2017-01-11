package com.example.dorasura1954.samplegetjson;

/**
 * Created by dorasura1954 on 2016/11/05.
 */
import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import android.os.Bundle;
import android.widget.EditText;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.io.DataOutputStream;

import java.io.PrintWriter;
import android.os.HandlerThread;
import android.os.Handler;
import java.net.URLEncoder;

/**
 * Created by com.swift_studying. on 15/10/24.
 */
public class JsonLoader extends AsyncTaskLoader<JSONObject> {
    private String urlText;
    private Context mContext;
    private String userid;
    private String password;
    private String login;
    private Map params=new HashMap();
    final InputStreamReader ist = new InputStreamReader(System.in);
    private static CookieManager manager = null;
    private Map map;





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









    public JsonLoader(Context context, String urlText,String user,String pass,String logins,Map<String,String> params) {

        super(context);
        this.urlText = urlText;
        mContext = context;
        this.userid =user;
        this.password =pass;
        this.login = logins;
        this.map = params;

        if (manager == null) {
            manager = new CookieManager();
// 以下をコメントにするとセッションは働きません
            buildCookieManager();
        }

    }

    @Override
    public JSONObject loadInBackground() {
        // HttpURLConnection connection = null;
        TrustManagerFactory trustManager;
        BufferedInputStream inputStream = null;
        ByteArrayOutputStream responseArray = null;
        byte[] buffer = new byte[1024];
        int length;
        DataOutputStream os = null;
/*
        try {
            URL curl = new URL(urlText);
            connection = (HttpURLConnection) curl.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf8"));

            StringBuilder sb = new StringBuilder();
            StringBuilder ReceiveStr = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject json = new JSONObject(sb.toString());
            return json;
        }
        catch (IOException exception){
            // 処理なし
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
*/
        /*
        StringBuilder userid = new StringBuilder();
        StringBuilder password = new StringBuilder();
        int iContentsLength = 0;
        int iContentsLength2 =0;
        try {
            user = new String(user.getBytes("UTF-8"));
            pass = new String(pass.getBytes("UTF-8"));
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        userid.append(user);
        password.append(pass);
        */

        try {
            //System.out.println("da");
            //this.map = new HashMap();
            //map.put("display", "環境");

            URL url = new URL("https://wakakusa.info.kochi-tech.ac.jp/test/main.php");
            String sendData = "userid=" + URLEncoder.encode(userid,"utf-8")+
                    "&password=" + URLEncoder.encode(password,"utf-8")+
                    "&submit=" + URLEncoder.encode(login,"utf-8");
            //connection = (HttpURLConnection)url.openConnection();
            //connection.setRequestMethod("GET");
            //connection.setDoOutput(true);
            //connection.setInstanceFollowRedirects(false);
            //connection.connect();

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
            // ★ポイント 2★ URI は https://で始める
            // ★ポイント 3★ 送信データにセンシティブな情報を含めてよい
            trustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManager.init(ks);
            SSLContext sslCon = SSLContext.getInstance("TLS");
            sslCon.init(null, trustManager.getTrustManagers(), new SecureRandom());
            //String s = new String(java.nio.charset.StandardCharsets.UTF_8);
            final HttpsURLConnection response = (HttpsURLConnection) url.openConnection();

            response.setConnectTimeout(30000);
            response.setReadTimeout(30000);
            response.setChunkedStreamingMode(0);

            //HttpsURLConnection response = (HttpsURLConnection) con;
            response.setDefaultSSLSocketFactory(sslCon.getSocketFactory());
            response.setSSLSocketFactory(sslCon.getSocketFactory());
/*
            try {

                iContentsLength = userid.toString().getBytes(CHARSET).length;
                iContentsLength = password.toString().getBytes(CHARSET).length;
            }
                catch(UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                */

            response.setUseCaches(false);
            response.setChunkedStreamingMode(0);
            response.setRequestMethod("POST");
            response.setInstanceFollowRedirects(false);
            response.setRequestProperty("Accept-Language", "jp");
            response.setRequestProperty("Connection", "Keep-Alive");
            //response.setDoInput(true);
            response.setDoOutput(true);
            response.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            //response.setRequestProperty("Content-Type", "application/login;charset=utf-8");
            //response.setRequestProperty("Content-Type", "application/userid;charset=utf-8");
            //response.setRequestProperty("Content-Type", "application/password;charset=utf-8");
            // POSTデータの形式を設定
            /*
            response.setRequestProperty("Content-Type", String.format("text/plain; boundary=%s", BOURDARY));
            // POSTデータの長さを設定
            response.setRequestProperty("Content-Length", String.valueOf(iContentsLength+iContentsLength2));
*/



            /*
            BufferedWriter userid = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()
                    /*,StandardCharsets.UTF_8)));

            userid.write(user);
            userid.write("\r\n");
            userid.flush();
            BufferedWriter password = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()
                    /*,StandardCharsets.UTF_8));
            password.write(pass);
            password.write("\r\n");
            password.flush();
            */


            //os = new DataOutputStream(response.getOutputStream());
            //os.write(userid.getBytes("UTF-8"));

            //OutputStream cz = response.getOutputStream();
            //OutputStreamWriter czw = new OutputStreamWriter(cz,"utf-8");
            // PrintStream userid = new PrintStream(cz);
            //PrintStream password = new PrintStream(cz);
            //PrintStream send = new PrintStream(cz);

            OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream());
            BufferedWriter bw = new BufferedWriter(osw);

            Iterator<Object> it = params.keySet().iterator();

            String key = null;
            Object value = null;
            String data = "";
            while (it.hasNext()) {
                key = it.next().toString();
                value = params.get(key);
                if (!data.equals("")) {
                    data += "&";
                }
                //String型 <- Object型
                String valueS = value.toString();

                data += key + "=" + URLEncoder.encode(valueS, "utf-8");
            }

            Log.i("lightbox", "送信内容 : " + data);

            //czw.write(sendData+data);
            bw.write(sendData +data);

            bw.close();
            osw.close();
            //czw.write(sendData);
            //czw.close();



/*
            DataOutputStream wr = new DataOutputStream(response.getOutputStream());
            try {
                wr.write(password.getBytes("UTF-8"));
                wr.write(login.getBytes("UTF-8"));
                wr.write(userid.getBytes("UTF-8"));
                //wr.writeUTF(userid);
                //wr.write("¥r¥n".getBytes("UTF-8"));

                //wr.writeUTF(userid);
                wr.flush();

            }catch(IOException e){

                e.printStackTrace();
            }

            wr.close();

*/          //checkResponse(response);

            checkResponse(response);
            inputStream = new BufferedInputStream(response.getInputStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //byte[] buffer = new byte[1024];

            while ((length = inputStream.read(buffer)) != -1) {
                if (length > 0) {

                    outputStream.write(buffer, 0, length);
                }
            }
            showCookie();
            //ここら辺で止まってる？
            JSONObject json = new JSONObject(new String(outputStream.toByteArray()));
            return json;

        } catch (SSLException e) {
            // ★ポイント 5★ SSLException に対しユーザーに通知する等の適切な例外処理をする
            // サンプルにつき例外処理は割愛
            //return e;
        } catch (Exception e) {
            e.printStackTrace();
            //return e;
        //} catch (MalformedURLException exception) {
            // 処理なし
        //} catch (IOException exception) {
            // 処理なし
        }
        //catch (IOException exception){
        //catch (IOException e) {
            // 処理なし
            //e.printStackTrace();
        //}
        //catch (JSONException e) {
            //e.printStackTrace();
        //}
        //ここでnullが返されている


        return null;
    }
    private void checkResponse(HttpsURLConnection response) throws IOException {
        int statusCode = response.getResponseCode();
        if (HttpURLConnection.HTTP_OK != statusCode) {
            throw new IOException("HttpStatus: " + statusCode);
        }

    }
}
