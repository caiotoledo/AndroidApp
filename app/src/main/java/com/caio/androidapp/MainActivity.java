package com.caio.androidapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ALARM = 1;
    public static final int REQUEST_CODE_FINISH_ALARM = 2;
    public static final int ALARM_VALUE_ERROR = -1;
    public static final String ALARM_FINISH = "FINISH ALARM";
    public static final String KEY_ALARM = "KEY_ALARM";
    public static final String KEY_MINUTE = "KEY_MINUTE";
    public static final String KEY_HOUR = "KEY_HOUR";

    public static Context serviceRing = null;
    public static Intent intentRing;

    Context myContext;

    Button bOffAlarm;
    FloatingActionButton fButton;
    TextView tvAlarm;

    List<String> equip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.myContext = this;

        ListView lista = (ListView) findViewById(R.id.AlarmList);

        equip = new ArrayList<String>();;
        equip.add("Teta");
        equip.add("Beta");
        equip.add("Gama");
        equip.add("Omega");
        equip.add("Omega");
        equip.add("Omega");
        equip.add("Omega");
        equip.add("Omega");
        equip.add("Omega");
        equip.add("Omega");
        equip.add("Omega");
        equip.add("Omega");

        //ArrayAdapter<String> arAdapter = new ArrayAdapter<String>(this.myContext, android.R.layout.simple_list_item_1, equip);
        lista.setAdapter(new ListViewAdapter(this.myContext, R.layout.my_listview_layout, equip));

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "cliquei! " + equip.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

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
                if (serviceRing != null) {
                    serviceRing.stopService(intentRing);
                } else {
                    Intent alarmIntent = new Intent(MainActivity.this, Alarm_Receiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, REQUEST_CODE_FINISH_ALARM, alarmIntent, 0);
                    AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    aManager.cancel(pendingIntent);
                }

                saveVariable(KEY_ALARM, "No Alarm Set!");
                tvAlarm.setText("No Alarm Set!");
                serviceRing = null;
            }
        });

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sTextAlarm = sharedPref.getString(KEY_ALARM, "No Alarm Set!");
        tvAlarm = (TextView) findViewById(R.id.tvAlarm);
        CharSequence csTextAlarm = sTextAlarm;
        tvAlarm.setText(csTextAlarm);
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
                    String sAlarm = data.getStringExtra(KEY_ALARM);

                    CharSequence csAlarm = sAlarm;
                    tvAlarm.setText(csAlarm);

                    saveVariable(KEY_ALARM, sAlarm);

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
        //alarmIntent = new Intent(this.myContext, Alarm_Receiver.class);
        Intent alarmIntent = new Intent(MainActivity.this, Alarm_Receiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, REQUEST_CODE_FINISH_ALARM, alarmIntent, 0);

        AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        aManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void saveVariable(String KeyName, String Value){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KeyName, Value);
        editor.commit();
    }

    public void onClickDelete(int position){
        Toast.makeText(getApplicationContext(), "cliquei! " + equip.get(position).toString(), Toast.LENGTH_SHORT).show();
    }
}
