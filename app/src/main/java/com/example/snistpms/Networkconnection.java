package com.example.snistpms;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Networkconnection {
    Context mcontext;
    public Networkconnection(Context context){
        mcontext=context;
    }
    public boolean isnetworkavailable(){
        ConnectivityManager cm = (ConnectivityManager)mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork!=null){
            return true;
        }
        else {
            return false;
        }
    }
}
