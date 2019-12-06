package com.example.fillinggood.Boundary.group_calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.fillinggood.R;

import java.util.ArrayList;

//장소 추천 리스트를 보여주는 class입니다(GroupRecommendationForm 하단에 뜨는 fragment)
public class LocationRecommendationList extends Fragment{
    private RadioButton rank1, rank2, rank3, rank4, rank5;
    private Button saveResult;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.location_recommendation_list, container, false);

        //GUI 구성을 보이기 위한 arryalist로 db 구축 후 지우고 사용해주세요
        ArrayList<String> rl = new ArrayList<>();
        //ArrayList<String> list = getRecommended().. 이런식으로 작성 필요
        rl.add("J관 4층 휴게실");
        rl.add("커피 브레이크");
        rl.add("파관 스터디룸");
        rl.add("J관 1층 스터디룸");
        rl.add("다산관 도서관 라운지");

        rank1 = (RadioButton)root.findViewById(R.id.rank1);
        rank2 = (RadioButton)root.findViewById(R.id.rank2);
        rank3 = (RadioButton)root.findViewById(R.id.rank3);
        rank4 = (RadioButton)root.findViewById(R.id.rank4);
        rank5 = (RadioButton)root.findViewById(R.id.rank5);

        rank1.setText("1순위 : "+rl.get(0));
        rank2.setText("2순위 : "+rl.get(1));
        rank3.setText("3순위 : "+rl.get(2));
        rank4.setText("4순위 : "+rl.get(3));
        rank5.setText("5순위 : "+rl.get(4));


        saveResult = (Button)root.findViewById(R.id.save_result);

        saveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //결과 저장하는 코드

                if (rank1.isChecked()==false && rank2.isChecked()==false && rank3.isChecked()==false && rank4.isChecked()==false &&rank5.isChecked()==false )
                {
                    Toast.makeText(getContext(), "선택된 장소가 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                //else, 아래 코드 실행
                getActivity().onBackPressed();
                Toast.makeText(getContext(), "추천 순위가 등록되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}
