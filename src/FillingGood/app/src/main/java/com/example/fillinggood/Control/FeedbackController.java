package com.example.fillinggood.Control;

import android.util.Log;

import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.Entity.GroupSchedule;
import com.example.fillinggood.Entity.PersonalSchedule;
import com.example.fillinggood.Entity.GroupMember;
import com.example.fillinggood.Boundary.DBboundary.DBmanager;

import java.util.ArrayList;

public class FeedbackController {
    private String groupName;
    public static GroupSchedule selectedSchedule;

    public FeedbackController(String groupName, GroupSchedule groupSchedule){
        this.groupName = groupName;
        this.selectedSchedule = groupSchedule;
    }
    public FeedbackController(){}

    public static ArrayList<GroupSchedule> getGroupSchedule(String groupName){
        ArrayList<GroupSchedule> groupSchedules = new ArrayList<>();
        groupSchedules = GroupSchedule.getGroupSchedule(groupName);
        return groupSchedules;
    }
    public static GroupSchedule getOneGroupSchedule(String groupName, String date, String startTime){
        GroupSchedule groupSchedule = new GroupSchedule();
        groupSchedule = GroupSchedule.getOneGroupSchedule(groupName, date, startTime);
        return groupSchedule;
    }

    public static ArrayList<GroupSchedule.Feed> getFeeds(String groupName, String date, String startTime){
        ArrayList<GroupSchedule.Feed> Feeds = new ArrayList<>();
        Feeds = GroupSchedule.getFeeds(groupName, date, startTime);
        return Feeds;
    }
    public static GroupSchedule.Feed getMyFeed(String groupName, String date, String startTime, String userID){
        GroupSchedule.Feed feed = new GroupSchedule.Feed();
        feed = GroupSchedule.getMyFeed(groupName, date, startTime, userID);
        return feed;
    }

    public void AddFeed(String groupName, String userID, String Feed){
        selectedSchedule.addFeed(groupName, userID, Feed);

        //selectedSchedule.ReAdjustment();

    }

    public static void UpdateFeed(String groupName, String userID, String Feed){
        selectedSchedule.updateFeed(groupName, userID, Feed);

        //selectedSchedule.ReAdjustment();

    }

    public static void DeleteFeed(String groupName, String userID){
        selectedSchedule.deleteFeed(groupName, userID);

        //selectedSchedule.ReAdjustment();

    }

}
