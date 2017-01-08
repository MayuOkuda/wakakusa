package com.wakakusa.kutportal;

import android.app.TabActivity;
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
import android.widget.ListView;
import android.widget.TabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CoursePage extends BasePage
        implements ViewPager.OnPageChangeListener {

    Calendar c = Calendar.getInstance();

    //detabase
    static DatabaseReader r_db;
    static String nemui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        r_db = new DatabaseReader(this,"course");

        //年度表示
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String str = sdf.format(c.getTime());
        TextView year = (TextView) findViewById(R.id.course_year);
        year.setText(str);

        couse_appearance("1Q");

    }

    protected void initTabs() {

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        //読み込まれる際に、隣も読み込んでいるのかも(スワイプするため）
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if ((position + 1) == 1) {
                    return activity_tab1.newInstance(position + 1);
                } else if ((position + 1) == 2) {
                    return activity_tab2.newInstance(position + 1);
                } else if ((position + 1) == 3) {
                    return activity_tab3.newInstance(position + 1);
                } else if ((position + 1) == 4) {
                    return activity_tab4.newInstance(position + 1);
                }
                return activity_tab5.newInstance(position + 1);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position + 1 < 5) {
                    return (position + 1) + "Q";
                }
                return "集中";
            }

            @Override
            public int getCount() {
                return 5;
            }
        };

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        //オートマチック方式: これだけで両方syncする
        tabLayout.setupWithViewPager(viewPager);

        //マニュアル方式: これでViewPagerのPositionとTabのPositionをsyncさせるらしい
        //tabLayout.setTabsFromPagerAdapter(adapter);
        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        TextView text = (TextView) findViewById(R.id.time_a1);
        text.setText(nemui);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //クオータを入れると表示
    static void couse_appearance(String Q){
        String[] str2 = {"scode", "subject", "daytime"};
        //属性名３が値３の属性１と属性２のデータを取ってくる。
        nemui = r_db.readDB2(str2, "period =?", new String[]{Q});


    }

        /*
        try {
            TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
            tabHost.setup();
            TabHost.TabSpec spec;
            Intent intent;
            //1Q
            intent = new Intent().setClassName(this, "activity_tab1.class");
            spec = tabHost.newTabSpec("Tab1")
                    .setIndicator("1Q")
                    .setContent(intent);
            //.setContent(intent);
            tabHost.addTab(spec);

            //2Q
            intent = new Intent().setClassName(this, "activity_tab2.class");
            spec = tabHost.newTabSpec("Tab2")
                    .setIndicator("2Q")
                    .setContent(intent);
            tabHost.addTab(spec);

            // 3Q
            intent = new Intent().setClassName(this, "activity_tab3.class");
            spec = tabHost.newTabSpec("Tab3")
                    .setIndicator("3Q")
                    .setContent(intent);
            tabHost.addTab(spec);

            //4Q
            intent = new Intent().setClassName(this, "activity_tab4.class");
            spec = tabHost.newTabSpec("Tab4")
                    .setIndicator("4Q")
                    .setContent(intent);
            tabHost.addTab(spec);

            //集中
            intent = new Intent().setClassName(this, "activity_tab5.class");
            spec = tabHost.newTabSpec("Tab5")
                    .setIndicator("集中")
                    .setContent(intent);
            tabHost.addTab(spec);


            tabHost.setCurrentTab(0);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
    */
}