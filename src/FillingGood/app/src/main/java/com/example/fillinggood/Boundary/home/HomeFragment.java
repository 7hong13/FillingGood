package com.example.fillinggood.Boundary.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.fillinggood.Boundary.EventDecorator;
import com.example.fillinggood.Boundary.MarkingDots;
import com.example.fillinggood.Boundary.OneDayDecorator;
import com.example.fillinggood.Boundary.personal_calendar.PersonalCalendarFragment;
import com.example.fillinggood.Boundary.personal_calendar.PersonalScheduleAdditionForm;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.Entity.GroupSchedule;
import com.example.fillinggood.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.fillinggood.R.layout.fragment_home;

//어플리케이션 들어왔을 때, 처음 맞이하게 되는 화면입니다
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        ;
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        }
        );*/
        View root = inflater.inflate(fragment_home, container, false);
        MaterialCalendarView materialCalendarView = (MaterialCalendarView)root.findViewById(R.id.calendarView);

        //일정이 등록된 날짜들에 점을 찍음
        materialCalendarView.addDecorator(new EventDecorator(Color.parseColor("#22c6cf"), MarkingDots.markingDots()));

        //오늘 날짜의 색을 바꿈
        materialCalendarView.addDecorators(new OneDayDecorator());

        ArrayList<CalendarDay> datesHavingMeetings = new ArrayList<>();
        ArrayList<Group> groups = Group.getAllUsersGroup(MainActivity.User.getID());
        for (int i=0; i<groups.size(); i++){
            ArrayList<GroupSchedule> gs = GroupSchedule.getGroupSchedule(groups.get(i).getName());
            Iterator<GroupSchedule> iter = gs.iterator();
            while (iter.hasNext()){
                GroupSchedule g = iter.next();
                String date = g.getDate();
                int year = Integer.parseInt(date.split(".")[0]);
                int month = Integer.parseInt(date.split(".")[1]);
                int day = Integer.parseInt(date.split(".")[2]);
                datesHavingMeetings.add(CalendarDay.from(year, month-1, day));
            }
        }

        materialCalendarView.addDecorator(new EventDecorator(Color.parseColor("#cf3922"), datesHavingMeetings));

        return root;
    }
}
