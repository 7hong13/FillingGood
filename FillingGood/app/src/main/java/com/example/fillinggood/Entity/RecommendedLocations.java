package com.example.fillinggood.Entity;

public class RecommendedLocations {
    private String groupName;
    private String date;
    private String startTime;
    private int recommendedRank;
    private String recommendedLocation;

    RecommendedLocations(){}
    RecommendedLocations(String groupName, String date, String startTime, int recommendedRank, String recommendedLocation){
        this.groupName = groupName;
        this.date = date;
        this.startTime = startTime;
        this.recommendedRank = recommendedRank;
        this.recommendedLocation = recommendedLocation;
    }

    public String getGroupName(){return groupName;}
    public void setGroupName(String groupName){this.groupName = groupName;}

    public String getDate(){return date;}
    public void setDate(String date){this.date = date;}

    public String getStartTime(){return startTime;}
    public void setStartTime(String startTime){this.startTime = startTime;}

    public int getRecommendedRank(){return recommendedRank;}
    public void setRecommendedRank(int recommendedRank){this.recommendedRank = recommendedRank;}

    public String getRecommendedLocation(){return recommendedLocation;}
    public void setRecommendedLocation(String recommendedLocation){this.recommendedLocation = recommendedLocation;}
}
