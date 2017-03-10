package com.caio.androidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MED_NAME = "medName";
    private static final String KEY_ALARM_ID = "AlarmRequestID";
    private static final String KEY_ALARM_START_TIME = "AlarmStartTime";
    private static final String KEY_ALARM_INTERVAL = "AlarmInterval";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MEDS + "("
        + KEY_ID + " INTEGER PRIMARY KEY,"
        + KEY_MED_NAME + " TEXT,"
        + KEY_ALARM_ID + " INTEGER,"
        + KEY_ALARM_START_TIME + " TEXT,"
        + KEY_ALARM_INTERVAL + " INTEGER"
        + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDS);
// Creating tables again
        onCreate(db);
    }

    // Adding new shop
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

    // Getting one shop
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

    // Getting All Shops
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
// Adding contact to list
                medList.add(med);
            } while (cursor.moveToNext());
        }

// return contact list
        return medList;
    }

    // Getting shops Count
    public int getMedsCount() {
        String countQuery = "SELECT * FROM " + TABLE_MEDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

// return count
        return cursor.getCount();
    }

    // Updating a shop
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

    // Deleting a shop
    public void deleteMed(MedicineAlarm med) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDS, KEY_ID + " = ?",
        new String[] { String.valueOf(med.getId()) });
        db.close();
    }
}