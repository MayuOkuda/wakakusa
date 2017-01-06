package com.wakakusa.kutportal;

import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

import java.net.URI;

public class LoginPage extends AppCompatActivity {


    //ユーザ名とパスワードを入力するための変数
    static EFlag actflag1;
    static EditText userName;
    static EditText password;
    static TextView jsontext;
    static String name;
    static String pass;
    static int checkLogin = 1;
    Intent intent = new Intent();
    private URI uri;
    private AsyncTaskLoader<JSONObject> mAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadingPage.actFlag.setFlagState(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        //テキストエディットの宣言
        actflag1 = new EFlag();
        userName = (EditText) findViewById(R.id.userNameEdit);
        password = (EditText) findViewById(R.id.PasswordEdit);

        // button = (Button)findViewById(R.id.loginButton);

    }

    @Override
    protected void onPause() {

        // このあとActivityが破棄される可能性があるので非同期処理をキャンセルしておく
        String url = "https://wakakusa.info.kochi-tech.ac.jp/test/main.php";

        name = userName.getText().toString();
        pass = password.getText().toString();


        if (mAsyncTask != null) mAsyncTask.cancelLoad();
        super.onPause();
    }

    public void onClick(View view) throws Exception {
        //URL,ユーザ名パスワードの設定
        String url = "https://wakakusa.info.kochi-tech.ac.jp/test/main.php";
        name = userName.getText().toString();
        pass = password.getText().toString();
        uri = new URI("http://wakakusa.info.kochi-tech.ac.jp");



        // 直前の非同期処理が終わってないこともあるのでキャンセルしておく
        if (mAsyncTask != null) mAsyncTask.cancelLoad();
        // UIスレッドで通信してはならないので、AsyncTaskによりワーカースレッドで通信する
        mAsyncTask = new JsonLoader(this,url);
        intent.setClassName("com.wakakusa.kutportal","com.wakakusa.kutportal.LoadingPage");
        startActivity(intent);


    }
    @Override
    public void onRestart(){
        super.onRestart();
        if(this.actflag1.getFlagState() == true){
            this.finish();

        }
    }
}
