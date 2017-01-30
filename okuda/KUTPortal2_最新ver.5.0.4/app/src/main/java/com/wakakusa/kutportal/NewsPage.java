package com.wakakusa.kutportal;

        import android.app.AlertDialog;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.support.design.widget.NavigationView;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.widget.Toolbar;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.view.View.OnClickListener;
        import java.util.Arrays;

public class NewsPage extends BasePage {

    /*
    * お知らせ画面クラス
    * 最新30件のお知らせを表示
    */

    static int i=1;
    private View.OnClickListener onClick_textview;

    //ポップ表示のための配列
    TextView[] npop = new TextView[11];
    static String s3[];

    //お知らせのジャンル用の変数
    String item;

    //読み出しの宣言
    DatabaseReader rd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        content();
    }

    void content(){
        rd = new DatabaseReader(this, "news");//お知らせの項目の読み出し宣言
        final DatabaseWriter dbWriter = new DatabaseWriter(this, "news");//お知らせの項目の書き込み宣言
        item = "すべて";//お知らせのジャンルの初期値

        //お知らせの切り替え用ボタンの宣言
        Button btn1 = (Button)findViewById(R.id.news1);
        Button btn2 = (Button)findViewById(R.id.news2);
        Button btn3 = (Button)findViewById(R.id.news3);

        //各削除ボタンの宣言
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

        //1,2,3の表示切り替え用ボタンを押した時の処理
        //最新のお知らせ1~10件を表示
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1;
                setNews();
            }
        });
        //最新のお知らせ11~20件を表示
        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                i=61;
                setNews();
            } });
        //最新のお知らせ21~30件を表示
        btn3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                i=121;
                setNews(); }
        });

        //削除ボタンを押した時の処理
        btn4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("axb4cy13u", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if (y == 180) break;
                }
                dbWriter.deleteDB2("newscode",s2[i-1]);//該当するお知らせコードのデータをデータベースから削除
                setNews(); }//表示の反映
        });

        btn5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("axb4cy13u", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                dbWriter.deleteDB2("newscode",s2[i+5]);
                setNews(); }
        });
        btn6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("axb4cy13u", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                dbWriter.deleteDB2("newscode",s2[i+11]);
                setNews(); }
        });
        btn7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("axb4cy13u", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                dbWriter.deleteDB2("newscode",s2[i+17]);
                setNews(); }
        });
        btn8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("axb4cy13u", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                dbWriter.deleteDB2("newscode",s2[i+23]);
                setNews(); }
        });
        btn9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("axb4cy13u", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                dbWriter.deleteDB2("newscode",s2[i+29]);
                setNews(); }
        });
        btn10.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("axb4cy13u", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                dbWriter.deleteDB2("newscode",s2[i+35]);
                setNews(); }
        });
        btn11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("axb4cy13u", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                dbWriter.deleteDB2("newscode",s2[i+41]);
                setNews(); }
        });
        btn12.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for(String n : s.split("axb4cy13u", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if(y==180) break;
                }
                dbWriter.deleteDB2("newscode",s2[i+47]);
                setNews(); }
        });
        btn13.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = getS2(); //データベースから読み出し
                String s2[] = new String[180];
                int y = 0;
                for (String n : s.split("axb4cy13u", 0)) {
                    s2[y] = n; //データの分割
                    y++;
                    if (y == 180) break;
                }
                dbWriter.deleteDB2("newscode", s2[i + 53]);
                setNews();
            }
        });

        //年度指定処理(スピナー)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // アイテムを追加
        adapter.add("すべて"); adapter.add("講義"); adapter.add("事務連絡"); adapter.add("イベント"); adapter.add("その他");
        Spinner spinner = (Spinner) findViewById(R.id.spinner); // アダプターを設定
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
        //ポップ表示のための宣言
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
        setNews();
    }
    //お知らせの表示
    private void setNews() {
        //お知らせの内容を表示するTextViewの宣言
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
        String s = getS2(); //データベースからお知らせの読み出し
        //お知らせ表示用の配列
        String s2[] = new String[180];
        int y = 0;
        //読み出したデータの分割と格納
        for(String n : s.split("axb4cy13u", 0)) {
            s2[y] = n;
            y++;
            if(y==180) break;
        }
        //ポップ表示処理
        s3= Arrays.copyOfRange(s2,i,i+99);
        //各項目毎のタップされた際の処理
        npop[1].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)//AlertDialog(ポップ)の作成
                        .setTitle(s3[1])//件名
                        .setMessage(s3[0].substring(0,4)+"/"+s3[0].substring( 4,6)+"/"+s3[0].substring(6,8) + "\n 送信者 " + s3[2] + "\n" + s3[3] + "\n\n" + s3[4])//日付,送信者名,送信者アドレス,本文
                        .setPositiveButton("OK", null)//ポップのボタン設定(押したら閉じる)
                        .show();//ポップを表示する
            } });

        npop[2].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)//2個目のポップ作成
                        .setTitle(s3[7])
                        .setMessage(s3[6].substring(0,4)+"/"+s3[6].substring( 4,6)+"/"+s3[6].substring(6,8)+"\n"+"送信者 " + s3[8] + "\n" + s3[9] + "\n\n" + s3[10])
                        .setPositiveButton("OK", null)
                        .show();
            } });

        npop[3].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)//3個目のポップ作成
                        .setTitle(s3[13])
                        .setMessage(s3[12].substring(0,4)+"/"+s3[12].substring(4,6)+"/"+s3[12].substring(6,8)+"\n"+"送信者 " + s3[14] + "\n" + s3[15] + "\n\n" + s3[16])
                        .setPositiveButton("OK", null)
                        .show();
            } });

        npop[4].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)//4個目のポップ作成
                        .setTitle(s3[19])
                        .setMessage(s3[18].substring(0,4)+"/"+s3[18].substring(4,6)+"/"+s3[18].substring(6,8)+"\n"+"送信者 " + s3[20] + "\n" + s3[21] + "\n\n" + s3[22])
                        .setPositiveButton("OK", null)
                        .show();
            } });

        npop[5].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)//5個目のポップ作成
                        .setTitle(s3[25])
                        .setMessage(s3[24].substring(0,4)+"/"+s3[24].substring(4,6)+"/"+s3[24].substring(6,8)+"\n"+"送信者 " + s3[26] + "\n" + s3[27] + "\n\n" + s3[28])
                        .setPositiveButton("OK", null)
                        .show();
            } });
        npop[6].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)//6個目のポップ作成
                        .setTitle(s3[31])
                        .setMessage(s3[30].substring(0,4)+"/"+s3[30].substring(4,6)+"/"+s3[30].substring(6,8)+"\n"+"送信者 " + s3[32] + "\n" + s3[33] + "\n\n" + s3[34])
                        .setPositiveButton("OK", null)
                        .show();
            } });

        npop[7].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)//7個目のポップ作成
                        .setTitle(s3[37])
                        .setMessage(s3[36].substring(0,4)+"/"+s3[36].substring(4,6)+"/"+s3[36].substring(6,8)+"\n"+"送信者 " + s3[38] + "\n" + s3[39] + "\n\n" + s3[40])
                        .setPositiveButton("OK", null)
                        .show();
            } });

        npop[8].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)//8個目のポップ作成
                        .setTitle(s3[43])
                        .setMessage(s3[42].substring(0,4)+"/"+s3[42].substring(4,6)+"/"+s3[42].substring(6,8)+"\n"+"送信者 " + s3[44] + "\n" + s3[45] + "\n\n" + s3[46])
                        .setPositiveButton("OK", null)
                        .show();
            } });

        npop[9].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)//9個目のポップ作成
                        .setTitle(s3[49])
                        .setMessage(s3[48].substring(0,4)+"/"+s3[48].substring(4,6)+"/"+s3[48].substring(6,8)+"\n"+"送信者 " + s3[50] + "\n" + s3[51] + "\n\n" + s3[52])
                .setPositiveButton("OK", null)
                        .show();
            } });

        npop[10].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NewsPage.this)//10個目のポップ作成
                        .setTitle(s3[55])
                        .setMessage(s3[54].substring(0,4)+"/"+s3[54].substring(4,6)+"/"+s3[54].substring(6,8)+"\n"+"送信者 " + s3[56] + "\n" + s3[57] + "\n\n" + s3[58])
                        .setPositiveButton("OK", null)
                        .show();
            } });
        //お知らせ30件の作成
        int a = 0;
        int p = 1;
        for ( int num = 1; num+2 <= 30; num+=3) {
            if (s2[i + a] == null) {//お知らせの内容がないとき
                tex[num].setText("");
                tex[num + 1].setText("");
                tex[num + 2].setText("");
                onClick_textview = null;
                npop[p].setOnClickListener(onClick_textview);
            } else{//お知らせの内容があるとき
                tex[num].setText(s2[i + a].substring(0,4)+"/"+s2[i + a].substring(4,6)+"/"+s2[i + a].substring(6,8));//日付
                tex[num + 1].setText(s2[i + a + 1]);//件名
                tex[num + 2].setText("送信者 " + s2[i + a + 2]);//送信者名
            }
            a += 6; p++;
        }
    }

    //選択されているジャンルに属するお知らせをデータベースから読み出す処理
    String getS2(){
        String s = "null";//sの初期値
        String[] str = {"newscode","day","title","adduser","address","content"};//読み出す項目の宣言(お知らせコード,日付,件名,送信者名,送信者アドレス,本文)
        if(item.equals("すべて")) s = rd.readDB2au(str,"",new String[]{}); //すべてのお知らせを読み出し
        else if(item.equals("講義")) s = rd.readDB2au(str,"neclass=?",new String[]{item});//講義に属するお知らせの読み出し
        else if(item.equals("事務連絡")) s = rd.readDB2au(str,"neclass=?",new String[]{item});//事務連絡に属するお知らせの読み出し
        else if(item.equals("イベント")) s = rd.readDB2au(str,"neclass=?",new String[]{item});//イベントに属するお知らせの読み出し
        else if(item.equals("その他")) s = rd.readDB2au(str,"neclass=?",new String[]{item});//その他に属するお知らせの読み出し
        return s;
    }
}