package com.hbv2.icelandevents;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hbv2.icelandevents.Entities.User;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Martin on 21.2.2017.
 */

public class StoreUser {

    private static User user;
    private static Context mBase;

    public static void storeUserInfo(String username,String password,Context base){
        mBase = base;
        Gson gson = new Gson();
        user.setPassword(password);
        user.setUsername(username);
        String u = gson.toJson(user);
        Log.d("user :",u);

        String filename = "userInfo";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(u.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void skipUserInfo(){
        Gson gson = new Gson();
        user.setPassword("");
        user.setUsername("");
        String u = gson.toJson(user);
        Log.d("user",u);

        String filename = "userInfo";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(u.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        return mBase.openFileOutput(name, mode);
    }

}
