package com.example.fillinggood.Boundary.group_calendar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggood.Boundary.personal_calendar.PersonalScheduleAdditionForm;
import com.example.fillinggood.Entity.GroupSchedule;
import com.example.fillinggood.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

//그룹의 일정 목록을 보여주는 class입니다
public class GroupScheduleList extends AppCompatActivity {

    /*
    //날짜순대로 일정 정렬하는 함수
    private final static Comparator<GroupSchedule> myComparator= new Comparator<GroupSchedule>() {
        private final Collator collator = Collator.getInstance();

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public int compare(GroupSchedule object1,GroupSchedule object2) {
            return  collator.compare(object1.getStartTime(), object2.getStartTime());

        }
    };*/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_schedule_list);
        /*
        ArrayList<GroupSchedule> meetingsList = getGroupSchedule(groupName);
        아래 코드 나중에 지워주세요
        * */
        ArrayList<GroupSchedule> meetingsList = new ArrayList<>();
        //choicedTimeRank와 choicedLocationRank가 -1일 때, 아직 추천을 안 받았다고 가정
        meetingsList.add(new GroupSchedule("4조","융종설 모임", "스타벅스", "추천 알고리즘 토의 모임", "2019.11.20", "15:00", "16:00", 1, 1));
        meetingsList.add(new GroupSchedule("4조","융종설 모임", "미정", "유스케이스 스펙 작성 토의", "미정", "미정", "미정", -1, -1));

        /*
        //일정들을 날짜 오름차순으로 정렬
        Collections.sort(meetingsList, myComparator);*/

        //meetingsList에 들어있는 일정들을 GroupSheduleListviewAdapter의 논리에 따라 화면상에 출력한다는 코드
        GroupScheduleListviewAdapter adapter = new GroupScheduleListviewAdapter(meetingsList, this);
        ListView listview = (ListView) findViewById(R.id.group_schedule_listview) ;
        listview.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.add_group_schedule_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                // 다음 넘어갈 클래스 지정
                intent = new Intent(GroupScheduleList.this, GroupScheduleAdditionForm.class);
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

    }

}