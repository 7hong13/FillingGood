package com.example.fillinggood.Control;

import android.util.Log;

import com.example.fillinggood.Boundary.DBboundary.DBmanager;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.Entity.GroupMember;
import com.example.fillinggood.Entity.GroupSchedule;
import com.example.fillinggood.Entity.PersonalSchedule;
import com.example.fillinggood.Entity.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RecommendationController {

    public static Group getGroup(String groupName){
        Group g = Group.getGroup(groupName);
        g.getTimeTable(groupName);
        return g;
    }

    public static void saveGroupSEperiod(String groupName, String startperiod, String endperiod){
        Group.saveGroupSEperiod(groupName, startperiod, endperiod);
    }

    public static void additionSaveRecommending(String groupName, String name, int ExpectTime){
        GroupSchedule.additionsaveRecommending(groupName, name, ExpectTime);
    }

    // 모임 일정 만드는 함수
    public double[][] CreateGroupSC(Group g, String startdate) {
        double[][] temp = GroupSchedule.createGroupSch(g, startdate);
        return temp;
    }


    // 일정 추천 함수 : starttime 으로는 월요일 날짜가 들어와서 그 주의 월~일 일정 추천
    public String[] RecommendSchedule(double[][] gs, int min, String starttime, String groupName) {
        String[] recomSch = GroupSchedule.RecommendSchedule(gs, min, starttime, groupName);
        return recomSch;
    }
    public String[] getRT(String groupName){
        String[] rt = GroupSchedule.getRT(groupName);
        return rt;
    }

    // 추천된 일정 내용 저장
    public static void saveTimeChoiceRecommending(String groupName, String userID, int rank){
        GroupSchedule.saveTimeChoiceRecommending(groupName, userID, rank);
    }
    public static void saveLocChoiceRecommending(String groupName, String userID, int rank){
        GroupSchedule.saveLocChoiceRecommending(groupName, userID, rank);
    }


    // [장소추천]
    //  전체 장소와의 피어슨 상관계수 구하고, value 순으로 정렬된 key List 반환
    public ArrayList top_match(HashMap<String, int[]> data, String loc) {
        ArrayList result = GroupSchedule.top_match(data, loc);
        return result;
    }

    // end recom
    public static void makeGsch(String groupName, String rt, int timerank, int locrank, String name, String loc){
        Log.d("check", "start Controller makeGsch");
        GroupSchedule.makeGsch(groupName, rt, timerank, locrank, name, loc);
    }
    public static void Adjustment(Group g, String rt, double[][] tt){
        double[][] ad_tt = Group.getFeedTimeTable(rt, tt);
        g.setGroupTimeTable(ad_tt);
        DBmanager.getInstance().saveTimeTable(g.getName(), ad_tt);
    }
}
