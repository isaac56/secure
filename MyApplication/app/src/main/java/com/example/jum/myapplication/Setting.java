package com.example.jum.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ajalt.reprint.core.Reprint;

public class Setting extends AppCompatActivity {

    EditText addressText;
    Button addressButton;
    Button fingerprintButton;
    DBHelper dbHelper;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    protected void onPause() {
        //이벤트
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        dbHelper = new DBHelper(getApplicationContext(), "Setting.db", null, 1);
        addressText = (EditText)findViewById(R.id.addressText);
        addressButton = (Button)findViewById(R.id.addressButton);
        fingerprintButton = (Button)findViewById(R.id.fingerprintButton);

        setSettingView();

        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.update(addressText.getText().toString());
                Toast.makeText(Setting.this, "앱을 다시 시작 해주세요.", Toast.LENGTH_LONG).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        moveTaskToBack(true);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }, 1000);
            }
        });
        fingerprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbHelper.getFingerprint() == 0) {
                    dbHelper.update(1);
                } else {
                    dbHelper.update(0);
                }
                setSettingView();
            }
        });

    }
    private void setSettingView() {
        addressText.setText(dbHelper.getAdress());
        if(dbHelper.getFingerprint() == 0) {
            fingerprintButton.setText("지문 OFF");
        } else {
            fingerprintButton.setText("지문 ON");
        }
    }
}
