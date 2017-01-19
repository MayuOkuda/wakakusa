package com.wakakusa.kutportal;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestCount extends BasePage {

/*
 *テストカウントダウンの表示のためのクラス
 */

    //表示場所ID
    int dayID[] = {R.id.tc_m1,R.id.tc_m2,R.id.tc_m3,R.id.tc_m4,R.id.tc_m5,R.id.tc_m6,R.id.tc_m7};
    int subID[] = {R.id.tc_sj1,R.id.tc_sj2,R.id.tc_sj3,R.id.tc_sj4,R.id.tc_sj5,R.id.tc_sj6,R.id.tc_sj7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_count);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //テスト情報を表示する
        SetTextView();


    }


    //履修状況表示
    void SetTextView() {
        textviewReset();
        Test t = TopPage.testclass;
        //表示する
        int i=0;
        while (t.day != null) {
            if(i == subID.length) break;
            TextView text = (TextView) findViewById(subID[i]);
            text.setText(t.subject);
            TextView text1 = (TextView) findViewById(dayID[i]);
            text1.setText(t.day.substring(4,6)+" / "+t.day.substring(6,8));
            i++;
            t = t.next;

        }

    }

    //内容のリセット
    void textviewReset() {

        for (int i = 0; i < 7; i++) {
            TextView text = (TextView) findViewById(subID[i]);
            text.setText("          ");
            TextView text1 = (TextView) findViewById(dayID[i]);
            text1.setText("       ");
        }
    }

}


/*
 *テスト情報を保存しておくクラス
 */

class Test{
    String day;
    String subject;
    Test next;

    Calendar c = Calendar.getInstance();

    Test(){}

    Test(String[] str){
        if(str[0].equals(null)) return;
        Test lo = this;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        int year = Integer.parseInt(sdf2.format(c.getTime()));

        for(int i = 0;i+2 < str.length;i+=3){

            if(str[i+1].equals("null")) continue;
            if(year <= Integer.parseInt(str[i+1])){
                System.out.println("subject1="+str[i]);
                lo.day = str[i+1];
                lo.subject = str[i];
                lo.next = new Test();
                lo = lo.next;
            }
            else if(year <= Integer.parseInt(str[i+2])) {
                lo.day = str[i + 2];
                lo.subject = str[i];
                lo.next = new Test();
                lo = lo.next;
            }
        }

    }

}