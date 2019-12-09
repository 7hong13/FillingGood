package com.example.fillinggood.Entity;

import java.util.ArrayList;

public class GroupMember {
    private String ID;
    private String name;
    private int age;
    private String major;
    private ArrayList<PersonalSchedule> schedules;
    private ArrayList<Group> groups;

    public GroupMember(){}
    GroupMember(String ID, String name, int age, String major){
        this.ID = ID;
        this.name = name;
        this.age = age;
        this.major = major;
    }

    public String getID(){return ID;}
    public void setID(String ID){this.ID = ID;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public int getAge(){return age;}
    public void setAge(int age){this.age = age;}

    public String getMajor(){return major;}
    public void setMajor(String major){this.major = major;}

    public ArrayList<Group> getGroups(){return groups;}
    public void setGroups(ArrayList<Group> groups){this.groups = groups;}

    public ArrayList<PersonalSchedule> getPS(){return schedules;}
    public void setSchedules(ArrayList<PersonalSchedule> personalSchedules){this.schedules = personalSchedules;}
}
