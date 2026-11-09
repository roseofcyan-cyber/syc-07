package com.syc.broadcastdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, R.string.toast_received_my, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, R.string.toast_abort, Toast.LENGTH_SHORT).show();
        abortBroadcast();
    }
}
