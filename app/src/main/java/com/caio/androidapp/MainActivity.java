package com.caio.androidapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ALARM = 1;
    public static final int ALARM_VALUE_ERROR = -1;
    public static final String KEY_ALARM = "KEY_ALARM";
    public static final String KEY_MINUTE = "KEY_MINUTE";
    public static final String KEY_HOUR = "KEY_HOUR";

    Context myContext;

    Button bOffAlarm;
    FloatingActionButton fButton;

    AlarmManager aManager;
    PendingIntent pendingIntent;
    Intent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.myContext = this;

        fButton = (FloatingActionButton) findViewById(R.id.AddAlarm);
        fButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(v);
            }
        } );

        bOffAlarm = (Button) findViewById(R.id.stopAlarm);
        bOffAlarm.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                aManager.cancel(pendingIntent);
                sendBroadcast(alarmIntent);
            }
        });
    }

    public void openActivity(View v){
        Intent intent;
        intent = new Intent(this, AddAlarm.class);
        //startActivity(intent);
        startActivityForResult(intent, REQUEST_CODE_ALARM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQUEST_CODE_ALARM) : {
                String returnValue;
                if (resultCode == Activity.RESULT_OK) {
                    returnValue = data.getStringExtra(KEY_ALARM);

                    int hour = data.getIntExtra(KEY_HOUR, ALARM_VALUE_ERROR);
                    int minute = data.getIntExtra(KEY_MINUTE, ALARM_VALUE_ERROR);

                    /* Configure Alarm: */
                    configAlarm(hour, minute);

                } else if (resultCode == Activity.RESULT_CANCELED){
                    returnValue = "Cancelado pelo Usuario";
                }
                else {
                    returnValue = String.valueOf(resultCode);
                }

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, returnValue, duration);
                toast.show();
                break;
            }
        }
    }

    private void configAlarm(int hour, int minute) {

        if (hour == ALARM_VALUE_ERROR || minute == ALARM_VALUE_ERROR)
            return;

        final Calendar calendar = Calendar.getInstance();

        /* Set Calendar with TimePicker widget */
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        /* Intent to Alarm Receiver: */
        alarmIntent = new Intent(this.myContext, Alarm_Receiver.class);

        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        aManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }
}
