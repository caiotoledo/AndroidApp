package com.caio.androidapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by caio on 20/02/17.
 */

public class Alarm_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Alarm_Receiver", "Get into the Receiver!");

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);

        context.startService(serviceIntent);
    }
}
