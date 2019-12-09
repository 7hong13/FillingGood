package com.example.fillinggood.Entity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.example.fillinggood.Boundary.DBboundary.DBmanager;

public class PersonalSchedule extends Schedule {
    private int priority;

    // 생성자
    public PersonalSchedule() {};

    public PersonalSchedule(String name, String location, String description, int priority,
                            String date, String startTime, String endTime) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.priority = priority;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static PersonalSchedule get(String userID, String date, String starttime){
        String Date = date.replace(".", "");
        String StartTime = starttime.replace(":", "") + "00";
        return  DBmanager.getInstance().getOnePersonalSchedule(userID,Date,StartTime);
    }

    public void delete(String userID, String date, String starttime){
        String Date = date.replace(".", "");
        String StartTime = starttime.replace(":", "") + "00";
        DBmanager.getInstance().delOnePsch(userID, Date, StartTime);
    }

    public void saveThis(String userID){
        DBmanager.getInstance().saveOnePersonalSchedule(userID, this);
    }





    // GETTERs & SETTERs
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
}

