package com.example.fillinggood.Entity;

public class GroupSchedule {
    // 필드
    private String groupName;
    private String name;
    private String location;
    private String description;
    private String date;
    private String startTime;
    private String endTime;
    private int choicedTimeRank;
    private int choicedLocationRank;

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
    public String getName() { return name;}
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public int getChoicedTimeRank() {return choicedTimeRank;}
    public void setChoicedTimeRank(int choicedTimeRank){this.choicedTimeRank = choicedTimeRank;}

    public int getChoicedLocationRank() {return choicedLocationRank;}
    public void setChoicedLocationRank(int choicedLocationRank){this.choicedLocationRank = choicedLocationRank;}
}
