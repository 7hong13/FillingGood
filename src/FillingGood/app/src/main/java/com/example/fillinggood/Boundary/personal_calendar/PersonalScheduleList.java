package com.example.fillinggood.Boundary.personal_calendar;

import android.app.Activity;
import android.app.Person;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.Control.PersonalScheduleController;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.Entity.GroupSchedule;
import com.example.fillinggood.Entity.PersonalSchedule;
import com.example.fillinggood.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//날짜를 선택할시, 해당 날짜에 등록된 일정들을 list-up하는 class 입니다
public class PersonalScheduleList extends Fragment {
    PersonalScheduleController personalScheduleController = new PersonalScheduleController();
    ArrayList<PersonalSchedule> eventsList;
    ArrayList<String> eventsForSelectedDay;

    private String date;

    public PersonalScheduleList() {
        // Required empty public constructor
    }

    public PersonalScheduleList(String date) {
        // Required empty public constructor
        this.date = date;
    }

    //날짜별 일정들 시간순대로 정렬하는 함수
    private final static Comparator<PersonalSchedule> myComparator = new Comparator<PersonalSchedule>() {
        private final Collator collator = Collator.getInstance();

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public int compare(PersonalSchedule object1, PersonalSchedule object2) {
            return collator.compare(object1.getStartTime(), object2.getStartTime());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.personal_schedule_list, container, false);

        eventsList = personalScheduleController.getAllPS(MainActivity.User.getID());
        if(eventsList == null)
            eventsList = new ArrayList<>();

        //날짜별 일정을 시간 오름차순으로 정렬
        Collections.sort(eventsList, myComparator);

        eventsForSelectedDay = new ArrayList<>();

        Iterator<PersonalSchedule> iter1 = eventsList.iterator(); // iterator(반복자)를 얻는다.
        while (iter1.hasNext()) {
            PersonalSchedule p = iter1.next();
            if (date.equals(p.getDate())){
                //"일정명 (00:00~00:00)" 형태 string으로 변환
                String s = p.getName()+" ("+p.getStartTime()+"~"+p.getEndTime()+")";
                eventsForSelectedDay.add(s);
            }
        }

        ArrayList<Group> groups = Group.getAllUsersGroup(MainActivity.User.getID());
        for (int i=0; i<groups.size(); i++){
            ArrayList<GroupSchedule> gs = GroupSchedule.getGroupSchedule(groups.get(i).getName());
            if(gs == null) {
                gs = new ArrayList<>();
                continue;
            }
            Iterator<GroupSchedule> iter = gs.iterator();
            while (iter.hasNext()){
                GroupSchedule g = iter.next();
                if (date.equals(g.getDate())){
                    //"일정명 (00:00~00:00)" 형태 string으로 변환
                    String s = "["+groups.get(i).getName()+"]"+g.getName()+" ("+g.getStartTime()+"~"+g.getEndTime()+")";
                    eventsForSelectedDay.add(s);
                }
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, eventsForSelectedDay);

        final ListView listview = (ListView) root.findViewById(R.id.personal_schedule_list);
        listview.setAdapter(adapter);


        // 리스트에서 아이템을 짧게 누르면 그 아이템(일정)을 불러오도록 하는 코드
        // PersonalScheduleModification(일정 수정/삭제) class로 이동합니다
        //DB 구축 후 적절한 코드로 대체해주세요
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                //일정명 (00:00PM~00:00PM)
                intent = new Intent(getActivity(), PersonalScheduleModificationForm.class);
                String s = (String) parent.getItemAtPosition(position);
                if (s.charAt(0) == '[') return;
                //if(s.contains("[")) return;
                int n = s.length();

                intent.putExtra("name", s.substring(0, n - 14));
                intent.putExtra("startTime", s.substring(n - 12, n - 7));
                intent.putExtra("endTime", s.substring(n - 6, n - 1));
                intent.putExtra("date", date);

                startActivity(intent);
            }
        });

        return root;
    }

}