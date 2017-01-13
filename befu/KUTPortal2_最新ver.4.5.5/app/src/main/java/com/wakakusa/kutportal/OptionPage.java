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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class OptionPage extends BasePage {

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
        for(String str : db_s){
            s[s_num]=str;
            s_num++;
        }
        int num = 0;
        for(int i : id) {
            TextView tex1 = (TextView) findViewById(i);
            tex1.setText(s[num]);
            num++;
        }
            final DatabaseWriter dbWriter = new DatabaseWriter(this, "loginData"); //データベースへの書き込み(初期値false)
            //ContentValues cvalue = new ContentValues(); cvalue.put("ara","false");
            //dbWriter.write.insert(dbWriter.Table_name,null, cvalue);
            final DatabaseReader rd2 = new DatabaseReader(this, "loginData");
            final String[] str = {"ara"};
            final Switch tex2 = (Switch) findViewById(R.id.switch2);
            tex2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //String s2 = rd2.readDB(str, 0);
                        //System.out.println("gyaaaaaaa; " + s2);
                        dbWriter.update("ara", "ara", "false", "true");
                        //s2 = rd2.readDB(str, 0);
                        //System.out.println("gyaaaaaaa2; " + s2);
                    } else {
                        //String s2 = rd2.readDB(str, 0);
                        //System.out.println("waaaaaa" + s2);
                        dbWriter.update("ara", "ara", "true", "false");
                        //s2 = rd2.readDB(str, 0);
                        //System.out.println("waaaaaaa2; " + s2);
                    }
                }
            });
            String s2 = String.valueOf(rd2.readDB(str, 0));
            if ("true\n".equals(s2)) {
                tex2.setChecked(true);
                System.out.println("trueで維持");
            } else {
                tex2.setChecked(false);
                //System.out.println(s2);
                System.out.println("falseで維持");
            }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ボタンがクリックされた時に呼び出されます
                dbWriter.deleteDB();

                // ログアウト後にログイン画面へ遷移
                Intent intent = new Intent();
                intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.LoginPage");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
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
