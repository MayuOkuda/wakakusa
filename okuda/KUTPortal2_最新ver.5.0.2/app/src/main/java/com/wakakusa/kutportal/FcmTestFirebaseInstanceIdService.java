package com.wakakusa.kutportal;

import android.content.ContentValues;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;



public class FcmTestFirebaseInstanceIdService extends FirebaseInstanceIdService {

    /*
     * push通知クラス
     * プッシュ通知に使用する登録トークンの生成、更新をハンドルするサービス
     */

    private static final String TAG = "FCM";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        // InstanceIDトークンを取得
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        DatabaseWriter dbWriter = new DatabaseWriter(this, "loginData");
        ContentValues cvalue = new ContentValues();
        dbWriter.deleteDB();
        cvalue.put("tokenID",refreshedToken);

        cvalue.put("ara", "option");
        cvalue.put("limittime", "00000000000001");
        cvalue.put("realtime", "00000000000000");
        cvalue.put("response","1111");
        dbWriter.write.insert(dbWriter.Table_name, null, cvalue);

        Log.i(TAG, "Refreshed token: " + refreshedToken);
    }

}
