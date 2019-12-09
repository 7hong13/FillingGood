package com.example.fillinggood.Boundary.group_calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.Control.FeedbackController;
import com.example.fillinggood.R;

public class FeedbackModificationForm extends Fragment {
    private String groupName;
    private String description;
    private String name;
    private String date;
    private String startTime;
    private String endTime;
    private String location;

    static protected FeedbackController feedController;

    FeedbackModificationForm(){}
    FeedbackModificationForm(String groupName, String name, String description, String date, String startTime, String endTime, String location){
        this.groupName = groupName;
        this.description = description;
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        feedController = new FeedbackController(groupName, FeedbackController.getOneGroupSchedule(this.groupName, this.date, this.startTime));
    }
    private TextView T_description, T_name, T_time, T_location;
    private RadioGroup timeRadioGroup, locationRadioGroup;
    private RadioButton timeGood, timeNotBad, timeBad, locationGood, locationNotBad, locationBad;
    private Button editButton, deleteButton;
    private EditText feedback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.modify_feedback, container, false);


        T_description = (TextView)root.findViewById(R.id.description);
        T_name = (TextView)root.findViewById(R.id.name);
        T_time = (TextView)root.findViewById(R.id.time);
        T_location = (TextView)root.findViewById(R.id.location);


        timeRadioGroup = (RadioGroup)root.findViewById(R.id.timeRadioGroup);
        locationRadioGroup = (RadioGroup)root.findViewById(R.id.locationRadioGroup);

        timeGood = (RadioButton)root.findViewById(R.id.timeGood);
        timeNotBad = (RadioButton)root.findViewById(R.id.timeNotBad);
        timeBad = (RadioButton)root.findViewById(R.id.timeBad);

        locationGood = (RadioButton)root.findViewById(R.id.locationGood);
        locationNotBad = (RadioButton)root.findViewById(R.id.locationNotBad);
        locationBad = (RadioButton)root.findViewById(R.id.locationBad);

        editButton = (Button)root.findViewById(R.id.edit_feedback);
        deleteButton = (Button)root.findViewById(R.id.delete_feedback);

        feedback = (EditText)root.findViewById(R.id.feedback);
        String feed = FeedbackController.getMyFeed(groupName, date, startTime, MainActivity.User.getID()).Feed;
        feedback.setText(feed);

        T_name.setText("일정명 : "+name);
        T_description.setText("일정 설명 : "+description);
        T_time.setText("시간 : "+startTime+"~"+endTime);
        T_location.setText("장소 : "+location);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //피드백 수정사항을 저장하는 코드
                if(feedback.getText().toString().length() < 1){
                    Toast.makeText(getContext(), "피드백을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if(feedback.getText().toString().length() > 45){
                    Toast.makeText(getContext(), "평가는 45자를 초과할 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {

                    feedController.UpdateFeed(groupName, MainActivity.User.getID(), feedback.getText().toString());

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.feedback, new GroupFeedbackList(groupName, date))
                            .addToBackStack(null)
                            .commit();

                    Toast.makeText(getContext(), "피드백이 수정되었습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //피드백을 삭제하는 코드
                feedController.DeleteFeed(groupName, MainActivity.User.getID());

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.feedback, new GroupFeedbackList(groupName, date))
                        .addToBackStack(null)
                        .commit();

                Toast.makeText(getContext(), "피드백이 삭제되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
