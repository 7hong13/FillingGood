package com.example.fillinggood.Boundary.group_calendar;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.fillinggood.Boundary.personal_calendar.PersonalScheduleList;
import com.example.fillinggood.R;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

//추천 결과 보여주는 class 입니다
public class ScheduleRecommendationForm extends AppCompatActivity implements MonthLoader.MonthChangeListener{
    private com.alamkanak.weekview.WeekView weekView;
    private String groupName, date, startTime;
    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        groupName = getIntent().getStringExtra("groupName");
        Bundle bundle = new Bundle();
        bundle.putString("groupName",groupName);
        bundle.putString("date", date);
        /*
        ArrayList<String> list = new ArrayList<>(); //시간 추천 결과 5순위를 담고있는 arraylist라고 가정
        //ArrayList<String> list = getRecommended().. 이런식으로..
        Calendar startTime;
        Calendar endTime;

        int i = 1;
        Iterator<String> iter = list.iterator();
        while (iter.hasNext()){
            String s = iter.next();
            //2019.11.11 12:00 ~ 2019.11.11 13:00
            int hour, minute, day, hour2, minute2;
            if (s.length()==35){
                hour = Integer.parseInt(s.substring(11,13));
                minute = Integer.parseInt(s.substring(14,16));
                day = Integer.parseInt(s.substring(9,11));
                hour2 = Integer.parseInt(s.substring(30,32));
                minute2 = Integer.parseInt(s.substring(33,35));
            }
            else {
            //2019.11.1 12:00 ~ 2019.11.1 13:00
                hour = Integer.parseInt(s.substring(10,12));
                minute = Integer.parseInt(s.substring(13,15));
                day = Integer.parseInt(s.substring(9,10));
                hour2 = Integer.parseInt(s.substring(28,30));
                minute2 = Integer.parseInt(s.substring(31,33));
            }
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
            WeekViewEvent event = new WeekViewEvent(i, i+"순위", startTime, endTime);
            event.setColor(Color.parseColor("#8022c6cf"));
            events.add(event);
        }*/

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 15);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.DAY_OF_MONTH, 9);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event = new WeekViewEvent(1, "1순위", startTime, endTime);
        event.setColor(Color.parseColor("#8022c6cf"));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 17);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.DAY_OF_MONTH, 12);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event1 = new WeekViewEvent(2, "2순위", startTime, endTime);
        event1.setColor(Color.parseColor("#8022c6cf"));
        events.add(event1);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 10);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.DAY_OF_MONTH, 10);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event2 = new WeekViewEvent(3, "3순위", startTime, endTime);
        event2.setColor(Color.parseColor("#8022c6cf"));
        events.add(event2);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 14);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.DAY_OF_MONTH, 11);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 15);
        endTime.set(Calendar.MINUTE, 10);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event3 = new WeekViewEvent(4, "4순위", startTime, endTime);
        event3.setColor(Color.parseColor("#8022c6cf"));
        events.add(event3);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 12);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.DAY_OF_MONTH, 13);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event4 = new WeekViewEvent(5, "5순위", startTime, endTime);
        event4.setColor(Color.parseColor("#8022c6cf"));
        events.add(event4);

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
        date = getIntent().getStringExtra("date");
        startTime = getIntent().getStringExtra("startTime");

        //시간 추천 리스트를 보여주는 fragment 호출
        showTimeRecommendation();
    }

    public void showTimeRecommendation(){
        Fragment fr = new TimeRecommendationList();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.add(R.id.recommendation_list, fr);

        fragmentTransaction.commit();
    }
}