package com.example.fillinggood.Boundary.personal_calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
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

import com.example.fillinggood.Boundary.EventDecorator;
import com.example.fillinggood.Boundary.MarkingDots;
import com.example.fillinggood.Boundary.OneDayDecorator;
import com.example.fillinggood.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.ravikoradiya.library.CenterTitle;

import java.util.Calendar;

//일정 추가 floating button을 눌렀을 때 실행되는 class 입니다
public class PersonalScheduleAdditionForm extends AppCompatActivity {
    private EditText mNameEditText, mLocationEditText, mDescriptionEditText;
    private TextView mDateTextView, mStartTimeTextView, mEndTimeTextView;
    private MaterialCalendarView mCalendarView;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button saveButton;
    private RadioGroup radioGroup;
    private RadioButton fixed, hardly, easily;
    private long mScheduleID = -1;
    public String name, location, description, priority, date, startTime, endTime, amPm;


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
                int day = Integer.parseInt(parsedDATA[2]);
                date =""+year+"."+month+"."+day; //db에 삽입할 값. 위에 public string으로 선언 돼 있음.
                mDateTextView.setText(date);
            }
        });

        //일정 등록된 날짜에 점찍기
        mCalendarView.addDecorator(new EventDecorator(Color.parseColor("#22c6cf"), MarkingDots.markingDots()));
        //오늘 날짜의 색을 바꿈
        mCalendarView.addDecorators(new OneDayDecorator());

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
                        PersonalScheduleAdditionForm.this,R.style.MyTimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endTime = String.format("%02d:%02d ", hourOfDay, minute);
                        mEndTimeTextView.setText(endTime);
                    }
                }, currentHour+1, currentMinute, true);
                dialog.show();
            }
        });

        //여기서부터는 mysql 문법에 맞춰 수정해주세요
        /*
        Intent intent = getIntent();
        if (intent != null) {
            // 누군가 나를 호출했다면,
            mScheduleID = intent.getLongExtra("id", -1);
            String NAME = intent.getStringExtra("name");
            String LOCATION = intent.getStringExtra("location");
            String DESCRIPTION = intent.getStringExtra("description");
            String PRIORITY = intent.getStringExtra("priority");
            String DATE = intent.getStringExtra("date");
            String START_TIME = intent.getStringExtra("startTime");
            String END_TIME = intent.getStringExtra("endTime");
            mNameEditText.setText(NAME);
            mLocationEditText.setText(LOCATION);
            mDescriptionEditText.setText(DESCRIPTION);
            mDateTextView.setText(DATE);
            mStartTimeTextView.setText(START_TIME);
            mEndTimeTextView.setText(END_TIME);
        } */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonClicked();
                onBackPressed();

                Toast.makeText(PersonalScheduleAdditionForm.this, "일정이 등록되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void saveButtonClicked() {

        String NAME = mNameEditText.getText().toString();
        String LOCATION = mLocationEditText.getText().toString();
        String DESCRIPTION = mDescriptionEditText.getText().toString();
        if (fixed.isChecked()) {
            priority = fixed.getText().toString();
        }
        else if (hardly.isChecked()) {
            priority = hardly.getText().toString();
        }
        else if (easily.isChecked()) {
            priority = easily.getText().toString();
        }
        else {
            priority = "null"; //조정불가 버튼 default 해제해서 해당 조건문 추가했습니다
        }
        String PRIORITY = priority;
        String DATE = mDateTextView.getText().toString();
        String START_TIME = mStartTimeTextView.getText().toString();
        String END_TIME = mEndTimeTextView.getText().toString();

        /*
        // SQLite에 저장하는 기본적인 방법 = ContentValues라는 객체를 만들어 거기에 담아서 DB에 저장
        ContentValues contentValues = new ContentValues();
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_NAME, NAME);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_LOCATION, LOCATION);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_DESCRIPTION, DESCRIPTION);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_PRIORITY, PRIORITY);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_DATE, DATE);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_START_TIME, START_TIME);
        contentValues.put(ScheduleContract.ScheduleEntry.COLUMN_NAME_END_TIME, END_TIME);

        // DB에 작성할 것이기 때문에 WritableDatabase
        SQLiteDatabase db = ScheduleDbHelper.getInstance(this).getWritableDatabase();
        // 수정이 아니라 최초 저장인 경우
        long newRowID = db.insert(ScheduleContract.ScheduleEntry.TABLE_NAME,
                null,
                contentValues);

        if (newRowID == -1) {
            Toast.makeText(this, "저장에 문제가 발생하였습니다", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "일정이 저장되었습니다", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
        }
        // 잘 되었는지 안 되었는지는 return 값으로 확인 가능
        // 잘 되었다면 return row_ID(long type), 안 되었다면 return -1*/
    }
}