package com.caio.androidapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ALARM = 1;
    public static final int REQUEST_CODE_FINISH_ALARM = 2;

    public static final int ALARM_VALUE_SUCCESS = 0;
    public static final int ALARM_VALUE_ERROR = -1;

    public static final String ALARM_FINISH = "FINISH ALARM";
    public static final String KEY_MEDNAME = "KEY_MEDNAME";
    public static final String KEY_ALARM = "KEY_ALARM";
    public static final String KEY_MINUTE = "KEY_MINUTE";
    public static final String KEY_HOUR = "KEY_HOUR";
    public static final String KEY_INTERVAL = "KEY_INTERVAL";

    public static final String KEY_REQ_CODE = "KEY_REQ_CODE";

    public static final String KEY_MED_ID = "KEY_MED_ID";

    public static Context serviceRing = null;
    public static Intent intentRing;
    private static int medAlarmIDRing = ALARM_VALUE_ERROR;

    public static DBHandler dbMed = null;
    private static List<MedicineAlarm> listMed = null;

    Context myContext;

    public static Button bMedOK = null;
    public static Button bMedNOK = null;
    FloatingActionButton bAddAlarm;
    ListView lvAlarm;

    ListViewAdapter lvAdapter;

    public static int getMedAlarmIDRing() {
        return medAlarmIDRing;
    }

    public static void setMedAlarmIDRing(int medAlarmIDRing) {
        MainActivity.medAlarmIDRing = medAlarmIDRing;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.myContext = this;

        /*Initialize Database Medicine:*/
        dbMed = new DBHandler(this);
        listMed = dbMed.getAllMeds();

        //Configuring Medicine Alarm ListView
        lvAlarm = (ListView) findViewById(R.id.AlarmList);
        lvAdapter = new ListViewAdapter(this.myContext, R.layout.my_listview_layout, listMed);
        lvAlarm.setAdapter(lvAdapter);

        //Configuring Add Alarm Button
        bAddAlarm = (FloatingActionButton) findViewById(R.id.AddAlarm);
        bAddAlarm.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(v);
            }
        } );

        //Buttons for Alarm Off:
        bMedOK = (Button) findViewById(R.id.medOK);
        bMedOK.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) { medAlarmButton(true); }
        });

        bMedNOK = (Button) findViewById(R.id.medNOK);
        bMedNOK.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                medAlarmButton(false);
            }
        });

        if (serviceRing == null){
            bMedOK.setVisibility(View.INVISIBLE);
            bMedNOK.setVisibility(View.INVISIBLE);
        }
    }

    private void medAlarmButton(boolean medTake){
        if (serviceRing != null) {
            serviceRing.stopService(intentRing);

            Calendar c = Calendar.getInstance();
            Log.i("TimeNow", c.getTime().toString());

            // Save current Medicine Taken:
            dbMed.addGraph( new GraphAlarm(0, medAlarmIDRing, c.getTime().toString(), new Boolean(medTake)) );
            medAlarmIDRing = ALARM_VALUE_ERROR;
        }
        serviceRing = null;

        bMedOK.setVisibility(View.INVISIBLE);
        bMedNOK.setVisibility(View.INVISIBLE);
    }

    public void openActivity(View v){
        Intent intent;
        intent = new Intent(this, AddAlarm.class);
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
                    long long_interval = data.getLongExtra(KEY_INTERVAL, ALARM_VALUE_ERROR);
                    int interval = (int) long_interval;
                    String sAlarm = data.getStringExtra(KEY_ALARM);
                    String sNameMed = data.getStringExtra(KEY_MEDNAME);

                    /* Generate a Unique Request Code for Alarm: */
                    Random r = new Random();
                    int request_code_alarm = ALARM_VALUE_ERROR;
                    while(request_code_alarm == ALARM_VALUE_ERROR){
                        request_code_alarm = r.nextInt(Integer.MAX_VALUE);
                        for(MedicineAlarm m : listMed){
                            if (m.getAlarmRequestID() == request_code_alarm)
                                request_code_alarm = ALARM_VALUE_ERROR;
                        }
                    }

                    /* Configure Alarm: */
                    int ret = configAlarm(hour, minute, interval, request_code_alarm);

                    if (ret == ALARM_VALUE_SUCCESS){
                        dbMed.addMed(new MedicineAlarm(0, sNameMed, request_code_alarm, sAlarm, interval));
                        listMed = dbMed.getAllMeds();
                        lvAdapter.updateDataSource(listMed);
                    }
                } else if (resultCode == Activity.RESULT_CANCELED){
                    returnValue = "Cancelado pelo Usuario";

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, returnValue, duration);
                    toast.show();
                }
                else {
                    returnValue = String.valueOf(resultCode);

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, returnValue, duration);
                    toast.show();
                }


                break;
            }
        }
    }

    private int configAlarm(int hour, int minute, int interval, int request_code_alarm) {

        if (hour == ALARM_VALUE_ERROR || minute == ALARM_VALUE_ERROR || interval < 0 || request_code_alarm < 0 )
            return ALARM_VALUE_ERROR;

        Calendar now = Calendar.getInstance();
        final Calendar calendar = Calendar.getInstance();

        /* Set Calendar with TimePicker widget */
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        long alarmTime = calendar.getTimeInMillis();

        /* If time already passed add a day: */
        if (calendar.getTimeInMillis() <= now.getTimeInMillis()){
            alarmTime += (AlarmManager.INTERVAL_DAY+1);
        }

        /* Intent to Alarm Receiver: */
        Intent alarmIntent = new Intent(MainActivity.this, Alarm_Receiver.class);
        alarmIntent.putExtra(KEY_REQ_CODE, request_code_alarm);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, request_code_alarm, alarmIntent, 0);

        AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (interval > 0) {
            aManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, interval, pendingIntent);
        } else {
            aManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        }
        return ALARM_VALUE_SUCCESS;
    }

    public static void removeMed(Context context, int pos){
        int request_code_alarm = listMed.get(pos).getAlarmRequestID();

        /* Delete Alarm: */
        Intent alarmIntent = new Intent(context, Alarm_Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, request_code_alarm, alarmIntent, 0);
        AlarmManager aManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        aManager.cancel(pendingIntent);

        dbMed.deleteMed(listMed.get(pos));
        listMed = dbMed.getAllMeds();
    }

    public void onClickDelete(int position){
        Toast.makeText(getApplicationContext(), "cliquei! " + listMed.get(position).toString(), Toast.LENGTH_SHORT).show();
    }
}
