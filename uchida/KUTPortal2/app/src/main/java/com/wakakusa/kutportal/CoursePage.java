package com.wakakusa.kutportal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TabHost;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

public class CoursePage extends BasePage {

    static DatabaseReader rd;

        @Override
        protected void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.course_page);

            // TabHostの初期化および設定処理
            initTabs();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);



            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

//            rd = new DatabaseReader(this, "student");
//            String[] str = {"id"};
//            TextView tex1 = (TextView) findViewById(R.id.time_a1);
//
//            String s = rd.readDB(str);
//            tex1.setText(s);


        }

    protected void initTabs() {
        try {
            TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
            tabHost.setup();
            TabHost.TabSpec spec;

            //1Q
            spec = tabHost.newTabSpec("Tab1");
                    spec.setIndicator("1Q");
                    spec.setContent(R.id.linearLayout);
            tabHost.addTab(spec);

            //2Q
            spec = tabHost.newTabSpec("Tab2");
                    spec.setIndicator("2Q");
                    spec.setContent(R.id.linearLayout2);
            tabHost.addTab(spec);

            // 3Q
            spec = tabHost.newTabSpec("Tab3");
                    spec.setIndicator("3Q");
                    spec.setContent(R.id.linearLayout3);
            tabHost.addTab(spec);

            //4Q
            spec = tabHost.newTabSpec("Tab4");
            spec.setIndicator("4Q");
            spec.setContent(R.id.linearLayout4);
            tabHost.addTab(spec);

            //集中
            spec = tabHost.newTabSpec("Tab5");
            spec.setIndicator("集中");
            spec.setContent(R.id.linearLayout5);
            tabHost.addTab(spec);


            tabHost.setCurrentTab(0);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void Tab1(View view){

            DatabaseReader rd = new DatabaseReader(this, "student");
            String[] str = {"id"};
            TextView tex1 = (TextView) findViewById(R.id.time_a1);

            String s = rd.readDB(str);
            tex1.setText(s);
    }

}
