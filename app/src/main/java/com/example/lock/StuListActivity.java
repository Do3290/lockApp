package com.example.lock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import com.example.lock.DB.CustomTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by 상용 on 2018-06-17.
 */

public class StuListActivity extends AppCompatActivity {

    private ListView lv_stu;
    private ArrayList<StuItem> stuData = new ArrayList<StuItem>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_list);
        lv_stu = (ListView)findViewById(R.id.lv_stu);
        dataSetting();
    }

    private void dataSetting(){
        try {
            String action = "StuList";
            CustomTask task = new CustomTask();
            String result = task.execute(action).get();
            JSONArray jsonArray = new JSONArray(result);
            for (int a = 0; a < jsonArray.length(); a++) {
                JSONObject jsonObject = jsonArray.getJSONObject(a);
                StuItem item = new StuItem();
                //item.setPicture(jsonObject.getString("PictureURL"));
                item.setPictureURL(jsonObject.getString("PictureURL"));
                item.setName(jsonObject.getString("Name"));
                item.setId(jsonObject.getString("Id"));
                item.setLockState(Integer.parseInt(jsonObject.getString("LockState")));
                stuData.add(item);
            }
        }catch (Exception e){
            Log.e("결과", e.toString());
        }
        StuAdapter mStuAdapter = new StuAdapter(this, stuData);
        lv_stu.setAdapter(mStuAdapter);
    }
}
