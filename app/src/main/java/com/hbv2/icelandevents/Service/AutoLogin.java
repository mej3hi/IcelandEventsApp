package com.hbv2.icelandevents.Service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hbv2.icelandevents.Entities.User;
import com.hbv2.icelandevents.HttpRequest.HttpRequestLogin;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Martin on 21.2.2017.
 */

public class AutoLogin  {

    private static Context mBase;

    public static boolean checkUserInfo(Context base){
        boolean userInfo = false;
        mBase = base;
        User user;
        Gson gson = new Gson();
        String filename = "userInfo";
        try {
            FileInputStream fin = openFileInput(filename);
            int c;
            String temp = "";
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            if(temp != ""){
                userInfo = true;
                Log.d("Temp ","Temp er ekki tómur");
                user = gson.fromJson(temp,User.class);

                if(user.getUsername() != "" && user.getPassword() != ""){
                    Log.d("Temp","Þá loga okku inn User = "+user.getUsername()+" Pass = "+user.getPassword());
                    new HttpRequestLogin().loginGet(user.getUsername(),user.getPassword());
                }
            }
            fin.close();
        }
        catch (Exception e){
            Log.d("tempErro","temp er ekki til");
            userInfo = false;
        }

        return userInfo;
    }

    public static FileInputStream openFileInput(String name)throws FileNotFoundException {
        return mBase.openFileInput(name);
    }

}
