package com.example.fillinggood.Boundary.group_calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fillinggood.Boundary.EventDecorator;
import com.example.fillinggood.Boundary.MarkingDots;
import com.example.fillinggood.Boundary.OneDayDecorator;
import com.example.fillinggood.Boundary.personal_calendar.PersonalScheduleList;
import com.example.fillinggood.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;

//그룹 내에서 가졌던 일정 내역을 보여주는 class 입니다
public class GroupScheduleHistoryForm extends AppCompatActivity {
    private MaterialCalendarView materialCalendarView;
    private String date;
    private String tempDate2 = "";
    private boolean isEventsShown = true ;
    private CalendarDay todayDate;
    private String groupName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_schedule_history);

        groupName = getIntent().getStringExtra("groupName");
        GroupFeedbackList fragment = new GroupFeedbackList();
        Bundle bundle = new Bundle();
        bundle.putString("groupName", groupName);

        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);
        todayDate = CalendarDay.today();
        //오늘 날짜값 계산, 다가올 날짜인지 이미 지난 날짜인지 확인하는 데 필요
        long today = todayDate.getYear()*10000 + (todayDate.getMonth()+1)*100 + todayDate.getDay();

        //GUI 구성을 보이기 위한 코드로 db 구축 후 지워주세요
        //datesPassed는 이미 지난 날짜로 회색 점을,
        //datesUpcomming은 오늘+다가올 날짜로 하늘색 점을 찍습니다
        //CalendarDay는 0월-11월로 구성돼 month-1을 했습니다
        ArrayList<CalendarDay> datesPassed = new ArrayList<>();
        ArrayList<CalendarDay> datesUpcoming = new ArrayList<>();

        /*이미 지난 일정인지, 다가오는 일정인지 체크하는 파트*/
        /*
        ArrayList<GroupSchedule> gs = getGroupSchedule(groupName);
        Iterator<GroupSchedule> iter = gs.iterator();
        while (iter.hasNext()){
            String date = iter.next().getDate();
            int year = Integer.parseInt(date.substring(0,4)); int month = Integer.parseInt(date.substring(5,7));
            int day = Integer.parseInt(date.substring(8,10));
            long Date = year*10000 + month*100 + day;
            if (Date<today), datesPassed.add(CalendarDay.from(year, month-1, day));
            if (Date>=today), datesUpcoming.add(CalendarDay.from(year, month-1, day));
        }
        * */
        datesPassed.add(CalendarDay.from(2019, 11, 2));
        datesPassed.add(CalendarDay.from(2019, 11, 3));
        datesUpcoming.add(CalendarDay.from(2019, 11, 10));

        //일정을 가진 날짜에 점 찍는 함수
        materialCalendarView.addDecorator(new EventDecorator(Color.parseColor("#808080"), datesPassed));
        materialCalendarView.addDecorator(new EventDecorator(Color.parseColor("#22c6cf"), datesUpcoming));

        //오늘 날짜의 색을 바꿈
        materialCalendarView.addDecorators(new OneDayDecorator());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay Date, boolean selected) {
                //해당 라이브러리는 Date를 "CalendarDay{0000-00-00}" 형태로 내보내기 때문에,
                // 년,월,일 정보만 빼오려면 아래와 같은 split 과정이 필요
                CalendarDay selectedDay = Date;
                String tempDate = selectedDay.toString();
                String[] parsedDATA = tempDate.split("[{]");
                parsedDATA = parsedDATA[1].split("[}]");
                parsedDATA = parsedDATA[0].split("-");
                int year = Integer.parseInt(parsedDATA[0]);
                int month = Integer.parseInt(parsedDATA[1])+1;
                int day = Integer.parseInt(parsedDATA[2]);
                date =""+year+"."+month+"."+day;

                //날짜를 선택할시, 하단에 해당 날짜에 대한 일정을 리스트업하는 fragment를 호출하는 함수
                if (tempDate2.equals("")) tempDate2 = date;
                showEvents(date);
                tempDate2 = date;
            }
        });
    }

    public void showEvents(String date){
        Fragment fr = new GroupFeedbackList(date);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        if (tempDate2.equals(date) && isEventsShown){
            fragmentTransaction.add(R.id.feedback, fr);
            isEventsShown = false;
        }

        else if (tempDate2.equals(date) && !isEventsShown){
            fragmentTransaction.remove(fr);
            isEventsShown = true;
        }

        else if (!tempDate2.equals(date)){
            fragmentTransaction.replace(R.id.feedback, fr);
            isEventsShown = false;
        }

        else{
            fragmentTransaction.add(R.id.feedback, fr);
            isEventsShown = false;
        }
        fragmentTransaction.commit();
    }
}
