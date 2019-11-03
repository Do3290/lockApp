package com.example.lock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lock.DB.CustomTask;
import com.example.lock.DB.DO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class LoginActivity extends AppCompatActivity {

    private final int PERMISSIONS_REQUEST_RESULT = 1;

    EditText edtID, edtPW;
    Button btnLogin;
    String action, result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //풀스크린
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtID = (EditText) findViewById(R.id.edtID);
        edtPW = (EditText) findViewById(R.id.edtPW);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //폰번 가져오기위한 퍼미션 및 작동부분
        String PhoneNum = "";
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
            }
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }else {
            TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            PhoneNum = telManager.getLine1Number();
            if (PhoneNum.startsWith("+82"))
                PhoneNum = PhoneNum.replace("+82", "0");

            edtID.setText(PhoneNum);
            //edtID.setFocusable(false);
            //edtID.setClickable(false);

            //지정 ip에 해당하는 와이파이 연결안하고 앱실행시 폰자체 와이파이세팅으로 넘어가게함
            try {
                checkNet();
            } catch (Exception e) {
                //ip는 맞는데 인터넷이 안될경우 와이파이 안된걸로 넘겨버리기
                Toast.makeText(getApplicationContext(), "와이파이가 연결되어있지 않습니다.", Toast.LENGTH_SHORT).show();
                Intent intentConfirm = new Intent();
                intentConfirm.setAction("android.settings.WIFI_SETTINGS");
                startActivity(intentConfirm);
                finish();
            }

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        //로그인 부분 디비 접속해서 다음 액티비티에서 쓸 데이터 전부 세팅
                        String id, pw;
                        action = "Login";
                        id = edtID.getText().toString().trim();
                        pw = edtPW.getText().toString().trim();

                        if (id.equals("") && pw.equals("")) {
                            Toast.makeText(getApplicationContext(), "ID와 PW 전부 입력해주세요!", Toast.LENGTH_SHORT).show();
                        } else {
                            CustomTask task = new CustomTask();
                            result = task.execute(action, id, pw).get();
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            if (result.equals("로그인 성공")) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                //이동하기 전에 서버로 부터 가지고 와서 다 넣어주자.
                                action = "SearchData";
                                result = new CustomTask().execute(action, id).get();
                                //디비접속 데이터 세팅
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String json_id = jsonObject.getString("Id");
                                    String json_pn = jsonObject.getString("PN");
                                    String json_name = jsonObject.getString("Name");
                                    String json_picture = jsonObject.getString("PictureURL");
                                    int json_lockState = Integer.parseInt(jsonObject.getString("LockState"));
                                    String json_timeTable = jsonObject.getString("TimeTable");
                                    int json_ad = Integer.parseInt(jsonObject.getString("ad"));
                                    DO doInstance = DO.getInstance(json_id, json_pn, json_name, json_picture, json_lockState, json_timeTable, json_ad);
                                } catch (Exception e) {
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    private void checkNet() { //지정 ip 확인 부분
        //ip 확인
        String ip = "";
        action = "CheckNet";
        //휴대폰의 연결된 네트워크의 IP를 가지고 옵니다.
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress(); //그래서 IP에다 넣어줍니다.
                    }
                }
            }
        } catch (Exception e) {
        }
        Log.e("통신1", ""+ip);
        try {
            CustomTask task = new CustomTask();
            result = task.execute(action, ip).get();
        } catch (Exception e) {
        }
        if (result.endsWith("입니다.")) //IP가 지정된 IP, A.B. 클래스로 시작을 하면 실행이 됩니다.
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(getApplicationContext(), "와이파이가 잘못 연결되어 있습니다.", Toast.LENGTH_SHORT).show();
            Intent intentConfirm = new Intent();
            intentConfirm.setAction("android.settings.WIFI_SETTINGS");
            startActivity(intentConfirm);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PERMISSIONS_REQUEST_RESULT == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "권한 요청이 됐습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "권한 요청을 해주세요.", Toast.LENGTH_SHORT).show();
            }
            finish();
            return;
        }
    }
}
