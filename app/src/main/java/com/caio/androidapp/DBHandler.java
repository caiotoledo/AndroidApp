package com.caio.androidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caio on 04/03/17.
 */

public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MedicineAlarms";
    // Tables name
    private static final String TABLE_MEDS = "MedAlarm";
    private static final String TABLE_GRAPH = "MedGraph";
    // TABLE_MEDS Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MED_NAME = "medName";
    private static final String KEY_ALARM_ID = "AlarmRequestID";
    private static final String KEY_ALARM_START_TIME = "AlarmStartTime";
    private static final String KEY_ALARM_INTERVAL = "AlarmInterval";
    // TABLE_GRAPH Table Columns names
    private static final String KEY_ID_GRAPH = "id";
    private static final String KEY_ID_MED = "id_med";
    private static final String KEY_ALARM_TIME = "AlarmTime";
    private static final String KEY_MED_OK = "MedicineTaken";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARM_TABLE = "CREATE TABLE " + TABLE_MEDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_MED_NAME + " TEXT,"
                + KEY_ALARM_ID + " INTEGER,"
                + KEY_ALARM_START_TIME + " TEXT,"
                + KEY_ALARM_INTERVAL + " INTEGER"
                + ")";
        db.execSQL(CREATE_ALARM_TABLE);

        String CREATE_GRAPH_TABLE = "CREATE TABLE " + TABLE_GRAPH + " ("
                + KEY_ID_GRAPH + " INTEGER PRIMARY KEY,"
                + KEY_ID_MED + " INTEGER,"
                + KEY_ALARM_TIME + " TEXT,"
                + KEY_MED_OK + " INTEGER"
                + " FOREIGN KEY ("+KEY_ID_MED+") REFERENCES " + TABLE_MEDS + " ("+KEY_ID+")"
                + ")";
        db.execSQL(CREATE_GRAPH_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRAPH);
// Creating tables again
        onCreate(db);
    }

    // Adding new Medicine Alarm
    public void addMed(MedicineAlarm med) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MED_NAME, med.getMedName()); // Med Name
        values.put(KEY_ALARM_ID, med.getAlarmRequestID()); // Alarm Request ID
        values.put(KEY_ALARM_START_TIME, med.getAlarmStartTime()); // Alarm Start Time
        values.put(KEY_ALARM_INTERVAL, med.getAlarmInterval()); // Alarm Interval

// Inserting Row
        db.insert(TABLE_MEDS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new Point Graph
    public void addGraph(GraphAlarm g){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_MED, g.getIdMedAlarm());
        values.put(KEY_ALARM_TIME, g.getAlarmTime());
        values.put(KEY_MED_OK, g.getMedTaken());

        Log.i("DBHandler", String.valueOf(g.getIdMedAlarm()) + " " + g.getAlarmTime() + " " + g.getMedTaken());

        db.insert(TABLE_GRAPH, null, values);
        db.close();
    }

    // Getting one Medicine Alarm
    public MedicineAlarm getMed(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MEDS,new String[]{   KEY_ID,
                                                            KEY_MED_NAME,
                                                            KEY_ALARM_ID,
                                                            KEY_ALARM_INTERVAL,
                                                            KEY_ALARM_START_TIME}, KEY_ID + "=?",
                                            new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MedicineAlarm med = new MedicineAlarm(  Integer.parseInt(cursor.getString(0)),
                                                cursor.getString(1),
                                                Integer.parseInt(cursor.getString(2)),
                                                cursor.getString(3),
                                                Integer.parseInt(cursor.getString(4)) );
// return med
        return med;
    }

    public GraphAlarm getGraph(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MEDS,new String[]{   KEY_ID_GRAPH,
                                                            KEY_ID_MED,
                                                            KEY_ALARM_TIME,
                                                            KEY_MED_OK}, KEY_ID_GRAPH + "=?",
                                            new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        GraphAlarm g = new GraphAlarm(  Integer.parseInt(cursor.getString(0)),
                                        Integer.parseInt(cursor.getString(1)),
                                        cursor.getString(2),
                                        Boolean.parseBoolean(cursor.getString(3)));
        return g;
    }

    // Getting All Medicine Alarms
    public List<MedicineAlarm> getAllMeds() {
        List<MedicineAlarm> medList = new ArrayList<MedicineAlarm>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MEDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MedicineAlarm med = new MedicineAlarm();
                med.setId(Integer.parseInt(cursor.getString(0)));
                med.setMedName(cursor.getString(1));
                med.setAlarmRequestID(Integer.parseInt(cursor.getString(2)));
                med.setAlarmStartTime(cursor.getString(3));
                med.setAlarmInterval(Integer.parseInt(cursor.getString(4)));
// Adding medicine to list
                medList.add(med);
            } while (cursor.moveToNext());
        }

// return Medicine list
        return medList;
    }

    public List<GraphAlarm> getAllGraphs(){
        List<GraphAlarm> graphList = new ArrayList<GraphAlarm>();

        //String selectQuery = "SELECT * FROM " + TABLE_GRAPH;
        String selectQuery = "SELECT *";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                GraphAlarm g = new GraphAlarm();
                g.setId(Integer.parseInt(cursor.getString(0)));
                g.setIdMedAlarm(Integer.parseInt(cursor.getString(1)));
                g.setAlarmTime(cursor.getString(2));
                g.setMedTaken(Boolean.parseBoolean(cursor.getString(3)));
                graphList.add(g);
            } while (cursor.moveToNext());
        }

        return graphList;
    }

    public List<GraphAlarm> getAllGraphsFromMed(int idMed){
        List<GraphAlarm> graphList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_GRAPH + " WHERE " + KEY_ID_MED + " = " + String.valueOf(idMed);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                GraphAlarm g = new GraphAlarm();
                g.setId(Integer.parseInt(cursor.getString(0)));
                g.setIdMedAlarm(Integer.parseInt(cursor.getString(1)));
                g.setAlarmTime(cursor.getString(2));
                g.setMedTaken(Boolean.parseBoolean(cursor.getString(3)));
                graphList.add(g);
            } while (cursor.moveToNext());
        }

        return graphList;
    }

    // Getting Med Count
    public int getMedsCount() {
        String countQuery = "SELECT * FROM " + TABLE_MEDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

// return count
        return cursor.getCount();
    }

    // Updating a Medicine Alarm
    public int updateMed(MedicineAlarm med) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MED_NAME, med.getMedName()); // Med Name
        values.put(KEY_ALARM_ID, med.getAlarmRequestID()); // Alarm Request ID
        values.put(KEY_ALARM_START_TIME, med.getAlarmStartTime()); // Alarm Start Time
        values.put(KEY_ALARM_INTERVAL, med.getAlarmInterval()); // Alarm Interval

// updating row
        return db.update(TABLE_MEDS, values, KEY_ID + " = ?",
        new String[]{String.valueOf(med.getId())});
    }

    // Deleting a Medicine Alarm
    public void deleteMed(MedicineAlarm med) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDS, KEY_ID + " = ?",
                new String[] { String.valueOf(med.getId()) });
        db.close();
    }

    public void deleteGraph(GraphAlarm g){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GRAPH, KEY_ID_GRAPH + " = ?",
                new String[] { String.valueOf(g.getId()) });
        db.close();
    }
}