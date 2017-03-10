package com.caio.androidapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.caio.androidapp.MainActivity.KEY_REQ_CODE;
import static com.caio.androidapp.MainActivity.intentRing;
import static com.caio.androidapp.MainActivity.serviceRing;

/**
 * Created by caio on 20/02/17.
 */

public class Alarm_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Alarm_Receiver", "Get into the Receiver!");

        int req_code = intent.getIntExtra(KEY_REQ_CODE, MainActivity.ALARM_VALUE_ERROR);

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra(KEY_REQ_CODE, req_code);

        context.startService(serviceIntent);

        intentRing = serviceIntent;
        serviceRing = context;
    }
}
