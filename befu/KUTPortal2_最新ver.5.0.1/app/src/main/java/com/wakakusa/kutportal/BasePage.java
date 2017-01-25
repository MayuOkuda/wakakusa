package com.wakakusa.kutportal;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class BasePage extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {


    /*
     * メニュー一覧画面クラス
     * 各画面構成クラスの元となるクラス
     */

    public static EFlag actFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actFlag = new EFlag();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // メニューボタン押下時にメニューを表示する
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item =menu.getItem(0);
        item.setEnabled(LoadingPage.b);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //更新ボタン処理
        if (id == R.id.renew) {
            LoadingPage.actFlag2.setFlagState(true);
            Intent intent = new Intent(this, LoadingPage.class);
            intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.LoadingPage");
            intent.putExtra("name", "update");
            intent.putExtra("ClassName","com.wakakusa.kutportal."+getLocalClassName());
            System.out.println(getLocalClassName() + "dd");
            startActivity(intent);
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // メニュー内ボタン押下後の処理
        int id = item.getItemId();
        Intent intent = new Intent();
        Uri lms = Uri.parse("https://lms.kochi-tech.ac.jp/"); //KUTLMSアドレス指定
        Uri mail = Uri.parse("https://webmail.kochi-tech.ac.jp/rc/");//WebMailアドレス指定
        Uri sirabasu = Uri.parse("http://portal.kochi-tech.ac.jp/Portal/Public/Syllabus/SearchMain.aspx");//シラバスアドレス指定

        if (id == R.id.home) {
            intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.TopPage");
            startActivity(intent);
            overridePendingTransition(0,0);
            ActivityStack.stackHistory(this);
        } else if (id == R.id.score) {
            intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.ScorePage");
            startActivity(intent);
            overridePendingTransition(0,0);
            ActivityStack.stackHistory(this);
        } else if (id == R.id.course) {
            intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.CoursePage");
            startActivity(intent);
            overridePendingTransition(0,0);
            ActivityStack.stackHistory(this);
        } else if (id == R.id.nav_share) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(lms);
            startActivity(intent);
            overridePendingTransition(0,0);
        } else if (id == R.id.nav_send) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(mail);
            startActivity(intent);
            overridePendingTransition(0,0);
        } else if (id == R.id.nav_sirabasu) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(sirabasu);
            startActivity(intent);
            overridePendingTransition(0,0);
        } else if (id == R.id.nav_help) {
            gazou();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void gazou() {
        // ヘルプボタン押下時の処理
        final int[] counter;
        counter = new int[2];
        counter[0] = 0;

        LayoutInflater inflater = LayoutInflater.from(this);
        final View layout = inflater.inflate(R.layout.photo, null);
        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
        final AlertDialog dialog = alertDialog2.create();
        final ImageView imageView2 = (ImageView) layout.findViewById(R.id.helpphoto);
        dialog.setView(layout);

        //次へボタン
        ImageButton button_next = (ImageButton) layout.findViewById(R.id.help_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (counter[0] == 0) {
                    layout.findViewById(R.id.help_previous).setVisibility(View.VISIBLE);
                    imageView2.setImageResource(R.drawable.help2);
                    counter[0] = 1;
                } else if (counter[0] == 1) {
                    imageView2.setImageResource(R.drawable.help3);
                    counter[0] = 2;
                } else if (counter[0] == 2) {
                    imageView2.setImageResource(R.drawable.help4);
                    counter[0] = 3;
                } else if (counter[0] == 3) {
                    imageView2.setImageResource(R.drawable.help5);
                    counter[0] = 4;
                } else if (counter[0] == 4) {
                    imageView2.setImageResource(R.drawable.help6);
                    counter[0] = 5;
                } else if (counter[0] == 5) {
                    imageView2.setImageResource(R.drawable.help7);
                    counter[0] = 6;
                } else if (counter[0] == 6) {
                    imageView2.setImageResource(R.drawable.help8);
                    counter[0] = 7;
                } else if (counter[0] == 7){
                    imageView2.setImageResource(R.drawable.help9);
                    counter[0] = 8;
                } else if (counter[0] == 8){
                    imageView2.setImageResource(R.drawable.help10);
                    counter[0] = 9;
                } else if (counter[0] == 9){
                    imageView2.setImageResource(R.drawable.help11);
                    counter[0] = 10;
                } else if (counter[0] == 10){
                    layout.findViewById(R.id.help_next).setVisibility(View.INVISIBLE);
                    imageView2.setImageResource(R.drawable.help12);
                    counter[0] = 11;
                }
            }


        });

        //前へボタン
        ImageButton button_pre = (ImageButton) layout.findViewById(R.id.help_previous);
        layout.findViewById(R.id.help_previous).setVisibility(View.INVISIBLE);
        button_pre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (counter[0] == 1) {
                    layout.findViewById(R.id.help_previous).setVisibility(View.INVISIBLE);
                    imageView2.setImageResource(R.drawable.help1);
                    counter[0] = 0;
                } else if (counter[0] == 2) {
                    imageView2.setImageResource(R.drawable.help2);
                    counter[0] = 1;
                } else if (counter[0] == 3) {
                    imageView2.setImageResource(R.drawable.help3);
                    counter[0] = 2;
                } else if (counter[0] == 4) {
                    imageView2.setImageResource(R.drawable.help4);
                    counter[0] = 3;
                } else if (counter[0] == 5) {
                    imageView2.setImageResource(R.drawable.help5);
                    counter[0] = 4;
                } else if (counter[0] == 6) {
                    imageView2.setImageResource(R.drawable.help6);
                    counter[0] = 5;
                } else if (counter[0] == 7) {
                    imageView2.setImageResource(R.drawable.help7);
                    counter[0] = 6;
                } else if (counter[0] == 8){
                    imageView2.setImageResource(R.drawable.help8);
                    counter[0] = 7;
                } else if (counter[0] == 9){
                    imageView2.setImageResource(R.drawable.help9);
                    counter[0] = 8;
                } else if (counter[0] == 10){
                    imageView2.setImageResource(R.drawable.help10);
                    counter[0] = 9;
                } else if (counter[0] == 11){
                    layout.findViewById(R.id.help_next).setVisibility(View.VISIBLE);
                    imageView2.setImageResource(R.drawable.help11);
                    counter[0] = 10;
                }
            }
        });

        Button button_close = (Button) layout.findViewById(R.id.help_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            this.finish();
            overridePendingTransition(0,0);
            ActivityStack.stackHistory(this);
            return true;

        }

        return false;

    }
    @Override
    public void onRestart(){

        super.onRestart();
        if(this.actFlag.getFlagState() == true){
            this.finish();
            overridePendingTransition(0, 0);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        // Google Play Servicesのインストールチェック
        // GCMの場合、GoogleApiClientで妥当なチェックできないため非推奨メソッドを利用
        int gpsResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (gpsResult != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(gpsResult)) {
                Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(gpsResult, this, 0);
                if (errorDialog != null) {
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance(errorDialog);
                    errorDialogFragment.show(getFragmentManager(), "");
                }
            } else {
                Toast.makeText(this, "Google Play Services が利用不可です", Toast.LENGTH_LONG).show();
            }
        }

    }


}
