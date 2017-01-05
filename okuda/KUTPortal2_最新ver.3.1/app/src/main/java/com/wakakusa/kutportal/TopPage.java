package com.wakakusa.kutportal;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
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

public class TopPage extends BasePage{

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


    //TextView textview6 = (TextView) findViewById(R.id.hometest);
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

        //ポップ処理のやつ
        setViews();

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
        cvalue.put("daytime","火2、木2");
        cvalue.put("period","4Q");
        cvalue.put("room","A106");
        dbWriter.write.insert(dbWriter.Table_name,null, cvalue);

        //ContentValues cvalue = new ContentValues();
        cvalue.put("scode","1000");
        cvalue.put("subject","数学");
        cvalue.put("daytime","火3、木3");
        cvalue.put("period","4Q");
        cvalue.put("room","A106");
        dbWriter.write.insert(dbWriter.Table_name,null, cvalue);

        cvalue.put("scode","1000");
        cvalue.put("subject","キャリアプラン");
        cvalue.put("daytime","火4");
        cvalue.put("period","4Q");
        cvalue.put("room","A106");
        dbWriter.write.insert(dbWriter.Table_name,null, cvalue);

        cvalue.put("scode","1000");
        cvalue.put("subject","通信網概論");
        cvalue.put("daytime","火5、木5");
        cvalue.put("period","4Q");
        cvalue.put("room","A106");
        dbWriter.write.insert(dbWriter.Table_name,null, cvalue);


//データベース書き込み処理
        DatabaseWriter dbWriter2 = new DatabaseWriter(this, "score");

        ContentValues cvalue2 = new ContentValues();
        cvalue2.put("scode","1000");
        cvalue2.put("year","2017");
        dbWriter.write.insert(dbWriter2.Table_name,null, cvalue2);


        course_db_R = new DatabaseReader(this,"course");
        score_db_R = new DatabaseReader(this, "score");


