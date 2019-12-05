package com.example.fillinggood.Boundary;

import android.app.Person;

import androidx.annotation.NonNull;

import com.example.fillinggood.Entity.PersonalSchedule;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class MarkingDots{

    public static List<CalendarDay> markingDots(@NonNull Void... voids){

        //GUI 구성을 보이기 위한 array로, DB 구축 후 적절한 코드로 대체해주세요
        //개인 회원이 가진 모든 일정 날짜 여기서 가져온 후, dates array-list에 추가

        ArrayList<String> datesHavingEvents = new ArrayList<>();
        /*
        ArrayList<PersonalSchedule> ps = getPersonalSchedule(userID);
        Iterator<PersonalSchedule> iter = ps.iterator();
        while (iter.hasNext()){
            PersonalSchedule p = iter.next();
            String date = p.getDate();
            datesHavingEvents.add(date);
        }
        (아래 임시 데이터 지우기)
        */

        datesHavingEvents.add("2019.12.05");
        datesHavingEvents.add("2019.12.08");
        datesHavingEvents.add("2019.12.10");

        ArrayList<CalendarDay> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        //일정을 가진 날짜에 점 표시해주는 파트
        for (int i=0; i<datesHavingEvents.size(); i++){
            String s = datesHavingEvents.get(i);
            int year = Integer.parseInt(s.substring(0,4));
            int month = Integer.parseInt(s.substring(5,7));
            int day = Integer.parseInt(s.substring(8,10));
            calendar.set(year,month-1,day);
            CalendarDay date = CalendarDay.from(calendar);
            dates.add(date);
        }

        return dates;
    }
}