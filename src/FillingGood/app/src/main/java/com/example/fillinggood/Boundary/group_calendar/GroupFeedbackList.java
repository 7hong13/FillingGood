package com.example.fillinggood.Boundary.group_calendar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.fillinggood.Boundary.personal_calendar.PersonalScheduleModificationForm;
import com.example.fillinggood.Entity.GroupSchedule;
import com.example.fillinggood.Entity.PersonalSchedule;
import com.example.fillinggood.R;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

//"모임 내역"에서 날짜 선택시, 해당 날짜에 가졌던(혹은 가질) 모임 내역을 리스트업 하는 fragment입니다
public class GroupFeedbackList extends Fragment {
    private String date;
    public GroupFeedbackList() {
        // Required empty public constructor
    }
    public GroupFeedbackList(String date) {
        // Required empty public constructor
        this.date = date;
    }

    //날짜별 일정들 시간순대로 정렬하는 함수
    private final static Comparator<GroupSchedule> myComparator= new Comparator<GroupSchedule>() {
        private final Collator collator = Collator.getInstance();

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public int compare(GroupSchedule object1,GroupSchedule object2) {
            return  collator.compare(object1.getStartTime(), object2.getStartTime());

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.feedback_listview, container, false);

        //GUI 구성을 보이기 위한 설정으로 choicedTimeRank와 choicedLocationRank 값이 -1이면 아직 피드백 등록을 안 한 걸로,
        //0이면 아직 모임 일정 날짜가 다가오지 않을 걸로, 그 외는 피드백 등록을 한 걸로 가정
        //(ScheduleFeeds entity attribute에 맞춰 코드 수정 예정)
        ArrayList<GroupSchedule> meetingsList = new ArrayList<>();
        meetingsList.add(new GroupSchedule("4조","융종설 모임", "스타벅스", "추천 알고리즘 토의 모임", "2019.11.20", "15:00", "16:00", 1, 1));
        meetingsList.add(new GroupSchedule("4조","융종설 모임", "K관", "발표 준비", "2019.11.20", "20:00", "21:00", -1, -1));
        meetingsList.add(new GroupSchedule("4조","융종설 모임", "J관", "유스케이스 스펙 작성 토의", "2019.11.21", "15:00", "16:00", -1, -1));
        meetingsList.add(new GroupSchedule("4조","융종설 모임", "신촌", "아이디어 상의", "2019.11.27", "12:00", "13:30", 0, 0));

        //날짜별 일정을 시간 오름차순으로 정렬
        Collections.sort(meetingsList, myComparator);

        Iterator<GroupSchedule> iter1 = meetingsList.iterator(); // iterator(반복자)를 얻는다.
        ArrayList<GroupSchedule> meetingsForSelectedDay = new ArrayList<>();
        while (iter1.hasNext()) {
            GroupSchedule gs = iter1.next();
            if (date.equals(gs.getDate())){
                meetingsForSelectedDay.add(gs);
            }
        }

        //ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, meetingsForSelectedDay);
        GroupFeedbackListAdapter adapter = new GroupFeedbackListAdapter(meetingsForSelectedDay, getActivity());

        ListView listview = (ListView) root.findViewById(R.id.feedback_listview) ;
        listview.setAdapter(adapter);


        return root;
    }

}
