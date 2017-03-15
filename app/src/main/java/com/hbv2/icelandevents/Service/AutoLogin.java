package com.hbv2.icelandevents.Service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbv2.icelandevents.API.UserAPI;
import com.hbv2.icelandevents.Entities.UserInfo;
import com.hbv2.icelandevents.HttpRequest.HttpRequestCall;

import java.io.FileInputStream;

import retrofit2.Call;

public class AutoLogin  {

    /**
     * It will check whether user has create userInfo and if so it will log in.
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
                if(UserInfo.getUsername() != "" && UserInfo.getPassword() != ""){
                    Call<String> call = ServiceGenerator.createService(UserAPI.class,UserInfo.getUsername(),UserInfo.getPassword()).getSignIn();
                    HttpRequestCall.callReponseMsg(call);
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
