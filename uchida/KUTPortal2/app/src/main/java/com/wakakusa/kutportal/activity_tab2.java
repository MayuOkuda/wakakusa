package com.wakakusa.kutportal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class activity_tab2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab2);

        DatabaseReader rd = new DatabaseReader(this, "student");
        String[] str = {"id"};
        TextView tex1 = (TextView) findViewById(R.id.time_a1);

        String s = rd.readDB(str);
        tex1.setText(s);
    }
}
