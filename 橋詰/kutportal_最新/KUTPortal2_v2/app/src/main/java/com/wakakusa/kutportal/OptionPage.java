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

      //  final int FP = ViewGroup.LayoutParams.FILL_PARENT;
       // final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        LinearLayout baseLayout =  new LinearLayout(this);
//        baseLayout.setOrientation(LinearLayout.VERTICAL);
//        baseLayout.setBackgroundColor(Color.rgb(175, 255, 255));
//        setContentView(baseLayout);
//
//        LinearLayout subLayout1 = new LinearLayout(this);
//        LinearLayout.LayoutParams layoutParams1
//                = new LinearLayout.LayoutParams(FP, WC);
//        baseLayout.addView(subLayout1, layoutParams1);
//        subLayout1.setOrientation(LinearLayout.HORIZONTAL);
//        subLayout1.setGravity(Gravity.CENTER);
//        subLayout1.setPadding(0, 5, 0, 5);
//        subLayout1.setBackgroundColor(Color.rgb(0,255,0));
//        layoutParams1.height = 100;
//        layoutParams1.setMargins(0, 0, 0, 0);
    }

       public void UserpageIntent(View view){
            Intent intent = new Intent();
            intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.UserPage");
            startActivity(intent);
        }

}
