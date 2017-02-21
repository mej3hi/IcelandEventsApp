package com.hbv2.icelandevents.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Martin on 21.2.2017.
 */

public class NetworkChecker{


    public static boolean isOnline(ConnectivityManager cm){
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        } else{
            return false;
        }
    }

}
