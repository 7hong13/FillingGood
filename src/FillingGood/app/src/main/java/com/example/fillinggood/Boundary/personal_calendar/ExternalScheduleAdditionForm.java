package com.example.fillinggood.Boundary.personal_calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggood.R;

//상단 우측 메뉴탭에서 "외부 일정 연동하기" 버튼을 눌렀을 때 실행되는 class 입니다
public class ExternalScheduleAdditionForm extends AppCompatActivity {
    private EditText ET_id, ET_pw, ET_year, ET_semester;
    private Button save_result;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.external_schedule);
        //에타 정보를 연동해오는 class를 작성해주세요

        ET_id = (EditText)findViewById(R.id.ET_id);
        ET_pw = (EditText)findViewById(R.id.ET_pw);
        ET_year = (EditText)findViewById(R.id.ET_year);
        ET_semester = (EditText)findViewById(R.id.ET_semester);

        save_result = (Button)findViewById(R.id.saveResult);

        save_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //정보 보내는 코드
            }
        });
    }

}
