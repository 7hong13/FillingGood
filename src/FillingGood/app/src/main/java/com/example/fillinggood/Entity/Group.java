package com.example.fillinggood.Entity;

import com.example.fillinggood.Boundary.DBboundary.DBmanager;

import java.util.ArrayList;

public class Group {
    private String name;
    private String description;
    private String startSchedulePeriod;
    private String endSchedulePeriod;
    private  GroupMember groupLeader;
    private ArrayList<GroupMember> groupMembers;
    private ArrayList<GroupSchedule> groupSchedules;
    private ArrayList<GroupSchedule> preivousSchedules;
    private TimeTable tb;
    private double[][] timetable;

    public Group(){
        groupMembers = new ArrayList<>();
        groupSchedules = new ArrayList<>();
        preivousSchedules = new ArrayList<>();
        this.timetable = tb.getTimeTable();
    }
    public Group(String name, String description, ArrayList<GroupMember> groupMembers){
        this.name = name;
        this.description = description;
        this.groupMembers = groupMembers;
        this.tb = new TimeTable(this.name);
    }

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}

    public String getStartSchedulePeriod(){return startSchedulePeriod;}
    public void setStartSchedulePeriod(String startSchedulePeriod){this.startSchedulePeriod = startSchedulePeriod;}
    public String getEndSchedulePeriod(){return endSchedulePeriod;}
    public void setEndSchedulePeriod(String endSchedulePeriod){this.endSchedulePeriod = endSchedulePeriod;}

    public GroupMember getGroupLeader(){return groupLeader;}
    public void setGroupLeader(GroupMember groupLeader){this.groupLeader = groupLeader;}
    public ArrayList<GroupMember> getGroupMembers(){return groupMembers;}
    public void setGroupMembers(ArrayList<GroupMember> groupMembers){this.groupMembers = groupMembers;}

    public double[][] getGroupTimeTable(){return timetable;}
    public void setGroupTimeTable(double[][] timetable){this.timetable = timetable;}

    public ArrayList<GroupSchedule> getGroupSchedules(){return groupSchedules;}
    public ArrayList<GroupSchedule> getPreivousSchedules(){return preivousSchedules;}

    public static ArrayList<Group> getAllUsersGroup(String userID){
        ArrayList<Group> groups = DBmanager.getInstance().getUserGroup(userID);
        return groups;
    }

    // 그룹 관리
    public int AddMember(String memID){
        for(int i = 0; i < groupMembers.size(); i++){
            if(groupMembers.get(i).getID().equals(memID))
                return -1;
        }
        GroupMember member = DBmanager.getInstance().getUser(memID);
        groupMembers.add(member);
        return 1;
    }
    public void DelMember(String memID){
        for(int i = 0; i < groupMembers.size(); i++){
            if(groupMembers.get(i).getID().equals(memID)) {
                groupMembers.remove(i);
                ArrayList<String> temps = new ArrayList<>();
                for(int j = 0; j < groupMembers.size(); j++){
                    temps.add(groupMembers.get(j).getName());
                }
                DBmanager.getInstance().saveGroupMembers(this.name, this.groupLeader.getID(), temps);
                return;
            }
        }
    }
    public static void delGroup(String groupName){
        DBmanager.getInstance().delGroup(groupName);
    }
    public static void saveGroupInfo(String groupName, String groupDescription){
        DBmanager.getInstance().saveGroupInfo(groupName, groupDescription);
    }
    public static void saveGroupMember(String groupName, String userID, ArrayList<String> members){
        DBmanager.getInstance().saveGroupMembers(groupName, userID, members);
    }

    public String getAllString(){
        return "Group{" +
                "Name='" + this.name + '\'' +
                ", Description='" + this.description + '\'' +
                ", Start_Schedule_Period=" + this.startSchedulePeriod +
                ", End_Schedule_Period=" + this.endSchedulePeriod +
                '}';
    }
}
