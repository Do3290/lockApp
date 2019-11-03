package com.example.lock;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lock.DB.CustomTask;
import com.example.lock.DB.DO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 도영 on 2018-06-07.
 */
public class MainActivity extends AppCompatActivity {
    static int adState = 0; //교사 잠금 상태
    static int myState = 0; //학생 잠금 상태

    ArrayList<ListItem> data = new ArrayList<ListItem>();
    ListView listView;
    MyAdapter adapter;
    TextView tv_name;
    TextView tv_realtime;

    Handler mHandler = new Handler();
    Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //풀스크린
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(getApplicationContext(), FService.class)); // 언데드 서비스

        listView = (ListView) findViewById(R.id.listview);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_realtime = (TextView) findViewById(R.id.tv_realtime);

        MainTimerTask timerTask = new MainTimerTask(); // 실시간 시계 부분
        mTimer = new Timer();
        mTimer.schedule(timerTask, 500, 1000);

        final DO doInstance = DO.getInstance();
        //Log.e("결과1:" ,doInstance.getAd() + "");
        tv_name.setText(doInstance.getName() + " [ " + doInstance.getId() + " ]"); // 상단부분 이름 [ 학번 ]

        try {
            JSONArray jsonArray = new JSONArray(doInstance.getTimeTable());
            for (int a = 0; a < jsonArray.length(); a++) {
                JSONObject jsonObject = jsonArray.getJSONObject(a);
                ListItem item = new ListItem();
                item.tv_time = jsonObject.getString("time");
                item.tv_classname = jsonObject.getString("name");
                item.tv_location = jsonObject.getString("place");
                //시간표 UI 세팅
                if (a == 0) item.iv_icon = getDrawable(getApplicationContext(), R.drawable.cr_empty_1);
                else if ((a + 1) == jsonArray.length())
                    item.iv_icon = getDrawable(getApplicationContext(), R.drawable.cr_empty_3);
                else item.iv_icon = getDrawable(getApplicationContext(), R.drawable.cr_empty_2);

                item.ib_list = getDrawable(getApplicationContext(), R.drawable.list);

                data.add(item);
            }
        }catch (Exception e){
        }

        adapter = new MyAdapter(this, data);
        listView.setAdapter(adapter);
        if (doInstance.getAd() == 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            adState = Integer.parseInt(new CustomTask().execute("Lock").get());
                            myState = Integer.parseInt(new CustomTask().execute("MyLock", doInstance.getId()).get());
                            Log.e("통신", adState  + " : "+ myState);
                            if ((adState == 1) && (myState == 1)) { //수업 on & 학생 출첵 on
                                Intent i1 = new Intent(getApplicationContext(), Lock.class);
                                startActivity(i1);
                            }
                            if ((adState == 1) && (myState == 0)) { // 수업 on인데 학생이 출첵을 안한경우
                                Handler mHandler = new Handler(Looper.getMainLooper());
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "수업이 시작됬습니다. 출석체크를 해주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                }, 1000);
                            }
                            Thread.sleep(1000);
                        } catch (Exception e) {
                        }
                    }
                }
            }).start();
        } else {
            //선생일 경우 목록을 만들어 논다.
        }
    }

    public Drawable getDrawable(Context context, int id) { //빌드 버전에따라 Drawble이 안먹어서 맞춰준것
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        } else {
            return context.getDrawable(id);
        }
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            Date rightNow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy.MM.dd hh:mm:ss ");
            String dateString = formatter.format(rightNow);
            tv_realtime.setText(dateString); // Task 를 반복적으로 돌려 실시간 setText

        }
    };

    class MainTimerTask extends TimerTask {
        public void run() {
            mHandler.post(mUpdateTimeTask);
        }
    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        MainTimerTask timerTask = new MainTimerTask();
        mTimer.schedule(timerTask, 500, 3000);
        super.onResume();
    }
}




