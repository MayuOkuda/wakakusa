package com.wakakusa.kutportal;

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

public class LoginPage extends AppCompatActivity {

    //ユーザ名とパスワードを入力するための変数
    static EditText userName;
    static EditText password;
    static TextView jsontext;
    static String name;
    static String pass;

    private AsyncTask<String, Void, Object> mAsyncTask ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        //テキストエディットの宣言
        userName = (EditText)findViewById(R.id.userNameEdit);
        password = (EditText)findViewById(R.id.PasswordEdit);
        jsontext = (TextView)findViewById(R.id.jsontest);
       // button = (Button)findViewById(R.id.loginButton);

    }

    @Override
    protected void onPause() {
        // このあとActivityが破棄される可能性があるので非同期処理をキャンセルしておく
        if (mAsyncTask != null) mAsyncTask.cancel(true);
        super.onPause();
    }

    public void onClick(View view) {
        //URL,ユーザ名パスワードの設定
        String url = "https://wakakusa.info.kochi-tech.ac.jp/js.php";
        name = userName.getText().toString();
        pass = password.getText().toString();

        // 直前の非同期処理が終わってないこともあるのでキャンセルしておく
        if (mAsyncTask != null) mAsyncTask.cancel(true);
            // UIスレッドで通信してはならないので、AsyncTaskによりワーカースレッドで通信する
            mAsyncTask = new PrivateCertificateHttpsGet(this) {
                @Override
                protected void onPostExecute(Object result) {
                    // UIスレッドで通信結果を処理する
                    if (result instanceof Exception) {
                        Exception e = (Exception) result;
                        System.out.println("exception = " + e);
                        Toast.makeText(LoginPage.this, "ユーザ名かパスワードが違います",Toast.LENGTH_LONG).show();
                        // mMsgBox.append("\n例外発生\n" + e.toString());
                    } else {
                        byte[] data = (byte[]) result;
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        Toast.makeText(LoginPage.this, "多分　　おkです。",Toast.LENGTH_LONG).show();


                    }
                }
            }.execute(url);    // URLを渡して非同期処理を開始
    }
}
