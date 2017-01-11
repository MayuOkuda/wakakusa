package com.example.dorasura1954.samplegetjson;

import android.app.LoaderManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Loader;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    TextView textView;
    private AsyncTask<String, Void, String> mAsyncTask;
    private TextView mMsgBox2;
    private Button mMsgBut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mMsgBox2 = (TextView) findViewById(R.id.msgbox2);
        mMsgBut = (Button) findViewById(R.id.msgbut);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });


        // TextViewを取得

        textView = (TextView) findViewById(R.id.textView);

        // JSONの取得
        getLoaderManager().restartLoader(1, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        //String urlText = "http://animemap.net/api/table/tokyo.json";
        //String urlText = "http://192.168.33.15:8000/posts.json";

        String urlText = "";
        Map map = new HashMap();
        map.put("display", "環境");

        String userid = ((EditText) findViewById(R.id.userid)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String login = ((EditText) findViewById(R.id.login)).getText().toString();

        //String urlText = "http://weather.livedoor.com/forecast/webservice/json/v1?city=270000";
        //String urlText = "http://www.ajaxtower.jp/googlemaps/gdownloadurl/data.json";

        JsonLoader jsonLoader = new JsonLoader(this, urlText, userid, password, login, map);
        jsonLoader.forceLoad();
        return jsonLoader;

    }


    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
        if (data != null) { //dataがnull？の状態
            //JSONArray json = null;
            try {
                //json = new JSONArray("data");
                //String json = data;
                //JSONObject jsonObject = data.getJSONObject("request");
                //JSONObject jsonObject = data.getJSONObject("posts");
                //textView.setText(jsonObject.getString("updated"));
                //JSONObject item = jsonObject.getJSONObject("posts");
                //textView.setText(jsonObject.getString("title" ));
                //JSONArray datas = data.getJSONArray("pinpointLocations");
                JSONArray datas = data.getJSONArray("student");
                //JSONArray datas = new JSONArray(data);
                //JSONArray datas = data.getJSONArray("marker");
                JSONObject jsonObject = datas.getJSONObject(0);
                //String data1 = data.getString("科目コード");

                textView.setText(jsonObject.getString("id"));
                //textView.setText(jsonObject.getString("name"));
                //textView.setText(jsonObject.getString("title"));
                //textView.setText(jsonObject.getString("lat"));

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