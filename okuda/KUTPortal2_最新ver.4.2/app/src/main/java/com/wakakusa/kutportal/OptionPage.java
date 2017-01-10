package com.wakakusa.kutportal;

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
import android.widget.TextView;


public class OptionPage extends BasePage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        String[] s = r_db.readDB(new String[]{"id","name"}, 0).split("\n",0);
        int num = 0;
        for(int i : id){
        TextView tex1 = (TextView) findViewById(i);
            tex1.setText(s[num]);
            num++;
        }


    }

    //ユーザページボタンの移動メソッド
       public void UserpageIntent(View view){
            Intent intent = new Intent();
            intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.UserPage");
            startActivity(intent);
        }

}
