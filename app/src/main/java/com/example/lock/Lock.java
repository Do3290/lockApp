package com.example.lock;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lock.DB.CustomTask;
import com.example.lock.DB.DO;

public class Lock extends AppCompatActivity {
    /**
     * Created by 도영 on 2018-06-07.
     */
    TextView escape;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //풀스크린
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD); //잠금화면 무시
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        escape = (TextView)findViewById(R.id.escape);//임시 탈출방편
        escape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        MainActivity.adState = Integer.parseInt(new CustomTask().execute("Lock").get());
                        // 교사 잠금상태 반복체크 - 수업시작 on 시 바로 잠기게 하기위함
                        if(MainActivity.adState == 0){
                            finish();
                        }
                        if(MainActivity.myState == 0){
                            finish();
                        }
                        Thread.sleep(10000);
                    }catch (Exception e){
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { // 키 다운 무시 - true값을 계속 반환해서 무시함
        if(event.getAction() == KeyEvent.ACTION_DOWN)return  true;
        if(event.isSystem()){
        }
        if(keyCode == KeyEvent.KEYCODE_BACK)return true;
        else if (keyCode == KeyEvent.KEYCODE_POWER)return true;
        else if (keyCode == KeyEvent.KEYCODE_MENU)return true;
        else  if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)return true;
        else  if(keyCode == KeyEvent.KEYCODE_VOLUME_UP)return true;
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onUserLeaveHint() { // 작업관리자 시 새로운 락액티비티 실행
        finish();
        Intent i = new Intent(this, Lock.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,0);
        try{
            pendingIntent.send();
        }catch (PendingIntent.CanceledException e){
            e.printStackTrace();
        }

        startActivity(i);
        super.onUserLeaveHint();
    }
    public class Do_BroadcastReceiver extends BroadcastReceiver {
        //이건 작동 x 무시
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
            {
                Intent i = new Intent(context,Lock.class);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
                try {
                    pendingIntent.send();
                }catch (PendingIntent.CanceledException e){
                    e.printStackTrace();
                }
            }
            else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {}
        }
    }
}
