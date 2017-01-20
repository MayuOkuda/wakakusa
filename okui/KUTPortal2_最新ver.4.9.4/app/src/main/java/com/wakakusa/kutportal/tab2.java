package com.wakakusa.kutportal;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;


/*
 * CouserPage(履修確認クラス)の2Qの時間割を表示するためのクラス
 */



public class tab2 extends Fragment {


    View rootView;

    //科目の詳細表示popに使う
    Today pop[][] = new Today[6][5];
    private View.OnClickListener[] onClick_textview = new View.OnClickListener[30];

    int[][] ID = {{R.id.time2_a1, R.id.time2_a2, R.id.time2_a3, R.id.time2_a4, R.id.time2_a5},
            {R.id.time2_b1, R.id.time2_b2, R.id.time2_b3, R.id.time2_b4, R.id.time2_b5},
            {R.id.time2_c1, R.id.time2_c2, R.id.time2_c3, R.id.time2_c4, R.id.time2_c5},
            {R.id.time2_d1, R.id.time2_d2, R.id.time2_d3, R.id.time2_d4, R.id.time2_d5},
            {R.id.time2_e1, R.id.time2_e2, R.id.time2_e3, R.id.time2_e4, R.id.time2_e5},
            {R.id.time2_f1, R.id.time2_f2, R.id.time2_f3, R.id.time2_f4, R.id.time2_f5}};

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
        rootView = inflater.inflate(R.layout.activity_tab2, container, false);
        CoursePage.couse_appearance("2Q");
        SetTextView();

        return rootView;
    }


    //履修状況表示
    void SetTextView() {
        textviewReset();
        //1Qの履修科目情報をtに保存しておく
        Today t = CoursePage.course;
        //科目の曜日判定と表示
        while (t.scode != null) {
            for (String time : t.daytime) {
                if (time.substring(0, 1).equals("月")) {
                    writeClass(time, t, 0);

                } else if (time.substring(0, 1).equals("火")) {
                    writeClass(time, t, 1);

                } else if (time.substring(0, 1).equals("水")) {
                    writeClass(time, t, 2);

                } else if (time.substring(0, 1).equals("木")) {
                    writeClass(time, t, 3);

                } else if (time.substring(0, 1).equals("金")) {
                    writeClass(time, t, 4);

                } else if (time.substring(0, 1).equals("土")) {
                    writeClass(time, t, 5);
                }
            }
            t = t.next;

        }
        //popのための処理
        tabpop();

    }

    //内容書き込み
    void writeClass(String time, Today t, int i) {
        System.out.println("erro"+time);
        int classtime = Integer.parseInt(time.substring(1, 2));
        TextView text = (TextView) rootView.findViewById(ID[i][classtime - 1]);
        text.setText(t.subject);
        pop[i][classtime - 1] = t;
    }


    //ダイアログ表示のメソッド
    void ClassDialog(Today t) {
        new AlertDialog.Builder(getActivity())
                .setTitle(t.subject)
                .setMessage("教室　　：" + t.room + "\n" + "担当教員：" + t.teacher + "\n" + "単位区分：" + t.sj + "\n" + "単位数　：" + t.sjclass)
                .setPositiveButton("OK", null)
                .show();
    }

    //内容のリセット
    void textviewReset() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                TextView text = (TextView) rootView.findViewById(ID[i][j]);
                text.setText("          ");
                pop[i][j] = null;
            }
        }
    }


    //以下pop表示のためのメソッド;
    void tabpop() {
        onClick_textview[0] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 0;
                int b = 0;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[1] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 0;
                int b = 1;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[2] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 0;
                int b = 2;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[3] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 0;
                int b = 3;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[4] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 0;
                int b = 4;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };


        onClick_textview[5] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 1;
                int b = 0;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[6] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 1;
                int b = 1;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[7] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 1;
                int b = 2;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[8] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 1;
                int b = 3;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[9] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 1;
                int b = 4;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };

        onClick_textview[10] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 2;
                int b = 0;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[11] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 2;
                int b = 1;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[12] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 2;
                int b = 2;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[13] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 2;
                int b = 3;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[14] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 2;
                int b = 4;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };

        onClick_textview[15] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 3;
                int b = 0;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[16] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 3;
                int b = 1;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[17] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 3;
                int b = 2;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[18] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 3;
                int b = 3;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[19] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 3;
                int b = 4;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };

        onClick_textview[20] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 4;
                int b = 0;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[21] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 4;
                int b = 1;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[22] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 4;
                int b = 2;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[23] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 4;
                int b = 3;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[24] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 4;
                int b = 4;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };

        onClick_textview[25] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 5;
                int b = 0;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[26] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 5;
                int b = 1;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[27] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 5;
                int b = 2;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[28] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 5;
                int b = 3;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[29] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 5;
                int b = 4;
                if (pop[a][b] != null)
                    ClassDialog(pop[a][b]);
            }
        };

        int p = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                TextView text = (TextView) rootView.findViewById(ID[i][j]);
                text.setOnClickListener(onClick_textview[p]);
                ;
                p++;
            }
        }

    }


}