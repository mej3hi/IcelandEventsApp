package com.hbv2.icelandevents;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hbv2.icelandevents.Entities.UserInfo;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Martin on 21.2.2017.
 */

public class StoreUser {


    private static Context mBase;

    public static void storeUserInfo(String username,String password,Context base){
        mBase = base;
        storeUser(username,password);
    }

    public static void skipUserInfo(Context base){
        mBase = base;
        storeUser("","");
    }

    public static void storeUser(String username,String password){
        Gson gson = new Gson();
        UserInfo user = new UserInfo();
        user.setPassword(username);
        user.setUsername(password);
        String u = gson.toJson(user);
        Log.d("user",u);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput("userInfo", Context.MODE_PRIVATE);
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
