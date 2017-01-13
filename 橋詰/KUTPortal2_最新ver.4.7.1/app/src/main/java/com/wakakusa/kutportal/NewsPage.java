package com.wakakusa.kutportal;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.content.ContentValues;
import android.widget.Toast;

import java.util.Arrays;

public class NewsPage extends BasePage {
    static int i=1;
    private View.OnClickListener onClick_textview;
    TextView[] npop = new TextView[11];
    static String s3[];
    String item;
    DatabaseReader rd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setNews();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rd = new DatabaseReader(this, "news");
        final DatabaseWriter dbWriter = new DatabaseWriter(this, "news");


        item = "すべて";

        //String[] str = {"newscode","day","title","adduser","content"};
        //String s = rd.readDB(str,0);
        //System.out.println(s);
        Button btn1 = (Button)findViewById(R.id.news1);
        Button btn2 = (Button)findViewById(R.id.news2);
        Button btn3 = (Button)findViewById(R.id.news3);
        Button btn4 = (Button)findViewById(R.id.deletebutton1);
        Button btn5 = (Button)findViewById(R.id.deletebutton2);
        Button btn6 = (Button)findViewById(R.id.deletebutton3);
        Button btn7 = (Button)findViewById(R.id.deletebutton4);
        Button btn8 = (Button)findViewById(R.id.deletebutton5);
        Button btn9 = (Button)findViewById(R.id.deletebutton6);
        Button btn10 = (Button)findViewById(R.id.deletebutton7);
        Button btn11 = (Button)findViewById(R.id.deletebutton8);
        Button btn12 = (Button)findViewById(R.id.deletebutton9);
        Button btn13 = (Button)findViewById(R.id.deletebutton10);
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1;
                System.out.println(i);
                setNews(); }
        });
        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                i=61;
                System.out.println(i);
                setNews();
            } });
        btn3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                i=121;
                System.out.println(i);
                setNews(); }
        });
        btn4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("\n", 0)) {

                    Log.i("lightbox", "送信内容 : " + y);
                    s2[y] = n; //データの分割
                    y++;
                    if (y == 180) break;

                }


                System.out.println("num=" + s2[0]);
                dbWriter.deleteDB2("newscode",s2[i-1]);
                setNews(); }
        });
        btn5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("\n", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                System.out.println("num=" + s2[0]);
                dbWriter.deleteDB2("newscode",s2[i+5]);
                setNews(); }
        });
        btn6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("\n", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                System.out.println("num=" + s2[0]);
                dbWriter.deleteDB2("newscode",s2[i+11]);
                setNews(); }
        });
        btn7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("\n", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                System.out.println("num=" + s2[0]);
                dbWriter.deleteDB2("newscode",s2[i+17]);
                setNews(); }
        });
        btn8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("\n", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                System.out.println("num=" + s2[0]);
                dbWriter.deleteDB2("newscode",s2[i+23]);
                setNews(); }
        });
        btn9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("\n", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                System.out.println("num=" + s2[0]);
                dbWriter.deleteDB2("newscode",s2[i+29]);
                setNews(); }
        });
        btn10.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("\n", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                System.out.println("num=" + s2[0]);
                dbWriter.deleteDB2("newscode",s2[i+35]);
                setNews(); }
        });
        btn11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("\n", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                System.out.println("num=" + s2[0]);
                dbWriter.deleteDB2("newscode",s2[i+41]);
                setNews(); }
        });
        btn12.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("\n", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                System.out.println("num=" + s2[0]);
                dbWriter.deleteDB2("newscode",s2[i+47]);
                setNews(); }
        });
        btn13.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("\n", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                System.out.println("num=" + s2[0]);
                dbWriter.deleteDB2("newscode",s2[i+53]);
                setNews(); }
        });

        //年度指定処理(スピナー)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // アイテムを追加
        adapter.add("すべて");
        adapter.add("講義関連");
        adapter.add("事務連絡");
        adapter.add("イベント");
        adapter.add("その他");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // アダプターを設定
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                // 選択されたアイテムを取得
                String spinneritem = (String) spinner.getSelectedItem();
                item = spinneritem;
                setNews();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        npop[1] = (TextView)findViewById(R.id.newscontent2);
        npop[2] = (TextView)findViewById(R.id.newscontent5);
        npop[3] = (TextView)findViewById(R.id.newscontent8);
        npop[4] = (TextView)findViewById(R.id.newscontent11);
        npop[5] = (TextView)findViewById(R.id.newscontent14);
        npop[6] = (TextView)findViewById(R.id.newscontent17);
        npop[7] = (TextView)findViewById(R.id.newscontent20);
        npop[8] = (TextView)findViewById(R.id.newscontent23);
        npop[9] = (TextView)findViewById(R.id.newscontent26);
        npop[10] = (TextView)findViewById(R.id.newscontent29);



        String s = getS2();
        System.out.println(s);
        setNews();
    }

    private  void setNews() {
        TextView[] tex = new TextView[31];
        tex[1] = (TextView) findViewById(R.id.newscontent1);
        tex[2] = (TextView) findViewById(R.id.newscontent2);
        tex[3] = (TextView) findViewById(R.id.newscontent3);
        tex[4] = (TextView) findViewById(R.id.newscontent4);
        tex[5] = (TextView) findViewById(R.id.newscontent5);
        tex[6] = (TextView) findViewById(R.id.newscontent6);
        tex[7] = (TextView) findViewById(R.id.newscontent7);
        tex[8] = (TextView) findViewById(R.id.newscontent8);
        tex[9] = (TextView) findViewById(R.id.newscontent9);
        tex[10] = (TextView) findViewById(R.id.newscontent10);
        tex[11] = (TextView) findViewById(R.id.newscontent11);
        tex[12] = (TextView) findViewById(R.id.newscontent12);
        tex[13] = (TextView) findViewById(R.id.newscontent13);
        tex[14] = (TextView) findViewById(R.id.newscontent14);
        tex[15] = (TextView) findViewById(R.id.newscontent15);
        tex[16] = (TextView) findViewById(R.id.newscontent16);
        tex[17] = (TextView) findViewById(R.id.newscontent17);
        tex[18] = (TextView) findViewById(R.id.newscontent18);
        tex[19] = (TextView) findViewById(R.id.newscontent19);
        tex[20] = (TextView) findViewById(R.id.newscontent20);
        tex[21] = (TextView) findViewById(R.id.newscontent21);
        tex[22] = (TextView) findViewById(R.id.newscontent22);
        tex[23] = (TextView) findViewById(R.id.newscontent23);
        tex[24] = (TextView) findViewById(R.id.newscontent24);
        tex[25] = (TextView) findViewById(R.id.newscontent25);
        tex[26] = (TextView) findViewById(R.id.newscontent26);
        tex[27] = (TextView) findViewById(R.id.newscontent27);
        tex[28] = (TextView) findViewById(R.id.newscontent28);
        tex[29] = (TextView) findViewById(R.id.newscontent29);
        tex[30] = (TextView) findViewById(R.id.newscontent30);
        String s = getS2(); //データベースから読み出し
        String s2[] = new String[180];
        int y = 0;

        for(String n : s.split("\n", 0)) {
            s2[y] = n; //データの分割
            y++;
            if(y==180) break;
        }
        s3= Arrays.copyOfRange(s2,i,i+49);
        npop[1].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)
                        .setTitle(s3[0])
                        .setMessage(s3[1] + "\n" + "送信者　" + s3[2] + "\n" + s3[3] + "\n" + s3[4])
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
        npop[2].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)
                        .setTitle(s3[6])
                        .setMessage(s3[7] + "\n" + "送信者　" + s3[8] + "\n" + s3[9] + "\n" + s3[10])
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
        npop[3].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)
                        .setTitle(s3[12])
                        .setMessage(s3[13] + "\n" + "送信者　" + s3[14] + "\n" + s3[15] + "\n" + s3[16])
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
        npop[4].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)
                        .setTitle(s3[18])
                        .setMessage(s3[19] + "\n" + "送信者　" + s3[20] + "\n" + s3[21] + "\n" + s3[22])
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
        npop[5].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)
                        .setTitle(s3[24])
                        .setMessage(s3[25] + "\n" + "送信者　" + s3[26] + "\n" + s3[27] + "\n" + s3[28])
                        .setPositiveButton("OK", null)
                        .show();
            }
        });


        int a = 0;
        int p = 1;
        for ( int num = 1; num+2 <= 30; num+=3) {
            if (s2[i + a] == null) {
                tex[num].setText("");
                tex[num + 1].setText("");
                tex[num + 2].setText("");
                onClick_textview = null;
                npop[p].setOnClickListener(onClick_textview);

            } else{
                tex[num].setText(s2[i + a]);
                tex[num + 1].setText(s2[i + a + 1]);
                tex[num + 2].setText("送信者 " + s2[i + a + 2]);

            }
            a += 6;
            p++;
        }
