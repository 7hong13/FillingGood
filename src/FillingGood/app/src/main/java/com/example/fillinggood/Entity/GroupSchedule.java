package com.example.fillinggood.Entity;

import android.util.Log;

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
        this.Feeds = new ArrayList<String>();
    }

    // GETTERs & SETTERs
    public int getChoicedTimeRank() {return choicedTimeRank;}
    public void setChoicedTimeRank(int choicedTimeRank){this.choicedTimeRank = choicedTimeRank;}

    public int getChoicedLocationRank() {return choicedLocationRank;}
    public void setChoicedLocationRank(int choicedLocationRank){this.choicedLocationRank = choicedLocationRank;}

    public ArrayList<String> initFeeds(){
        ArrayList<String> temp;

        // temp에 db로부터 읽어오는 과정.
        temp = new ArrayList<String>();

        return temp;
    }
    public void setPreviousFeeds(){Feeds = new ArrayList<String>();}
    public ArrayList<String> getFeeds(){return this.Feeds;}

    // Previous Schedule
    // Add Update Del Feeds
    public void addFeed(String feed){
        Feeds.add(feed);
        Log.d("get", "Add"+feed);
    }
    public void updateFeed(String feed){
        for(int i = 0; i < Feeds.size(); i++){
            if(Feeds.get(i).equals(feed)){
                Feeds.add(i, feed);
                Feeds.remove(i+1);
                break;
            }
        }
    }
    public void deleteFeed(String feed){
        for(int i = 0; i < Feeds.size(); i++){
            if(Feeds.get(i).equals(feed)){
                Feeds.remove(i);
                break;
            }
        }
    }
    // groupName 바탕으로 group 구하기.
    // group 구해서 groupMember 구하기.
    // groupMember 구해서 일정 구하기.
    // 특정 member가 작성한다는 정보가 필요함.
    public static void ReAdjustment() {
        ArrayList<PersonalSchedule> pschs;
        String action = "좋음/나쁨"; // 얘 받아와야함.
/*
        // 시간 조정
        for(int i = 0; i < pschs.size();i++){
            if(isOverlap(pschs.get(i), this)) {
                if (action.equals("좋음"))
                    return;
                else if (action.equals("나쁨"))
                    return;
            }
            else{
                if(action.equals("좋음"))
                    return;
                else if(action.equals("나쁨"))
                    return;
            }
        }
*/
        // 장소 조정

        // DB에 저장
    }
    private static boolean isOverlap(Schedule one, Schedule two){
        TimeTable temp = new TimeTable();

        // 0 초기화는 임시.
        int one_start = 0, one_end = 0;
        int two_start = 0, two_end = 0;

        // 이 과정 꼭 필요할까?? getTimeName 해오는거. getTimeIndex는??
        for(int i = 0; i < temp.getHoursLength(); i++){
            if(temp.getTimeName(i).equals(one.startTime))
                one_start = i;
            if(temp.getTimeName(i).equals(one.endTime))
                one_end = i;
            if(temp.getTimeName(i).equals(two.startTime))
                two_start = i;
            if(temp.getTimeName(i).equals(two.endTime))
                two_end = i;
        }
        if(one_start <= two_start && one_end >= two_end) // one 중에 two
            return true;
        if(two_start <= one_start && two_end >= one_end) // two 중에 one
            return true;
        if(one_start <= two_end || one_end >= two_start) // 시작시간 끝시간 겹침
            return true;

        return false;
    }

}
