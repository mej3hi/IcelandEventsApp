package com.hbv2.icelandevents;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbv2.icelandevents.Entities.UserInfo;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Modifier;

/**
 * Created by Martin on 21.2.2017.
 */

public class StoreUser {


    public static void storeUserInfo(String username,String password,Context base){
        storeUser(username,password,true,base);
    }

    public static void skipUserInfo(Context base){
        storeUser("","",false,base);
    }

    private static void storeUser(String username, String password, boolean login, Context base){
        Gson gson = new GsonBuilder().excludeFieldsWithModifiers().create();
        UserInfo.setPassword(username);
        UserInfo.setUsername(password);
        UserInfo.setLogin(login);
        String userInfo = gson.toJson(new UserInfo());
        FileOutputStream outputStream;
        try {
            Log.d("User info StoreUser",userInfo);
            outputStream = base.openFileOutput("userInfo", Context.MODE_PRIVATE);
            outputStream.write(userInfo.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
