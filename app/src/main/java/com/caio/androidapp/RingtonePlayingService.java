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

import static com.caio.androidapp.MainActivity.ALARM_FINISH;

/**
 * Created by caio on 20/02/17.
 */

public class RingtonePlayingService extends Service {

    Context ringContext;

    MediaPlayer mPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        this.ringContext = this;

        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        mPlayer = MediaPlayer.create(this, R.raw.ring);
        mPlayer.start();
        mPlayer.setLooping(true);

        Intent intentMain = new Intent(this.getApplicationContext(), MainActivity.class);
        intentMain.putExtra(ALARM_FINISH, ALARM_FINISH);
        PendingIntent pendIntentMain = PendingIntent.getActivity(this, 0, intentMain, 0);
        //PendingIntent pendIntentMain = PendingIntent.getBroadcast(this, 0, intentMain, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder(this)
                .setContentTitle("Alarm!")
                .setContentText("Click me to turn it off!")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pendIntentMain)
                .setAutoCancel(true)
                .build();
        notifyManager.notify(0, notify);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        mPlayer.stop();

        Toast.makeText(this, "On Destroy Called", Toast.LENGTH_SHORT).show();

        super.onDestroy();
    }


}
