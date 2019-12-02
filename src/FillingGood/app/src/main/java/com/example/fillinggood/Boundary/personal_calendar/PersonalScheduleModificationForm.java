package com.example.fillinggood.Boundary.personal_calendar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggood.Boundary.EventDecorator;
import com.example.fillinggood.Boundary.MarkingDots;
import com.example.fillinggood.Boundary.OneDayDecorator;
import com.example.fillinggood.Boundary.group_calendar.GroupScheduleAdditionForm;
import com.example.fillinggood.Control.PersonalScheduleController;
import com.example.fillinggood.Entity.PersonalSchedule;
import com.example.fillinggood.R;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fillinggood.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.ravikoradiya.library.CenterTitle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

//리스트업 된 일정을 눌렀을 때, 해당 일정 정보를 수정 및 삭제하는 class 입니다
public class PersonalScheduleModificationForm extends AppCompatActivity {
    private EditText mNameEditText, mLocationEditText, mDescriptionEditText;
    private TextView mDateTextView, mStartTimeTextView, mEndTimeTextView;
    private MaterialCalendarView mCalendarView;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button modifyButton, deleteButton;
    private RadioGroup radioGroup;
    private RadioButton fixed, hardly, easily;
    private long mScheduleID = -1;
    public String name, location, description, priority, date, startTime, endTime, amPm;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_personal_schedule);

        //GUI 구성을 보이기 위한 array-list로 db 구축 후 지워주세요
        ArrayList<PersonalSchedule> eventsList = new ArrayList<>();
        PersonalSchedule event1 = new PersonalSchedule("조별과제", "문휴", "융종설 조별모임", "불가", "2019.11.20", "12:00", "13:00");
        eventsList.add(event1);
        PersonalSchedule event2 = new PersonalSchedule("약속", "신촌역", "친구랑 영화", "가능", "2019.11.20", "14:00", "16:00");
        eventsList.add(event2);
        PersonalSchedule event3 = new PersonalSchedule("알바", "대흥역", "카페 알바", "약간 가능", "2019.11.22", "12:00", "17:00");
        eventsList.add(event3);
        PersonalSchedule event4 = new PersonalSchedule("수업", "J관", "전공 수업", "불가", "2019.11.20", "09:00", "10:15");
        eventsList.add(event4);
        PersonalSchedule event5 = new PersonalSchedule("수업", "K관", "교양 수업", "불가", "2019.11.20", "10:30", "12:00");
        eventsList.add(event5);
        PersonalSchedule event6 = new PersonalSchedule("스터디", "GN관", "알고리즘 스터디", "null", "2019.11.27", "15:00", "16:00");
        eventsList.add(event6);

        // element 할당하기
        mCalendarView = findViewById(R.id.calendarView);

        mNameEditText = findViewById(R.id.event_name);
        name = getIntent().getStringExtra("name");
        mNameEditText.setText(name);

        mLocationEditText = findViewById(R.id.event_location);
        mDescriptionEditText = findViewById(R.id.event_description);

        mDateTextView = findViewById(R.id.event_date);

        mStartTimeTextView = findViewById(R.id.event_start_time);
        startTime = getIntent().getStringExtra("startTime");
        mStartTimeTextView.setText(startTime);

        mEndTimeTextView = findViewById(R.id.event_end_time);
        endTime = getIntent().getStringExtra("endTime");
        mEndTimeTextView.setText(endTime);

        radioGroup = findViewById(R.id.radioGroup);
        fixed = findViewById(R.id.fixed);
        hardly = findViewById(R.id.hardly);
        easily = findViewById(R.id.easily);

        modifyButton = findViewById(R.id.modify_event);
        deleteButton = findViewById(R.id.delete_event);

        //일정이 등록된 날짜들에 점을 찍음
        mCalendarView.addDecorator(new EventDecorator(Color.parseColor("#22c6cf"), MarkingDots.markingDots()));
        //오늘 날짜의 색을 바꿈
        mCalendarView.addDecorators(new OneDayDecorator());

        //이전 정보를 불러 setting하는 코드로, db 구축 후 적절한 코드로 대체해주세요
        Iterator<PersonalSchedule> iter = eventsList.iterator();
        while (iter.hasNext()) {
            PersonalSchedule p = iter.next();
            if (p.getName().equals(name) && p.getStartTime().equals(startTime) && p.getEndTime().equals(endTime)){
                mLocationEditText.setText(p.getLocation());
                mDescriptionEditText.setText(p.getDescription());
                mDateTextView.setText(p.getDate());
                //조정 가능성 체크
                if (p.getPriority().equals("불가")) fixed.setChecked(true);
                else if (p.getPriority().equals("약간 가능")) hardly.setChecked(true);
                else if (p.getPriority().equals("가능")) easily.setChecked(true);
                int year = Integer.parseInt(p.getDate().substring(0,4));
                int month = Integer.parseInt(p.getDate().substring(5,7));
                int day = Integer.parseInt(p.getDate().substring(8,10));
                //선택한 날짜 체크 표시
                CalendarDay selectedDate = CalendarDay.from(year, month-1, day);
                mCalendarView.setDateSelected(selectedDate,true);
            }
        }

        // 화면에 표시된 캘린더뷰에서 선택한 날짜를 "이벤트날짜"로 지정해주는 코드
        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay Date, boolean selected) {
                //해당 라이브러리는 Date를 "CalendarDay{0000-00-00}" 형태로 내보내기 때문에,
                // 년,월,일 정보만 빼오려면 아래와 같은 split 과정이 필요
                CalendarDay selectedDay = Date;
                String tempDate = selectedDay.toString();
                String[] parsedDATA = tempDate.split("[{]");
                parsedDATA = parsedDATA[1].split("[}]");
                parsedDATA = parsedDATA[0].split("-");
                int year = Integer.parseInt(parsedDATA[0]);
                int month = Integer.parseInt(parsedDATA[1])+1;
                int day = Integer.parseInt(parsedDATA[2]);
                date =""+year+"."+month+"."+day; //db에 삽입할 값. 위에 public string으로 선언 돼 있음.
                mDateTextView.setText(date);
            }
        });

        // "이벤트시작시간"은 현재시간이 default로 설정되며, 이후에 자유롭게 설정 가능
        mStartTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                int currentMinute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        PersonalScheduleModificationForm.this, R.style.MyTimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startTime = String.format("%02d:%02d ", hourOfDay, minute);
                        mStartTimeTextView.setText(startTime);
                    }
                }, currentHour, currentMinute, true);
                dialog.show();
            }
        });

        //"이벤트시작시간"은 (현재시간+1시간)이 default로 설정되며, 이후에 자유롭게 설정 가능
        mEndTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                int currentMinute = cal.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(
                        PersonalScheduleModificationForm.this,R.style.MyTimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endTime = String.format("%02d:%02d ", hourOfDay, minute);
                        mEndTimeTextView.setText(endTime);
                    }
                }, currentHour+1, currentMinute, true);
                dialog.show();
            }
        });



        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyButtonClicked();

                onBackPressed();
                Toast.makeText(PersonalScheduleModificationForm.this, "일정이 수정되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalScheduleModificationForm.this);
                builder.setTitle("개인 일정 삭제")        // 제목 설정
                        .setMessage("해당 일정을 삭제하겠습니까?")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("삭제 확인", new DialogInterface.OnClickListener(){
                            // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                            public void onClick(DialogInterface dialog, int whichButton){
                                //원하는 클릭 이벤트를 넣으시면 됩니다.
                                deleteButtonClicked();
                                onBackPressed();
                                Toast.makeText(PersonalScheduleModificationForm.this, "일정이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                            // 취소 버튼 클릭시 설정, 왼쪽 버튼입니다.
                            public void onClick(DialogInterface dialog, int whichButton){
                                //원하는 클릭 이벤트를 넣으시면 됩니다.

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


    public void modifyButtonClicked() {
        //수정 내용을 db로 보내는 적절한 코드를 적어주세요
    }
    public void deleteButtonClicked() {
        //db에서 해당 일정정보를 삭제하는 코드를 적어주세요
    }
}