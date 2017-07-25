package com.caio.androidapp;

/**
 * Created by caio on 18/03/17.
 */

public class GraphAlarm {

    private int     id;
    private int     idMedAlarm;
    private String  alarmTime;
    private Boolean medTaken;

    public GraphAlarm (){

    }

    public GraphAlarm(int id, int idMedAlarm, String alarmTime, Boolean medTaken) {
        this.id = id;
        this.idMedAlarm = idMedAlarm;
        this.alarmTime = alarmTime;
        this.medTaken = medTaken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMedAlarm() {
        return idMedAlarm;
    }

    public void setIdMedAlarm(int idMedAlarm) {
        this.idMedAlarm = idMedAlarm;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Boolean getMedTaken() {
        return medTaken;
    }

    public void setMedTaken(Boolean medTaken) {
        this.medTaken = medTaken;
    }
}
