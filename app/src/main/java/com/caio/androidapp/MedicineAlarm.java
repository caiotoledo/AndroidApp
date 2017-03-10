package com.caio.androidapp;

/**
 * Created by caio on 03/03/17.
 */

public class MedicineAlarm {

    private int     id;
    private String  medName;
    private int     AlarmRequestID;
    private String  AlarmStartTime;
    private int     AlarmInterval;

    public MedicineAlarm() {
    }

    public MedicineAlarm(int id, String medName, int alarmRequestID, String alarmStartTime, int alarmInterval) {
        this.id = id;
        this.medName = medName;
        AlarmRequestID = alarmRequestID;
        AlarmStartTime = alarmStartTime;
        AlarmInterval = alarmInterval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public int getAlarmRequestID() {
        return AlarmRequestID;
    }

    public void setAlarmRequestID(int alarmRequestID) {
        AlarmRequestID = alarmRequestID;
    }

    public String getAlarmStartTime() {
        return AlarmStartTime;
    }

    public void setAlarmStartTime(String alarmStartTime) {
        AlarmStartTime = alarmStartTime;
    }

    public int getAlarmInterval() {
        return AlarmInterval;
    }

    public String getAlarmIntervalStringTime(){
        int hour = AlarmInterval / (60 * 60 * 1000);
        int minute = (AlarmInterval - (hour * (60 * 60 * 1000))) / (60 * 1000);

        String sHour = String.valueOf(hour);
        String sMinute = String.valueOf(minute);
        if (hour < 10){
            sHour = "0" + sHour;
        }
        if (minute < 10){
            sMinute = "0" + sMinute;
        }

        return sHour + ":" + sMinute;
    }

    public void setAlarmInterval(int alarmInterval) {
        AlarmInterval = alarmInterval;
    }
}
