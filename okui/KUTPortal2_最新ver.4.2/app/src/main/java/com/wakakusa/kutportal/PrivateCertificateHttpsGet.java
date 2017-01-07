package com.wakakusa.kutportal;


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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import android.content.SharedPreferences;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.CookieSyncManager;

import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
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
import android.annotation.TargetApi;
@TargetApi(18)
@SuppressWarnings("deprecation")
public abstract class PrivateCertificateHttpsGet extends AsyncTask<String, Void, Object> {

    private Context mContext;
    private CookieStore store;
    private static CookieManager manager;
    private Map map = new HashMap();
    private HttpCookie cookie;
    //private CookieManager cookieManager =CookieManager.getInstance();
    MyCookieStore cookiestore;

    private long cookietime;
    private URI uri;


    public PrivateCertificateHttpsGet(Context context) throws  Exception{
        mContext = context;

        uri = new URI("https://wakakusa.info.kochi-tech.ac.jp/test/main.php");
        cookiestore =new MyCookieStore(mContext);
        if (manager == null) {
            manager = new CookieManager();
//以下をコメントにするとセッションは働きません
            buildCookieManager();
        }
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
        for(int i=0 ;i<cookies.size();i++){
            cookie = cookies.get(0);
            uri = cookieuri.get(0);
            cookietime = cookie.getMaxAge();
            store.add(uri, cookie);
            store.get(uri);
            System.out.println(store);
            System.out.println(cookie);
            System.out.println(cookies.size());
            Log.i("lightbox", "Cookies[" + 0 + "]: " + cookie + "/" + cookietime);
        }
    }


    @Override
    protected Object doInBackground(String... params) {
        TrustManagerFactory trustManager;
        BufferedInputStream inputStream = null;
        ByteArrayOutputStream responseArray = null;
        byte[] buff = new byte[1024];
        int length;
        map.put("display", "環境");

        try {
            //↓ここにユーザ名とパスワード
            String USERNAME = LoginPage.name;
            String PASSWORD = LoginPage.pass;
            String sendData = "userid=" + URLEncoder.encode(USERNAME,"utf-8")+
                    "&password=" + URLEncoder.encode(PASSWORD,"utf-8")+
                    "&submit=" + URLEncoder.encode("login","utf-8");
            //
            URL url = new URL(params[0]);
            uri = new URI(params[0]);
            // ★ポイント1★ https://localhost/index.htmlプライベート証明書でサーバー証明書を検証する
            // assetsに格納しておいたプライベート証明書だけを含むKeyStoreを設定
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
            //final String userPassword = USERNAME+":" + PASSWORD;
            //final String encodeAuthorization = Base64.encodeToString(userPassword.getBytes(), Base64.NO_WRAP);
            response.setConnectTimeout(30000);
            response.setReadTimeout(30000);
            response.setChunkedStreamingMode(0);

            response.setDefaultSSLSocketFactory(sslCon.getSocketFactory());
            response.setSSLSocketFactory(sslCon.getSocketFactory());
            //response.setRequestProperty("Authorization", "Basic " + encodeAuthorization);

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
            System.out.println("OKじゃないじゃないか!");
            //czw.write(sendData+data);


            if(store==null)bw.write(sendData + data);
            else bw.write(data);

            bw.close();
            osw.close();

            checkResponse(response);    //エラーをキャッチする


            // ★ポイント4★ 受信データを接続先サーバーと同じ程度に信用してよい
            inputStream = new BufferedInputStream(response.getInputStream());
            responseArray = new ByteArrayOutputStream();
            while ((length = inputStream.read(buff)) != -1) {
                if (length > 0) {
                    responseArray.write(buff, 0, length);
                }
            }
            showCookie();


            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();

            Calendar cal = Calendar.getInstance();

            long t = Long.parseLong(sdf1.format(date.getTime()));
            //long in = Long.parseLong(t);

            System.out.println("cal1:" + sdf1.format(cal.getTime()));
            System.out.println("date:" + sdf1.format(date));

            //cal.set(Time[0], Time[1], Time[2], Time[3], Time[4], Time[5]);
            cal.add(Calendar.SECOND, (int)cookietime);
            //System.out.println(sdf1.format(cal.getTime()));

            System.out.println("cal2:" + sdf1.format(cal.getTime()));

            cookiestore.add(uri, cookie);
            cookiestore.get(uri).get(0).setMaxAge(30);

            System.out.println();
            System.out.println(cookiestore.get(uri));

            return cookie;

        } catch(SSLException e) {
            // ★ポイント5★ SSLExceptionに対しユーザーに通知する等の適切な例外処理をする
            // サンプルにつき例外処理は割愛
            return e;
        } catch(Exception e) {
            return e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    // 例外処理は割愛
                }
            }
            if (responseArray != null) {
                try {
                    responseArray.close();
                } catch (Exception e) {
                    // 例外処理は割愛
                }
            }
        }
    }

    private void checkResponse(HttpURLConnection response) throws IOException {
        int statusCode = response.getResponseCode();
        if (HttpURLConnection.HTTP_OK != statusCode) {
            throw new IOException("HttpStatus: " + statusCode);
        }
    }
}




