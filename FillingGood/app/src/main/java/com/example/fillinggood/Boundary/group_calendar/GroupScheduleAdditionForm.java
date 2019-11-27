package com.example.fillinggood.Boundary.group_calendar;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggood.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

//그룹 내 일정 생성을 하는 class 입니다
public class GroupScheduleAdditionForm extends AppCompatActivity {
    private EditText name, description, expectedTime;
    private TextView startDate, endDate;
    private RadioButton setNull, setDuration;
    private DatePickerDialog.OnDateSetListener startDateSetListener, endDateSetListener;
    private Button saveSchedule;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group_schedule);

        name = (EditText)findViewById(R.id.name);
        description = (EditText)findViewById(R.id.description);
        expectedTime = (EditText)findViewById(R.id.expectedTime);

        startDate = (TextView)findViewById(R.id.startDate);
        endDate = (TextView)findViewById(R.id.endDate);

        setNull = (RadioButton)findViewById(R.id.setNull);
        setDuration = (RadioButton)findViewById(R.id.setDuration);

        saveSchedule = (Button)findViewById(R.id.save_schedule);

        //"설정안함"을 체크하지 않았다면, 날짜를 지정
        if (!setNull.isChecked()){
            //시작날짜 지정
            startDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(
                            GroupScheduleAdditionForm.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            startDateSetListener,
                            year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });
            startDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date = (year) + "." + (month+1) + "." + dayOfMonth;
                    startDate.setText(date);
                }
            };

            //종료날짜 지정
            endDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(
                            GroupScheduleAdditionForm.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            endDateSetListener,
                            year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

            endDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date = (year) + "." + (month+1) + "." + dayOfMonth;
                    endDate.setText(date);
                }
            };


        }

        saveSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //일정 등록하는 코드
            }
        });

    }

}
