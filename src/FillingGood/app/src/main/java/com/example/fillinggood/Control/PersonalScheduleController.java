package com.example.fillinggood.Control;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.example.fillinggood.Boundary.DBboundary.DBmanager;
import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.Boundary.personal_calendar.PersonalScheduleModificationForm;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.Entity.GroupMember;
import com.example.fillinggood.Entity.GroupSchedule;
import com.example.fillinggood.Entity.PersonalSchedule;
import com.example.fillinggood.Entity.Schedule;

import java.util.ArrayList;

public class PersonalScheduleController {

    public ArrayList<PersonalSchedule> getAllPS(String userID){
        ArrayList<PersonalSchedule> personalSchedules = DBmanager.getInstance().getPersonalSchedule(userID);
        MainActivity.User.setSchedules(personalSchedules);
        return personalSchedules;
    }

    public PersonalSchedule getPS(String userID, String date, String starttime){
        PersonalSchedule personalSchedule = PersonalSchedule.get(userID, date, starttime);
        return personalSchedule;
    }

    public ArrayList<GroupSchedule> getAllGS(String userID){
        ArrayList<Group> groups = Group.getAllUsersGroup(userID);
        ArrayList<GroupSchedule> allGS = new ArrayList<>();
        ArrayList<GroupSchedule> temp;
        for(int i = 0; i < groups.size(); i++){
            temp = GroupSchedule.getGroupSchedule(groups.get(i).getName());
            if(temp != null)
                allGS.addAll(GroupSchedule.getGroupSchedule(groups.get(i).getName()));
        }
        return allGS;
    }

    public void AddSchedule(GroupMember user, String name, String location, String description, int priority, String date, String startTime, String endTime){
        ArrayList<PersonalSchedule> personalSchedules = user.getPS();
        if(personalSchedules == null)
            personalSchedules = new ArrayList<>();
        PersonalSchedule personalSchedule = new PersonalSchedule(name, location, description, priority, date, startTime, endTime);
        personalSchedules.add(personalSchedule);
        //DBmanager.getInstance().savePersonalSchedule(user, personalSchedules);
        personalSchedule.saveThis(user.getID());
    }

    public void ModifySchedule(GroupMember user, PersonalSchedule thisSch, String name, String location, String description, int priority, String date, String startTime, String endTime){
        ArrayList<PersonalSchedule> personalSchedules = user.getPS();
        PersonalSchedule newone = new PersonalSchedule(name, location, description,priority,date,startTime,endTime);

        personalSchedules.remove(thisSch);
        personalSchedules.add(newone);
        user.setSchedules(personalSchedules);
        thisSch.delete(user.getID(), thisSch.getDate(), thisSch.getStartTime());
        newone.saveThis(user.getID());
    }

    public void DeleteSchedule(GroupMember user, PersonalSchedule thisSch){
        ArrayList<PersonalSchedule> personalSchedules = user.getPS();
        personalSchedules.remove(thisSch);
        user.setSchedules(personalSchedules);
        thisSch.delete(user.getID(), thisSch.getDate(), thisSch.getStartTime());
    }

    // 두 개의 PersonalSchedule 객체를 인자로 받아 이 두개의 시간대가 겹치는지 비교하여
    // 겹치면 true, 겹치지 않으면 false를 리턴합니다.
    public static boolean Overlap_add(PersonalSchedule newPS, Schedule PS) {
        return newPS.isOverlap_add(PS);
    }

    public static boolean Overlap_modify(PersonalSchedule previous, PersonalSchedule newPS, Schedule PS) {
        return newPS.isOverlap_mod(previous, PS);
    }

    public static void EveryTimeSave(ArrayList<PersonalSchedule> personalSchedules){
        for(int i = 0; i < personalSchedules.size(); i++){
            personalSchedules.get(i).saveThis(MainActivity.User.getID());
        }
    }
}
