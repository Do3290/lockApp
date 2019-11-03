package com.example.lock;


import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by 도영 on 2018-06-07.
 */
public class ListItem {
    public String tv_time; // 강의시간
    public Drawable iv_icon; //circle + line부분
    public String tv_classname;//강의 제목
    public String tv_location;//강의 위치
    public Drawable ib_list;//학생목록 -> 교수
    public Drawable ib_lock;//잠금버튼 -> 학생 수업시작 -> 교수
}
