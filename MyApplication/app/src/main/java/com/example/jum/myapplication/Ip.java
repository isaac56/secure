package com.example.jum.myapplication;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.view.Window;
        import android.widget.Button;
        import android.widget.EditText;


public class Ip extends Activity {

    String ipadress;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ip);


        final EditText ip_text = (EditText)findViewById(R.id.ipaddress);
        Button NextButton = (Button)findViewById(R.id.button);
        View.OnClickListener listen =  new View.OnClickListener(){
            public void onClick(View v) {
                Log.d("network", "It's in try");
                Intent Password_Intent = new Intent(Ip.this,Password.class);
                //Intent FcmIntent = new Intent(Ip.this,MyFirebaseInstanceIDService.class);
                //FcmIntent.putExtra("ip",ipadress);
                ipadress = ip_text.getText().toString();
                Password_Intent.putExtra("ip",ipadress);
                startActivity(Password_Intent);
                finish();
            }
        };
        NextButton.setOnClickListener(listen);
    }
}