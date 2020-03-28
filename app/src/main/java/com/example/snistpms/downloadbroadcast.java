package com.example.snistpms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class downloadbroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("com.snistpms.downloadbroadcastACtion".equals(intent.getAction())){
            String rtext=intent.getStringExtra("com.snistpms.downloadverified");

        }
    }
}
