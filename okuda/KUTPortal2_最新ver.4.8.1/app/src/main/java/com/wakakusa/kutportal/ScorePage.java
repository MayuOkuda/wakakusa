package com.wakakusa.kutportal;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.HashMap;


public class ScorePage extends BasePage{

    static int total; //科目区分別修得状況の総計
    static int totalsub; //年度・学期別修得状況の取得単位(総計及び学期別)用の箱
    static double total2; //通算GPA計算用の被除数
    static double sum; //通算GPA計算用の除数
    static String item; //指定された年度(スピナー)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //new!
/*
        DatabaseWriter dbWriter = new DatabaseWriter(this, "score");
        dbWriter.deleteDB();
        ContentValues cvalue = new ContentValues();
        cvalue.put("scode", "112"); //科目コード
        cvalue.put("score", "AA"); //取得単位
        cvalue.put("year", "2016"); //取得年度
        cvalue.put("period", "2Q"); //取得期間
        dbWriter.write.insert(dbWriter.Table_name, null, cvalue);

        cvalue.put("scode", "111");
        cvalue.put("score", "A");
        cvalue.put("year", "2016");
        cvalue.put("period", "1Q");
        dbWriter.write.insert(dbWriter.Table_name, null, cvalue);

        cvalue.put("scode", "124");
        cvalue.put("score", "AA");
        cvalue.put("year", "2015");
        cvalue.put("period", "1Q");
        dbWriter.write.insert(dbWriter.Table_name, null, cvalue);

        cvalue.put("score", "A");
        cvalue.put("year", "2015");
        cvalue.put("period", "1Q");
        dbWriter.write.insert(dbWriter.Table_name, null, cvalue);

        cvalue.put("score", "AA");
        cvalue.put("year", "2015");
        cvalue.put("period", "2Q");
        dbWriter.write.insert(dbWriter.Table_name, null, cvalue);

        cvalue.put("score", "F");
        cvalue.put("year", "2015");
        cvalue.put("period", "3Q");
        dbWriter.write.insert(dbWriter.Table_name, null, cvalue);

        cvalue.put("score", "A");
        cvalue.put("year", "2015");
        cvalue.put("period", "3Q");
        dbWriter.write.insert(dbWriter.Table_name, null, cvalue);

        cvalue.put("score", "AA");
        cvalue.put("year", "2015");
        cvalue.put("period", "4Q");
        dbWriter.write.insert(dbWriter.Table_name, null, cvalue);

        DatabaseWriter dbWriter2 = new DatabaseWriter(this, "student");
        dbWriter2.deleteDB();
        ContentValues cvalue2 = new ContentValues();
        cvalue2.put("adm", "20150401"); //入学年度
        dbWriter2.write.insert(dbWriter2.Table_name, null, cvalue2);

        DatabaseWriter dbWriter3 = new DatabaseWriter(this, "course");
        dbWriter3.deleteDB();
        ContentValues cvalue3 = new ContentValues();
        cvalue3.put("scode", "1121"); //科目コード
        cvalue3.put("period", "1Q"); //科目時期(Q指定)
        cvalue3.put("sjclass", "1"); //単位数
        cvalue3.put("sj","人文・社会科学等科目"); //単位区分(科目別)
        dbWriter3.write.insert(dbWriter3.Table_name, null, cvalue3);

        cvalue3.put("scode", "1122");
        cvalue3.put("period", "1Q");
        cvalue3.put("sjclass", "1");
        cvalue3.put("sj","自然科学等科目");
        dbWriter3.write.insert(dbWriter3.Table_name, null, cvalue3);

        cvalue3.put("scode", "1243");
        cvalue3.put("period", "2Q");
        cvalue3.put("sjclass", "4");
        cvalue3.put("sj","情報学群専門科目");
        dbWriter3.write.insert(dbWriter3.Table_name, null, cvalue3);

        cvalue3.put("scode", "1324");
        cvalue3.put("period", "3Q");
        cvalue3.put("sjclass", "2");
        cvalue3.put("sj","工業系共通科目");
        dbWriter3.write.insert(dbWriter3.Table_name, null, cvalue3);

        cvalue3.put("scode", "155");
        cvalue3.put("period", "4Q");
        cvalue3.put("sjclass", "3");
        cvalue3.put("sj","専門基礎科目");
        dbWriter3.write.insert(dbWriter3.Table_name, null, cvalue3);

        cvalue3.put("scode", "1436");
        cvalue3.put("period", "4Q");
        cvalue3.put("sjclass", "3");
        cvalue3.put("sj","専門発展科目");
        dbWriter3.write.insert(dbWriter3.Table_name, null, cvalue3);
  */

        //科目区分別修得状況(呼び出し処理)
        scoresjc();

