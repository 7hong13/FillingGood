package com.example.fillinggood.Control;

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


    // 모임 일정 만드는 함수
    public double[][] CreateGroupSC(Group g, String startdate) {
        double[][] temp = GroupSchedule.createGroupSch(g, startdate);
        return temp;
    }


    // 일정 추전 함수 : starttime 으로는 월요일 날짜가 들어와서 그 주의 월~일 일정 추천
    public String[] RecommendSchedule(double[][] gs, int min, String starttime) {
        String [] recomSch = GroupSchedule.RecommendSchedule(gs, min, starttime);
        return recomSch;
    }


    // [장소추천]
    //  전체 장소와의 피어슨 상관계수 구하고, value 순으로 정렬된 key List 반환
    public ArrayList top_match(HashMap<String, int[]> data, String loc) {
        ArrayList result = GroupSchedule.top_match(data, loc);
        return result;
    }
}
