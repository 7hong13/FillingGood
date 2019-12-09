package com.example.fillinggood.Boundary.group_calendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fillinggood.Boundary.DBboundary.DBmanager;
import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.Entity.GroupSchedule;
import com.example.fillinggood.R;
import com.example.fillinggood.Control.FeedbackController;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class GroupFeedbackListAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    private String groupName;
    private ArrayList<GroupSchedule> groupScheduleList = new ArrayList<>();

    public GroupFeedbackListAdapter() { }
    public GroupFeedbackListAdapter(String groupName, ArrayList<GroupSchedule> groupScheduleList, Context context) {
        this.groupName = groupName;
        this.groupScheduleList = groupScheduleList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return groupScheduleList.size();
    }

    @Override
    public Object getItem(int pos) {
        return groupScheduleList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
        //list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }
    @Override
    public int getViewTypeCount() {
        return 3; //return 3, you have three types that the getView() method will return
    }
    @Override
    public int getItemViewType(int position) {
        //모임 일정이 아직 다가오지 않아 피드백 등록이 불가능하다면

        String Date = groupScheduleList.get(position).getDate().toString();
        int year = Integer.parseInt(Date.substring(0,4));
        int month = Integer.parseInt(Date.substring(5,7));
        int day = Integer.parseInt(Date.substring(8,10));
        long date = year*10000 + month*100 + day;

        CalendarDay todayDate = CalendarDay.today();
        long today = todayDate.getYear()*10000 + (todayDate.getMonth()+1)*100 + todayDate.getDay();
        if (today <= date)
            return 2;

            //아직 피드백을 등록하지 않았다면
            //if groupName, date, startTime 정보가 일치하는 feedback 중 사용자 ID와 일치하는 feedID가 존재하지 않을 때
        GroupSchedule gs = groupScheduleList.get(position);

        ArrayList<GroupSchedule.Feed> list = FeedbackController.getFeeds(groupName, gs.getDate(), gs.getStartTime());
        if(list == null) // 해당 일정에 feedback이 하나도 없음
            return 0;
        Iterator<GroupSchedule.Feed> iter = list.iterator();
        boolean flag = false;
        while (iter.hasNext()){
            GroupSchedule.Feed gf = iter.next();
            if (gf.writer.equals(MainActivity.User.getID())){
                flag = true;
                break;
            }
        }
        if (!flag) return 0;

            //피드백을 등록했다면
            //if groupName, date, startTime 정보가 일치하는 feedback 중 사용자 ID와 일치하는 feedID가 존재할 때
        if (flag) return 1;
        else return 1;
    }
    public static class ViewHolder {
        public TextView scheduleName;
        public TextView time;
        public Button button;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        int theType = getItemViewType(position);
        if (view == null) {
            ViewHolder holder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final GroupSchedule gs = groupScheduleList.get(position);
            if (theType == 0) {
                // 날짜가 이미 지났고, 피드백 입력을 아직 안 한 그룹 일정
                view = vi.inflate(R.layout.feedback_listview1, null);
                holder.scheduleName = (TextView)view.findViewById(R.id.name);
                holder.time = (TextView)view.findViewById(R.id.time);
                holder.scheduleName.setText("일정명 : "+gs.getName());
                holder.time.setText("("+gs.getStartTime()+"~"+gs.getEndTime()+")");
                holder.button = (Button)view.findViewById(R.id.feedback);

                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFeedback(groupName, gs.getName(), gs.getDescription(), gs.getDate(), gs.getStartTime(), gs.getEndTime(), gs.getLocation());
                        // gs를 넘겨줘야함.
                        FeedbackAdditionForm.feedController.selectedSchedule = gs;
                    }
                });
            } else if (theType == 1){
                // 날짜가 이미 지났고, 피드백 입력 또한 마친 그룹 일정
                view = vi.inflate(R.layout.feedback_listview2, null);
                holder.scheduleName = (TextView)view.findViewById(R.id.name);
                holder.time = (TextView)view.findViewById(R.id.time);
                holder.scheduleName.setText("일정명 : "+gs.getName());
                holder.time.setText("("+gs.getStartTime()+"~"+gs.getEndTime()+")");
                holder.button = (Button)view.findViewById(R.id.feedback_already_done);

                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFeedback(groupName, gs.getName(), gs.getDescription(), gs.getDate(), gs.getStartTime(), gs.getEndTime(), gs.getLocation());
                        // gs를 넘겨줘야함.
                        FeedbackModificationForm.feedController.selectedSchedule = gs;
                    }
                });
            }
            else {
                // 아직 지나지 않아 피드백 입력이 불가한 그룹 일정
                view = vi.inflate(R.layout.feedback_listview3, null);
                holder.scheduleName = (TextView)view.findViewById(R.id.name);
                holder.time = (TextView)view.findViewById(R.id.time);
                holder.scheduleName.setText("일정명 : "+gs.getName());
                holder.time.setText("("+gs.getStartTime()+"~"+gs.getEndTime()+")");
                holder.button = (Button)view.findViewById(R.id.feedback_not_available_yet);

                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "아직 피드백을 등록할 수 없습니다", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            view.setTag(holder);
        }
        return view;
    }
    public void addFeedback(String groupName, String name, String description, String date, String startTime, String endTime, String location){
        Fragment fr = new FeedbackAdditionForm(groupName, name, description, date, startTime, endTime, location);
        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.feedback, fr);

        fragmentTransaction.commit();
    }
    public void showFeedback(String groupName, String name, String description, String date, String startTime, String endTime, String location){
        Fragment fr = new FeedbackModificationForm(groupName, name, description, date, startTime, endTime, location);
        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.feedback, fr);

        fragmentTransaction.commit();
    }
}
