package com.example.fillinggood.Control;

import android.util.Log;

import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.Entity.GroupSchedule;
import com.example.fillinggood.Entity.PersonalSchedule;
import com.example.fillinggood.Entity.GroupMember;
import com.example.fillinggood.Boundary.DBboundary.DBmanager;

public class FeedbackController {
    private Group group;
    public static GroupSchedule selectedSchedule;

    public FeedbackController(){
        //String groupName = selectedSchedule.getGroupName();

    }

    public static void AddFeed(String Feed){
        selectedSchedule.addFeed(Feed);

        //selectedSchedule.ReAdjustment();
        Log.println(Log.ASSERT, "Good", "Add");
    }

    public static void UpdateFeed(String Feed){
        //selectedSchedule.updateFeed(Feed);

        //selectedSchedule.ReAdjustment();
        Log.println(Log.ASSERT, "Good", "Update");
    }

    public static void DeleteFeed(String Feed){
        //selectedSchedule.deleteFeed(Feed);

        //selectedSchedule.ReAdjustment();
        Log.println(Log.ASSERT, "Good", "Del");
    }

}
