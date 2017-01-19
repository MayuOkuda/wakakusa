package com.wakakusa.kutportal;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class UserPage extends BasePage {

    /*
     *UserPage(ユーザ情報クラス)
     *ユーザ情報を表示する
     */

    final String[] student_property = {"id","name", "birth","adm","ug","dpm","mjr","sub1","sub2","teacher","address","mailaddress"};
    final int[] id_box = {R.id.userinfo1, R.id.userinfo2, R.id.userinfo3,R.id.userinfo4,R.id.userinfo5
                                ,R.id.userinfo6,R.id.userinfo7,R.id.userinfo8,R.id.userinfo9,R.id.userinfo10,R.id.userinfo11,R.id.userinfo12};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        userinfo();

    }


    //データベースからユーザ情報を記述するためのメソッド
    void userinfo(){

        //学籍番号と名前の表示
        DatabaseReader r_db = new DatabaseReader(this, "student");
        String s[] = new String[12];
        for(int i=0; i< 12; i++)s[i] = "null";
        String[] db_s = r_db.readDB(student_property, 0).split("\n",0);
        int s_num = 0;
        for(String str : db_s) {
            s[s_num] = str;
            s_num++;
        }
        int num = 0;
        for(int i : id_box){
            TextView tex1 = (TextView) findViewById(i);
            if(s[num].equals("null"))tex1.setText("　　");
            else {
                if( i==id_box[2] || i==id_box[3] ) {
                    tex1.setText(" " + s[num].substring(0,4)+"/"+s[num].substring(4,6)+"/" + s[num].substring(6,8));
                }else{
                    tex1.setText(" " + s[num]);
                }

            }
            num++;
        }

    }

}
