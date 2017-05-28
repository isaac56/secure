package com.example.jum.myapplication;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;

        import com.google.firebase.iid.FirebaseInstanceId;


public class MainActivity extends AppCompatActivity {
	//abcdedf
    String ipadress;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText ip_text = (EditText)findViewById(R.id.ipaddress);
        Button NextButton = (Button)findViewById(R.id.network);
        View.OnClickListener listen =  new View.OnClickListener(){
            public void onClick(View v) {
                Log.d("network", "It's in try");
                Intent Network_Intent = new Intent(MainActivity.this,Network.class);
                Intent FcmIntent = new Intent(MainActivity.this,MyFirebaseInstanceIDService.class);
                FcmIntent.putExtra("ip",ipadress);
                ipadress = ip_text.getText().toString();
                Network_Intent.putExtra("ip",ipadress);
                startActivity(Network_Intent);
            }
        };
        NextButton.setOnClickListener(listen);

    }
}