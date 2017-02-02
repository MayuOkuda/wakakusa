package com.wakakusa.kutportal;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FcmTestFirebaseMessagingService extends FirebaseMessagingService {

    /*
     * push通知クラス
     * 通知を受け取るクラス
     */

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // 通知設定
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");

        DatabaseReader dbReader = new DatabaseReader(this, "loginData");
        String[] str4 = {"response"};
        String res = dbReader.readDB(str4, 0);
        if ((res.substring(0, 1).equals("1") && body.equals("講義"))
                || (res.substring(1, 2).equals("1") && body.equals("事務連絡"))
                || (res.substring(2, 3).equals("1") && body.equals("イベント"))
                || (res.substring(3, 4).equals("1") && body.equals("その他"))) {
            NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(this);
            notificationCompatBuilder.setSmallIcon(R.mipmap.wakakusa);
            notificationCompatBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.wakakusa));
            notificationCompatBuilder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            notificationCompatBuilder.setContentTitle((title != null) ? title : "");
            notificationCompatBuilder.setContentText((body != null) ? body : "");
            notificationCompatBuilder.setDefaults(Notification.DEFAULT_ALL);
            notificationCompatBuilder.setAutoCancel(true);

            // タップ時の動作設定
            Intent intent = new Intent(this, LoadingPage.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationCompatBuilder.setContentIntent(pendingIntent);
            notificationCompatBuilder.setFullScreenIntent(pendingIntent, false);
            // 通知表示
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(346, notificationCompatBuilder.build());
        }
    }

}