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
import android.view.View;

import java.util.List;

import static com.caio.androidapp.MainActivity.ALARM_FINISH;
import static com.caio.androidapp.MainActivity.KEY_REQ_CODE;

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

        Log.d("RingtonePlayingService", "Received start id " + startId + ": " + intent);

        int req_code = intent.getIntExtra(KEY_REQ_CODE, MainActivity.ALARM_VALUE_ERROR);
        DBHandler dbMed = new DBHandler(this);
        List<MedicineAlarm> listMed = dbMed.getAllMeds();
        MedicineAlarm medAlarm = null;
        for (MedicineAlarm med : listMed){
            if (med.getAlarmRequestID() == req_code){
                medAlarm = med;
                MainActivity.setMedAlarmIDRing(medAlarm.getId());
            }
        }

        Log.d("RingtonePlayingService", "KEY_REQ_CODE: " + String.valueOf(req_code) );
        Log.d("RingtonePlayingService", "Medicine Name: " + medAlarm.getMedName());

        mPlayer = MediaPlayer.create(this, R.raw.ring);
        mPlayer.start();
        mPlayer.setLooping(true);

        Intent intentMain = new Intent(this.getApplicationContext(), MainActivity.class);
        intentMain.putExtra(ALARM_FINISH, ALARM_FINISH);
        PendingIntent pendIntentMain = PendingIntent.getActivity(this, 0, intentMain, 0);

        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder(this)
                .setContentTitle("Alarme Medicamento!")
                .setContentText("Hora de tomar " + medAlarm.getMedName())
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pendIntentMain)
                .setAutoCancel(true)
                .build();
        notifyManager.notify(0, notify);

        if (MainActivity.bMedOK != null) {
            MainActivity.bMedOK.setVisibility(View.VISIBLE);
        }
        if (MainActivity.bMedNOK != null){
            MainActivity.bMedNOK.setVisibility(View.VISIBLE);
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        mPlayer.stop();

        super.onDestroy();
    }
}
