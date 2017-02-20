package com.caio.androidapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by caio on 20/02/17.
 */

public class RingtonePlayingService extends Service {

    MediaPlayer mPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        mPlayer = MediaPlayer.create(this, R.raw.ring);
        mPlayer.start();

        Intent intentMain = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendIntentMain = PendingIntent.getActivity(this, 0, intentMain, 0);

        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder(this)
                .setContentTitle("Alarm!")
                .setContentText("Click me to turn it off!")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pendIntentMain)
                .setAutoCancel(true)
                .build();
        notifyManager.notify(0, notify);

        //return START_STICKY;
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        Toast.makeText(this, "On Destroy Called", Toast.LENGTH_SHORT).show();

        super.onDestroy();
    }


}
