

package com.wakakusa.kutportal;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.util.ArrayMap;
import android.content.Intent;
import android.app.AlertDialog;//アラートダイアログ(ポップアップ表示)
import android.widget.TextView;//アラートダイアログ用
import android.widget.ImageView;//ポップアップ画像表示用
import android.widget.ImageButton;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TopPage extends BasePage {

    static int dayflag = 0;
    static DatabaseReader course_db_R;
    static DatabaseReader score_db_R;

    //時間割表示用
    TextView textview;
    TextView textview2;
    TextView textview3;
    TextView textview4;
    TextView textview5;
    TextView roomtext;
    TextView roomtext2;
    TextView roomtext3;
    TextView roomtext4;
    TextView roomtext5;

    //現在日時を取得する
    Calendar c = Calendar.getInstance();


    Map<String, String> map = new HashMap<String, String>() {
        {   put("Sun","日");
            put("Mon","月");
            put("Tue","火");
            put("Wed","水");
            put("Thu","木");
            put("Fri","金");
            put("Sat","土");
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        LoadingPage.actFlag.setFlagState(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textview = (TextView) findViewById(R.id.firstclass);
        textview2 = (TextView) findViewById(R.id.secondclass);
        textview3 = (TextView) findViewById(R.id.thirdclass);
        textview4 = (TextView) findViewById(R.id.fourthclass);
        textview5 = (TextView) findViewById(R.id.fifthclass);


        //教室表示の処理
        roomtext = (TextView) findViewById(R.id.roomtext);
        roomtext2 = (TextView) findViewById(R.id.roomtext2);
        roomtext3 = (TextView) findViewById(R.id.roomtext3);
        roomtext4 = (TextView) findViewById(R.id.roomtext4);
        roomtext5 = (TextView) findViewById(R.id.roomtext5);



        //データベース書き込み処理
        DatabaseWriter dbWriter = new DatabaseWriter(this, "course");
        dbWriter.deleteDB();

        ContentValues cvalue = new ContentValues();
        cvalue.put("scode","1000");
        cvalue.put("subject","文化としての戦略と戦術");
        cvalue.put("daytime","火1、木2");
        cvalue.put("period","4Q");
        cvalue.put("teacher","篠森先生");
        cvalue.put("sj","専門発展科目");
        cvalue.put("sjclass","2");
        dbWriter.write.insert(dbWriter.Table_name,null, cvalue);

        //ContentValues cvalue = new ContentValues();
        cvalue.put("scode","1000");
        cvalue.put("subject","数学");
        cvalue.put("daytime","火3、木3");
        cvalue.put("period","4Q");
        cvalue.put("room","A106");
        cvalue.put("teacher","篠森先生");
        cvalue.put("sj","専門発展科目");
        cvalue.put("sjclass","2");
        dbWriter.write.insert(dbWriter.Table_name,null, cvalue);

        cvalue.put("scode","1000");
        cvalue.put("subject","キャリアプラン");
        cvalue.put("daytime","火4");
        cvalue.put("period","4Q");
        cvalue.put("room","A106");
        cvalue.put("teacher","篠森先生");
        cvalue.put("sj","専門発展科目");
        cvalue.put("sjclass","2");
        dbWriter.write.insert(dbWriter.Table_name,null, cvalue);

        cvalue.put("scode","1000");
        cvalue.put("subject","通信網概論");
        cvalue.put("daytime","火5、木5");
        cvalue.put("period","4Q");
        cvalue.put("room","A106");
        cvalue.put("teacher","篠森先生");
        cvalue.put("sj","専門発展科目");
        cvalue.put("sjclass","2");
        dbWriter.write.insert(dbWriter.Table_name,null, cvalue);


        cvalue.put("scode","1004");
        cvalue.put("subject","倫理学");
        cvalue.put("daytime","1学期");
        cvalue.put("period","集中");
        cvalue.put("room","A106");
        cvalue.put("teacher","篠森先生");
        cvalue.put("sj","専門発展科目");
        cvalue.put("sjclass","2");
        dbWriter.write.insert(dbWriter.Table_name,null, cvalue);

        cvalue.put("scode","1005");
        cvalue.put("subject","倫理学");
        cvalue.put("daytime","1学期");
        cvalue.put("period","集中");
        cvalue.put("room","A106");
        cvalue.put("teacher","篠森先生");
        cvalue.put("sj","専門発展科目");
        cvalue.put("sjclass","2");
        dbWriter.write.insert(dbWriter.Table_name,null, cvalue);


        //データベース書き込み処理
        DatabaseWriter dbWriter2 = new DatabaseWriter(this, "score");
        dbWriter2.deleteDB();
        ContentValues cvalue2 = new ContentValues();
        cvalue2.put("scode","1000");
        cvalue2.put("year","2017");
        dbWriter.write.insert(dbWriter2.Table_name,null, cvalue2);

        cvalue2.put("scode","1005");
        cvalue2.put("year","2017");
        dbWriter.write.insert(dbWriter2.Table_name,null, cvalue2);



        course_db_R = new DatabaseReader(this,"course");
        score_db_R = new DatabaseReader(this, "score");


        //今日の時間割
        threedays("center");
        findViewById(R.id.lef_button).setVisibility(View.INVISIBLE);
        dayflag = 0;

        //ポップ処理のやつ
        setViews();

    }


    //画面の右三角ボタンの処理
    public void rightButton(View view){

        if(0 <= dayflag && dayflag <= 1){
            if(dayflag == 0) findViewById(R.id.lef_button).setVisibility(View.VISIBLE);
            // 日を指定
            c.add(c.DAY_OF_MONTH, 1);
            threedays("right");
            dayflag++;
            if(dayflag == 2) findViewById(R.id.ri_button).setVisibility(View.INVISIBLE);
        }
        //ポップ処理のやつ
        setViews();

    }
    //画面の左三角ボタンの処理
    public void leftButton(View view){

        if(1 <= dayflag && dayflag <= 2){
            // 右側矢印がみえるようになる処理
            if(dayflag == 2) findViewById(R.id.ri_button).setVisibility(View.VISIBLE);
            // 日を指定
            c.add(c.DAY_OF_MONTH, -1);
            threedays("left");
            dayflag--;
            if(dayflag == 0) findViewById(R.id.lef_button).setVisibility(View.INVISIBLE);
        }
        //ポップ処理のやつ
        setViews();
    }

    //日付表示
    void threedays(String dist){
        //フォーマットパターンを指定して表示する
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd #E");
        TextView tex1 = (TextView) findViewById(R.id.day_text);
        String[] str = sdf.format(c.getTime()).split("#",0);
        tex1.setText(str[0]+ " (" +map.get(str[1]) + "曜日)");
        //曜日処理　　　day
        String day = map.get(str[1]);
        if(day == null) day = str[1];
        if(day.equals("日")){
            if(dist.equals("left"))c.add(c.DAY_OF_MONTH, -1);
            else  c.add(c.DAY_OF_MONTH, 1);
            threedays(null);
        }else {
            tex1.setText(str[0] + " (" + day + "曜日)");
            //時間割の表示
            Today today = todayDatabase(str[0]);
            todayStudy(today, day);
        }
    }

    //設定ボタン
    public void optionButton(View view){
        Intent intent = new Intent();
        intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.OptionPage");
        startActivity(intent);
    }

    //テストボタン
    public void testButton(View view){
        Intent intent = new Intent();
        intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TestCount");
        startActivity(intent);
    }


    private void setViews() {

        textview.setOnClickListener(onClick_textview);
        textview2.setOnClickListener(onClick_textview2);
        textview3.setOnClickListener(onClick_textview3);
        textview4.setOnClickListener(onClick_textview4);
        textview5.setOnClickListener(onClick_textview5);
        //textview6.setOnClickListener(onClick_textview6);
    }
    private View.OnClickListener onClick_textview;

    private View.OnClickListener onClick_textview2;

    private View.OnClickListener onClick_textview3;

    private View.OnClickListener onClick_textview4;
    private View.OnClickListener onClick_textview5;




    //クオーター判定のプログラム
    String quartJudge(int today){
        System.out.println("today="+today);
        String quart = "";
        if(403<= today && today <=606) quart = "1Q";
        else if(608<= today && today <=806) quart = "2Q";
        else if(1001<= today && today <=1205) quart = "3Q";
        else if((1207 <= today && today <= 1222) ||
                (103 <= today && today <= 218)) quart = "4Q";
        return quart;
    }

    //クオータからデータベースの中身を引っ張ってくる処理
    Today todayDatabase(String str) {
        //第一引数 str= 日にちYYYY/MM/DD
        String[] s = str.split("/", 0);
        String MD = s[1] + s[2].split(" ", 0)[0];
        int today = Integer.parseInt(MD);
        String q = quartJudge(today);

        String[] str1 = {"scode"};
        //属性名３が値３の属性１と属性２のデータを取ってくる。
        String scode = score_db_R.readDB2(str1, "year=?", new String[]{s[0]});


        String[] str2 = {"scode", "subject", "room", "daytime","teacher","sj","sjclass"};
        //属性名３が値３の属性１と属性２のデータを取ってくる。
        String inputdata = course_db_R.readDB2(str2, "period =?", new String[]{q});

        //今年の今クオータの中身をクラス分け
        Today today1 = new Today(inputdata.split("\n", 0), scode.split("\n", 0));
        return today1;
    }

    static Today[] pop = new Today[10];

    //本日の時間割の表示
    void todayStudy(Today today, String E) {
        textviewReset();
        Today t = today;
        //回す
        while (t.scode != null) {
            for (String time : t.daytime) {
                    System.out.println("t="+time);
                if (time.substring(0,1).equals(E)) {
                    if(time.substring(1,2).equals("1")){
                        pop[0] = t;
                        textview.setText(t.subject);
                        roomtext.setText(t.room);
                        onClick_textview = new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(TopPage.this)
                                        .setTitle(pop[0].subject)
                                        .setMessage("教室　　："+pop[0].room + "\n" + "担当教員："+ pop[0].teacher + "\n" + "単位区分："+pop[0].sj + "\n" + "単位数　："+pop[0].sjclass)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        };
                    }
                    else if(time.substring(1,2).equals("2")){
                        pop[1] = t;
                        textview2.setText(t.subject);
                        roomtext2.setText(t.room);
                        onClick_textview2 = new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(TopPage.this)
                                        .setTitle(pop[1].subject)
                                        .setMessage("教室　　："+pop[1].room + "\n" + "担当教員："+ pop[1].teacher + "\n" + "単位区分："+pop[1].sj + "\n" + "単位数　："+pop[1].sjclass)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        };
                    }
                    else if(time.substring(1,2).equals("3")){
                        pop[2] = t;
                        textview3.setText(t.subject);
                        roomtext3.setText(t.room);
                        onClick_textview3 = new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(TopPage.this)
                                        .setTitle(pop[2].subject)
                                        .setMessage("教室　　："+pop[2].room + "\n" + "担当教員："+ pop[2].teacher + "\n" + "単位区分："+pop[2].sj + "\n" + "単位数　："+pop[2].sjclass)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        };
                    }
                    else if(time.substring(1,2).equals("4")){
                        pop[3] = t;
                        textview4.setText(t.subject);
                        roomtext4.setText(t.room);
                        onClick_textview4 = new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(TopPage.this)
                                        .setTitle(pop[3].subject)
                                        .setMessage("教室　　："+pop[3].room + "\n" + "担当教員："+ pop[3].teacher + "\n" + "単位区分："+pop[3].sj + "\n" + "単位数　："+pop[3].sjclass)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        };
                    }
                    else if(time.substring(1,2).equals("5")){
                        pop[4] = t;
                        textview5.setText(t.subject);
                        roomtext5.setText(t.room);
                        onClick_textview5 = new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(TopPage.this)
                                        .setTitle(pop[4].subject)
                                        .setMessage("教室　　："+pop[4].room + "\n" + "担当教員："+ pop[4].teacher + "\n" + "単位区分："+pop[4].sj + "\n" + "単位数　："+pop[4].sjclass)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        };
                    }
                }
            }
            t = t.next;
        }

    }
    //表示の初期化
    void textviewReset(){
        textview.setText("　　　 　　　　　　　");
        textview2.setText("     ");
        textview3.setText("     ");
        textview4.setText("     ");
        textview5.setText("     ");
        roomtext.setText("    ");
        roomtext2.setText("   ");
        roomtext3.setText("   ");
        roomtext4.setText("   ");
        roomtext5.setText("   ");
        onClick_textview =null;
        onClick_textview2 =null;
        onClick_textview3 =null;
        onClick_textview4 =null;
        onClick_textview5 =null;

    }



}

class Today {
    String scode;
    String subject;
    String room;
    String[] daytime;
    String teacher;
    String sj;
    String sjclass;
    Today next;

    Today() {
    }

    Today(String[] str1, String[] str2) {
        Today koko = this;

        for (int i = 0; i + 7 <= str1.length; i += 7) {
            if (mawasu(str1[i], str2)) continue;
            koko.scode = str1[i];
            koko.subject = str1[i + 1];
            koko.room = str1[i + 2];
            koko.daytime = str1[i + 3].split("、", 0);
            koko.teacher = str1[i+4];
            koko.sj = str1[i+5];
            koko.sjclass= str1[i+6];
            koko.next = new Today();
            koko = koko.next;

        }

    }

    boolean mawasu(String s, String[] str) {

        boolean flag = true;

        for (String i : str)
            if (s.equals(i)) flag = false;

        return flag;
    }

}