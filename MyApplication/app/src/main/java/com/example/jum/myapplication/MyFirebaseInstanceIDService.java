package com.example.jum.myapplication;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

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
 * Created by jum on 2017-05-10.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "GameRank";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        Log.d("myfcm","안으로 들어왔습니다.");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();



        // 이 token을 서버에 전달 한다.
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String refreshedToken) {
        //서버에 토큰을 보내야 한다.
        //해당 토큰을 post 형식으로 ipadress/token 에 요청한다.
        OutputStream os   = null;
        InputStream is   = null;
        Network network = new Network();
        String url_ip =network.url_ip+"/token";
        String rcv_msg=null;
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
}