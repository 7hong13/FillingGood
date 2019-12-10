package com.example.fillinggood.Boundary.group_calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fillinggood.Boundary.DBboundary.DBmanager;
import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.Control.RecommendationController;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.Entity.PersonalSchedule;
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
    RecommendationController recommendationController = new RecommendationController();
    private String groupName;
    private String startdate, enddate;
    // 모임 일정 생성 시 입력받은 모임 일정 기간. '설정 안 함' 선택했으면 오늘 날짜로부터 일주일
    private int time;  // 모임 일정 생성 시 입력받은 예상 모임 시간 가져오기(아직 구현 못함)
    private Calendar cal = Calendar.getInstance();
    private SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");
    private String[] rt;
    private double[][] tt;

    public TimeRecommendationList(String groupName){
        this.groupName = groupName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.time_recommendation_list, container, false);

        Group thisGroup = RecommendationController.getGroup(groupName);

        // startdate, endate, time 받아오기(수정 필요)

        if (true) {  // 모임 일정 생성 시 모임 일정 기간 '설정 안 함' 선택
            startdate = date.format(cal.getTime());  // 오늘 날짜

            cal.add(Calendar.DATE, 7);
            enddate = date.format(cal.getTime());  // 일주일 뒤 날짜
        }
        // 모임 일정 기간 선택 시에는 그 값 불러오기

        rt = new String[5];

        if(DBmanager.getInstance().FindRecommendingTime(groupName)){ // 추천 중인 일정이 있음
            // 불러오기
            time = DBmanager.getInstance().getExpectTime(groupName);
            rt = recommendationController.getRT(groupName);
        }
        else {
            time = 60;
            rt = recommendationController.RecommendSchedule(
                    recommendationController.CreateGroupSC(thisGroup, startdate),
                    time, startdate, groupName);
        }
        Log.d("check", "" + time);

        // 그 그룹의 시간 가중치 테이블 불러오기
        thisGroup.getTimeTable(thisGroup.getName());
        tt = thisGroup.getGroupTimeTable();


        //ArrayList<String> list = getRecommended().. 이런식으로 작성 필요

        rank1 = (RadioButton)root.findViewById(R.id.rank1);
        rank2 = (RadioButton)root.findViewById(R.id.rank2);
        rank3 = (RadioButton)root.findViewById(R.id.rank3);
        rank4 = (RadioButton)root.findViewById(R.id.rank4);
        rank5 = (RadioButton)root.findViewById(R.id.rank5);

        if (!thisGroup.getGroupLeader().getID().equals(MainActivity.User.getID())){
            rank1.setText("1순위 : "+rt[0]);
            rank2.setText("2순위 : "+rt[1]);
            rank3.setText("3순위 : "+rt[2]);
            rank4.setText("4순위 : "+rt[3]);
            rank5.setText("5순위 : "+rt[4]);
        }
        else {
            rank1.setText("1순위 : "+rt[0]);//+"\n(투표한 멤버: "+//해당 투표한 멤버 이름들+")");
            rank2.setText("2순위 : "+rt[1]);//+"\n(투표한 멤버: "+//해당 투표한 멤버 이름들+")");
            rank3.setText("3순위 : "+rt[2]);//+"\n(투표한 멤버: "+//해당 투표한 멤버 이름들+")");
            rank4.setText("4순위 : "+rt[3]);//+"\n(투표한 멤버: "+//해당 투표한 멤버 이름들+")");
            rank5.setText("5순위 : "+rt[4]);//+"\n(투표한 멤버: "+//해당 투표한 멤버 이름들+")");
        }

        saveResult = (Button)root.findViewById(R.id.save_result);

        saveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //결과 저장하는 코드를 작성해주세요
                if (!rank1.isChecked() && !rank2.isChecked() && !rank3.isChecked() &&!rank4.isChecked() && !rank5.isChecked()){
                    Toast.makeText(getContext(), "선택된 시간이 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                int rank;
                if(rank1.isChecked())
                    rank = 1;
                else if(rank2.isChecked())
                    rank = 2;
                else if(rank3.isChecked())
                    rank = 3;
                else if(rank4.isChecked())
                    rank = 4;
                else if(rank5.isChecked())
                    rank = 5;
                else
                    rank = 1;
                RecommendationController.saveTimeChoiceRecommending(groupName, MainActivity.User.getID(), rank);

                //else, 장소 추천 목록을 보여주는 fragment 호출
                showLocationRecommendation();
            }
        });
        return root;
    }
    public void showLocationRecommendation(){
        int rtidx = 1;
        if(rank1.isChecked())
            rtidx = 1;
        else if(rank2.isChecked())
            rtidx = 2;
        else if(rank3.isChecked())
            rtidx = 3;
        else if(rank4.isChecked())
            rtidx = 4;
        else if(rank5.isChecked())
            rtidx = 5;
        Fragment fr = new LocationRecommendationList(groupName, rt[rtidx-1], tt, rtidx);
        Log.d("check", ""+ rt[rtidx-1]);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.recommendation_list, fr);

        fragmentTransaction.commit();
    }

}
