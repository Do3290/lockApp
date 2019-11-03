package com.example.lock.DB;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 도영 on 2018-05-31.
 */

public class CustomTask extends AsyncTask<String, Void, String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try {
            String str;
            URL url = new URL("http://14.34.195.229:8080/ILock_Server_v2/" + strings[0] + ".jsp");
            //Log.e("결과", url + "");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            if(strings[0].equals("CheckNet"))
                sendMsg = "ip=" + strings[1];
            else if(strings[0].equals("Login"))
                sendMsg = "PN=" + strings[1] + "&pw=" + strings[2];
            else if(strings[0].equals("SearchData"))
                sendMsg = "PN=" + strings[1];
            else if(strings[0].equals("MyLock"))
                sendMsg = "Id=" + strings[1];
            else if(strings[0].equals("ChangeLock"))
                sendMsg = "cId=" + strings[1];
            else //Lock //StuList
                sendMsg = "";
            //Log.e("결과", sendMsg + "");
            osw.write(sendMsg);
            osw.flush();

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();

            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }
}
