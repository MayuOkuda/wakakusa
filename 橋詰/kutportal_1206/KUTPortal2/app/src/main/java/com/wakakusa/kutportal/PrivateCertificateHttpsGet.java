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
import java.net.URL;
import java.net.URI;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import android.annotation.TargetApi;
import android.util.Base64;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import android.webkit.ValueCallback;
import android.webkit.CookieSyncManager;
import java.util.HashSet;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.net.URISyntaxException;
import java.util.Set;
import android.content.SharedPreferences.Editor;
import java.util.Collection;
@TargetApi(18)
@SuppressWarnings("deprecation")
public abstract class PrivateCertificateHttpsGet extends AsyncTask<String, Void, Object> {

    private Context mContext;
    private CookieStore store;
    private static CookieManager manager;
    private Map map = new HashMap();
    private HttpCookie cookie;
    private MyCookieStore cookiestore;
    private long cookietime;
    private URI uri;


    public PrivateCertificateHttpsGet(Context context) throws  Exception{
        mContext = context;
        uri = new URI("https://wakakusa.info.kochi-tech.ac.jp/test/main.php");
        cookiestore = new MyCookieStore(context);
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
        if(cookie == null) {
            manager = new CookieManager();
            CookieHandler.setDefault(manager);
        }
    }

    // **********************************************
// Cookie の表示テスト
// **********************************************
    public void showCookie() {

        store = manager.getCookieStore();
        List<HttpCookie> cookies = store.getCookies();
        List<URI> cookieuri = store.getURIs();
        for (int i = 0; i < cookies.size(); i++) {
            cookie = cookies.get(i);
            uri = cookieuri.get(i);
            cookietime = cookie.getMaxAge();
            store.add(uri,cookie);
            store.get(uri);
            System.out.println(store);
            Log.i("lightbox", "Cookie[" + i + "]: " + cookie + "/" + cookietime);
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
            //


            String sendData = "userid=" + URLEncoder.encode(USERNAME,"utf-8")+
                    "&password=" + URLEncoder.encode(PASSWORD,"utf-8")+
                    "&submit=" + URLEncoder.encode("login","utf-8");
            //String sendCookie="&Cookie="+cookie;
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

            response.setConnectTimeout(30000);
            response.setReadTimeout(30000);
            response.setChunkedStreamingMode(0);
            //final String userPassword = USERNAME+":" + PASSWORD;
            //final String encodeAuthorization = Base64.encodeToString(userPassword.getBytes(), Base64.NO_WRAP);

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
            //cookiestore.remove(uri,cookie);
            if(cookiestore.get(uri)==null) cookiestore.add(uri,cookie);
            Log.i("a",cookiestore.get(uri).toString());
            if(cookie.getDiscard()==true)
               cookiestore.remove(uri,cookie);
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
        //return null;
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

    public static void loadX509Certificate(KeyStore ks, InputStream is)
            throws CertificateException, KeyStoreException {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X509");
            X509Certificate x509 = (X509Certificate)factory.generateCertificate(is);
            String alias = x509.getSubjectDN().getName();
            ks.setCertificateEntry(alias, x509);
        } finally {
            try { is.close(); } catch (IOException e) { /* 例外処理は割愛 */ }
        }
    }
}

class MyCookieStore implements CookieStore {

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
        if (cookies == null) {
            cookies = new ArrayList<HttpCookie>();
            mapCookies.put(uri, cookies);
        }
        cookies.add(cookie);

        Editor ediWriter = spePreferences.edit();
        HashSet<String> setCookies = new HashSet<String>();
        setCookies.add(cookie.toString());
        ediWriter.putStringSet(uri.toString(), spePreferences.getStringSet(uri.toString(), setCookies));
        ediWriter.commit();

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


        if (lstCookies == null)
            return false;

        return lstCookies.remove(cookie);

    }

}
