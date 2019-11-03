package com.example.lock;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.lock.DB.CustomTask;

/**
 * Created by 도영 on 2018-06-07.
 */
public class FService extends Service { //포그라운드에 돌 서비스
    int stateon = 1;
    int statelock = 0;
    Intent i = null;
    int f  = 0;
    int startId =0;

    @Override
    public void onCreate() { //여기다 넣으시면 재시작 후 실행이 됩니다.
        startForeground(1,new Notification());
        super.onCreate();
        /*
        try {
            MainActivity.adState = Integer.parseInt(new CustomTask().execute("Lock").get());
            if(true){
                //잠그기
                Intent i1 = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i1);
            }
        }catch (Exception e){
        }*/
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { // 서비스가 실행됬던 상태에서 재시작할경우 StartCommand 실행
        locklock(intent.getIntExtra("state",0));
        startForeground(2,new Notification());
        return START_STICKY;
    }
    public void locklock(int state){ // 토스트메세지를 띄워볼랬던 lockstate 확인

        statelock = state;
        if(stateon==statelock){
            Intent i2 = new Intent(getApplicationContext(),LoginActivity.class);
            i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i2);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
