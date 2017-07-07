package com.example.jum.myapplication;

        import android.app.Activity;
        import android.content.Intent;
        import android.graphics.Color;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
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

/**
 * Created by jun on 2016-10-28.
 */

public class Network extends Activity {

    String msg;
    String rcv_msg;
    public String url_ip;
    TextView rcv_data;
    boolean door_opened=true;
    ToggleButton door_button;
    String StatusDoorOpen = "open";
    String StatusDoorClose = "close";
    char CommandOpenDoor = 'a';
    char CommandCloseDoor = 'a';


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.networklayout);
        final EditText etMessage = (EditText) findViewById(R.id.msg_text);
        final Button btnSend = (Button) findViewById(R.id.send_button);
        rcv_data = (TextView) findViewById(R.id.rcv_data);
        door_button = (ToggleButton) findViewById(R.id.control_door);
        Intent intent = getIntent();
        url_ip = intent.getStringExtra("ip");
        //처음에 시작할때 상태를 한번 받는다.
        /*
        try {
            new SendPost().execute(0);
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"오류가 발생했습니다 "+ e,Toast.LENGTH_SHORT);
        }*/

        String Token= FirebaseInstanceId.getInstance().getToken();
        Log.d("neetwork",Token);

        View.OnClickListener button_listener = new View.OnClickListener() {
            public void onClick(View v) {
                msg = etMessage.getText().toString(); // 보내는 메시지를 받아옴
                etMessage.setText("");
                new SendPost().execute(1);
            }
        };
        btnSend.setOnClickListener(button_listener);

        door_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(door_opened == false){
                    //문 열기
                    new SendPost().execute(2);
                    Toast.makeText(getApplicationContext(), "문 열기를 시도했습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    //문 닫기
                    new SendPost().execute(3);
                    Toast.makeText(getApplicationContext(), "문 닫기를 시도했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void GetStatus(){
        OutputStream os   = null;
        InputStream is   = null;
        try {
            URL url = new URL(url_ip);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(5000);
            http.setReadTimeout(5000);
            http.setDefaultUseCaches(false);
            http.setRequestProperty("Cache-Control", "no-cache");
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("GET");              //전송방식은 post
            //   서버로 값 전송
            //-------------------------
            JSONObject job = new JSONObject();
            job.put("status","get" );
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
            //json 형식으로 데이터 변환
            //위의 코드를 서버에서 json으로 보내준다는 가정하에 다시 짠다.
            try
            {
                //디바이스의 상태를 받는것을 구현해야 한다.
                rcv_msg = builder.toString();
                JSONObject jsonObject = new JSONObject(rcv_msg);
                String resultId = jsonObject.getString("userid");
                String resultPassword = jsonObject.getString("password");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }


            Log.d("network", "받기 완료");
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void ControlDoor(char DoorMsg){
        OutputStream os   = null;
        InputStream is   = null;
        try {
            URL url = new URL(url_ip+"/door");
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

    //서버에 메시지를 보내고 받는다.
    private void sendmsg(){
        OutputStream os   = null;
        InputStream is   = null;
        try {
            URL url = new URL(url_ip);
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
            job.put("name",msg);

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
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void SetDoor(){
        if(rcv_msg == StatusDoorClose){
            door_opened=false;
            Toast.makeText(getApplicationContext(), "문이 잠겼습니다.", Toast.LENGTH_LONG).show();
            door_button.setBackgroundColor(0xff0000);
            rcv_msg = null;
        }else if(rcv_msg == StatusDoorOpen){
            door_opened=true;
            Toast.makeText(getApplicationContext(), "문이 열렸습니다.", Toast.LENGTH_LONG).show();
            door_button.setBackgroundColor(0x0000ff);
            rcv_msg = null;
        }else{
            if(door_opened == true){
                Toast.makeText(getApplicationContext(), "문이 잠겼습니다.", Toast.LENGTH_LONG).show();
            }
            else if(door_opened == false){
                Toast.makeText(getApplicationContext(), "문이 열렸습니다.", Toast.LENGTH_LONG).show();
            }
        }

    }



    //백그라운드로 실행.
    private class SendPost extends AsyncTask<Integer, Integer, String> {
        protected String doInBackground(Integer... params) {
            //실시간으로 라즈베리로부터 상태를 전송받는다.
            //10초마다 계속 받게끔 되어 있으나 배터리 문제를 고려하여
            //켜질때만 받도록 할지 고민.
            //10초마다 받게끔 하려면 service를 통하여 구동해야 함.
            if(params[0]==0)
            {
                GetStatus();
            }
            //1은 메시지 보내기, 2는 문 열기, 3 문 닫기
            if(params[0] == 1)
                sendmsg();
            if(params[0] == 2)
                ControlDoor(CommandOpenDoor);
            if(params[0] == 3)
                ControlDoor(CommandCloseDoor);
            publishProgress();
            return "done";
        }
        protected void onPostExecute(String result) {
            // 모두 작업을 마치고 실행할 일 (메소드 등등)
            rcv_data.setText(rcv_msg);
            Log.d("명령어",rcv_msg);
            if(rcv_msg.contains(StatusDoorClose)){
                door_opened=false;
                Toast.makeText(getApplicationContext(), "문이 잠겼습니다.", Toast.LENGTH_LONG).show();
                door_button.setBackgroundColor(Color.RED);
                rcv_msg = null;
            }else if(rcv_msg.contains(StatusDoorOpen)){
                door_opened=true;
                Toast.makeText(getApplicationContext(), "문이 열렸습니다.", Toast.LENGTH_LONG).show();
                door_button.setBackgroundColor(Color.GREEN);
                rcv_msg = null;
            }else{
                if(door_opened == true){
                    Toast.makeText(getApplicationContext(), "문이 잠겼습니다.", Toast.LENGTH_LONG).show();
                    Log.d("door","여기들어옴");
                }
                else if(door_opened == false){
                    Toast.makeText(getApplicationContext(), "문이 열렸습니다.", Toast.LENGTH_LONG).show();
                    Log.d("door","여기로 들어왓음");
                }
            }
        }
    }
}