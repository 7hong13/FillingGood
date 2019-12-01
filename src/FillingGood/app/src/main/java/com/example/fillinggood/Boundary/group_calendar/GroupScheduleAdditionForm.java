package com.example.fillinggood.Boundary.group_calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggood.Boundary.personal_calendar.PersonalScheduleModificationForm;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupScheduleAdditionForm.this);
                builder.setTitle("모임 일정")        // 제목 설정
                        .setMessage("일정을 등록하겠습니까?")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                            public void onClick(DialogInterface dialog, int whichButton){
                                //원하는 클릭 이벤트를 넣으시면 됩니다.
                                onBackPressed();
                                Toast.makeText(GroupScheduleAdditionForm.this, "일정이 등록되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                            // 취소 버튼 클릭시 설정, 왼쪽 버튼입니다.
                            public void onClick(DialogInterface dialog, int whichButton){
                                //원하는 클릭 이벤트를 넣으시면 됩니다.
                                onBackPressed();
                            }
                        });
                final AlertDialog dialog = builder.create();    // 알림창 객체 생성

                dialog.setOnShowListener( new DialogInterface.OnShowListener()
                { @Override public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK); } });

                dialog.show();    // 알림창 띄우기
        }
        });
    }

}
