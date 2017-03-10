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
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.caio.androidapp.MainActivity.KEY_ALARM;
import static com.caio.androidapp.MainActivity.KEY_HOUR;
import static com.caio.androidapp.MainActivity.KEY_INTERVAL;
import static com.caio.androidapp.MainActivity.KEY_MINUTE;

public class AddAlarm extends AppCompatActivity {

    Button bAddAlarm;
    AlarmManager aManager;
    TimePicker tPicker;
    Context myContext;
    EditText etInterval;
    EditText etNameMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        //Start Variables:
        tPicker = (TimePicker) findViewById(R.id.timePicker);
        tPicker.setIs24HourView(true);
        bAddAlarm = (Button) findViewById(R.id.AddAlarm);
        etInterval = (EditText) findViewById(R.id.etInterval);
        etNameMed = (EditText) findViewById(R.id.etNameMed);

        /* Configure Alarm Button to add new Alarm: */
        bAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = tPicker.getHour();
                int minute = tPicker.getMinute();
                String sInterval = etInterval.getText().toString();
                String sNameMed = etNameMed.getText().toString();

                String sHour = String.valueOf(hour);
                String sMinute = String.valueOf(minute);
                if (hour < 10){
                    sHour = "0" + sHour;
                }
                if (minute < 10){
                    sMinute = "0" + sMinute;
                }

                long interval = -1;
                DateFormat formatter = new SimpleDateFormat("hh:mm");
                try {
                    Date dateStart = formatter.parse("00:00");
                    Date dateInterval = formatter.parse(sInterval);
                    interval = dateInterval.getTime() - dateStart.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                    setResult(Activity.RESULT_FIRST_USER);
                    finish();
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_ALARM, sHour + ":" + sMinute );
                resultIntent.putExtra(KEY_HOUR, hour);
                resultIntent.putExtra(KEY_MINUTE, minute);
                resultIntent.putExtra(KEY_INTERVAL, interval);
                resultIntent.putExtra(MainActivity.KEY_MEDNAME, sNameMed);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });


        Intent intent = getIntent();
    }
}
