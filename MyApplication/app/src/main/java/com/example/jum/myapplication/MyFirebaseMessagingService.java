package com.example.jum.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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
        String link = message.getData().get("link").toString();
        Log.d("fcm",link);
        NotificationSomethings(link);
    }
    public void NotificationSomethings(String link) {
    Resources res = getResources();

        Intent intent = new Intent(this, Webview_fcm.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", link);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle("알림이 왔습니다")
                .setContentText(msg)
                .setTicker("상태바 한줄 메시지")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
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