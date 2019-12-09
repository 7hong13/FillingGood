package com.example.fillinggood.Boundary.personal_calendar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggood.Boundary.EventDecorator;
import com.example.fillinggood.Boundary.MarkingDots;
import com.example.fillinggood.Boundary.OneDayDecorator;
import com.example.fillinggood.Boundary.group_calendar.GroupScheduleAdditionForm;
import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.Control.PersonalScheduleController;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.Entity.GroupSchedule;
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
    public String name, location, description, priority, date, startTime, endTime, userID;
    public String NAME, LOCATION, DESCRIPTION, DATE, START_TIME, END_TIME;
    public int PRIORITY;

    private String tempDate, tempStartTime;
    private PersonalScheduleController personalScheduleController = new PersonalScheduleController();
    private PersonalSchedule tempps;
    private String checked;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_personal_schedule);

        ArrayList<PersonalSchedule> eventsList = new ArrayList<>();
        date = getIntent().getStringExtra("date");
        startTime = getIntent().getStringExtra("startTime");
        tempps = personalScheduleController.getPS(MainActivity.User.getID(), date, startTime);

        // element 할당하기
        mCalendarView = findViewById(R.id.calendarView);

        mNameEditText = findViewById(R.id.event_name);
        name = getIntent().getStringExtra("name");
        mNameEditText.setText(name);

        mLocationEditText = findViewById(R.id.event_location);
        mLocationEditText.setText(tempps.getLocation());
        mDescriptionEditText = findViewById(R.id.event_description);
        mDescriptionEditText.setText(tempps.getDescription());

        mDateTextView = findViewById(R.id.event_date);
        mDateTextView.setText(date);

        mStartTimeTextView = findViewById(R.id.event_start_time);
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

        if (tempps.getPriority()>7 && tempps.getPriority() <= 10) {
            fixed.setChecked(true);
            checked = "fixed";
        }
        else if (tempps.getPriority() > 3 && tempps.getPriority() <= 7) {
            hardly.setChecked(true);
            checked = "hardly";
        }
        else if (tempps.getPriority() >= 0 && tempps.getPriority() <= 3) {
            easily.setChecked(true);
            checked = "easily";
        }else {
            checked = "none";
        }
        year = Integer.parseInt(tempps.getDate().substring(0,4));
        month = Integer.parseInt(tempps.getDate().substring(5,7));
        day = Integer.parseInt(tempps.getDate().substring(8));
        //선택한 날짜 체크 표시
        CalendarDay selectedDate = CalendarDay.from(year, month-1, day);
        mCalendarView.setDateSelected(selectedDate,true);


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
                        PersonalScheduleModificationForm.this,R.style.MyTimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
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



        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //필수 기입 정보인 일정명, 시간 기입 안 한 경우
                //date를 startDate랑 endDate로 나누시면 거기에 맞춰서 조건문 수정해주세요
                if (mNameEditText.getText().toString().length() == 0 || mDateTextView.getText().toString().length()==0
                        || mStartTimeTextView.getText().toString().length()==0 || mEndTimeTextView.getText().toString().length()==0){
                    Toast.makeText(PersonalScheduleModificationForm.this, "기입하지 않은 정보가 존재합니다\n(이름,날짜,시간)", Toast.LENGTH_SHORT).show();
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
                    if(personalScheduleController.Overlap_modify(tempps, thisSch, myps.get(i))) {
                        overlap_check = true;
                        break;
                    }
                }
                ArrayList<GroupSchedule> mygs = personalScheduleController.getAllGS(MainActivity.User.getID());
                for(int i = 0; i < mygs.size(); i++){
                    if(personalScheduleController.Overlap_modify(tempps, thisSch, mygs.get(i))){
                        overlap_check = true;
                        break;
                    }
                }

                if(overlap_check){
                    Toast.makeText(PersonalScheduleModificationForm.this, "해당 시간대에 이미 등록된 일정이 존재합니다", Toast.LENGTH_SHORT).show();
                }else {
                    modifyButtonClicked();
                    //onBackPressed();
                    finish();

                    Toast.makeText(PersonalScheduleModificationForm.this, "일정이 수정되었습니다", Toast.LENGTH_SHORT).show();
                }
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
                                finish();

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
        NAME = mNameEditText.getText().toString();
        LOCATION = mLocationEditText.getText().toString();
        DESCRIPTION = mDescriptionEditText.getText().toString();
        if (fixed.isChecked() && !checked.equals("fixed")) { PRIORITY = 10; }
        else if (hardly.isChecked() && !checked.equals("hardly")) { PRIORITY = 5; }
        else if (easily.isChecked() && !checked.equals("easily")) { PRIORITY = 0; }
        else { PRIORITY = tempps.getPriority(); }
        DATE = mDateTextView.getText().toString();
        START_TIME = mStartTimeTextView.getText().toString();
        END_TIME = mEndTimeTextView.getText().toString();

        personalScheduleController.ModifySchedule(MainActivity.User, tempps, NAME, LOCATION, DESCRIPTION, PRIORITY, DATE, START_TIME, END_TIME);

    }
    public void deleteButtonClicked() {
        //db에서 해당 일정정보를 삭제하는 코드를 적어주세요
        personalScheduleController.DeleteSchedule(MainActivity.User, tempps);

    }
}