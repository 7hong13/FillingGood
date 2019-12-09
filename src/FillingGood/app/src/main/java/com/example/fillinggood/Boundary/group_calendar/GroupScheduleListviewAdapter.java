package com.example.fillinggood.Boundary.group_calendar;

import android.content.Context;
import android.content.Intent;
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

import com.example.fillinggood.Entity.GroupSchedule;
import com.example.fillinggood.R;

import java.util.ArrayList;

//그룹 일정 리스트를 생성하는 class 입니다
//그룹 일정 리스트에서 추천받기 버튼을 눌렀을 때나, 추천 투표하기 버튼을 눌렀을 때 발생하는 event에 대해서만 코드 작성하시면 됩니다
public class GroupScheduleListviewAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    private ArrayList<GroupSchedule> groupScheduleList = new ArrayList<>();

    public GroupScheduleListviewAdapter() { }
    public GroupScheduleListviewAdapter(ArrayList<GroupSchedule> groupScheduleList, Context context) {
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
        return 2; //return 3, you have three types that the getView() method will return
    }
    @Override
    public int getItemViewType(int position) {
        //아직 추천을 안받은 경우(추천 생성 안 되어있거나(모임장), 생성은 돼 있으나 로그인한 멤버가 추천 투표 안한 경우(비모임장))
        //if (groupScheduleList.get(position).getRecommending()==null || groupScheduleList.get(position).getChoicedTimeRank()==null)
        //나중에 아래 if문 지워주세요
        if (groupScheduleList.get(position).getChoicedTimeRank()==-1 && groupScheduleList.get(position).getChoicedTimeRank()==-1)
            return 1;
            //추천을 이미 받은 경우
        else return 0;
    }
    public static class ViewHolder {
        public TextView name;
        public TextView time;
        public TextView location;
        public Button recommendButton;
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
                //추천 이미 받은 경우(추천받기 버튼을 띄우지 않음)
                view = vi.inflate(R.layout.group_schedule_listview1, null);
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.location = (TextView) view.findViewById(R.id.location);
                holder.time = (TextView) view.findViewById(R.id.time);
                holder.name.setText("모임 일정명 : " + gs.getName());
                holder.location.setText("장소 : " + gs.getLocation());
                holder.time.setText("시간 : " + gs.getStartTime() + "~" + gs.getEndTime());
            } else {
                // 추천 아직 안 받은 경우
                view = vi.inflate(R.layout.group_schedule_listview2, null);
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.location = (TextView) view.findViewById(R.id.location);
                holder.time = (TextView) view.findViewById(R.id.time);
                holder.name.setText("모임 일정명 : " + gs.getName());
                holder.location.setText("장소 : 미정");
                holder.time.setText("시간 : 미정");
                holder.recommendButton = (Button) view.findViewById(R.id.recommend);

                holder.recommendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if 해당 회원의 모임내 역할이 리더가 아니고, 리더가 추천을 아직 받지 않은 경우,
                        //리더가 추천을 아직 받지 않았다는 메세지 띄우고 접근 제한
                        /*if (!getGroupLeader(gs.getGroupName).equals(userID) && groupScheduleList.get(position).getRecommending()==null)
                        Toast.makeText(context, "모임장이 아직 추천 생성을 하지 않았습니다.", Toast.LENGTH_LONG).show();*/

                        //else일 경우, 아래 activity class로 이동
                        //(else 일 때 case1: 리더이다(프리패스), case2: 리더는 아니지만 리더가 추천생성을 해둔 상태(조건부 패스))
                        Intent intent = new Intent(v.getContext(), ScheduleRecommendationForm.class);
                        intent.putExtra("groupName", gs.getGroupName());
                        intent.putExtra("date", gs.getDate());
                        intent.putExtra("startTime", gs.getStartTime());
                        context.startActivity(intent);
                    }
                });
                view.setTag(holder);
            }
        }
        return view;
    }
}
