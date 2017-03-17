package com.hbv2.icelandevents.Service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbv2.icelandevents.Entities.UserInfo;
import com.hbv2.icelandevents.ExtraUtilities.PopUpMsg;
import com.hbv2.icelandevents.HttpRequest.HttpRequestUser;

import java.io.FileInputStream;
import java.util.Objects;


public class AutoLogin  {

    /**
     * It will check whether user has create userInfo and call Sign In.
     * @param base Is the Context
     * @return It return TRUE if it has crate userInfo, else it return FALSE.
     */
    public static boolean checkUserInfo(Context base){
        boolean userInfoExists = false;
        UserInfo userInfo;
        Gson gson = new GsonBuilder().excludeFieldsWithModifiers().create();
        try {
            FileInputStream fin = base.openFileInput("userInfo");
            int c;
            String temp = "";
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            if(temp != ""){
                userInfoExists = true;
                userInfo = gson.fromJson(temp,UserInfo.class);
                Log.d("AutoLogin",temp);
                if(NetworkChecker.isOnline(base)) {
                    if(!Objects.equals(UserInfo.getUsername(), "") && !Objects.equals(UserInfo.getPassword(), "")){
                        new HttpRequestUser().signInGet(UserInfo.getUsername(),UserInfo.getPassword());
                    }
                }else{
                    PopUpMsg.toastMsg("Cannot autoLogin network isn't available",base);
                }
            }
            fin.close();
        }
        catch (Exception e){
            userInfoExists = false;
        }
        return userInfoExists;
    }


}
