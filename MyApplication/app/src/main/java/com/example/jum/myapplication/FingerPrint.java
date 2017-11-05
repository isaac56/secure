package com.example.jum.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ajalt.reprint.core.AuthenticationFailureReason;
import com.github.ajalt.reprint.core.AuthenticationListener;
import com.github.ajalt.reprint.core.Reprint;

public class FingerPrint extends AppCompatActivity {

    TextView textView;
    ImageView fingerView;
    Vibrator vibe;
    Handler handler;
    DBHelper dbHelper;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    private long pressedTime;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(dbHelper.settingExist()) {
            exitProcess();
        } else {
            Intent Ip_intent = new Intent(FingerPrint.this, Ip.class);
            startActivity(Ip_intent);
            finish();
        }
    }
    private void exitProcess() {
        if(pressedTime == 0) {
            Toast.makeText(FingerPrint.this, "한번 더 누르면 종료됩니다", Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis();
        }
        else {
            int seconds = (int)(System.currentTimeMillis() - pressedTime);

            if( seconds > 2000) {
                pressedTime = 0;
            }
            else {
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

    @Override
    protected void onPause() {
        //이벤트
        super.onPause();
        Reprint.cancelAuthentication();
    }
    @Override
    protected  void onResume() {
        super.onResume();
        startFingerPrint();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        dbHelper = new DBHelper(getApplicationContext(), "Setting.db", null, 1);

        textView = (TextView)findViewById(R.id.fingerText);
        fingerView = (ImageView)findViewById(R.id.fingerView);
        vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        handler = new Handler();

        startFingerPrint();
    }

    private void startFingerPrint() {
        textView.setText("지문을 입력하세요.");
        fingerView.setImageResource(R.drawable.fingerprint);

        Reprint.authenticate(new AuthenticationListener() {
            @Override
            public void onSuccess(int moduleTag) {
                showSuccess();
            }

            @Override
            public void onFailure(AuthenticationFailureReason failureReason, boolean fatal,
                                  CharSequence errorMessage, int moduleTag, int errorCode) {
                showError(errorMessage, fatal);
            }
      });
    }

    private void showSuccess() {
        textView.setText("인식에 성공하였습니다.");
        fingerView.setImageResource(R.drawable.fingerprint_success);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent Password_Intent = new Intent(FingerPrint.this,Main.class);
                //비밀번호가 맞으므로 핸드폰의 fcm을 보내준다.
                MyFirebaseInstanceIDService fcm = new MyFirebaseInstanceIDService();
                fcm.onTokenRefresh();

                startActivity(Password_Intent);
                finish();
            }
        }, 500);
    }


    private void showError(CharSequence errorMessage, boolean fatal) {
        vibe.vibrate(400);
        textView.setText(errorMessage);
        fingerView.setImageResource(R.drawable.fingerprint_fail);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               startFingerPrint();
            }
        }, 500);
        if(fatal) {
        }
    }
}
