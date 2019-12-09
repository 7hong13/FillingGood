package com.example.fillinggood.Boundary.group_calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fillinggood.Boundary.DBboundary.DBmanager;
import com.example.fillinggood.Control.RecommendationController;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.Entity.PersonalSchedule;
import com.example.fillinggood.Entity.TimeTable;
import com.example.fillinggood.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


//시간 추천 리스트를 보여주는 class입니다(GroupRecommendationForm 하단에 뜨는 fragment)
public class TimeRecommendationList extends Fragment {
    private RadioButton rank1, rank2, rank3, rank4, rank5;
    private Button saveResult;
    RecommendationController recommendationController;
    private String groupName;
    private String startdate, enddate;
    // 모임 일정 생성 시 입력받은 모임 일정 기간. '설정 안 함' 선택했으면 오늘 날짜로부터 일주일
    private int time;  // 모임 일정 생성 시 입력받은 예상 모임 시간 가져오기(아직 구현 못함)
    private Calendar cal = Calendar.getInstance();
    private SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.time_recommendation_list, container, false);
        Bundle extra = this.getArguments();
        if(extra != null) {
            extra = getArguments();
            groupName = extra.getString("groupName");
        }

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
                        DBmanager.getInstance().getGroupInfo(groupName), startdate), time, startdate);

        // 그 그룹의 시간 가중치 테이블 불러오기
        Group g1 = DBmanager.getInstance().getGroupInfo(groupName);
        TimeTable timetb = new TimeTable(groupName);
        double[][] tt = new double[7][96];

        // 만약 그룹 시간 가중치 테이블이 없었다면, 새로 생성
        if (g1.getGroupMembers() == null)
            tt = timetb.getTimeTable();
        else  // 그룹 시간 가장취 테이블이 있었다면, 불러오기
            tt = g1.getGroupTimeTable();

        // 피드백 과정 추가(모임장이 추천 시간 중 1번 추천시간 선택했다고 가정)
        // 최종적으로 선택한 시간대 가중치 -0.1
        g1.setGroupTimeTable(timetb.getFeedTimeTable(rt[0], tt));


        //ArrayList<String> list = getRecommended().. 이런식으로 작성 필요
        /*
        rt.add("2019.12.9 15:00 ~ 2019.12.9 16:00");
        rt.add("2019.12.12 17:00 ~ 2019.12.12 18:00");
        rt.add("2019.12.10 10:00 ~ 2019.12.10 11:00");
        rt.add("2019.12.11 14:30 ~ 2019.12.11 15:10");
        rt.add("2019.12.13 12:00 ~ 2019.12.13 13:00");
        */

        rank1 = (RadioButton)root.findViewById(R.id.rank1);
        rank2 = (RadioButton)root.findViewById(R.id.rank2);
        rank3 = (RadioButton)root.findViewById(R.id.rank3);
        rank4 = (RadioButton)root.findViewById(R.id.rank4);
        rank5 = (RadioButton)root.findViewById(R.id.rank5);

        rank1.setText("1순위 : "+rt[0]);
        rank2.setText("2순위 : "+rt[1]);
        rank3.setText("3순위 : "+rt[2]);
        rank4.setText("4순위 : "+rt[3]);
        rank5.setText("5순위 : "+rt[4]);

        saveResult = (Button)root.findViewById(R.id.save_result);

        saveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //결과 저장하는 코드를 작성해주세요

                //if 추천순위 미선택,  Toast.makeText(getContext(), “선택된 시간이 없습니다", Toast.LENGTH_SHORT).show();

                //else, 장소 추천 목록을 보여주는 fragment 호출
                showLocationRecommendation();
            }
        });
        return root;
    }
    public void showLocationRecommendation(){
        Fragment fr = new LocationRecommendationList();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.recommendation_list, fr);

        fragmentTransaction.commit();
    }

}
