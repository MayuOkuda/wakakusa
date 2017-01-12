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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestCount extends BasePage {

    static String this_Q;
    int today;
    //現在日時を取得する
    Calendar c = Calendar.getInstance();
    Test testclass;
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

//        //データ入力
//        DatabaseWriter dbr1 = new DatabaseWriter(this,"test");
//        DatabaseWriter dbr2 = new DatabaseWriter(this,"course");
//
//        dbr1.deleteDB();
//        dbr2.deleteDB();
//        ContentValues cvalue = new ContentValues();
//        cvalue.put("scode", "10000"); //科目コード
//        cvalue.put("test1", "20170120"); //取得単位
//        cvalue.put("test2", "20170201"); //取得年度
//        dbr1.write.insert(dbr1.Table_name, null, cvalue);
//
//        cvalue.put("scode", "10001"); //科目コード
//        cvalue.put("test1", "20170202"); //取得単位
//        cvalue.put("test2", "20170301"); //取得年度
//        dbr1.write.insert(dbr1.Table_name, null, cvalue);
//
//        cvalue.put("scode", "10002"); //科目コード
//        cvalue.put("test1", "20170202"); //取得単位
//        cvalue.put("test2", "20170301"); //取得年度
//        dbr1.write.insert(dbr1.Table_name, null, cvalue);
//
//
//        ContentValues cvalue2 = new ContentValues();
//        cvalue2.put("scode","10000");
//        cvalue2.put("subject","文化としての戦略と戦術");
//        cvalue2.put("daytime","火1、木2");
//        cvalue2.put("period","4Q");
//        cvalue2.put("teacher","篠森先生");
//        cvalue2.put("sj","専門発展科目");
//        cvalue2.put("sjclass","2");
//        dbr2.write.insert(dbr2.Table_name,null, cvalue2);
//
//
//        cvalue2.put("scode","10001");
//        cvalue2.put("subject","数学");
//        cvalue2.put("daytime","火3、木3");
//        cvalue2.put("period","4Q");
//        cvalue2.put("room","A106");
//        cvalue2.put("teacher","篠森先生");
//        cvalue2.put("sj","専門発展科目");
//        cvalue2.put("sjclass","2");
//        dbr2.write.insert(dbr2.Table_name,null, cvalue2);
//
//        cvalue2.put("scode","10002");
//        cvalue2.put("subject","キャリアプラン");
//        cvalue2.put("daytime","火4");
//        cvalue2.put("period","4Q");
//        cvalue2.put("room","A106");
//        cvalue2.put("teacher","篠森先生");
//        cvalue2.put("sj","専門発展科目");
//        cvalue2.put("sjclass","2");
//        dbr2.write.insert(dbr2.Table_name,null, cvalue2);

        //年度表示
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        today = Integer.parseInt(sdf.format(c.getTime()));
        //this_Q = TopPage.quartJudge(today);

        DatabaseReader db_R = new DatabaseReader(this, "test");
        String test = db_R.readDB3(new String[]{"subject","test1", "test2"}, "test.scode = course.scode AND course.period='"+this_Q+"'", new String[]{},"test, course");
        String[] str = test.split("\n",0);
        testclass = new Test(str);
        SetTextView();


    }


    //履修状況表示
    void SetTextView() {
        textviewReset();
        Test t = testclass;
        //回す
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


class Test{
    String day;
    String subject;
    Test next;

    Calendar c = Calendar.getInstance();

    Test(){}

    Test(String[] str){
        Test lo = this;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        int year = Integer.parseInt(sdf2.format(c.getTime()));

        for(int i = 0;i+2 < str.length;i+=3){
            if(year <= Integer.parseInt(str[i+1])){
                lo.day = str[i+1];
                lo.subject = str[i];
                lo.next = new Test();
                lo = lo.next;
            }
            else if(year <= Integer.parseInt(str[i+1])) {
                lo.day = str[i + 2];
                lo.subject = str[i];
                lo.next = new Test();
                lo = lo.next;
            }
        }

    }

}