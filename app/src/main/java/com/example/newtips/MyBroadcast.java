package com.example.newtips;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String mAction = intent.getAction();
        assert mAction != null;
        /**接收來自UDP回傳之訊息*/

        }
    }

