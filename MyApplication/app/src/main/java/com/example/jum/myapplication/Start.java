package com.example.jum.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Start extends AppCompatActivity {

    public static Activity startActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "Setting.db", null, 1);
        final Handler handler = new Handler();
        final Intent next_intent;

        if(dbHelper.settingExist()){
            switch(dbHelper.getFingerprint()) {
                case 0:
                    next_intent = new Intent(Start.this, Password.class);
                    break;
                case 1:
                    next_intent = new Intent(Start.this, FingerPrint.class);
                    break;
                default:
                    next_intent = new Intent(Start.this, Password.class);
                    break;
            }
        } else {
            next_intent = new Intent(Start.this,Ip.class);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(next_intent);   // Intent 시작
            }
        }, 500);  // 로딩화면 시간
    }
}
