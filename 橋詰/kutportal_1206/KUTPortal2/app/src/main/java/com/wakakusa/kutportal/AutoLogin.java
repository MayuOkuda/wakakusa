package com.wakakusa.kutportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieSyncManager;
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

import android.content.Intent;

import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("deprecation")

/**
 * Created by hashidzumeshinya on 2016/12/06.
 */

public class AutoLogin extends AppCompatActivity {
    //Cookieがあるかどうか判断
    //Cookieがあるならtop
    //Cookieがないならlogin
    Intent intent = new Intent();
    MyCookieStore cookiestore;
    URI uri;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            cookiestore = new MyCookieStore(this);
            uri = new URI("https://wakakusa.info.kochi-tech.ac.jp/test/main.php");

            if (cookiestore.get(uri) == null) {


                intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.LoginPage");
                startActivity(intent);

            } else {

                intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
                startActivity(intent);
            }
        }
        catch (URISyntaxException e){
            e.printStackTrace();
        }

    }

}

