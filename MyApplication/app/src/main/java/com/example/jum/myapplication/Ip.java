package com.example.jum.myapplication;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.Window;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;


public class Ip extends Activity {

    String ipadress;



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    private long pressedTime;
    @Override
    public void onBackPressed() {
        if(pressedTime == 0) {
            Toast.makeText(Ip.this, "한번 더 누르면 종료됩니다", Toast.LENGTH_LONG).show();
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ip);
        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "Setting.db", null, 1);

        final EditText ip_text = (EditText)findViewById(R.id.ipaddress);
        final Button NextButton = (Button)findViewById(R.id.cancel);

        ip_text.setText(dbHelper.getAdress());

        View.OnClickListener listen =  new View.OnClickListener(){
            public void onClick(View v) {
                Log.d("network", "It's in try");

                Intent next_Intent;
                ipadress = "http://" + ip_text.getText().toString();
                if(dbHelper.settingExist()){
                    dbHelper.update(ipadress);
                } else {
                    dbHelper.insert(ipadress);
                }

                if(dbHelper.getFingerprint() == 0){
                    next_Intent = new Intent(Ip.this, Password.class);
                } else {
                    next_Intent = new Intent(Ip.this, FingerPrint.class);
                }

                startActivity(next_Intent);
                finish();
            }
        };
        NextButton.setOnClickListener(listen);
    }


}