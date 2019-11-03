package com.example.lock;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lock.DB.CustomTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by 상용 on 2018-06-17.
 */

public class StuAdapter extends BaseAdapter{
    static String[] pos_id = null;
    private ArrayList<StuItem> stuItems = new ArrayList<>();
    private int stuCnt = 0;
    private Context context;
    public StuAdapter(Context context, ArrayList<StuItem> list){
        this.context = context;
        stuItems = list;
        stuCnt = stuItems.size();
        pos_id = new String[stuCnt];
    }


    @Override
    public int getCount() {
        return stuItems.size();
    }

    @Override
    public StuItem getItem(int i) {
        return stuItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.stu_list_item, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        ImageView iv_picture = (ImageView) convertView.findViewById(R.id.iv_picture) ;
        TextView tv_stuName = (TextView) convertView.findViewById(R.id.tv_stuName) ;
        TextView tv_stuId = (TextView) convertView.findViewById(R.id.tv_stuId) ;
        CheckBox chk_lock = (CheckBox)convertView.findViewById(R.id.chk_lock) ;
        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        StuItem stuItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        Picasso.with(context).load("http://121.170.72.152:8080/ILock_Server_v2/picture/" + stuItem.getPictureURL()).into(iv_picture);
        //iv_picture.setImageDrawable(stuItem.getPicture());
        tv_stuName.setText(stuItem.getName());
        tv_stuId.setText(stuItem.getId());

        if(stuItem.getLockState() == 1){
            chk_lock.setChecked(true);
            pos_id[position] = stuItem.getId();
        }

        chk_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomTask().execute("ChangeLock", pos_id[position]);
            }
        });
        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */


        return convertView;
    }

    public void addItem(String pictureURL, String name, String id){
        StuItem stuItem = new StuItem();
        stuItem.setPictureURL(pictureURL);
        stuItem.setName(name);
        stuItem.setId(id);
        stuItems.add(stuItem);
    }
}
