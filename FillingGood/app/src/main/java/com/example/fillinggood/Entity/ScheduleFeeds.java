package com.example.fillinggood.Entity;

public class ScheduleFeeds {
    private String groupName;
    private String date;
    private String startTime;
    private int feedNum;
    private String feed;

    ScheduleFeeds(){}
    ScheduleFeeds(String groupName, String date, String startTime, int feedNum, String feed){
        this.groupName = groupName;
        this.date = date;
        this.startTime = startTime;
        this.feedNum = feedNum;
        this.feed = feed;
    }

    public String getGroupName(){return groupName;}
    public void setGroupName(String groupName){this.groupName = groupName;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public String getStartTime(){return startTime;}
    public void setStartTime(String startTime) {this.startTime = startTime;}

    public int getFeedNum(){return feedNum;}
    public void setFeedNum(int feedNum){this.feedNum = feedNum;}

    public String getFeed(){return feed;}
    public void setFeed(String feed){this.feed = feed;}
}
