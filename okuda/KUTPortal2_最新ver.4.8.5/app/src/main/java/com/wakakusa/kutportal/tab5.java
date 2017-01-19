package com.wakakusa.kutportal;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

public class tab5 extends Fragment {

    /*
 * CouserPage(履修確認クラス)の集中講義の時間割を表示するためのクラス
 */



    View rootView;

    //科目の詳細表示popに使う
    Today pop[][] = new Today[2][12];
    private View.OnClickListener[] onClick_textview = new View.OnClickListener[24];

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
        rootView = inflater.inflate(R.layout.activity_tab5, container, false);
            CoursePage.couse_appearance("集中");
            SetTextView();
            tabpop();

        return rootView;
    }

    //履修状況表示
    void SetTextView() {
        textviewReset();
        //履修状況を表示するメソッド呼び出し
        Today t = CoursePage.course;
        //科目の曜日判定と表示
        int f = 0;
        int s = 0;
        while (t.scode != null) {
            for (String time : t.daytime) {
                System.out.println("t=" + time);
                if (time.equals("1学期")) {
                    TextView text = (TextView) rootView.findViewById(ID[0][f]);
                    text.setText(t.subject);
                    pop[0][f] = t;
                    f++;
                } else if (time.equals("2学期")) {
                    TextView text = (TextView) rootView.findViewById(ID[0][s]);
                    text.setText(t.subject);
                    pop[0][s] = t;
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
                pop[i][j] = null;
            }
        }
    }

    //ダイアログ表示のメソッド
    void ClassDialog(Today t) {
        new AlertDialog.Builder(getActivity())
                .setTitle(t.subject)
                .setMessage("教室　　：" + t.room + "\n" + "担当教員：" + t.teacher + "\n" + "単位区分：" + t.sj + "\n" + "単位数　：" + t.sjclass)
                .setPositiveButton("OK", null)
                .show();
    }


    //以下pop表示のためのメソッド;
    void tabpop(){
        onClick_textview[0] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 0; int b = 0;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[1] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 0; int b = 1;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[2] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 0; int b = 2;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[3] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 0; int b = 3;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[4] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 0; int b = 4;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };

        onClick_textview[5] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 0; int b = 5;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[6] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 0; int b = 6;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[7] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 0; int b = 7;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        }; onClick_textview[8] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 0; int b = 8;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        }; onClick_textview[9] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 0; int b = 9;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };

        onClick_textview[10] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 0; int b = 10;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[11] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 0; int b = 11;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[12] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 1; int b = 0;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[13] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 1; int b = 1;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[14] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 1; int b = 2;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };

        onClick_textview[15] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 1; int b = 3;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[16] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 1; int b = 4;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[17] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 1; int b = 5;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[18] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 1; int b = 6;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[19] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 1; int b = 7;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };

        onClick_textview[20] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 1; int b = 8;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[21] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 1; int b = 9;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[22] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 1; int b = 10;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };
        onClick_textview[23] = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a = 1; int b = 11;
                if(pop[a][b]!=null)
                    ClassDialog(pop[a][b]);
            }
        };

        int p = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 12; j++) {
                TextView text = (TextView) rootView.findViewById(ID[i][j]);
                text.setOnClickListener(onClick_textview[p]);;
                p++;
            }
        }

    }

}