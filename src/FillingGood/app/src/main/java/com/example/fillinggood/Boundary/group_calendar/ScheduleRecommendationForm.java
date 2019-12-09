package com.example.fillinggood.Boundary.group_calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.fillinggood.Boundary.personal_calendar.PersonalScheduleList;
import com.example.fillinggood.Control.RecommendationController;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

//추천 결과 보여주는 class 입니다
public class ScheduleRecommendationForm extends AppCompatActivity implements MonthLoader.MonthChangeListener{
    private com.alamkanak.weekview.WeekView weekView;
    private String groupName;
    RecommendationController recommendationController = new RecommendationController();
    private String startdate, enddate;
    // 모임 일정 생성 시 입력받은 모임 일정 기간. '설정 안 함' 선택했으면 오늘 날짜로부터 일주일
    private int time;  // 모임 일정 생성 시 입력받은 예상 모임 시간 가져오기(아직 구현 못함)
    private Calendar cal = Calendar.getInstance();
    private SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");
    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        groupName = getIntent().getStringExtra("groupName");
        Bundle bundle = new Bundle();
        bundle.putString("groupName",groupName);
        bundle.putString("date", date.toString());

        // startdate, endate, time 받아오기(수정 필요)

        if (true) {  // 모임 일정 생성 시 모임 일정 기간 '설정 안 함' 선택
            startdate = date.format(cal.getTime());  // 오늘 날짜

            cal.add(Calendar.DATE, 7);
            enddate = date.format(cal.getTime());  // 일주일 뒤 날짜
        }
        // 모임 일정 기간 선택 시에는 그 값 불러오기
        time = 120;
        String[] rt = new String[5];
        rt = recommendationController.RecommendSchedule(
                recommendationController.CreateGroupSC(
                        Group.getGroup(groupName), startdate), time, startdate, groupName);
        Calendar startTime;
        Calendar endTime;

        for (int i=0; i<5; i++){
            int hour, minute, day, hour2, minute2;
            String s = rt[i];
            hour = Integer.parseInt(s.substring(11,13));
            minute = Integer.parseInt(s.substring(14,16));
            day = Integer.parseInt(s.substring(8,10));
            hour2 = Integer.parseInt(s.substring(30,32));
            minute2 = Integer.parseInt(s.substring(33));

            startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, hour);
            startTime.set(Calendar.MINUTE, minute);
            startTime.set(Calendar.DAY_OF_MONTH, day);
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);

            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, hour2);
            endTime.set(Calendar.MINUTE, minute2);
            endTime.set(Calendar.MONTH, newMonth - 1);
            WeekViewEvent event = new WeekViewEvent(i+1, (i+1)+"순위", startTime, endTime);
            event.setColor(Color.parseColor("#8022c6cf"));
            events.add(event);
        }
        return events;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_schedule);

        //달력 할당
        weekView = findViewById(R.id.weekView);
        weekView.setMonthChangeListener(this);
        weekView.goToHour(8);
        groupName = getIntent().getStringExtra("groupName");

        //시간 추천 리스트를 보여주는 fragment 호출
        showTimeRecommendation();
    }

    public void showTimeRecommendation(){
        Fragment fr = new TimeRecommendationList(groupName);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.add(R.id.recommendation_list, fr);

        fragmentTransaction.commit();
    }
}