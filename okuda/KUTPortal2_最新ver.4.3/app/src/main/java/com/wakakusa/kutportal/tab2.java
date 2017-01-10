package com.wakakusa.kutportal;

import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

public class tab2 extends Fragment{


    View rootView;

    int[][] ID =    {{R.id.time2_a1,R.id.time2_a2,R.id.time2_a3,R.id.time2_a4,R.id.time2_a5},
            {R.id.time2_b1,R.id.time2_b2,R.id.time2_b3,R.id.time2_b4,R.id.time2_b5},
            {R.id.time2_c1,R.id.time2_c2,R.id.time2_c3,R.id.time2_c4,R.id.time2_c5},
            {R.id.time2_d1,R.id.time2_d2,R.id.time2_d3,R.id.time2_d4,R.id.time2_d5},
            {R.id.time2_e1,R.id.time2_e2,R.id.time2_e3,R.id.time2_e4,R.id.time2_e5},
            {R.id.time2_f1,R.id.time2_f2,R.id.time2_f3,R.id.time2_f4,R.id.time2_f5}};

    public tab2() {
    }

    public static tab2 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        tab2 fragment = new tab2();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int page = getArguments().getInt("page", 0);
        rootView = inflater.inflate(R.layout.activity_tab2, container, false);
            CoursePage.couse_appearance("2Q");
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
            }
        }
    }
}
