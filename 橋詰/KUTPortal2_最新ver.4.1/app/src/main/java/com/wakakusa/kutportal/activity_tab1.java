package com.wakakusa.kutportal;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

public class activity_tab1 extends Fragment{

    //コンストラクタ
    public activity_tab1() {
    }

    public static activity_tab1 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        activity_tab1 fragment = new activity_tab1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int page = getArguments().getInt("page", 0);
        View view = inflater.inflate(R.layout.activity_tab1, container, false);
        return view;
    }
}
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1);
        System.out.println("tab1");
    }
    */