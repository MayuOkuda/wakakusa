package com.wakakusa.pushmessage;


import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * メイン画面です。
 * <p/>
 * Created by Shirai on 2016/08/04.
 */
public class MainActivity extends AppCompatActivity {

    protected void onCreate(){


        FirebaseMessaging.getInstance().subscribeToTopic("test");
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        // Google Play Servicesのインストールチェック
        // GCMの場合、GoogleApiClientで妥当なチェックできないため非推奨メソッドを利用
        int gpsResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (gpsResult != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(gpsResult)) {
                Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(gpsResult, this, 0);
                if (errorDialog != null) {
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance(errorDialog);
                    errorDialogFragment.show(getFragmentManager(), "");
                }
            } else {
                Toast.makeText(this, "Google Play Services が利用不可です", Toast.LENGTH_LONG).show();
            }
        }
    }

}