package com.wakakusa.kutportal;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

public class activity_tab3 extends Fragment{

    public activity_tab3() {
    }

    public static activity_tab3 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        activity_tab3 fragment = new activity_tab3();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int page = getArguments().getInt("page", 0);
        View view = inflater.inflate(R.layout.activity_tab3, container, false);
        return view;
    }
}