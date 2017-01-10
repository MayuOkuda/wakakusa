package com.wakakusa.kutportal;

import android.app.AlertDialog;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.Set;


public class tab1 extends Fragment{

    View rootView;
    //pop表示のためのやつ
  //  private View.OnClickListener[][] onClick_textview = new View.OnClickListener[6][5];


    int[][] ID =    {{R.id.time1_a1,R.id.time1_a2,R.id.time1_a3,R.id.time1_a4,R.id.time1_a5},
            {R.id.time1_b1,R.id.time1_b2,R.id.time1_b3,R.id.time1_b4,R.id.time1_b5},
            {R.id.time1_c1,R.id.time1_c2,R.id.time1_c3,R.id.time1_c4,R.id.time1_c5},
            {R.id.time1_d1,R.id.time1_d2,R.id.time1_d3,R.id.time1_d4,R.id.time1_d5},
            {R.id.time1_e1,R.id.time1_e2,R.id.time1_e3,R.id.time1_e4,R.id.time1_e5},
            {R.id.time1_f1,R.id.time1_f2,R.id.time1_f3,R.id.time1_f4,R.id.time1_f5}};

    //コンストラクタ
    public tab1() {
    }

    public static tab1 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        tab1 fragment = new tab1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int page = getArguments().getInt("page", 0);
        rootView = inflater.inflate(R.layout.activity_tab1, container, false);

           CoursePage.couse_appearance("1Q");
           SetTextView();

        return rootView;
    }

    //表示
     void SetTextView(){
        textviewReset();
        Today t = CoursePage.course;
        //回す
         while (t.scode != null) {
             for (String time : t.daytime) {
                 System.out.println("t=" + time);
                 if (time.substring(0, 1).equals("月")) {
                     int classtime = Integer.parseInt(time.substring(1, 2));
                     TextView text = (TextView) rootView.findViewById(ID[0][classtime - 1]);
                     text.setText(t.subject);
                 } else if (time.substring(0, 1).equals("火")) {
                     int classtime = Integer.parseInt(time.substring(1, 2));
                     TextView text = (TextView) rootView.findViewById(ID[1][classtime - 1]);
                     text.setText(t.subject);
                 } else if (time.substring(0, 1).equals("水")) {
                     int classtime = Integer.parseInt(time.substring(1, 2));
                     TextView text = (TextView) rootView.findViewById(ID[2][classtime - 1]);
                     text.setText(t.subject);
                 } else if (time.substring(0, 1).equals("木")) {
                     int classtime = Integer.parseInt(time.substring(1, 2));
                     TextView text = (TextView) rootView.findViewById(ID[3][classtime - 1]);
                     text.setText(t.subject);
                 } else if (time.substring(0, 1).equals("金")) {
                     int classtime = Integer.parseInt(time.substring(1, 2));
                     TextView text = (TextView) rootView.findViewById(ID[4][classtime - 1]);
                     text.setText(t.subject);
                 } else if (time.substring(0, 1).equals("土")) {
                     int classtime = Integer.parseInt(time.substring(1, 2));
                     TextView text = (TextView) rootView.findViewById(ID[5][classtime - 1]);
                     text.setText(t.subject);
                 }
             }
                 t = t.next;

         }


        }


    //内容のリセット
        void textviewReset() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                TextView text = (TextView) rootView.findViewById(ID[i][j]);
                text.setText("          ");
                //pop表示をなくす処理
                //onClick_textview[i][j] = null;
                //text.setOnClickListener(onClick_textview[i][j]);
            }
        }
        }
}