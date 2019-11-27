package com.example.fillinggood.Entity;

public class RecommendedTimes {
    private String groupName;
    private String date;
    private String startTime;
    private int recommendedRank;
    private String recommendedDate;
    private String recommendedStartTime;
    private String recommendedEndTime;

    RecommendedTimes(){}
    RecommendedTimes(String groupName, String date, String startTime, int recommendedRank, String recommendedDate, String recommendedStartTime, String recommendedEndTime){
        this.groupName = groupName;
        this.date = date;
        this.startTime = startTime;
        this.recommendedRank = recommendedRank;
        this.recommendedDate = recommendedDate;
        this.recommendedStartTime = recommendedStartTime;
        this.recommendedEndTime = recommendedEndTime;
    }

    public String getGroupNmae(){return groupName;}
    public void setGroupName(String groupName){this.groupName = groupName;}

    public String getDate(){return date;}
    public void setDate(String date){this.date = date;}

    public int getRecommendedRank(){return recommendedRank;}
    public void setRecommendedRank(int recommendedRank){this.recommendedRank = recommendedRank;}

    public String getRecommendedDate(){return recommendedDate;}
    public void setRecommendedDate(String recommendedDate){this.recommendedDate = recommendedDate;}

    public String getRecommendedStartTime(){return recommendedStartTime;}
    public void setRecommendedStartTime(String recommendedStartTime){this.recommendedStartTime = recommendedStartTime;}

    public String getRecommendedEndTime(){return recommendedEndTime;}
    public void setRecommendedEndTime(String recommendedEndTime){this.recommendedEndTime = recommendedEndTime;}
}
