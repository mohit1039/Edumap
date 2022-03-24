package com.edumap.Activity;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.edumap.Interface.OnGetDataListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

    public static void readData(DatabaseReference ref, final OnGetDataListener listener) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    listener.onSuccess(snapshot);
                } else {
                    listener.onFailure();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure();
            }
        });
    }

    public static void readQueryData(Query query , final OnGetDataListener listener) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    listener.onSuccess(snapshot);
                } else {
                    listener.onFailure();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure();
            }
        });
    }
}