        //日付けの表示
        //フォーマットパターンを指定して表示する
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd #E");
        TextView tex1 = (TextView) findViewById(R.id.day_text);
        String[] str = sdf.format(c.getTime()).split("#",0);
        tex1.setText(str[0]+ " (" +map.get(str[1]) + "曜日)");
        //時間割の表示
        Today today = todayDatabase(str[0]);
        todayStudy(today, map.get(str[1]));
        findViewById(R.id.lef_button).setVisibility(View.INVISIBLE);
        dayflag = 0;

    }


    //画面の右三角ボタンの処理
    public void rightButton(View view){

        if(0 <= dayflag && dayflag <= 1){
            if(dayflag == 0) findViewById(R.id.lef_button).setVisibility(View.VISIBLE);
            // 日を指定
            c.add(c.DAY_OF_MONTH, 1);
            //フォーマットパターンを指定して表示する
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd #E");
            TextView tex1 = (TextView) findViewById(R.id.day_text);
            String[] str = sdf.format(c.getTime()).split("#",0);
            tex1.setText(str[0]+ " (" +map.get(str[1]) + "曜日)");
            //時間割の表示
            Today today = todayDatabase(str[0]);
            todayStudy(today, map.get(str[1]));
            dayflag++;
            if(dayflag == 2) findViewById(R.id.ri_button).setVisibility(View.INVISIBLE);
        }

    }
    //画面の左三角ボタンの処理
    public void leftButton(View view){

        if(1 <= dayflag && dayflag <= 2){
            // 右側矢印がみえるようになる処理
            if(dayflag == 2) findViewById(R.id.ri_button).setVisibility(View.VISIBLE);
            // 日を指定
            c.add(c.DAY_OF_MONTH, -1);
            //フォーマットパターンを指定して表示する
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd #E");
            TextView tex1 = (TextView) findViewById(R.id.day_text);
            String[] str = sdf.format(c.getTime()).split("#",0);
            tex1.setText(str[0]+ " (" +map.get(str[1]) + "曜日)");
            //時間割の表示
            Today today = todayDatabase(str[0]);
            todayStudy(today, map.get(str[1]));
            dayflag--;
            if(dayflag == 0) findViewById(R.id.lef_button).setVisibility(View.INVISIBLE);
        }
    }

    //設定ボタンの処理
    public void optionButton(View view){
        Intent intent = new Intent();
        intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.OptionPage");
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
    private View.OnClickListener onClick_textview = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(TopPage.this)
                    .setTitle("文化としての戦略と戦術")
                    .setMessage("教室　　：K101" + "\n" + "担当教員：篠森 敬三" + "\n" + "単位区分：選択" + "\n" + "単位数　：2")
                    .setPositiveButton("OK", null)
                    .show();
        }
    };
    private View.OnClickListener onClick_textview2 = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(TopPage.this)
                    .setTitle("コンパイラ")
                    .setMessage("教室　　：A106" + "\n" + "担当教員：鵜川 始陽" + "\n" + "単位区分：選択" + "\n" + "単位数　：2")
                    .setPositiveButton("OK", null)
                    .show();
        }
    };
    private View.OnClickListener onClick_textview3 = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(TopPage.this)
                    .setTitle("データベース")
                    .setMessage("教室　　：A106" + "\n" + "担当教員：横山さん" + "\n" + "単位区分：選択" + "\n" + "単位数　：2")
                    .setPositiveButton("OK", null)
                    .show();
        }
    };
    private View.OnClickListener onClick_textview4 = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(TopPage.this)
                    .setTitle("コンピュータグラフィックス")
                    .setMessage("教室　　：A106" + "\n" + "担当教員：栗原 徹" + "\n" + "単位区分：選択" + "\n" + "単位数　：2")
                    .setPositiveButton("OK", null)
                    .show();
        }
    };
    private View.OnClickListener onClick_textview5 = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(TopPage.this)
                    .setTitle("ソフトウェア工学")
                    .setMessage("教室　　：A106" + "\n" + "担当教員：高田さん" + "\n" + "単位区分：選択" + "\n" + "単位数　：2")
                    .setPositiveButton("OK", null)
                    .show();
        }
    };
    private View.OnClickListener onClick_textview6 = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(TopPage.this)
                    .setTitle("テスト一覧")
                    .setMessage("12/6" + "\n" + "　　 1. 文化としての戦略と戦術")
                    .setPositiveButton("OK", null)
                    .show();
        }
    };



    //クオーター判定のプログラム
    String quartJudge(int today){
        System.out.println("today="+today);
        String quart = "";
        if(403<= today && today <=606) quart = "1Q";
        else if(608<= today && today <=806) quart = "2Q";
        else if(1001<= today && today <=1205) quart = "3Q";
        else if((1207 <= today && today <= 1222) ||
                (103 <= today && today <= 218)) quart = "4Q";
        System.out.println("Q="+quart);
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


        String[] str2 = {"scode", "subject", "room", "daytime"};
        //属性名３が値３の属性１と属性２のデータを取ってくる。
        String inputdata = course_db_R.readDB2(str2, "period =?", new String[]{q});

        //今年の今クオータの中身をクラス分け
        Today today1 = new Today(inputdata.split("\n", 0), scode.split("\n", 0));
        return today1;
    }

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
                        textview.setText(t.subject);
                        roomtext.setText(t.room);
                    }
                    else if(time.substring(1,2).equals("2")){
                        textview2.setText(t.subject);
                        roomtext2.setText(t.room);
                    }
                    else if(time.substring(1,2).equals("3")){
                        textview3.setText(t.subject);
                        roomtext3.setText(t.room);
                    }
                    else if(time.substring(1,2).equals("4")){
                        textview4.setText(t.subject);
                        roomtext4.setText(t.subject);
                    }
                    else if(time.substring(1,2).equals("5")){
                        textview5.setText(t.subject);
                        roomtext5.setText(t.room);
                    }
                }
            }
            t = t.next;
        }

    }
    //表示の初期化
    void textviewReset(){
        textview.setText("　　　　　　　　　　");
        textview2.setText("     ");
        textview3.setText("     ");
        textview4.setText("     ");
        textview5.setText("     ");
        roomtext.setText("   ");
        roomtext2.setText("   ");
        roomtext3.setText("   ");
        roomtext4.setText("   ");
        roomtext5.setText("   ");

    }



}

class Today{
    String scode;
    String subject;
    String room;
    String[] daytime;
    Today next;

    Today(){
    }

    Today(String[] str1, String[] str2){
        Today koko = this;

        for(int i = 0; i + 4 <= str1.length ; i +=4){
            if(mawasu(str1[i],str2)) continue;
            koko.scode = str1[i];
            koko.subject = str1[i+1];
            koko.room = str1[i+2];
            koko.daytime = str1[i+3].split("、",0);
            koko.next = new Today();
            koko = koko.next;
        }

    }
    boolean mawasu(String s, String[] str){

        boolean flag = true;

        for(String i : str)
            if(s.equals(i)) flag = false;

        return flag;
    }

}