package com.example.newtips;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public  String dataType="" ;
    public byte datable1;
    @Override
    public void onReceive(Context context, Intent intent) {
        String dataType=intent.getStringExtra("iuforon");
        byte datable1=intent.getByteExtra("stream3",(byte)0x00);
        this.dataType =dataType;
        this.datable1 =datable1;

    }
}
