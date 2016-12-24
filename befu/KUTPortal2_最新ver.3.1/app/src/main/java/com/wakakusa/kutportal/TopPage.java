package com.wakakusa.kutportal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class TopPage extends BasePage{

    static int dayflag = 0;
    //現在日時を取得する
    Calendar c = Calendar.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setViews();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //日付けの表示

        //フォーマットパターンを指定して表示する
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (E曜日)");
        TextView tex1 = (TextView) findViewById(R.id.day_text);
        tex1.setText(sdf.format(c.getTime()));
        findViewById(R.id.lef_button).setVisibility(View.INVISIBLE);
        dayflag = 0;

    }

    public void rightButton(View view){

        if(0 <= dayflag && dayflag <= 1){
            if(dayflag == 0)
                findViewById(R.id.lef_button).setVisibility(View.VISIBLE);
            // 日を指定
            c.add(c.DAY_OF_MONTH, 1);
            //フォーマットパターンを指定して表示する
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (E曜日)");
            TextView tex1 = (TextView) findViewById(R.id.day_text);
            tex1.setText(sdf.format(c.getTime()));
            dayflag++;
            if(dayflag == 2)
            findViewById(R.id.ri_button).setVisibility(View.INVISIBLE);
        }

    }

    public void leftButton(View view){

        if(1 <= dayflag && dayflag <= 2){
            // 右側矢印がみえるようになる処理
            if(dayflag == 2)
                findViewById(R.id.ri_button).setVisibility(View.VISIBLE);
            // 日を指定
            c.add(c.DAY_OF_MONTH, -1);
            //フォーマットパターンを指定して表示する
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (E曜日)");
            TextView tex1 = (TextView) findViewById(R.id.day_text);
            tex1.setText(sdf.format(c.getTime()));
            dayflag--;
            if(dayflag == 0)
                findViewById(R.id.lef_button).setVisibility(View.INVISIBLE);
        }
    }

    public void optionButton(View view){
        Intent intent = new Intent();
        intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.OptionPage");
        startActivity(intent);
    }

    private void setViews() {
        TextView textview = (TextView) findViewById(R.id.firstclass);
        TextView textview2 = (TextView) findViewById(R.id.secondclass);
        TextView textview3 = (TextView) findViewById(R.id.thirdclass);
        TextView textview4 = (TextView) findViewById(R.id.fourthclass);
        TextView textview5 = (TextView) findViewById(R.id.fifthclass);
        //TextView textview6 = (TextView) findViewById(R.id.hometest);
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
}