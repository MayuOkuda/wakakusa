package com.wakakusa.kutportal;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class UserPage extends BasePage {

    final String[] student_property = {"id","name", "birth","ug","mjr","sub1","sub2","teacher","address","mailaddress"};
    //final int[] id_box = {R.id.setus_num, R.id.setus_name, R.id.,};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        userinfo();

    }

    //データベースからユーザ情報を記述するためのメソッド
    void userinfo(){
        DatabaseWriter w_db = new DatabaseWriter(this,"student");
        ContentValues cvalue = new ContentValues();
        cvalue.put("id","1180XXX");
        w_db.write.insert(w_db.Table_name,null, cvalue);


        DatabaseReader r_db = new DatabaseReader(this, "student");
        String s = r_db.readDB(student_property, 0);
        //TextView tex1 = (TextView) findViewById(1);

    }

}
