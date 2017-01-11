package com.example.dorasura1954.samplegetjson;

/**
 * Created by dorasura1954 on 2016/11/05.
 */
import android.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by com.swift_studying. on 15/10/24.
 */
public class JsonLoader extends AsyncTaskLoader<JSONObject> {
    private String urlText;

    public JsonLoader(Context context, String urlText) {
        super(context);
        this.urlText = urlText;
    }

    @Override
    public JSONObject loadInBackground() {
        HttpURLConnection connection = null;
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
        try{
            URL url = new URL(urlText);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.connect();

        }
        catch (MalformedURLException exception){
            // 処理なし
        }
        catch (IOException exception){
            // 処理なし
        }

        try{
            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int length;
            while ((length = inputStream.read(buffer)) != -1){
                if (length > 0){

                    outputStream.write(buffer, 0, length);
                }
            }
            //ここら辺で止まってる？
            JSONObject json = new JSONObject(new String(outputStream.toByteArray()));
            return json;
        }
        //catch (IOException exception){
        catch (IOException e) {
            // 処理なし
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //ここでnullが返されている
        return null;
    }
}