        //年度指定処理(スピナー)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // アイテムを追加
        DatabaseReader dbReader = new DatabaseReader(this, "student");
        String ymd = dbReader.readDB(new String[]{"adm"}, 0).substring(0, 4);
        adapter.add(ymd + "年度");
        int ymdi = Integer.parseInt(ymd);
        ymdi++;
        ymd = String.valueOf(ymdi);
        adapter.add(ymd + "年度");
        int ymdi2 = Integer.parseInt(ymd);
        ymdi++;
        ymd = String.valueOf(ymdi);
        adapter.add(ymd + "年度");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // アダプターを設定
        spinner.setAdapter(adapter);

        // スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します(スピナー処理)
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                // 選択されたアイテムを取得
                String spinneritem = (String) spinner.getSelectedItem();
                item = spinneritem;

                scoreapp();
                scorecount();
                Toast.makeText(ScorePage.this, String.format("%sが表示されました。", spinneritem), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    //科目区分別修得状況(現在dbReadが使用できない、カラム無し状態)
    public void scoresjc(){
        total = 0;
        DatabaseReader dbReader = new DatabaseReader(this, "course");

        String[] strcs = {"sj"};
        String sjcl ="course.sjclass=? AND course.scode = score.scode AND score.score !=? AND score.score !=?";
        String sjcl_sub ="course.sjclasssub=? AND course.scode = score.scode AND score.score !=? AND score.score !=?";
        String[] cs =  dbReader.readDB3(strcs, sjcl, new String[]{"人文・社会科学等科目", "F", ""},"course, score").split("\n");
        String[] cs2 = dbReader.readDB3(strcs, sjcl, new String[]{"自然科学等科目", "F",""},"course, score").split("\n");
        String[] cs3 = dbReader.readDB3(strcs, sjcl , new String[]{"専門科目", "F", ""},"course,score").split("\n");
        String[] cs4 = dbReader.readDB3(strcs, sjcl_sub , new String[]{"工学系共通科目", "F", ""},"course, score").split("\n");
        String[] cs5 = dbReader.readDB3(strcs, sjcl_sub , new String[]{"専門基礎科目", "F",""},"course,score").split("\n");
        String[] cs6 = dbReader.readDB3(strcs, sjcl_sub , new String[]{"専門発展科目", "F", ""},"course, score").split("\n");
        String[] cs7 = dbReader.readDB3(strcs, sjcl_sub, new String[]{"専攻領域科目", "F", ""},"course, score").split("\n");
        String[] cs8 = dbReader.readDB3(strcs, sjcl_sub, new String[]{"他学群・他学部専門科目", "F", ""},"course,score").split("\n");


        int csvjc = countsjc(cs);
        int csvjc2 = countsjc(cs2);
        int csvjc3 = countsjc(cs3);
        int csvjc4 = countsjc(cs4);
        int csvjc5 = countsjc(cs5);
        int csvjc6 = countsjc(cs6);
        int csvjc7 = countsjc(cs7);
        int csvjc8 = countsjc(cs8);

        TextView csv = (TextView) findViewById(R.id.csv);
        TextView csv2 = (TextView) findViewById(R.id.csv2);
        TextView csv3 = (TextView) findViewById(R.id.csv3);
        TextView csv4 = (TextView) findViewById(R.id.csv4);
        TextView csv5 = (TextView) findViewById(R.id.csv5);
        TextView csv6 = (TextView) findViewById(R.id.csv6);
        TextView csv7 = (TextView) findViewById(R.id.csv7);
        TextView csv8 = (TextView) findViewById(R.id.csv8);
        TextView csvtotal = (TextView) findViewById(R.id.csvtotal);

        csv.setText("");
        csv2.setText("");
        csv3.setText("");
        csv4.setText("");
        csv5.setText("");
        csv6.setText("");
        csv7.setText("");
        csv8.setText("");
        csvtotal.setText("");

        if (csvjc != 0) csv.setText(String.valueOf(csvjc));
        if (csvjc2 != 0) csv2.setText(String.valueOf(csvjc2));
        if (csvjc3 != 0) csv3.setText(String.valueOf(csvjc3));
        if (csvjc4 != 0) csv4.setText(String.valueOf(csvjc4));
        if (csvjc5 != 0) csv5.setText(String.valueOf(csvjc5));
        if (csvjc6 != 0) csv6.setText(String.valueOf(csvjc6));
        if (csvjc7 != 0) csv7.setText(String.valueOf(csvjc7));
        if (csvjc8 != 0) csv8.setText(String.valueOf(csvjc8));
        if (total != 0) csvtotal.setText(String.valueOf(total));

    }

    //年度・学期別修得状況
    public void scorecount() {
        int year = 0; //年度指定
        int totalc = 0; //取得単位数の総計
        int total = 0; //取得単位の総計
        int totalcu = 0; //取得単位数(1学期)
        int totalcd = 0; //取得単位数(2学期)
        int totalu = 0; //取得単位数(1学期)
        int totald =0; //取得単位数(2学期)
        DatabaseReader dbReader = new DatabaseReader(this, "score");
        DatabaseReader dbReader2 = new DatabaseReader(this, "student");
        DatabaseReader dbReader3 = new DatabaseReader(this, "course");
        //現在の年度取得及び3年間の指定
        String ymd = dbReader2.readDB(new String[]{"adm"}, 0).substring(0, 4); //2017年度
        TextView scymd = (TextView) findViewById(R.id.scymd);
        scymd.setText("");
        scymd.setText(ymd + "年度");
        int ymds = Integer.parseInt(ymd);
        ymds++;
        String ymd2 = String.valueOf(ymds); //2018年度
        int ymdt = Integer.parseInt(ymd2);
        ymdt++;
        String ymd3 = String.valueOf(ymdt); //2019年度
        item = item.substring(0,4);
        year = Integer.valueOf(item);

        TextView scv = (TextView) findViewById(R.id.sjctotal); //取得単位数の総計
        TextView scv2 = (TextView) findViewById(R.id.sjc); //取得単位数(1学期)
        TextView scv3 = (TextView) findViewById(R.id.sjc2); //取得単位数(2学期)
        TextView scv4 = (TextView) findViewById(R.id.sjtotal); //取得単位の総計
        TextView scv5 = (TextView) findViewById(R.id.sj); //取得単位(1学期)
        TextView scv6 = (TextView) findViewById(R.id.sj2); //取得単位数(2学期)
        scv.setText("");
        scv2.setText("");
        scv3.setText("");
        scv4.setText("");
        scv5.setText("");
        scv6.setText("");

        //1年目
        int ft = 0;
        String[] str = {"score"};
        String[] str0 = {"sj"};
        String[] sc11 = dbReader.readDB2(str, "year=? AND period=?", new String[]{ymd, "1Q"}).split("\n");
        String[] s11 = dbReader.readDB3(str0, "score.scode = course.scode AND score.year=? AND course.period=? AND score.score!=? AND score.score!=?", new String[]{ymd, "1Q", "F", ""}, "score, course").split("\n");
        int f11 = 0;
        if(sc11 != null) f11 = fcheck(sc11);
        if(s11 != null) ft = fcheck2(s11);
        totalcu += f11;
        totalc += f11;
        totalu += ft;
        total += ft;
        ft = 0;
        String[] sc12 = dbReader.readDB2(str, "year=? AND (period=? OR period=?)", new String[]{ymd, "2Q", "1学期"}).split("\n");
        String[] s12 = dbReader.readDB3(str0, "score.scode = course.scode AND score.year=? AND (course.period=? OR course.period=?) AND score.score!=? AND score.score!=?", new String[]{ymd, "2Q","1学期", "F", ""}, "score, course").split("\n");
        int f12 = 0;
        if(sc12 != null) f12 = fcheck(sc12);
        if(s12 != null) ft = fcheck2(s12);
        totalcu += f12;
        totalc += f12;
        totalu += ft;
        total += ft;
        ft = 0;
        String[] sc13 = dbReader.readDB2(str, "year=? AND period=?", new String[]{ymd, "3Q"}).split("\n");
        String[] s13 = dbReader.readDB3(str0, "score.scode = course.scode AND score.year=? AND course.period=? AND score.score!=? AND score.score!=?", new String[]{ymd, "3Q", "F", ""}, "score, course").split("\n");
        int f13 = 0;
        if(sc13 != null) f13 = fcheck(sc13);
        if(s13 != null) ft = fcheck2(s13);
        totalcd += f13;
        totalc += f13;
        totald += ft;
        total += ft;
        ft = 0;
        String[] sc14 = dbReader.readDB2(str, "year=? AND (period=? OR period=?) ", new String[]{ymd, "4Q", "2学期"}).split("\n");
        String[] s14 = dbReader.readDB3(str0, "score.scode = course.scode AND score.year=? AND (course.period=? OR course.period=?) AND score.score!=? AND score.score!=?", new String[]{ymd, "4Q","2学期", "F", ""}, "score, course").split("\n");
        int f14 = 0;
        if(sc14 != null) f14 = fcheck(sc14);
        if(s14 != null) ft = fcheck2(s14);
        totalcd += f14;
        totalc += f14;
        totald += ft;
        total += ft;
        ft = 0;

        if(totalcu != 0) scv2.setText(String.valueOf(totalcu));
        if(totalcd != 0) scv3.setText(String.valueOf(totalcd));
        if(totalu != 0) scv5.setText(String.valueOf(totalu));
        if(totald != 0) scv6.setText(String.valueOf(totald));

        //2年目
        totalcu = 0;
        totalcd = 0;
        totalu = 0;
        totald = 0;
        String[] str2 = {"score"};
        String[] sc21 = dbReader.readDB2(str2, "year=? AND period=?", new String[]{ymd2, "1Q"}).split("\n");
        String[] s21 = dbReader.readDB3(str0, "score.scode = course.scode AND score.year=? AND course.period=? AND score.score!=? AND score.score!=?", new String[]{ymd2, "1Q", "F", ""}, "score, course").split("\n");
        int f21 = 0;
        if(sc21 != null) f21 = fcheck(sc21);
        if(s21 != null) ft = fcheck2(s21);
        totalcu += f21;
        totalc += f21;
        totalu += ft;
        total += ft;
        ft = 0;
        String[] sc22 = dbReader.readDB2(str2, "year=? AND period=? ", new String[]{ymd2, "2Q"}).split("\n");
        String[] s22 = dbReader.readDB3(str0, "score.scode = course.scode AND score.year=? AND (course.period=? OR course.period=?) AND score.score!=? AND score.score!=?", new String[]{ymd2, "2Q","1学期", "F", ""}, "score, course").split("\n");
        int f22 = 0;
        if(sc22 != null) f22 = fcheck(sc22);
        if(s22 != null) ft = fcheck2(s22);
        totalcu += f22;
        totalc += f22;
        totalu += ft;
        total += ft;
        ft = 0;
        String[] sc23 = dbReader.readDB2(str2, "year=? AND period=?", new String[]{ymd2, "3Q"}).split("\n");
        String[] s23 = dbReader.readDB3(str0, "score.scode = course.scode AND score.year=? AND course.period=? AND score.score!=? AND score.score!=?", new String[]{ymd2, "3Q", "F", ""}, "score, course").split("\n");
        int f23 = 0;
        if(sc23 != null) f23 = fcheck(sc23);
        if(s23 != null) ft = fcheck2(s23);
        totalcd += f23;
        totalc += f23;
        totald += ft;
        total += ft;
        ft = 0;
        String[] sc24 = dbReader.readDB2(str2, "year=? AND period=? ", new String[]{ymd2, "4Q"}).split("\n");
        String[] s24 = dbReader.readDB3(str0, "score.scode = course.scode AND score.year=? AND (course.period=? OR course.period=?) AND score.score!=? AND score.score!=?", new String[]{ymd2, "4Q","2学期", "F", ""}, "score, course").split("\n");
        int f24 = 0;
        if(sc24 != null) f24 = fcheck(sc24);
        if(s24 != null) ft = fcheck2(s24);
        totalcd += f24;
        totalc += f24;
        totald += ft;
        total += ft;
        ft = 0;

        if(year == ymds || year == ymdt) {
            scymd.setText("");
            scv2.setText("");
            scv3.setText("");
            scv5.setText("");
            scv6.setText("");
            scymd.setText(ymds + "年度");
            if(totalcu != 0) scv2.setText(String.valueOf(totalcu));
            if(totalcd != 0) scv3.setText(String.valueOf(totalcd));
            if(totalu != 0) scv5.setText(String.valueOf(totalu));
            if(totald != 0) scv6.setText(String.valueOf(totald));
        }

        //3年目
        totalcu = 0; //
        totalcd = 0;
        totalu = 0;
        totald = 0;
        String[] str3 = {"score"};
        String[] sc31 = dbReader.readDB2(str3, "year=? AND period=?", new String[]{ymd3, "1Q"}).split("\n");
        String[] s31 = dbReader.readDB3(str0, "score.scode = course.scode AND score.year=? AND course.period=? AND score.score!=? AND score.score!=?", new String[]{ymd3, "1Q", "F", ""}, "score, course").split("\n");
        int f31 = 0;
        if(sc31 != null) f31 = fcheck(sc31);
        if(s31 != null) ft = fcheck2(s31);
        totalcu += f31;
        totalc += f31;
        totalu += ft;
        total += ft;
        ft = 0;
        String[] sc32 = dbReader.readDB2(str3, "year=? AND period=? ", new String[]{ymd3, "2Q"}).split("\n");
        String[] s32 = dbReader.readDB3(str0, "score.scode = course.scode AND score.year=? AND (course.period=? OR course.period=?) AND score.score!=? AND score.score!=?", new String[]{ymd3, "2Q","1学期", "F", ""}, "score, course").split("\n");
        int f32 = 0;
        if(sc32 != null) f32 = fcheck(sc32);
        if(s32 != null) ft = fcheck2(s32);
        totalcu += f32;
        totalc += f32;
        totalu += ft;
        total += ft;
        ft = 0;
        String[] sc33 = dbReader.readDB2(str3, "year=? AND period=?", new String[]{ymd3, "3Q"}).split("\n");
        String[] s33 = dbReader.readDB3(str0, "score.scode = course.scode AND score.year=? AND course.period=? AND score.score!=? AND score.score!=?", new String[]{ymd3, "3Q", "F", ""}, "score, course").split("\n");
        int f33 = 0;
        if(sc33 != null) f33 = fcheck(sc33);
        if(s33 != null) ft = fcheck2(s33);
        totalcd += f33;
        totalc += f33;
        totald += ft;
        total += ft;
        ft = 0;
        String[] sc34 = dbReader.readDB2(str3, "year=? AND period=? ", new String[]{ymd3, "4Q"}).split("\n");
        String[] s34 = dbReader.readDB3(str0, "score.scode = course.scode AND score.year=? AND (course.period=? OR course.period=?) AND score.score!=? AND score.score!=?", new String[]{ymd3, "4Q","2学期", "F", ""}, "score, course").split("\n");
        int f34 = 0;
        if(sc34 != null) f34 = fcheck(sc34);
        if(s34 != null) ft = fcheck2(s34);
        totalcd += f34;
        totalc += f34;
        totald += ft;
        total += ft;
        ft = 0;

        if(year == ymdt) {
            scymd.setText("");
            scv2.setText("");
            scv3.setText("");
            scv5.setText("");
            scv6.setText("");
            scymd.setText(ymdt + "年度");
            if(totalcu != 0) scv2.setText(String.valueOf(totalcu));
            if(totalcd != 0) scv3.setText(String.valueOf(totalcd));
            if(totalu != 0) scv5.setText(String.valueOf(totalu));
            if(totald != 0) scv6.setText(String.valueOf(totald));
        }
        if(totalc != 0) scv.setText(String.valueOf(totalc));
        if(total != 0) scv4.setText(String.valueOf(total));
    }

    //GPA
    public void scoreapp() {
        total2 = 0;
        sum = 0;

        DatabaseReader dbReader = new DatabaseReader(this, "score");
        DatabaseReader dbReader2 = new DatabaseReader(this, "student");
        //現在の年度取得及び3年間の指定
        String ymd = dbReader2.readDB(new String[]{"adm"}, 0).substring(0, 4); //2017年度
        TextView scymd2 = (TextView) findViewById(R.id.scymd2);
        scymd2.setText("");
        scymd2.setText(ymd + "年度");
        int ymds = Integer.parseInt(ymd);
        ymds++;
        String ymd2 = String.valueOf(ymds); //2018年度
        int ymdt = Integer.parseInt(ymd2);
        ymdt++;
        String ymd3 = String.valueOf(ymdt); //2019年度
        item = item.substring(0,4);
        int year = Integer.valueOf(item);

        //1年目
        //期間GPA 1Q
        String[] str = {"score"};
        String[] s = dbReader.readDB2(str, "year=? AND period=?", new String[]{item, "1Q"}).split("\n");

        TextView sv = (TextView) findViewById(R.id.gpa11q);
        double cs = Changescore(s);
        sv.setText("");
        if (cs != 0) sv.setText(String.valueOf(cs));

        //通算GPA 1Q
        double tps = TotalSum();
        TextView svt = (TextView) findViewById(R.id.gpa21q);
        svt.setText("");
        if (tps != 0) svt.setText(String.valueOf(tps));

        //期間GPA 2Q
        String[] str2 = {"score"};
        String[] s2 = dbReader.readDB2(str2, "year=? AND (period=? OR period=?)", new String[]{item, "2Q","1学期"}).split("\n");
        TextView sv2 = (TextView) findViewById(R.id.gpa12q);
        double cs2 = Changescore(s2);
        sv2.setText("");
        if (cs2 != 0) sv2.setText(String.valueOf(cs2));

        //通算GPA 2Q and 1学期
        double tps2 = TotalSum();
        TextView svt2 = (TextView) findViewById(R.id.gpa22q);
        svt2.setText("");
        TextView svt12 = (TextView) findViewById(R.id.gpa21p);
        svt12.setText("");
        if (tps2 != 0) {
            svt2.setText(String.valueOf(tps2));
            svt12.setText(String.valueOf(tps2));
        }

        //期間GPA 3Q
        String[] str3 = {"score"};
        String[] s3 = dbReader.readDB2(str3, "year=? AND period=?", new String[]{item, "3Q"}).split("\n");
        TextView sv3 = (TextView) findViewById(R.id.gpa13q);
        double cs3 = Changescore(s3);
        sv3.setText("");
        if (cs3 != 0) sv3.setText(String.valueOf(cs3));

        //通算GPA 3Q
        double tps3 = TotalSum();
        TextView svt3 = (TextView) findViewById(R.id.gpa23q);
        svt3.setText("");
        if (tps3 != 0) svt3.setText(String.valueOf(tps3));

        //期間GPA 4Q
        String[] str4 = {"score"};
        String[] s4 = dbReader.readDB2(str4, "year=? AND (period=? OR period=?)", new String[]{item, "4Q", "2学期"}).split("\n");
        TextView sv4 = (TextView) findViewById(R.id.gpa14q);
        double cs4 = Changescore(s4);
        sv4.setText("");
        if (cs4 != 0) sv4.setText(String.valueOf(cs4));

        //通算GPA 4Q and 2学期 and 年間
        double tps4 = TotalSum();
        TextView svt4 = (TextView) findViewById(R.id.gpa24q);
        svt4.setText("");
        TextView svt34 = (TextView) findViewById(R.id.gpa22p);
        svt34.setText("");
        TextView svt1234 = (TextView) findViewById(R.id.gpa22t);
        svt1234.setText("");
        if (tps4 != 0) {
            svt4.setText(String.valueOf(tps4));
            svt34.setText(String.valueOf(tps4));
            svt1234.setText(String.valueOf(tps4));
        }

        //期間GPA 1学期
        String[] s12 = new String[s.length + s2.length];
        System.arraycopy(s,0,s12,0,s.length);
        System.arraycopy(s2,0,s12,s.length,s2.length);
        //System.out.println(Arrays.toString(s12));
        TextView sv12 = (TextView) findViewById(R.id.gpa11p);
        double cs12 = Changescore2(s12);
        sv12.setText("");
        if (cs12 != 0) sv12.setText(String.valueOf(cs12));

        //期間GPA 2学期
        String[] s34 = new String[s3.length + s4.length];
        System.arraycopy(s3,0,s34,0,s3.length);
        System.arraycopy(s4,0,s34,s3.length,s4.length);
        //System.out.println(Arrays.toString(s34));
        TextView sv34 = (TextView) findViewById(R.id.gpa12p);
        double cs34 = Changescore2(s34);
        sv34.setText("");
        if (cs34 != 0)  sv34.setText(String.valueOf(cs34));

        //期間GPA 年間
        String[] s1234 = new String[s12.length + s34.length];
        System.arraycopy(s12,0,s1234,0,s12.length);
        System.arraycopy(s34,0,s1234,s12.length,s34.length);
        //System.out.println(Arrays.toString(s1234));
        TextView sv1234 = (TextView) findViewById(R.id.gpa11t);
        double cs1234 = Changescore2(s1234);
        sv1234.setText("");
        if (cs1234 != 0) sv1234.setText(String.valueOf(cs1234));

        //2年目
        //期間GPA 1Q
        if(year == ymds || year == ymdt) {
            scymd2.setText("");
            scymd2.setText(ymd2 + "年度");
            System.out.println(item);
            String[] str_2 = {"score"};
            String[] s_2 = dbReader.readDB2(str_2, "year=? AND period=?", new String[]{item, "1Q"}).split("\n");
            TextView sv_2 = (TextView) findViewById(R.id.gpa11q);
            //System.out.print(total +"/"+sum+"/");
            double cs_2 = Changescore(s_2);
            sv_2.setText("");
            if (cs_2 != 0) sv_2.setText(String.valueOf(cs_2));

            //通算GPA 1Q//
            double tps_2 = TotalSum();
            TextView svt_2 = (TextView) findViewById(R.id.gpa21q);
            svt_2.setText("");
            if (tps_2 != 0) svt_2.setText(String.valueOf(tps_2));

            //期間GPA 2Q
            String[] str2_2 = {"score"};
            String[] s2_2 = dbReader.readDB2(str2_2, "year=? AND (period=? OR period=?) ", new String[]{item, "2Q", "1学期"}).split("\n");
            TextView sv2_2 = (TextView) findViewById(R.id.gpa12q);
            double cs2_2 = Changescore(s2_2);
            sv2_2.setText("");
            if (cs2_2 != 0) sv2_2.setText(String.valueOf(cs2_2));

            //通算GPA 2Q and 1学期
            double tps2_2 = TotalSum();
            TextView svt2_2 = (TextView) findViewById(R.id.gpa22q);
            svt2_2.setText("");
            TextView svt12_2 = (TextView) findViewById(R.id.gpa21p);
            svt12_2.setText("");
            if (tps2_2 != 0) {
                svt2_2.setText(String.valueOf(tps2_2));
                svt12_2.setText(String.valueOf(tps2_2));
            }

            //期間GPA 3Q
            String[] str3_2 = {"score"};
            String[] s3_2 = dbReader.readDB2(str3_2, "year=? AND period=?", new String[]{item, "3Q"}).split("\n");
            TextView sv3_2 = (TextView) findViewById(R.id.gpa13q);
            double cs3_2 = Changescore(s3_2);
            sv3_2.setText("");
            if (cs3_2 != 0) sv3_2.setText(String.valueOf(cs3_2));

            //通算GPA 3Q
            double tps3_2 = TotalSum();
            TextView svt3_2 = (TextView) findViewById(R.id.gpa23q);
            svt3_2.setText("");
            if (tps3_2 != 0) svt3_2.setText(String.valueOf(tps3_2));

            //期間GPA 4Q
            String[] str4_2 = {"score"};
            String[] s4_2 = dbReader.readDB2(str4_2, "year=? AND (period=? OR period=?)", new String[]{item, "4Q", "2学期"}).split("\n");
            TextView sv4_2 = (TextView) findViewById(R.id.gpa14q);
            double cs4_2 = Changescore(s4_2);
            sv4_2.setText("");
            if (cs4_2 != 0) sv4_2.setText(String.valueOf(cs4_2));

            //通算GPA 4Q and 2学期 and 年間
            double tps4_2 = TotalSum();
            TextView svt4_2 = (TextView) findViewById(R.id.gpa24q);
            svt4_2.setText("");
            TextView svt34_2 = (TextView) findViewById(R.id.gpa22p);
            svt34_2.setText("");
            TextView svt1234_2 = (TextView) findViewById(R.id.gpa22t);
            svt1234_2.setText("");
            if (tps4_2 != 0) {
                svt4_2.setText(String.valueOf(tps4_2));
                svt34_2.setText(String.valueOf(tps4_2));
                svt1234_2.setText(String.valueOf(tps4_2));
            }

            //期間GPA 1学期
            String[] s12_2 = new String[s_2.length + s2_2.length];
            System.arraycopy(s_2,0,s12_2,0,s_2.length);
            System.arraycopy(s2_2,0,s12_2,s.length,s2_2.length);
            //System.out.println(Arrays.toString(s12));
            TextView sv12_2 = (TextView) findViewById(R.id.gpa11p);
            double cs12_2 = Changescore2(s12_2);
            sv12_2.setText("");
            if (cs12_2 != 0) sv12_2.setText(String.valueOf(cs12_2));

            //期間GPA 2学期
            String[] s34_2 = new String[s3_2.length + s4_2.length];
            System.arraycopy(s3_2,0,s34_2,0,s3_2.length);
            System.arraycopy(s4_2,0,s34_2,s3_2.length,s4_2.length);
            //System.out.println(Arrays.toString(s34));
            TextView sv34_2 = (TextView) findViewById(R.id.gpa12p);
            double cs34_2 = Changescore2(s34_2);
            sv34_2.setText("");
            if (cs34_2 != 0)  sv34_2.setText(String.valueOf(cs34_2));

            //期間GPA 年間
            String[] s1234_2 = new String[s12_2.length + s34_2.length];
            System.arraycopy(s12_2,0,s1234_2,0,s12_2.length);
            System.arraycopy(s34_2,0,s1234_2,s12_2.length,s34_2.length);
            //System.out.println(Arrays.toString(s1234));
            TextView sv1234_2 = (TextView) findViewById(R.id.gpa11t);
            double cs1234_2 = Changescore2(s1234_2);
            sv1234_2.setText("");
            if (cs1234_2 != 0) sv1234_2.setText(String.valueOf(cs1234_2));

        }

        //3年目
        //期間GPA 1Q
        if(year == ymdt) {
            scymd2.setText("");
            scymd2.setText(ymd3 + "年度");
            String[] str_3 = {"score"};
            String[] s_3 = dbReader.readDB2(str_3, "year=? AND period=?", new String[]{item, "1Q"}).split("\n");
            TextView sv_3 = (TextView) findViewById(R.id.gpa11q);
            double cs_3 = Changescore(s_3);
            sv_3.setText("");
            if (cs_3 != 0) sv_3.setText(String.valueOf(cs_3));

            //通算GPA 1Q
            double tps_3 = TotalSum();
            TextView svt_3 = (TextView) findViewById(R.id.gpa21q);
            svt_3.setText("");
            if (tps_3 != 0) svt_3.setText(String.valueOf(tps_3));

            //期間GPA 2Q
            String[] str2_3 = {"score"};
            String[] s2_3 = dbReader.readDB2(str2_3, "year=? AND (period=? OR period=?)", new String[]{item, "2Q", "1学期"}).split("\n");
            TextView sv2_3 = (TextView) findViewById(R.id.gpa12q);
            double cs2_3 = Changescore(s2_3);
            sv2_3.setText("");
            if (cs2_3 != 0) sv2_3.setText(String.valueOf(cs2_3));

            //通算GPA 2Q and 1学期
            double tps2_3 = TotalSum();
            TextView svt2_3 = (TextView) findViewById(R.id.gpa22q);
            svt2_3.setText("");
            TextView svt12_3 = (TextView) findViewById(R.id.gpa21p);
            svt12_3.setText("");
            if (tps2_3 != 0) {
                svt2_3.setText(String.valueOf(tps2_3));
                svt12_3.setText(String.valueOf(tps2_3));
            }

            //期間GPA 3Q
            String[] str3_3 = {"score"};
            String[] s3_3 = dbReader.readDB2(str3_3, "year=? AND period=?", new String[]{item, "3Q"}).split("\n");
            TextView sv3_3 = (TextView) findViewById(R.id.gpa13q);
            double cs3_3 = Changescore(s3_3);
            sv3_3.setText("");
            if (cs3_3 != 0) sv3_3.setText(String.valueOf(cs3_3));

            //通算GPA 3Q
            double tps3_3 = TotalSum();
            TextView svt3_3 = (TextView) findViewById(R.id.gpa23q);
            svt3_3.setText("");
            if (tps3_3 != 0) svt3_3.setText(String.valueOf(tps3_3));

            //期間GPA 4Q
            String[] str4_3 = {"score"};
            String[] s4_3 = dbReader.readDB2(str4_3, "year=? AND(period=? OR period=?)", new String[]{item, "4Q", "2学期"}).split("\n");
            TextView sv4_3 = (TextView) findViewById(R.id.gpa14q);
            double cs4_3 = Changescore(s4_3);
            sv4_3.setText("");
            if (cs4_3 != 0) sv4_3.setText(String.valueOf(cs4_3));

            //通算GPA 4Q and 2学期 and 年間
            double tps4_3 = TotalSum();
            TextView svt4_3 = (TextView) findViewById(R.id.gpa24q);
            svt4_3.setText("");
            TextView svt34_3 = (TextView) findViewById(R.id.gpa22p);
            svt34_3.setText("");
            TextView svt1234_3 = (TextView) findViewById(R.id.gpa22t);
            svt1234_3.setText("");
            if (tps4_3 != 0) {
                svt4_3.setText(String.valueOf(tps4_3));
                svt34_3.setText(String.valueOf(tps4_3));
                svt1234_3.setText(String.valueOf(tps4_3));
            }

            //期間GPA 1学期
            String[] s12_3 = new String[s_3.length + s2_3.length];
            System.arraycopy(s_3,0,s12_3,0,s_3.length);
            System.arraycopy(s2_3,0,s12_3,s.length,s2_3.length);
            //System.out.println(Arrays.toString(s12));
            TextView sv12_3 = (TextView) findViewById(R.id.gpa11p);
            double cs12_3 = Changescore2(s12_3);
            sv12_3.setText("");
            if (cs12_3 != 0) sv12_3.setText(String.valueOf(cs12_3));

            //期間GPA 2学期
            String[] s34_3 = new String[s3_3.length + s4_3.length];
            System.arraycopy(s3_3,0,s34_3,0,s3_3.length);
            System.arraycopy(s4_3,0,s34_3,s3_3.length,s4_3.length);
            //System.out.println(Arrays.toString(s34));
            TextView sv34_3 = (TextView) findViewById(R.id.gpa12p);
            double cs34_3 = Changescore2(s34_3);
            sv34_3.setText("");
            if (cs34_3 != 0)  sv34_3.setText(String.valueOf(cs34_3));

            //期間GPA 年間
            String[] s1234_3 = new String[s12_3.length + s34_3.length];
            System.arraycopy(s12_3,0,s1234_3,0,s12_3.length);
            System.arraycopy(s34_3,0,s1234_3,s12_3.length,s34_3.length);
            //System.out.println(Arrays.toString(s1234));
            TextView sv1234_3 = (TextView) findViewById(R.id.gpa11t);
            double cs1234_3 = Changescore2(s1234_3);
            sv1234_3.setText("");
            if (cs1234_3 != 0) sv1234_3.setText(String.valueOf(cs1234_3));

        }

    }

    //科目コード別の取得単位集計
    public int countsjc(String[] s) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("4", 4);
        map.put("3", 3);
        map.put("2", 2);
        map.put("1", 1);
        map.put("0", 0);

        int i = 0;
        for (String str : s){
            System.out.println("s="+str);
            if (map.get(str) != null) i += map.get(str);
        }
        total += i;
        //System.out.println(i);
        return i;
    }

