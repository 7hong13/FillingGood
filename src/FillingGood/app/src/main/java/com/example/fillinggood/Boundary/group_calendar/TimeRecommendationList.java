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

import com.example.fillinggood.R;

import java.util.ArrayList;
import java.util.Iterator;


//시간 추천 리스트를 보여주는 class입니다(GroupRecommendationForm 하단에 뜨는 fragment)
public class TimeRecommendationList extends Fragment {
    private RadioButton rank1, rank2, rank3, rank4, rank5;
    private Button saveResult;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.time_recommendation_list, container, false);

        //GUI 구성을 보이기 위한 arryalist로 db 구축 후 지우고 사용해주세요
        ArrayList<String> rt = new ArrayList<>();
        rt.add("11월 26일 화요일 15:00-16:00");
        rt.add("11월 27일 수요일 17:00-18:00");
        rt.add("11월 29일 금요일 11:00-12:00");
        rt.add("11월 28일 목요일 14:30-15:10");
        rt.add("11월 28일 목요일 21:00-22:00");

        rank1 = (RadioButton)root.findViewById(R.id.rank1);
        rank2 = (RadioButton)root.findViewById(R.id.rank2);
        rank3 = (RadioButton)root.findViewById(R.id.rank3);
        rank4 = (RadioButton)root.findViewById(R.id.rank4);
        rank5 = (RadioButton)root.findViewById(R.id.rank5);

        rank1.setText("1순위 : "+rt.get(0));
        rank2.setText("2순위 : "+rt.get(1));
        rank3.setText("3순위 : "+rt.get(2));
        rank4.setText("4순위 : "+rt.get(3));
        rank5.setText("5순위 : "+rt.get(4));

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
