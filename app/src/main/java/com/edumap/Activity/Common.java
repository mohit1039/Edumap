package com.edumap.Activity;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common {


    public static final String Update="Update";
    public static final String Delete="Delete";
    public static String year ;


    public static  boolean isConnectedtoInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null)
            {
                for (int i=0;i<info.length;i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean checkAdmin(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("Admin", MODE_PRIVATE);
        return sharedPref.getBoolean("Admin", false);
    }
}
