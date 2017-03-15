package com.hbv2.icelandevents.Service;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChecker{

    /**
     * Here ew are check for internet connection.
     * @param context context is the Context
     * @return Will return true if we have internet,else it return FALSE.
     */
    public static boolean isOnline(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        } else{
            return false;
        }
    }

}
