package com.wakakusa.kutportal;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.util.TypedValue;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class OptionPage extends BasePage {

    /*
     * 設定画面クラス
     * 自動ログイン設定や通知設定を行うクラス
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button button =(Button) findViewById(R.id.button6);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //学籍番号と名前の表示
        int[] id = {R.id.setus_num, R.id.setus_name};
        DatabaseReader r_db = new DatabaseReader(this, "student");
        String s[] = new String[3];
        String[] db_s = r_db.readDB(new String[]{"id","name"}, 0).split("\n",0);
        int s_num = 0;
        int num = 0;

        for(String str : db_s){
            s[s_num]=str;
            s_num++;
        }

        for(int i : id) {
            TextView tex1 = (TextView) findViewById(i);
            tex1.setText(s[num]);
            num++;
        }

        final DatabaseWriter dbWriter = new DatabaseWriter(this, "loginData"); //データベースへの書き込み(初期値false)
        final DatabaseReader rd2 = new DatabaseReader(this, "loginData");
        final String[] str = {"ara"};
        final String[] str2 = {"response"};
        final Switch tex2 = (Switch) findViewById(R.id.switch2);
        String s2 = String.valueOf(rd2.readDB(str, 0));

        tex2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dbWriter.update("ara", "ara", "false", "true");
                } else {
                    dbWriter.update("ara", "ara", "true", "false");
                }
            }
        });

        if ("true\n".equals(s2)) {
            tex2.setChecked(true);
        } else {
            tex2.setChecked(false);
        }

        final CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox2);
        final CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox3);
        final CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox4);
        final CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox5);
        String s3= rd2.readDB(str2,0);

        //チェックボタンの初期状態の判定
        //講義
        if ("1".equals(s3.substring(0,1))) {
            checkBox1.setChecked(true);
        } else {
            checkBox1.setChecked(false);
        }

        //事務連絡
        if ("1".equals(s3.substring(1,2))) {
            checkBox2.setChecked(true);
        } else {
            checkBox2.setChecked(false);
        }

        //イベント
        if ("1".equals(s3.substring(2,3))) {
            checkBox3.setChecked(true);
        } else {
            checkBox3.setChecked(false);
        }

        //その他
        if ("1".equals(s3.substring(3,4))) {
            checkBox4.setChecked(true);
        } else {
            checkBox4.setChecked(false);
        }

        //チェックボタンを押した祭の処理
        checkBox1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String s3 = rd2.readDB(str2,0);
                if(s3.substring(0,1).equals("1")){
                    String s4 = ("0" + s3.substring(1,4));
                    dbWriter.update("response", "response", s3.substring(0,4) , s4);
                } else {
                    String s4 = ("1" + s3.substring(1,4));
                    dbWriter.update("response", "response", s3.substring(0,4) ,s4);
                }
            }
        });

        checkBox2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String s3= rd2.readDB(str2,0);
                if(s3.substring(1,2).equals("1")){
                    System.out.println("1のとき");
                    String s4 = (s3.substring(0,1) +"0"+ s3.substring(2,4));
                    dbWriter.update("response", "response", s3.substring(0,4) , s4);
                } else {
                    System.out.println("0のとき");
                    String s4 = (s3.substring(0,1) +"1"+ s3.substring(2,4));
                    dbWriter.update("response", "response", s3.substring(0,4) ,s4);
                }
            }
        });

        checkBox3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String s3= rd2.readDB(str2,0);
                if(s3.substring(2,3).equals("1")){
                    System.out.println("1のとき");
                    String s4 = (s3.substring(0,2) +"0"+ s3.substring(3,4));
                    dbWriter.update("response", "response", s3.substring(0,4) , s4);
                } else {
                    System.out.println("0のとき");
                    String s4 = (s3.substring(0,2)+"1"+ s3.substring(3,4));
                    dbWriter.update("response", "response", s3.substring(0,4) ,s4);
                }
            }
        });

        checkBox4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String s3= rd2.readDB(str2,0);
                if(s3.substring(3,4).equals("1")){
                    String s4 = (s3.substring(0,3) +"0");
                    dbWriter.update("response", "response", s3.substring(0,4) , s4);
                } else {
                    String s4 = (s3.substring(0,3)+"1");
                    dbWriter.update("response", "response", s3.substring(0,4) ,s4);
                }
            }
        });

        //ログアウトボタン
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ボタンがクリックされた時に呼び出されます
                //更新時のloginDataの初期化
                String[] pre_login= rd2.readDB(new String[]{"tokenID"},0).split("\n",0);
                String[] pre_login2= rd2.readDB(new String[]{"limittime"},0).split("\n",0);
                String[] pre_login3= rd2.readDB(new String[]{"realtime"},0).split("\n",0);
                String[] pre_login4= rd2.readDB(new String[]{"response"},0).split("\n",0);
                dbWriter.update("ara","ara",pre_login[0],"true");
                dbWriter.update("limittime","limittime", pre_login2[0],"00000000000001");
                dbWriter.update("realtime","realtime",pre_login3[0], "00000000000000");
                dbWriter.update("response","response",pre_login4[0],"1111");

                //全トピック脱退処理
                FirebaseMessaging.getInstance().unsubscribeFromTopic("all");

                //学群トピック脱退処理
                HashMap<String,String> mj_map = new HashMap<String,String>();
                mj_map.put("システム工学群", "system");
                mj_map.put("環境理工学群", "milieu");
                mj_map.put("情報学群", "info");
                mj_map.put("経済・マネジメント学群", "manage");
                if(mj_map.get(TopPage.ug)!=null)
                FirebaseMessaging.getInstance().unsubscribeFromTopic(mj_map.get(TopPage.ug));

                //科目トピック脱退処理
                String[] course = TopPage.course_db_R.readDB(new String[]{"scode"},0).split("\n",0);
                for(String scode : course) {
                    if (scode != null)
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(scode);

                }


                //学年トピック参加
                if(TopPage.this_grade!=null)
                    FirebaseMessaging.getInstance().subscribeToTopic(TopPage.this_grade);

                // ログアウト後にログイン画面へ遷移
                Intent intent = new Intent();
                BasePage.actFlag.setFlagState(true);
                intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.LoginPage");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                ActivityStack.removeHistory();
                startActivity(intent);

            }
        });


    }

    //ユーザページボタンの移動メソッド
    public void UserpageIntent(View view){
        Intent intent = new Intent();
        intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.UserPage");
        startActivity(intent);
    }

}
