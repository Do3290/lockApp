package com.example.lock;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lock.DB.CustomTask;
import com.example.lock.DB.DO;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by 도영 on 2018-06-07.
 */
public class MyAdapter extends BaseAdapter { //리스트뷰 어댑터

    ListItem i;
    DO doInstance = null;
    Context context;
    //Adapter 에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListItem> listItemList = null;
    private int listCnt = 0;

    public MyAdapter(Context context,ArrayList<ListItem> list){
        this.context = context;
        listItemList = list;
        listCnt = listItemList.size();
    }


    @Override
    public int getCount() {//Adapter에 사용되는 데이터의 개수를 리턴
        return listCnt;
    }

    @Override
    public ListItem getItem(int position) {//position에 있는 데이터 리턴
        return listItemList.get(position);
    }

    @Override
    public long getItemId(int position) {//position에 있는 데이터와 관계된 row의 ID를 리턴
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//position에 위치한 데이터를 화면에 출력하는데 사용활 View를 리턴
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView==null){
            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_list, parent, false);
        }

        final TextView tv_time = (TextView)convertView.findViewById(R.id.tv_time);
        final TextView tv_classname = (TextView)convertView.findViewById(R.id.tv_classname);
        final TextView tv_location = (TextView)convertView.findViewById(R.id.tv_location);
        final ImageView iv_icon = (ImageView)convertView.findViewById(R.id.iv_icon);
        final ImageView ib_list = (ImageView)convertView.findViewById(R.id.ib_list);
        final CheckBox ib_lock = (CheckBox)convertView.findViewById(R.id.ib_lock);

        doInstance = DO.getInstance();

        tv_time.setText(listItemList.get(position).tv_time);
        tv_classname.setText(listItemList.get(position).tv_classname);
        tv_location.setText(listItemList.get(position).tv_location);
        iv_icon.setImageDrawable(listItemList.get(position).iv_icon);
        ib_list.setImageDrawable(listItemList.get(position).ib_list);
        GradientDrawable roundDraw = (GradientDrawable) context.getDrawable(R.drawable.background_round);
        ib_list.setBackground(roundDraw);
        ib_list.setClipToOutline(true);
        if((doInstance.getLockState() == 1)&&(pos == 0)){
            ib_lock.setChecked(true);
        }

        if(doInstance.getAd()==1){
            ib_list.setVisibility(View.VISIBLE);
        }else ib_list.setVisibility(View.GONE);

        ib_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pos == 0)
                    new CustomTask().execute("ChangeLock", doInstance.getId());
            }
        });
        ib_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pos == 0){
                    Intent intent = new Intent(context, StuListActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }
}
