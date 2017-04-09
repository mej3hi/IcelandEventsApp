package com.hbv2.icelandevents;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbv2.icelandevents.Entities.UserInfo;

import java.io.FileOutputStream;

public class StoreUser {

    /**
     * Here we will store User information about user
     * @param username
     * @param password
     * @param base
     */
    public static void storeUserInfo(String username,String password,boolean login,Context base){
        storeUser(username,password,login,base);
    }

    public static void skipUserInfo(Context base){
        storeUser("","",false,base);
    }

    private static void storeUser(String username, String password, boolean login, Context base){
        Gson gson = new GsonBuilder().excludeFieldsWithModifiers().create();
        UserInfo.setPassword(password);
        UserInfo.setUsername(username);
        UserInfo.setLogin(false);
        String userInfo = gson.toJson(new UserInfo());
        UserInfo.setLogin(login);
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
