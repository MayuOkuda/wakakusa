package com.wakakusa.kutportal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
/**
 *
 * Created by hashimoto on 2016/12/6.
 */
public class BasePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();
        Uri lms = Uri.parse("https://lms.kochi-tech.ac.jp/"); //KUTLMSアドレス指定
        Uri mail = Uri.parse("https://webmail.kochi-tech.ac.jp/rc/");//WebMailアドレス指定
        Uri sirabasu = Uri.parse("http://portal.kochi-tech.ac.jp/Portal/Public/Syllabus/SearchMain.aspx");//シラバスアドレス指定
        if (id == R.id.score) {
            // Handle the camera action
            intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.ScorePage");
            startActivity(intent);
        } else if (id == R.id.course) {
            intent.setClassName("com.wakakusa.kutportal", "com.wakakusa.kutportal.CoursePage");
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(lms);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(mail);
            startActivity(intent);
        } else if (id == R.id.nav_sirabasu) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(sirabasu);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            setContentView(R.layout.photo);
            ImageView imageView2 = (ImageView)findViewById(R.id.helpphoto);
            imageView2.setImageResource(R.drawable.test);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