    //取得単位数の集計及び合計数の保持
    public int fcheck(String[] s){
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        //HashMap<String, String> map2 = new HashMap<String, String>();
        map.put("AA", 4);
        map.put("A", 3);
        map.put("B", 2);
        map.put("C", 1);
        map.put("F", 0);

        int count = 0;
        for(String str : s){
            if(map.get(str) != null && map.get(str) != 0) {
                count++;
                //totalsub += map.get(str);
                //if(map2.get(str) == "f") count--;

            }
        }
        System.out.println("count = " + count);
        //System.out.println("totalsub = " + totalsub);
        return count;

    }
    public int fcheck2(String[] s){
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        //HashMap<String, String> map2 = new HashMap<String, String>();
        map.put("2", 2);
        map.put("1", 1);

        int i = 0;
        for(String str : s){
            if(map.get(str) != null) {
                i += map.get(str);

            }
        }
        System.out.println("totalsub = " + i);
        return i;

    }

    //期間GPAのスコア変換及び集計
    public double  Changescore(String[] s){
        HashMap<String,Integer> map = new HashMap<String,Integer>();
        map.put("AA", 4);
        map.put("A", 3);
        map.put("B", 2);
        map.put("C", 1);
        map.put("F", 0); //System.out.println(total+"/"+sum);

        double i = 0;
        for(String str : s){
            if(map.get(str) != null) i += (double) map.get(str);
        }

        total2 += i;
        sum = sum + s.length;


        if(i != 0) {
            i = i / s.length;
            BigDecimal i2 = new BigDecimal(i);
            i2 = i2.setScale(2, BigDecimal.ROUND_HALF_UP);
            i = i2.doubleValue();
        }

        return i;
    }

    public double  Changescore2(String[] s){
        HashMap<String,Integer> map = new HashMap<String,Integer>();
        map.put("AA", 4);
        map.put("A", 3);
        map.put("B", 2);
        map.put("C", 1);
        map.put("F", 0);

        double i = 0;
        for(String str : s){
            if(map.get(str) != null) i += (double) map.get(str);
        }

        if(i != 0) {
            i = i / s.length;
            BigDecimal i2 = new BigDecimal(i);
            i2 = i2.setScale(2, BigDecimal.ROUND_HALF_UP);
            i = i2.doubleValue();
        }
        return i;
    }

    //通算GPA計算
    public double TotalSum(){
        double s = 0;
        s = total2/sum;
        BigDecimal s2 = new BigDecimal(s);
        s2 = s2.setScale(2, BigDecimal.ROUND_HALF_UP);
        s = s2.doubleValue();
        return s;
    }

}
