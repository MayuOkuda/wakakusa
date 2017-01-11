package com.example.dorasura1954.samplegetjson;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Loader;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        String urlText = "http://222.229.69.240/test4.php";
        //String urlText = "http://weather.livedoor.com/forecast/webservice/json/v1?city=270000";
        //String urlText = "http://www.ajaxtower.jp/googlemaps/gdownloadurl/data.json";
        JsonLoader jsonLoader = new JsonLoader(this, urlText);
        jsonLoader.forceLoad();
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
        if (data != null) { //dataがnull？の状態

            try {
                //JSONObject jsonObject = data.getJSONObject("request");
                //JSONObject jsonObject = data.getJSONObject("posts");
                //textView.setText(jsonObject.getString("updated"));
                //JSONObject item = jsonObject.getJSONObject("posts");
                //textView.setText(jsonObject.getString("title" ));
                //JSONArray datas = data.getJSONArray("pinpointLocations");
                JSONArray datas = data.getJSONArray("students");
                //JSONArray datas = data.getJSONArray("marker");
                final Database db = new Database(this);
                db.deleteDB();
                for (int i = 0; i < 6; i++) {
                    JSONObject jsonObject = datas.getJSONObject(i);
                    db.writeDB(jsonObject.getString("id"),jsonObject.getString("name"));
                }
                db.update("word","11X002","num","橋詰新也");
                textView.setText(db.readDB()); //データの入力
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
