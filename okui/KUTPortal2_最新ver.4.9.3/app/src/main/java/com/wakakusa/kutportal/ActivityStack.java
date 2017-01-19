package com.wakakusa.kutportal;

/**
 * Created by hashidzumeshinya on 2017/01/16.
 */

import android.app.Activity;

import java.util.ArrayList;

public class ActivityStack {
    private static ArrayList<Activity> stackList = new ArrayList<Activity>();

    /**
     * コンストラクタ
     */
    private ActivityStack() {
    }

    public static void removeHistory() {
        for (Activity stack : stackList) {
            stack.moveTaskToBack(true);
            System.out.println("a"+stack);
        }
        //stackList = new ArrayList<Activity>();
    }

    public static void stackHistory(Activity stack) {
        stackList.add(stack);
    }

}


