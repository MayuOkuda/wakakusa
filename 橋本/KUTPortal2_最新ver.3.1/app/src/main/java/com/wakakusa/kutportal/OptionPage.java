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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class OptionPage extends BasePage {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //  final int FP = ViewGroup.LayoutParams.FILL_PARENT;
        // final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final DatabaseWriter dbWriter = new DatabaseWriter(this, "loginData");
        /*ContentValues cvalue = new ContentValues();
        cvalue.put("ara","false");
        dbWriter.write.insert(dbWriter.Table_name,null, cvalue);*/


        final DatabaseReader rd = new DatabaseReader(this, "loginData");
        final String[] str = {"ara"};
        //TextView tex1 = (TextView) findViewById(R.id.login2);
        final Switch tex2 = (Switch) findViewById(R.id.switch2);
        //String s = rd.readDB(str,0);
        tex2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
               String s = rd.readDB(str,0);
                System.out.println("gyaaaaaaa; " + s);
                dbWriter.update("ara","false","ara","true");
                s = rd.readDB(str,0);
                System.out.println("gyaaaaaaa2; " + s);
                //tex2.setChecked(true);
            }else{
                String s = rd.readDB(str,0);
                System.out.println("waaaaaa" + s);
                dbWriter.update("ara","true","ara","false");
                s = rd.readDB(str,0);
                System.out.println("waaaaaaa2; " + s);
                //tex2.setChecked(false);
            }
            }
        });
        String s = rd.readDB(str,0);
        s=getString(s);
        //tex1.setText(s);
        if("true".equals(s)) {
            tex2.setChecked(true);
            System.out.println("trueで維持");
        } else {
            tex2.setChecked(false);
            System.out.println(s);
            System.out.println("falseで維持");
        }

    }

    public void UserpageIntent(View view){

            Intent intent = new Intent();
            intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.UserPage");
            startActivity(intent);
    }

/*
    public void onCheckChanged(CompoundButton buttonView, boolean isChecked) {

        DatabaseWriter dbWriter = new DatabaseWriter(this, "loginData");
        if(isChecked == true){
            dbWriter.update("ara","false","ara","true");
        } else {
            dbWriter.update("ara","true","ara","false");
        }
    }
    */

}
