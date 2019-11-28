package com.example.fillinggood.Entity;

import java.util.ArrayList;

public class GroupSchedule extends Schedule {
    // 필드
    private int choicedTimeRank;
    private int choicedLocationRank;
    private ArrayList<String> Feeds;

    // 생성자
    public GroupSchedule() {};
    public GroupSchedule(String groupName, String name, String location, String description, String date,
                         String startTime, String endTime, int choicedTimeRank, int choicedLocationRank) {
        this.groupName = groupName;
        this.name = name;
        this.location = location;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.choicedTimeRank = choicedTimeRank;
        this.choicedLocationRank = choicedLocationRank;
    }

    // GETTERs & SETTERs
    public int getChoicedTimeRank() {return choicedTimeRank;}
    public void setChoicedTimeRank(int choicedTimeRank){this.choicedTimeRank = choicedTimeRank;}

    public int getChoicedLocationRank() {return choicedLocationRank;}
    public void setChoicedLocationRank(int choicedLocationRank){this.choicedLocationRank = choicedLocationRank;}

    public void setPreviousFeeds(){Feeds = new ArrayList<String>();}
    public ArrayList<String> getFeeds(){return this.Feeds;}
    // Add Update Del Feeds
}