//        tex4.setText(s2[i + 5]);
//        tex5.setText(s2[i + 6]);
//        tex6.setText("送信者 " + s2[i + 7]);
//        tex7.setText(s2[i + 10]);
//        tex8.setText(s2[i + 11]);
//        tex9.setText("送信者 " + s2[i + 12]);
//        tex10.setText(s2[i + 15]);
//        tex11.setText(s2[i + 16]);
//        tex12.setText("送信者 " + s2[i + 17]);
//        tex13.setText(s2[i + 20]);
//        tex14.setText(s2[i + 21]);
//        tex15.setText("送信者 " + s2[i + 22]);
//        tex16.setText(s2[i + 25]);
//        tex17.setText(s2[i + 26]);
//        tex18.setText("送信者 " + s2[i + 27]);
//        tex19.setText(s2[i + 30]);
//        tex20.setText(s2[i + 31]);
//        tex21.setText("送信者 " + s2[i + 32]);
//        tex22.setText(s2[i + 35]);
//        tex23.setText(s2[i + 36]);
//        tex24.setText("送信者 " + s2[i + 37]);
//        tex25.setText(s2[i + 40]);
//        tex26.setText(s2[i + 41]);
//        tex27.setText("送信者 " + s2[i + 42]);
//        tex28.setText(s2[i + 45]);
//        tex29.setText(s2[i + 46]);
//        tex30.setText("送信者 " + s2[i + 47]);
    }
    String getS2(){
        String s = "null";
        String[] str = {"newscode","day","title","adduser","address","content"};
        if(item.equals("すべて")) s = rd.readDB(str,0); //データベースから読み出し
        else if(item.equals("講義関連")) s = rd.readDB2(str,"neclass=?",new String[]{item});
        else if(item.equals("事務連絡")) s = rd.readDB2(str,"neclass=?",new String[]{item});
        else if(item.equals("イベント")) s = rd.readDB2(str,"neclass=?",new String[]{item});
        else if(item.equals("その他")) s = rd.readDB2(str,"neclass=?",new String[]{item});
        return s;
    }

}
