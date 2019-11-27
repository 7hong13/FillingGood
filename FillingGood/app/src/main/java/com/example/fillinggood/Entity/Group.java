package com.example.fillinggood.Entity;

import java.util.ArrayList;

public class Group {
    private String name;
    private String description;
    private ArrayList<String> groupMembers;

    public Group(){

    }
    public Group(String name, String description, ArrayList<String> groupMembers){
        this.name = name;
        this.description = description;
        this.groupMembers = groupMembers;
    }

    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}
    public ArrayList<String> getGroupMembers(){return groupMembers;}
    public void setGroupMembers(ArrayList<String> groupMembers){this.groupMembers = groupMembers;}
}
