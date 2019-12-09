package com.example.fillinggood.Boundary.personal_calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import com.example.fillinggood.Boundary.EventDecorator;
import com.example.fillinggood.Boundary.MarkingDots;
import com.example.fillinggood.Boundary.OneDayDecorator;
import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.Control.PersonalScheduleController;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.Entity.GroupSchedule;
import com.example.fillinggood.Entity.PersonalSchedule;
import com.example.fillinggood.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.ravikoradiya.library.CenterTitle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

//일정 추가 floating button을 눌렀을 때 실행되는 class 입니다
public class PersonalScheduleAdditionForm extends AppCompatActivity {
    private EditText mNameEditText, mLocationEditText, mDescriptionEditText;
    private TextView mDateTextView, mStartTimeTextView, mEndTimeTextView;
    private MaterialCalendarView mCalendarView;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button saveButton;
    private RadioGroup radioGroup;
    private RadioButton fixed, hardly, easily;
    public String name, location, description, priority, date, startTime, endTime, amPm;

    private PersonalScheduleController personalScheduleController = new PersonalScheduleController();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_personal_schedule);

        // element 할당하기
        mCalendarView = findViewById(R.id.calendarView);

        mNameEditText = findViewById(R.id.event_name);
        mLocationEditText = findViewById(R.id.event_location);
        mDescriptionEditText = findViewById(R.id.event_description);

        mDateTextView = findViewById(R.id.event_date);
        mStartTimeTextView = findViewById(R.id.event_start_time);
        mEndTimeTextView = findViewById(R.id.event_end_time);

        radioGroup = findViewById(R.id.radioGroup);
        fixed = findViewById(R.id.fixed);
        hardly = findViewById(R.id.hardly);
        easily = findViewById(R.id.easily);

        saveButton = findViewById(R.id.save_event);

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
                String Month;
                if(month/10 < 1)
                    Month = "0"+month;
                else
                    Month = ""+month;
                int day = Integer.parseInt(parsedDATA[2]);
                String Day;
                if(day/10 < 1)
                    Day = "0"+day;
                else
                    Day = ""+day;
                date =""+year+"."+Month+"."+Day; //db에 삽입할 값. 위에 public string으로 선언 돼 있음.
                mDateTextView.setText(date);
            }
        });

        //일정 등록된 날짜에 점찍기
        mCalendarView.addDecorator(new EventDecorator(Color.parseColor("#22c6cf"), MarkingDots.markingDots()));
        //오늘 날짜의 색을 바꿈
        mCalendarView.addDecorators(new OneDayDecorator());

        // 그룹 일정 점찍기
        ArrayList<CalendarDay> datesHavingMeetings = new ArrayList<>();
        ArrayList<Group> groups = Group.getAllUsersGroup(MainActivity.User.getID());
        int year, month, day;
        for (int i=0; i<groups.size(); i++){
            ArrayList<GroupSchedule> gs = GroupSchedule.getGroupSchedule(groups.get(i).getName());
            Iterator<GroupSchedule> iter = gs.iterator();
            while (iter.hasNext()){
                GroupSchedule g = iter.next();
                String date = g.getDate();
                year = Integer.parseInt(date.substring(0,4));
                month = Integer.parseInt(date.substring(5,7));
                day = Integer.parseInt(date.substring(8));
                datesHavingMeetings.add(CalendarDay.from(year, month-1, day));
            }
        }

        mCalendarView.addDecorator(new EventDecorator(Color.parseColor("#cf3922"), datesHavingMeetings));

        // "이벤트시작시간"은 현재시간이 default로 설정되며, 이후에 자유롭게 설정 가능
        mStartTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                int currentMinute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        PersonalScheduleAdditionForm.this, R.style.MyTimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String HourOfDay, Minute;
                        if(hourOfDay/10 < 1)
                            HourOfDay = "0" + hourOfDay;
                        else
                            HourOfDay = ""+hourOfDay;
                        if(minute/10 < 1)
                            Minute = "0" + minute;
                        else
                            Minute = ""+minute;
                        startTime = String.format("%2s:%2s ", HourOfDay, Minute);
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
                        PersonalScheduleAdditionForm.this,R.style.MyTimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String HourOfDay, Minute;
                        if(hourOfDay/10 < 1)
                            HourOfDay = "0" + hourOfDay;
                        else
                            HourOfDay = ""+hourOfDay;
                        if(minute/10 < 1)
                            Minute = "0" + minute;
                        else
                            Minute = ""+minute;
                        endTime = String.format("%2s:%2s ", HourOfDay, Minute);
                        mEndTimeTextView.setText(endTime);
                    }
                }, currentHour+1, currentMinute, true);
                dialog.show();
            }
        });

        //여기서부터는 mysql 문법에 맞춰 수정해주세요

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //필수 기입 정보인 일정명, 시간 기입 안 한 경우
                //date를 startDate랑 endDate로 나누시면 거기에 맞춰서 조건문 수정해주세요
                if (mNameEditText.getText().toString().length() == 0 || mDateTextView.getText().toString().length()==0
                        || mStartTimeTextView.getText().toString().length()==0 || mEndTimeTextView.getText().toString().length()==0){
                    Toast.makeText(PersonalScheduleAdditionForm.this, "기입하지 않은 정보가 존재합니다\n(이름,날짜,시간)", Toast.LENGTH_SHORT).show();
                    return;
                }
                //시간대가 타 일정과 중복될 경우(시작시간과 끝시간이 완전히 일치하는 경우만 고려했습니다...test date set도 맞춰서 작성할게요..)
                PersonalSchedule thisSch = new PersonalSchedule();
                thisSch.setDate(mDateTextView.getText().toString());
                thisSch.setStartTime(mStartTimeTextView.getText().toString());
                thisSch.setEndTime(mEndTimeTextView.getText().toString());

                boolean overlap_check = false;
                ArrayList<PersonalSchedule> myps = personalScheduleController.getAllPS(MainActivity.User.getID());
                for(int i = 0; i < myps.size(); i++){
                    if(personalScheduleController.Overlap_add(thisSch, myps.get(i))) {
                        overlap_check = true;
                        break;
                    }
                }
                ArrayList<GroupSchedule> mygs = personalScheduleController.getAllGS(MainActivity.User.getID());
                for(int i = 0; i < mygs.size(); i++){
                    if(personalScheduleController.Overlap_add(thisSch, mygs.get(i))){
                        overlap_check = true;
                        break;
                    }
                }


                if(overlap_check == true){
                    Toast.makeText(PersonalScheduleAdditionForm.this, "해당 시간대에 이미 등록된 일정이 존재합니다", Toast.LENGTH_SHORT).show();
                }else {
                    saveButtonClicked();
                    onBackPressed();
                    //finish();

                    Toast.makeText(PersonalScheduleAdditionForm.this, "일정이 등록되었습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void saveButtonClicked() {
        int PRIORITY;

        String NAME = mNameEditText.getText().toString();
        String LOCATION = mLocationEditText.getText().toString();
        String DESCRIPTION = mDescriptionEditText.getText().toString();
        if (fixed.isChecked()) {
            priority = fixed.getText().toString();
            PRIORITY = 10;
        }
        else if (hardly.isChecked()) {
            priority = hardly.getText().toString();
            PRIORITY = 5;
        }
        else if (easily.isChecked()) {
            priority = easily.getText().toString();
            PRIORITY = 0;
        }
        else {
            priority = "null"; //조정불가 버튼 default 해제해서 해당 조건문 추가했습니다
            if (NAME.contains("약속") || DESCRIPTION.contains("약속"))
                PRIORITY = 1;
            else if (NAME.contains("은행") || NAME.contains("미용실") || NAME.contains("병원")
                    || DESCRIPTION.contains("은행") || DESCRIPTION.contains("미용실") || DESCRIPTION.contains("병원"))
                PRIORITY = 2;
            else if (NAME.contains("취미") || DESCRIPTION.contains("취미"))
                PRIORITY = 3;
            else if (NAME.contains("고정 밥") || NAME.contains("고밥") ||
                    DESCRIPTION.contains("고정 밥") || DESCRIPTION.contains("고밥"))
                PRIORITY = 4;
            else if (NAME.contains("가족 행사") || DESCRIPTION.contains("가족 행사"))
                PRIORITY = 6;
            else if (NAME.contains("팀플") || NAME.contains("팀 프로젝트") || NAME.contains("조별과제") ||
                    DESCRIPTION.contains("팀플") || DESCRIPTION.contains("팀 프로젝트") || DESCRIPTION.contains("조별과제"))
                PRIORITY = 7;
            else if (NAME.contains("동아리") || NAME.contains("학회") ||
                    DESCRIPTION.contains("동아리") || DESCRIPTION.contains("학회"))
                PRIORITY = 8;
            else if (NAME.contains("알바") || NAME.contains("학교 행사") ||
                    DESCRIPTION.contains("알바") || DESCRIPTION.contains("학교 행사"))
                PRIORITY = 9;
            else
                PRIORITY = -1;
        }
        String DATE = mDateTextView.getText().toString();
        String START_TIME = mStartTimeTextView.getText().toString();
        String END_TIME = mEndTimeTextView.getText().toString();

        personalScheduleController.AddSchedule(MainActivity.User, NAME, LOCATION, DESCRIPTION, PRIORITY, DATE, START_TIME, END_TIME);
    }
}