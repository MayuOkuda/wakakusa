package com.wakakusa.kutportal;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CoursePage extends BasePage
        implements ViewPager.OnPageChangeListener{


    /*
 * CouserPage(履修確認クラス)
 */



    //detabase呼び出し変数
    static DatabaseReader course_db_R;
    static DatabaseReader score_db_R;
    static String thisYear;
    static Today course;

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

        //データベースの設定
        course_db_R = new DatabaseReader(this,"course");
        score_db_R = new DatabaseReader(this, "score");


        Calendar c = Calendar.getInstance();
        //年度表示
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        thisYear = sdf.format(c.getTime());
        String thisMonth = sdf2.format(c.getTime());

        //もし１月２月３月なら
        if(thisMonth.equals("01") || thisMonth.equals("02")||thisMonth.equals("03"))
            thisYear = String.valueOf(Integer.parseInt(thisYear)-1);
        TextView year = (TextView) findViewById(R.id.course_year);
        year.setText(thisYear);

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
                    return tab1.newInstance(position + 1);
                } else if ((position + 1) == 2) {
                    return tab2.newInstance(position + 1);
                } else if ((position + 1) == 3) {
                    return tab3.newInstance(position + 1);
                } else if ((position + 1) == 4) {
                    return tab4.newInstance(position + 1);
                }
                return tab5.newInstance(position + 1);
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


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //クオータを入れると表示（tabクラスと各連携）
    static void couse_appearance(String Q){

        String[] str1 = {"scode"};
        //属性名３が値３の属性１と属性２のデータを取ってくる。
        String scode = score_db_R.readDB2(str1, "year=?", new String[]{thisYear});


        String[] str2 = {"scode", "subject", "room", "daytime","teacher","sj","sjclass"};
        //属性名３が値３の属性１と属性２のデータを取ってくる。
        String inputdata = course_db_R.readDB2(str2, "period =?", new String[]{Q});

        //今年の今クオータの中身をクラス分け
        course = new Today(inputdata.split("\n", 0), scode.split("\n", 0));


    }



}