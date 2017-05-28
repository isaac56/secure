package com.example.jum.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by jum on 2017-05-10.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    String title = null;
    String msg = null;
    @Override
    public void onMessageReceived(RemoteMessage message)
    {
        String from = message.getFrom();

        msg = message.getData().get("key").toString();
        Log.d("fcm",msg);
        //진동이 한번 울리면 계속 울림.
        //알림을 클릭했을 때 멈추게 해야함. 혹은 몇번 울린뒤 멈추게 하기.
        //Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //long[] pattern = {500,500,500};
        //vib.vibrate(pattern,1);
        NotificationSomethings();
    }
    public void NotificationSomethings() {
    Resources res = getResources();

    Intent notificationIntent = new Intent(this, Network.class);
        notificationIntent.putExtra("notificationId", 9999); //전달할 값
    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    //알림창에 대한 빌더
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle("알림이 왔습니다")
                .setContentText(msg)
                .setTicker("상태바 한줄 메시지")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
        .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
        .setDefaults(Notification.DEFAULT_ALL);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
    builder.setCategory(Notification.CATEGORY_MESSAGE)
            .setPriority(Notification.PRIORITY_HIGH)
            .setVisibility(Notification.VISIBILITY_PUBLIC);
}

    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1234, builder.build());
}
}