package com.example.fillinggood.Boundary.personal_calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.fillinggood.Boundary.EventDecorator;
import com.example.fillinggood.Boundary.MarkingDots;
import com.example.fillinggood.Boundary.OneDayDecorator;
import com.example.fillinggood.Boundary.group_calendar.GroupAdditionForm;
import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.Control.PersonalScheduleController;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.Entity.GroupSchedule;
import com.example.fillinggood.Entity.PersonalSchedule;
import com.example.fillinggood.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Iterator;

//내 일정 관리 페이지로 들어왔을 때, 처음 맞이하게 되는 화면입니다
public class PersonalCalendarFragment extends Fragment {

    private PersonalCalendarViewModel privateCalendarViewModel;
    private MaterialCalendarView materialCalendarView;
    private String date;
    private String tempDate = "";
    private boolean isEventsShown = true ;
    private String tempDate2 = "";

    private PersonalScheduleController personalScheduleController = new PersonalScheduleController();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        privateCalendarViewModel =
                ViewModelProviders.of(this).get(PersonalCalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_personal_calendar, container, false);
        /*final TextView textView = root.findViewById(R.id.text_gallery);
        privateCalendarViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        //+ floating button을 눌러 일정 추가 페이지로 전환
        FloatingActionButton fab = (FloatingActionButton)root.findViewById(R.id.add_schedule_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                // 다음 넘어갈 클래스 지정
                intent = new Intent(getActivity(), PersonalScheduleAdditionForm.class);
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        //날짜를 선택하면, 해당 날짜에 등록된 일정을 list-up
        materialCalendarView = (MaterialCalendarView)root.findViewById(R.id.calendarView);
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
                String Month;
                if(month/10 < 1)
                    Month = "0"+month;
                else
                    Month = ""+month;
                int day = Integer.parseInt(parsedDATA[2]);
                String Day;
                if(day/10 < 1)
                    Day = "0"+day;
                else
                    Day = ""+day;
                date =""+year+"."+Month+"."+Day; //db에 삽입할 값. 위에 public string으로 선언 돼 있음.

                //날짜를 선택할시, 하단에 해당 날짜에 대한 일정을 리스트업하는 fragment를 호출하는 함수
                if (tempDate2.equals("")) tempDate2 = date;
                showEvents(date);
                tempDate2 = date;
            }
        });


        //일정을 가진 날짜에 점 찍는 함수
        materialCalendarView.addDecorator(new EventDecorator(Color.parseColor("#22c6cf"), MarkingDots.markingDots()));
        //오늘 날짜의 색을 바꿈
        materialCalendarView.addDecorators(new OneDayDecorator());

        // 그룹 일정 점찍기
        ArrayList<CalendarDay> datesHavingMeetings = new ArrayList<>();
        ArrayList<Group> groups = Group.getAllUsersGroup(MainActivity.User.getID());
        int year, month, day;
        for (int i=0; i<groups.size(); i++){
            ArrayList<GroupSchedule> gs = GroupSchedule.getGroupSchedule(groups.get(i).getName());
            Iterator<GroupSchedule> iter = gs.iterator();
            while (iter.hasNext()){
                GroupSchedule g = iter.next();
                String date = g.getDate();
                year = Integer.parseInt(date.substring(0,4));
                month = Integer.parseInt(date.substring(5,7));
                day = Integer.parseInt(date.substring(8));
                datesHavingMeetings.add(CalendarDay.from(year, month-1, day));
            }
        }

        materialCalendarView.addDecorator(new EventDecorator(Color.parseColor("#cf3922"), datesHavingMeetings));

        setHasOptionsMenu(true);
        return root;
    }


    public void showEvents(String date){
        Fragment fr = new PersonalScheduleList(date);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        if (tempDate2.equals(date) && isEventsShown){
            fragmentTransaction.add(R.id.button_or_list, fr);
            isEventsShown = false;
        }

        else if (tempDate2.equals(date) && !isEventsShown){
            fragmentTransaction.remove(fr);
            isEventsShown = true;
        }

        else if (!tempDate2.equals(date)){
            fragmentTransaction.replace(R.id.button_or_list, fr);
            isEventsShown = false;
        }

        else{
            fragmentTransaction.add(R.id.button_or_list, fr);
            isEventsShown = false;
        }
        fragmentTransaction.commit();
    }

    //상단 우측 탭 메뉴 설정
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sub1, menu) ;
        super.onCreateOptionsMenu(menu,inflater);

    }
    //상단 우측 탭 메뉴 클릭시, 해당 액티비티(외부 일정 연동)로 이동
    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        if (item.getItemId()==R.id.add_external_calendar){
            Intent intent = new Intent(getActivity(), ExternalScheduleAdditionForm.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}