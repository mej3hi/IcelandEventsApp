package com.hbv2.icelandevents.Service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hbv2.icelandevents.Entities.UserInfo;
import com.hbv2.icelandevents.HttpRequest.HttpRequestSignIn;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Martin on 21.2.2017.
 */

public class AutoLogin  {



    /**
     * It will check whether user has create userInfo,
     * and if it
     * @param base
     * @return
     */
    public static boolean checkUserInfo(Context base){
        boolean userInfo = false;
        UserInfo user;
        Gson gson = new Gson();
        try {
            //FileInputStream fin = openFileInput("userInfo",base);
            FileInputStream fin = base.openFileInput("userInfo");
            int c;
            String temp = "";
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            if(temp != ""){
                userInfo = true;
                Log.d("Autologin","userInfo er til");
                user = gson.fromJson(temp,UserInfo.class);
                UserInfo.setLogin(false);
                UserInfo.setLoginUsername(user.getUsername());
                if(user.getUsername() != "" && user.getPassword() != ""){
                    Log.d("Autologin","Þá loga okku inn UserInfo = "+user.getUsername()+" Pass = "+user.getPassword());
                    new HttpRequestSignIn().autoSignInGet(user.getUsername(),user.getPassword());
                }
            }
            fin.close();
        }
        catch (Exception e){
            Log.d("AutoLogin","userInfo er ekki til ");
            userInfo = false;
        }
        return userInfo;
    }

   /* private static FileInputStream openFileInput(String name, Context mBase)throws FileNotFoundException {
        return mBase.openFileInput(name);
    }*/

}
