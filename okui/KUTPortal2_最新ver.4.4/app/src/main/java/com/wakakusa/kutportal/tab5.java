package com.wakakusa.kutportal;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

public class tab5 extends Fragment {

    View rootView;

    int[][] ID = {{R.id.ct101,R.id.ct102,R.id.ct103,R.id.ct104,R.id.ct105,R.id.ct106,R.id.ct107,R.id.ct108
                    ,R.id.ct109,R.id.ct110,R.id.ct111,R.id.ct112},
            {R.id.ct201,R.id.ct202,R.id.ct203,R.id.ct204,R.id.ct205,R.id.ct206,R.id.ct207,R.id.ct208
                    ,R.id.ct209,R.id.ct210,R.id.ct211,R.id.ct212}
          };

    public tab5() {
    }

    public static tab5 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        tab5 fragment = new tab5();
        fragment.setArguments(args);
        System.out.println("tab5");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int page = getArguments().getInt("page", 0);
        rootView = inflater.inflate(R.layout.activity_tab5, container, false);
            CoursePage.couse_appearance("集中");
            SetTextView();

        return rootView;
    }

    //表示
    void SetTextView() {
        textviewReset();
        Today t = CoursePage.course;
        //回す
        int f = 0;
        int s = 0;
        while (t.scode != null) {
            for (String time : t.daytime) {
                System.out.println("t=" + time);
                if (time.equals("1学期")) {
                    TextView text = (TextView) rootView.findViewById(ID[0][f]);
                    text.setText(t.subject);
                    f++;
                } else if (time.equals("2学期")) {
                    TextView text = (TextView) rootView.findViewById(ID[0][s]);
                    text.setText(t.subject);
                    s++;
                }
            }
            t = t.next;

        }


    }

    //内容のリセット
    void textviewReset() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 12; j++) {
                TextView text = (TextView) rootView.findViewById(ID[i][j]);
                text.setText("          ");
            }
        }
    }

}