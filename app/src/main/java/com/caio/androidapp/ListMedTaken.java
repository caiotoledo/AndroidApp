package com.caio.androidapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static com.caio.androidapp.MainActivity.KEY_MED_ID;

/**
 * Created by caio on 18/03/17.
 */

public class ListMedTaken extends AppCompatActivity {

    ListView lvMed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_med_taken);

        int idMed = getIntent().getIntExtra(KEY_MED_ID, MainActivity.ALARM_VALUE_ERROR);
        DBHandler db = MainActivity.dbMed;
        //DBHandler db = new DBHandler(this);
        //List<GraphAlarm> graph = db.getAllGraphsFromMed(idMed);
        List<GraphAlarm> graph = db.getAllGraphs();

        ArrayList<String> values = new ArrayList<String>();

        for ( GraphAlarm g : graph ){
            values.add(g.getAlarmTime() + " " + String.valueOf(g.getMedTaken()) );
        }

        ArrayAdapter<String> lvAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);

        lvMed = (ListView) findViewById(R.id.lvMedTaken);
        lvMed.setAdapter(lvAdapter);
    }
}
