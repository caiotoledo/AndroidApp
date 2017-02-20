package com.caio.androidapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.caio.androidapp.MainActivity.KEY_ALARM;
import static com.caio.androidapp.MainActivity.KEY_HOUR;
import static com.caio.androidapp.MainActivity.KEY_MINUTE;

public class AddAlarm extends AppCompatActivity {

    Button bAddAlarm;
    AlarmManager aManager;
    TimePicker tPicker;
    Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        //Start Variables:
        tPicker = (TimePicker) findViewById(R.id.timePicker);
        tPicker.setIs24HourView(true);
        bAddAlarm = (Button) findViewById(R.id.AddAlarm);

        /* Configure Alarm Button to add new Alarm: */
        bAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = tPicker.getHour();
                int minute = tPicker.getMinute();

                String sHour = String.valueOf(hour);
                String sMinute = String.valueOf(minute);

                if (hour < 10){
                    sHour = "0" + sHour;
                }
                if (minute < 10){
                    sMinute = "0" + sMinute;
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_ALARM, "Time Choose: " + sHour + ":" + sMinute );
                resultIntent.putExtra(KEY_HOUR, hour);
                resultIntent.putExtra(KEY_MINUTE, minute);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });


        Intent intent = getIntent();
    }
}
