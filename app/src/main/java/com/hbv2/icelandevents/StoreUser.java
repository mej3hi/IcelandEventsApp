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


    public static void storeUserInfo(String username,String password,Context base){
        storeUser(username,password,true,base);
    }

    public static void skipUserInfo(Context base){
        storeUser("","",false,base);
    }

    private static void storeUser(String username, String password, boolean login, Context base){
        Gson gson = new Gson();
        UserInfo user = new UserInfo();
        user.setPassword(username);
        user.setUsername(password);
        UserInfo.setLogin(login);
        UserInfo.setLoginUsername(username);

        String u = gson.toJson(user);
        Log.d("user",u);
        Log.d("user Is login",""+ user.isLogin());
       FileOutputStream outputStream;
        try {
            outputStream = base.openFileOutput("userInfo", Context.MODE_PRIVATE);
            outputStream.write(u.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private static FileOutputStream openFileOutput(String name, int mode, Context mBase) throws FileNotFoundException {
        return mBase.openFileOutput(name, mode);
    }*/

}
