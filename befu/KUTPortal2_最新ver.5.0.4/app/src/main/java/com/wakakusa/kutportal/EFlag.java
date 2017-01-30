package com.wakakusa.kutportal;

/**
 *画面遷移時に遷移元を閉じるために用いるフラグを操作するクラス
 */

public class EFlag {
    private boolean flag;

    public void setFlagState(boolean f){ flag = f;}

    public boolean getFlagState(){return flag;}
}
