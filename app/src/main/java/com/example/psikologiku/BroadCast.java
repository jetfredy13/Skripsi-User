package com.example.psikologiku;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("toastMessage");
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
