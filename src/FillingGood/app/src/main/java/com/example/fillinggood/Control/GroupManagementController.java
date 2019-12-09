package com.example.fillinggood.Control;

import android.content.Context;
import android.util.Log;

import com.example.fillinggood.Boundary.DBboundary.DBmanager;
import com.example.fillinggood.Boundary.group_calendar.GroupListviewAdapter;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.Entity.GroupMember;
import com.example.fillinggood.Entity.GroupSchedule;

import java.util.ArrayList;
import java.util.List;

public class GroupManagementController {
    private GroupMember user;
    private Group group;

    // 새 그룹 생성시
    public GroupManagementController(GroupMember user){
        this.user = user;
        this.group = new Group();
    }
    // 그룹 수정시
    public GroupManagementController(String groupname, GroupMember user){
        this.user = user;
        this.group = DBmanager.getInstance().getGroupInfo(groupname);
        this.group.setGroupLeader(DBmanager.getInstance().getGroupLeader(groupname));
        this.group.setGroupMembers(DBmanager.getInstance().getGroupMember(groupname));
    }
    // 내 모임 목록 클릭시

    public int FindMember(String memID){
        if(memID.equals(user.getID()))
            return -2; // 본인 ID
        ArrayList<String> AllUserID = DBmanager.getInstance().getAllUserID();
        // All user 뽑아서 해당 memID가 있는지 비교하기
        for(int i = 0; i < AllUserID.size(); i++){
            if(AllUserID.get(i).equals(memID))
                return 1; // 존재하는 id
        }
        return -1; // 존재하지 않는 id
    }

    public int AddMember(String memID){
        int adding = group.AddMember(memID);
        return adding;
    }

    public int FindGroup(String groupName){
        ArrayList<String> AllGroupName = Group.getAllGroupName();
        // All group 뽑아서 해당 groupName 있는지 비교
        for(int i = 0; i < AllGroupName.size(); i++){
            if(AllGroupName.get(i).equals(groupName))
                return 1; // 존재하는 name
        }
        return -1; // 존재하지 않는 name
    }

    public ArrayList<Group> FindAllUserGroup(String userID){
        ArrayList<Group> groups = Group.getAllUsersGroup(userID);
        return groups;
    }
    public void saveGroup(String userID, String groupName, String groupDescription, ArrayList<String> members){
        Group.saveGroupInfo(groupName, groupDescription);
        Group.saveGroupMember(groupName, userID, members);
    }

    public static ArrayList<GroupSchedule> getGroupSchedule(String groupName){
        return GroupSchedule.getGroupSchedule(groupName);
    }

    public void DelGroup(String groupName){
        Group.delGroup(groupName);
    }
}