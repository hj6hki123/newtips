package com.example.newtips;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public static String data_light ;
    public static String data_device ;
    @Override
    public void onReceive(Context context, Intent intent) {
        String data_light=intent.getStringExtra("light");
        String data_device=intent.getStringExtra("device");

        MyBroadcastReceiver.data_light =data_light;
        MyBroadcastReceiver.data_device =data_device;

    }
}
