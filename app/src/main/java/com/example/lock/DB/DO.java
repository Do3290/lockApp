package com.example.lock.DB;

import android.util.Log;

/**
 * Created by 도영 on 2018-06-03.
 */

public class DO {
    private String Id ="";
    private String PN = "";
    private String Name = "";
    private String PictureURL = "";
    private int LockState = 0;
    private String TimeTable = "";
    private int ad = 0;
    private static  DO doInstance = null;

    private DO(){}
    private DO(String id, String pn, String name, int lockState, String timeTable, int ad){
        setId(id);
        setPN(pn);
        setName(name);
        setPictureURL(null);
        setLockState(lockState);
        setTimeTable(timeTable);
        setAd(ad);
    }

    private DO(String id, String pn, String name, String pictureURL, int lockState, String timeTable, int ad){
        setId(id);
        setPN(pn);
        setName(name);
        setPictureURL(pictureURL);
        setLockState(lockState);
        setTimeTable(timeTable);
        setAd(ad);
    }

    public  static  DO getInstance(){
        if(doInstance == null)
            doInstance = new DO();
        return doInstance;
    }

    public  static DO getInstance(String id, String pn, String name, int lockState, String timeTable, int ad){
        if(doInstance == null)
            doInstance = new DO(id, pn, name, lockState, timeTable, ad);

        Log.e("결과", id + " do");
        return doInstance;
    }

    public  static DO getInstance(String id, String pn, String name, String pictureURL, int lockState, String timeTable, int ad){
        if(doInstance == null)
            doInstance = new DO(id, pn, name, pictureURL, lockState, timeTable, ad);
        return doInstance;
    }

    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }
    public String getPN() {
        return PN;
    }
    public void setPN(String pN) {
        PN = pN;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getPictureURL() {
        return PictureURL;
    }
    public void setPictureURL(String pictureURL) {
        PictureURL = pictureURL;
    }
    public int getLockState() {
        return LockState;
    }
    public void setLockState(int lockState) {
        LockState = lockState;
    }
    public String getTimeTable() {
        return TimeTable;
    }
    public void setTimeTable(String timeTable) {
        TimeTable = timeTable;
    }
    public int getAd() {
        return ad;
    }
    public void setAd(int ad) {
        this.ad = ad;
    }
}