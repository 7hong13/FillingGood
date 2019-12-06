package com.example.fillinggood.Boundary.personal_calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggood.R;

import java.io.IOException;

//상단 우측 메뉴탭에서 "외부 일정 연동하기" 버튼을 눌렀을 때 실행되는 class 입니다
public class ExternalScheduleAdditionForm extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.external_schedule);
    }
    @Override
    public void onClick(View v) {
        Log.d("network", "call ON CLICKED");
        EditText t = (EditText) findViewById(R.id.ET_id);
        EditText t2 = (EditText) findViewById(R.id.ET_pw);
        EditText t3 = (EditText) findViewById(R.id.ET_year);
        EditText t4 = (EditText) findViewById(R.id.ET_semester);

        new Thread(new EveryTimePatch(t.getText().toString(),
                t2.getText().toString(),
                t3.getText().toString(),
                t4.getText().toString())).start();
    }

}

class EveryTimePatch implements Runnable {
    private String id;
    private String pwd;
    private String year;
    private String semester;

    public EveryTimePatch(String id, String pwd, String year, String semester){
        this.id= id;
        this.pwd = pwd;
        this.year = year;
        this.semester = semester;
    }

    public void run(){
        EveryTimeXMLParser sss = new EveryTimeXMLParser();
        try {
            String d = sss.everytime(this.id, this.pwd, this.year, this.semester);
            Log.d("html", "data : " + d);



            //여기에서 데이터 베이스로 저장하는 코드를 적어주시면 됩니다!!



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}