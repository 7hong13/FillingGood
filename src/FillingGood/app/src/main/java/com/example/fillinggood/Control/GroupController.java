package com.example.fillinggood.Control;

import android.content.Context;

import com.example.fillinggood.Boundary.group_calendar.GroupListviewAdapter;
import com.example.fillinggood.Entity.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupController {

    private ArrayList<Group> mdatas;
    private GroupListviewAdapter mPA;
    private Context a;

    public GroupController(ArrayList<Group> A, GroupListviewAdapter b){
        mdatas = A;
        mPA = b;
    }


    public List<Group> getMdatas(){
        return this.mdatas;
    }

    public GroupListviewAdapter getMPA(){
        return this.mPA;
    }


}
