package com.example.jum.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Password extends Activity {

    EditText password;
    Button button;
    TextView text;
    String url_ip;

    String msg;
    String rcv_msg;
    boolean pwExist;
    boolean pwCorrect;
    int  isTherePw = 0, checkPw = 1, makePw = 2;

    Handler UIHandler = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_password);

        password = (EditText) findViewById(R.id.password);
        button = (Button) findViewById(R.id.button);
        text = (TextView) findViewById(R.id.text);
        Intent intent = getIntent();
        url_ip = "http://" + intent.getStringExtra("ip");

        UIHandler = new Handler();

        new SendPost().execute(isTherePw);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                msg = password.getText().toString();
                if(pwExist) {
                    new SendPost().execute(checkPw);
                } else {
                    new SendPost().execute(makePw);
                }
            }
        });
    }

    private void pwExist(){
        OutputStream os   = null;
        InputStream is   = null;
        try {
            URL url = new URL(url_ip + "/pwExist");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(5000);
            http.setReadTimeout(5000);
            http.setDefaultUseCaches(false);
            http.setRequestProperty("Cache-Control", "no-cache");
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");              //전송방식은 post

            //--------------------------
            //   서버에서 전송받기
            //--------------------------
            is = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }
            rcv_msg = builder.toString();
            UIHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(rcv_msg.equals("0\n")) {
                        text.setText("새 비밀번호를 입력하세요");
                        pwExist = false;
                    } else {
                        text.setText("비밀번호를 입력하세요");
                        pwExist = true;
                    }
                }
            });

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makingPw(){
        OutputStream os   = null;
        InputStream is   = null;
        try {
            URL url = new URL(url_ip + "/makePw");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(5000);
            http.setReadTimeout(5000);
            http.setDefaultUseCaches(false);
            http.setRequestProperty("Cache-Control", "no-cache");
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");              //전송방식은 post
            //   서버로 값 전송
            //-------------------------
            JSONObject job = new JSONObject();
            job.put("pw",msg);

            os = http.getOutputStream();

            Log.d("network", "전송시작");
            os.write(job.toString().getBytes());
            os.flush();
            Log.d("network", "전송완료");
            //--------------------------
            //   서버에서 전송받기
            //--------------------------
            is = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }
            rcv_msg = builder.toString();
            Log.d("network", "받기 완료");
            UIHandler.post(new Runnable() {
                @Override
                public void run() {
                    Intent Password_Intent = new Intent(Password.this,Network.class);
                    Password_Intent.putExtra("ip",url_ip);
                    startActivity(Password_Intent);
                    finish();
                }
            });
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void checkingPw(){
        OutputStream os   = null;
        InputStream is   = null;
        try {
            URL url = new URL(url_ip + "/checkPw");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(5000);
            http.setReadTimeout(5000);
            http.setDefaultUseCaches(false);
            http.setRequestProperty("Cache-Control", "no-cache");
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");              //전송방식은 post
            //   서버로 값 전송
            //-------------------------
            JSONObject job = new JSONObject();
            job.put("pw",msg);

            os = http.getOutputStream();

            Log.d("network", "전송시작");
            os.write(job.toString().getBytes());
            os.flush();
            Log.d("network", "전송완료");
            //--------------------------
            //   서버에서 전송받기
            //--------------------------
            is = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }
            rcv_msg = builder.toString();
            UIHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(rcv_msg.equals("0\n")) {
                        text.setText("비밀번호가 틀렸습니다.");
                    } else {
                        Intent Password_Intent = new Intent(Password.this,Network.class);
                        Password_Intent.putExtra("ip",url_ip);
                        startActivity(Password_Intent);
                        finish();
                    }
                }
            });
            Log.d("network", "받기 완료");
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    class SendPost extends AsyncTask<Integer, Integer, String> {
        protected String doInBackground(Integer... params) {

            if(params[0] == isTherePw)
                pwExist();
            if(params[0] == makePw)
                makingPw();
            if(params[0] == checkPw)
                checkingPw();
            publishProgress();
            return "done";
        }
    }
}

