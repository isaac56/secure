package com.example.jum.myapplication;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

public class Main extends AppCompatActivity {

    TextView test;
    ImageButton settingBtn;

    Button doorBtn, windowBtn,window2Btn;
    Button cameraBtn;
    Button allwindowcloseBtn,allwindowopenBtn;

    String url_ip, rcv_msg;

    int status = 0, door = 1, window = 2, fcmstatus = 4, allopenwindow = 5, allclosewindow = 6, window2 = 8;
    int dstate = 2, wstate = 2, w2state = 2;
    String State = "2\n";
    boolean stateReact = true;
    Handler UIHandler = null;
    private TimerTask dTask, wTask,w2Task;
    private Timer dTimer, wTimer,w2Timer;
    Toast toast_door, toast_window,toast_window2;
    DBHelper dbHelper;
    boolean isFirstTime = true;

    private long pressedTime;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(pressedTime == 0) {
            Toast.makeText(Main.this, "한번 더 누르면 종료됩니다", Toast.LENGTH_LONG).show();
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
    protected void onRestart() {
        //이벤트
        super.onRestart();
//        if(!isFirstTime){
            Intent security;
            if(dbHelper.getFingerprint() == 0) {
                security = new Intent(Main.this,Password.class);
            } else {
                security = new Intent(Main.this,FingerPrint.class);
            }
            startActivity(security);
            finish();
//        }
//        isFirstTime = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(getApplicationContext(), "Setting.db", null, 1);

        settingBtn = (ImageButton)findViewById(R.id.settingButton);
        doorBtn = (Button)findViewById(R.id.door);
        windowBtn = (Button)findViewById(R.id.window);
        window2Btn = (Button)findViewById(R.id.window2);
        cameraBtn = (Button)findViewById(R.id.camera);
        allwindowcloseBtn = (Button)findViewById(R.id.allclose);
        allwindowopenBtn = (Button)findViewById(R.id.allopen);
        test = (TextView)findViewById(R.id.test);

        url_ip = dbHelper.getAdress();

        UIHandler = new Handler();
        test.setText(url_ip);

        new SendPost().execute(fcmstatus);

        dTask = new TimerTask() {
            @Override
            public void run() {
                new SendPost().execute(status);
            }
        };
        dTimer = new Timer();
        dTimer.schedule(dTask,0,2000);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent(Main.this, Setting.class);
                startActivity(settingIntent);
            }
        });

        doorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendPost().execute(door);
                switch(dstate){
                    case 1:
                        toast_door = Toast.makeText(Main.this, "문닫기를 시도했습니다.", Toast.LENGTH_SHORT );
                        break;
                    case 2 :
                        toast_door = Toast.makeText(Main.this, "문열기를 시도했습니다.", Toast.LENGTH_SHORT );
                        break;
                    case 3 :
                        toast_door = Toast.makeText(Main.this, "비상상황 종료를 시도했습니다.", Toast.LENGTH_SHORT );
                        break;
                    default :
                        toast_door = Toast.makeText(Main.this, "에러", Toast.LENGTH_SHORT );
                        break;
                }
                new CountDownTimer(1000, 500) {
                    public void onTick(long millisUnitilFinished){
                        toast_door.show();
                    }
                    public void onFinish(){toast_door.cancel();}
                }.start();
            }
        });

        windowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendPost().execute(window);
                switch(wstate){
                    case 1:
                        toast_window = Toast.makeText(Main.this, "창문닫기를 시도했습니다.", Toast.LENGTH_SHORT );
                        break;
                    case 2 :
                        toast_window = Toast.makeText(Main.this, "창문열기를 시도했습니다.", Toast.LENGTH_SHORT );
                        break;
                    case 3 :
                        toast_window = Toast.makeText(Main.this, "비상상황 종료를 시도했습니다.", Toast.LENGTH_SHORT );
                        break;
                    default :
                        toast_window = Toast.makeText(Main.this, "에러", Toast.LENGTH_SHORT );
                        break;
                }
                new CountDownTimer(1000, 500) {
                    public void onTick(long millisUnitilFinished){
                        toast_window.show();
                    }
                    public void onFinish(){toast_window.cancel();}
                }.start();
                //new SendPost().execute(status);
            }
        });

        window2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendPost().execute(window2);
                switch(w2state){
                    case 1:
                        toast_window2 = Toast.makeText(Main.this, "창문닫기를 시도했습니다.", Toast.LENGTH_SHORT );
                        break;
                    case 2 :
                        toast_window2 = Toast.makeText(Main.this, "창문열기를 시도했습니다.", Toast.LENGTH_SHORT );
                        break;
                    case 3 :
                        toast_window2 = Toast.makeText(Main.this, "비상상황 종료를 시도했습니다.", Toast.LENGTH_SHORT );
                        break;
                    default :
                        toast_window2 = Toast.makeText(Main.this, "에러", Toast.LENGTH_SHORT );
                        break;
                }
                new CountDownTimer(1000, 500) {
                    public void onTick(long millisUnitilFinished){
                        toast_window2.show();
                    }
                    public void onFinish(){toast_window2.cancel();}
                }.start();
                //new SendPost().execute(status);
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Camera_Intent = new Intent(Main.this,Camera.class);
                Camera_Intent.putExtra("ip",url_ip);
                startActivity(Camera_Intent);
            }
        });

        allwindowcloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendPost().execute(allclosewindow);
                toast_door = Toast.makeText(Main.this, "모든 창문을 닫기를 시도.", Toast.LENGTH_SHORT );
                new CountDownTimer(1000, 500) {
                    public void onTick(long millisUnitilFinished){
                        toast_door.show();
                    }
                    public void onFinish(){toast_door.cancel();}
                }.start();
            }
        });

        allwindowopenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendPost().execute(allopenwindow);
                toast_door = Toast.makeText(Main.this, "모든 창문을 열기를 시도.", Toast.LENGTH_SHORT );
                new CountDownTimer(1000, 500) {
                    public void onTick(long millisUnitilFinished){
                        toast_door.show();
                    }
                    public void onFinish(){toast_door.cancel();}
                }.start();
            }
        });
    }

    class SendPost extends AsyncTask<Integer, Integer, String> {
        protected String doInBackground(Integer... params) {

            if (params[0] == status) {
                Main.this.getStatus();
            }
            if(params[0] == door) {
                switch(dstate) {
                    case 1 :
                        changeDoor('2');
                        break;
                    case 2 :
                        changeDoor('1');
                        break;
                    case 3 :
                        changeDoor('5');
                        break;
                    default :
                        break;
                }
            }
            if(params[0] == window) {
                switch(wstate) {
                    case 1 :
                        changeWindow('2');
                        break;
                    case 2 :
                        changeWindow('1');
                        break;
                    case 3 :
                        changeWindow('5');
                         break;
                    default :
                        break;
                }
            }

            if(params[0] == window2) {
                switch(w2state) {
                    case 1 :
                        changeWindow2('2');
                        break;
                    case 2 :
                        changeWindow2('1');
                        break;
                    case 3 :
                        changeWindow2('5');
                        break;
                    default :
                        break;
                }
            }
            if(params[0] == fcmstatus){
                fcmsend();
            }
            if(params[0] == allclosewindow){
                allClose('1');
            }
            if(params[0] == allopenwindow){
                allOpen('1');
            }
            publishProgress();
            return "done";
        }
    }
    private void getStatus(){
        if(stateReact == false)
             return;
        stateReact = false;
        OutputStream os   = null;
        InputStream is   = null;
        try {
            URL url = new URL(url_ip + ":3000/Status");
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
            State = builder.toString();
            int index = State.indexOf("\n");
            State = State.substring(0,index);
            w2state = Integer.parseInt(State) >>> 6;
            wstate = (Integer.parseInt(State) >>> 3) & 7;
            dstate = Integer.parseInt(State) & 7;

            UIHandler.post(new Runnable() {
                @Override
                public void run() {
                    //test.setText("door status is " + doorState);
                    switch(dstate){
                        case 1 :
                            doorBtn.setBackgroundResource(R.drawable.open_event);
                            break;
                        case 2 :
                            doorBtn.setBackgroundResource(R.drawable.close_event);
                            break;
                        case 3 :
                            doorBtn.setBackgroundResource(R.drawable.emergency_event);
                            break;
                        default :
                            doorBtn.setBackgroundColor(0x66666666);
                            break;
                    }
                    switch(wstate){
                        case 1 :
                            windowBtn.setBackgroundResource(R.drawable.open_event);
                            break;
                        case 2 :
                            windowBtn.setBackgroundResource(R.drawable.close_event);
                            break;
                        case 3 :
                            windowBtn.setBackgroundResource(R.drawable.emergency_event);
                            break;
                        default :
                            windowBtn.setBackgroundColor(0x66666666);
                            break;
                    }
                    switch(w2state){
                        case 1 :
                            window2Btn.setBackgroundResource(R.drawable.open_event);
                            break;
                        case 2 :
                            window2Btn.setBackgroundResource(R.drawable.close_event);
                            break;
                        case 3 :
                            window2Btn.setBackgroundResource(R.drawable.emergency_event);
                            break;
                        default :
                            window2Btn.setBackgroundColor(0x66666666);
                            break;
                    }
                    stateReact = true;
                }
            });

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    private void getWStatus(){
//        if(windowReact == false)
//            return;
//        windowReact = false;
//        OutputStream os   = null;
//        InputStream is   = null;
//        try {
//            URL url = new URL(url_ip + ":3000/windowStatus");
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            http.setConnectTimeout(5000);
//            http.setReadTimeout(5000);
//            http.setDefaultUseCaches(false);
//            http.setRequestProperty("Cache-Control", "no-cache");
//            http.setRequestProperty("Content-Type", "application/json");
//            http.setRequestProperty("Accept", "application/json");
//            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
//            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
//            http.setRequestMethod("POST");              //전송방식은 post
//
//            //--------------------------
//            //   서버에서 전송받기
//            //--------------------------
//            is = http.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//            StringBuilder builder = new StringBuilder();
//            String str;
//            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
//                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
//            }
//            windowState = builder.toString();
//            UIHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    test.setText("window status is " + windowState);
//                    switch(windowState){
//                        case "1\n":
//                            windowBtn.setBackgroundResource(R.drawable.open_event);
//                            break;
//                        case "2\n" :
//                            windowBtn.setBackgroundResource(R.drawable.close_event);
//                            break;
//                        case "3\n" :
//                            windowBtn.setBackgroundResource(R.drawable.emergency_event);
//                            break;
//                        default :
//                            windowBtn.setBackgroundColor(0x66666666);
//                            break;
//                    }
//                    windowReact = true;
//                }
//            });
//
//        }catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getW2Status(){
//        if(windowReact == false)
//            return;
//        windowReact = false;
//        OutputStream os   = null;
//        InputStream is   = null;
//        try {
//            URL url = new URL(url_ip + ":3000/window2Status");
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            http.setConnectTimeout(5000);
//            http.setReadTimeout(5000);
//            http.setDefaultUseCaches(false);
//            http.setRequestProperty("Cache-Control", "no-cache");
//            http.setRequestProperty("Content-Type", "application/json");
//            http.setRequestProperty("Accept", "application/json");
//            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
//            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
//            http.setRequestMethod("POST");              //전송방식은 post
//
//            //--------------------------
//            //   서버에서 전송받기
//            //--------------------------
//            is = http.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//            StringBuilder builder = new StringBuilder();
//            String str;
//            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
//                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
//            }
//            window2State = builder.toString();
//            UIHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    test.setText("window status is " + window2State);
//                    switch(window2State){
//                        case "1\n":
//                            window2Btn.setBackgroundResource(R.drawable.open_event);
//                            break;
//                        case "2\n" :
//                            window2Btn.setBackgroundResource(R.drawable.close_event);
//                            break;
//                        case "3\n" :
//                            window2Btn.setBackgroundResource(R.drawable.emergency_event);
//                            break;
//                        default :
//                            window2Btn.setBackgroundColor(0x66666666);
//                            break;
//                    }
//                    window2React = true;
//                }
//            });
//
//        }catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void changeDoor(char DoorMsg){
        OutputStream os   = null;
        InputStream is   = null;
        try {
            URL url = new URL(url_ip+":3000/door");
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
            job.put("name",DoorMsg);
            os = http.getOutputStream();
            os.write(job.toString().getBytes());
            os.flush();
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
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e) {
            //Toast.makeText(getApplicationContext(), e.toString() , Toast.LENGTH_LONG).show();
        }
    }

    private void changeWindow(char WindowMsg){
        OutputStream os   = null;
        InputStream is   = null;
        try {
            URL url = new URL(url_ip+":3000/window");
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
            job.put("name",WindowMsg);
            os = http.getOutputStream();
            os.write(job.toString().getBytes());
            os.flush();
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
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e) {
            //Toast.makeText(getApplicationContext(), e.toString() , Toast.LENGTH_LONG).show();
        }
    }

    private void changeWindow2(char WindowMsg){
        OutputStream os   = null;
        InputStream is   = null;
        try {
            URL url = new URL(url_ip+":3000/window2");
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
            job.put("name",WindowMsg);
            os = http.getOutputStream();
            os.write(job.toString().getBytes());
            os.flush();
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
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e) {
            //Toast.makeText(getApplicationContext(), e.toString() , Toast.LENGTH_LONG).show();
        }
    }

    private void allOpen(char a){
        OutputStream os   = null;
        InputStream is   = null;
        try {
            URL url = new URL(url_ip + ":3000/allOpen");
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
            State = builder.toString();


        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void allClose(char a){
        OutputStream os   = null;
        InputStream is   = null;
        try {
            URL url = new URL(url_ip + ":3000/allClose");
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
            State = builder.toString();


        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void fcmsend(){
        MyFirebaseInstanceIDService fcm = new MyFirebaseInstanceIDService();
        fcm.onTokenRefresh();
        fcm.sendRegistrationToServer(fcm.refreshedToken,url_ip);
    }

}
