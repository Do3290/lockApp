package com.example.lock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * Created by 도영 on 2018-06-07.
 */
public class StartReceiver extends BroadcastReceiver { // 부팅완료 메세지를 받아 다시 실행시키는 모듈
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){//기기가 켜진 액션을 받아와 어플실행
            context.startActivity(new Intent(context,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
