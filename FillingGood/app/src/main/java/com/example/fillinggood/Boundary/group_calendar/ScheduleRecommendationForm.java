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
import com.example.fillinggood.Entity.RecommendedTimes;
import com.example.fillinggood.R;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//추천 결과 보여주는 class 입니다
public class ScheduleRecommendationForm extends AppCompatActivity implements MonthLoader.MonthChangeListener{
    private com.alamkanak.weekview.WeekView weekView;
    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 15);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.DAY_OF_MONTH, 26);
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
        startTime.set(Calendar.DAY_OF_MONTH, 27);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event1 = new WeekViewEvent(2, "2순위", startTime, endTime);
        event1.setColor(Color.parseColor("#8022c6cf"));
        events.add(event1);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 11);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.DAY_OF_MONTH, 29);
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
        startTime.set(Calendar.DAY_OF_MONTH, 28);
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
        startTime.set(Calendar.HOUR_OF_DAY, 21);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.DAY_OF_MONTH, 29);
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

        /*  ArrayList<WeekViewEvent> events = new ArrayList<>();
            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 3);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.DAY_OF_MONTH, 25);
            startTime.set(Calendar.MONTH, 10);
            startTime.set(Calendar.YEAR, 2019);
            Calendar endTime = (Calendar) startTime.clone();

            endTime.set(Calendar.DAY_OF_MONTH, 25);
            endTime.set(Calendar.MONTH, 10);
            endTime.set(Calendar.YEAR, 2019);
            endTime.set(Calendar.HOUR_OF_DAY, 4);
            endTime.set(Calendar.MINUTE, 0);
            WeekViewEvent event = new WeekViewEvent(1, "1순위", startTime, endTime);

            event.setColor(Color.parseColor("#22c6cf"));
            events.add(event);*/

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