package com.wakakusa.kutportal;


import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * メイン画面です。
 * <p/>
 * Created by Shirai on 2016/08/04.
 */
public class PushMessage extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FirebaseMessaging.getInstance().subscribeToTopic("test");

    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseMessaging.getInstance().subscribeToTopic("test");

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


/**
 * プッシュ通知を受け取るサービスです。
 * <p/>
 * Created by Shirai on 2016/08/05.
 */
 class FcmTestFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // 通知設定
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");
        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(this);
        notificationCompatBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationCompatBuilder.setContentTitle((title != null) ? title : "");
        notificationCompatBuilder.setContentText((body != null) ? body : "");
        notificationCompatBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationCompatBuilder.setAutoCancel(true);
        // タップ時の動作設定
        Intent intent = new Intent(this, PushMessage.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationCompatBuilder.setContentIntent(pendingIntent);
        notificationCompatBuilder.setFullScreenIntent(pendingIntent, false);
        // 通知表示
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(346, notificationCompatBuilder.build());
    }

}


/**
 * プッシュ通知に使用する登録トークンの生成、更新をハンドルするサービスです。
 * <p/>
 * Created by Shirai on 2016/08/05.
 */
class FcmTestFirebaseInstanceIdService extends FirebaseInstanceIdService {

    /**
     * ログ出力用
     */
    private static final String TAG = "FCM";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        // InstanceIDトークンを取得
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refreshed token: " + refreshedToken);
    }

}
