package com.syc.broadcastdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_MY_BROADCAST = "com.syc.broadcastdemo.MY_BROADCAST";

    public static final String ACTION_LOCAL_BROADCAST = "com.syc.broadcastdemo.LOCAL_BROADCAST";

    private TimeChangeReceiver timeChangeReceiver;

    private LocalReceiver localReceiver;

    private class TimeChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, R.string.toast_time_changed, Toast.LENGTH_SHORT).show();
        }
    }

    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, R.string.toast_received_local, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerTimeChangeReceiver();
        registerLocalReceiver();

        Button btnSendStandard = findViewById(R.id.btn_send_standard);
        btnSendStandard.setOnClickListener(v -> {
            Intent intent = new Intent(ACTION_MY_BROADCAST);
            intent.setPackage(getPackageName());
            sendBroadcast(intent);
        });

        Button btnSendOrdered = findViewById(R.id.btn_send_ordered);
        btnSendOrdered.setOnClickListener(v -> {
            Intent intent = new Intent(ACTION_MY_BROADCAST);
            intent.setPackage(getPackageName());
            sendOrderedBroadcast(intent, null);
        });

        Button btnSendLocal = findViewById(R.id.btn_send_local);
        btnSendLocal.setOnClickListener(v -> {
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(new Intent(ACTION_LOCAL_BROADCAST));
        });
    }

    private void registerTimeChangeReceiver() {
        timeChangeReceiver = new TimeChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(timeChangeReceiver, intentFilter);
    }

    private void registerLocalReceiver() {
        localReceiver = new LocalReceiver();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(localReceiver, new IntentFilter(ACTION_LOCAL_BROADCAST));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeChangeReceiver != null) {
            unregisterReceiver(timeChangeReceiver);
            timeChangeReceiver = null;
        }
        if (localReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(localReceiver);
            localReceiver = null;
        }
    }
}
