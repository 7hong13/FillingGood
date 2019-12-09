package com.example.fillinggood.Boundary.personal_calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.Control.PersonalScheduleController;
import com.example.fillinggood.Entity.PersonalSchedule;
import com.example.fillinggood.R;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

//상단 우측 메뉴탭에서 "외부 일정 연동하기" 버튼을 눌렀을 때 실행되는 class 입니다
public class ExternalScheduleAdditionForm extends AppCompatActivity implements View.OnClickListener{
    static public int Size;

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

        Thread t1 = new Thread(new EveryTimePatch(t.getText().toString(),
                t2.getText().toString(),
                t3.getText().toString(),
                t4.getText().toString()));
        t1.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onBackPressed();
        if (Size == 0) Toast.makeText(this, "외부 어플리케이션에 등록된 일정이 없습니다", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "외부 일정이 연동되었습니다", Toast.LENGTH_SHORT).show();
    }
}

class EveryTimePatch implements Runnable {
    private String id;
    private String pwd;
    private String year;
    private String semester;

    private PersonalScheduleController personalScheduleController = new PersonalScheduleController();

    public EveryTimePatch(String id, String pwd, String year, String semester){
        this.id= id;
        this.pwd = pwd;
        this.year = year;
        this.semester = semester;
    }

    public void run(){
        EveryTimeXMLParser sss = new EveryTimeXMLParser();
        try {
            ArrayList<PersonalSchedule> d = sss.everytime(this.id, this.pwd, this.year, this.semester);
            //real_result를 디비로 넘겨야 됩니다
            ArrayList<PersonalSchedule> real_result= new ArrayList<>();
            Log.d("html", "data : " + d);

            for (int i=0; i<d.size(); i++){
                for (int j=1; j<=4; j++){
                    PersonalSchedule temp = d.get(i);
                    System.out.println(temp.getDate()+ " "+temp.getStartTime()+" "+ temp.getEndTime());
                    int day = (Integer.parseInt(temp.getDate())+2 + (j-1)*7);
                    String date;
                    if(day/10 < 1)
                        date = "2019.12.0"+ day;
                    else
                        date = "2019.12." + day;

                    int start_time = Integer.parseInt(temp.getStartTime())/12;
                    int hour = start_time;
                    start_time = Integer.parseInt(temp.getStartTime())%12;
                    int minute = (start_time/3)*15;

                    int end_time = Integer.parseInt(temp.getEndTime())/12;
                    int hour2 = end_time;
                    end_time = Integer.parseInt(temp.getEndTime())%12;
                    int minute2 = (end_time/3)*15;

                    String startTime, endTime;
                    if (hour<=9) startTime  = " 0"+hour+":";
                    else startTime = ""+hour+":";

                    if (minute<=9) startTime += "0"+minute;
                    else startTime += minute;

                    if (hour2<=9) endTime  = " 0"+hour2+":";
                    else endTime = ""+hour2+":";

                    if (minute2<=9) endTime += "0"+minute2;
                    else endTime += minute2;

                    real_result.add(new PersonalSchedule(temp.getName(), temp.getLocation(), temp.getDescription(), temp.getPriority(), date, startTime, endTime));
                    // 하나씩 넣기 > controller 통해서 > db로

                    personalScheduleController.AddSchedule(MainActivity.User, temp.getName(), temp.getLocation(), temp.getDescription(), temp.getPriority(), date, startTime, endTime);
                }
            }
            //여기에서 데이터 베이스로 저장하는 코드를 적어주시면 됩니다!!
            //PersonalScheduleController.EveryTimeSave(real_result);

            //결과 확인용 코드
            for (int m=0; m<real_result.size(); m++)
                System.out.println("real result : "+real_result.get(m).getName()+" "+real_result.get(m).getDate()+" "
                        +real_result.get(m).getStartTime()+" "+real_result.get(m).getEndTime());

            ExternalScheduleAdditionForm.Size = real_result.size();
            //real_result를 DB로 보내는 코드 적어주세요
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}