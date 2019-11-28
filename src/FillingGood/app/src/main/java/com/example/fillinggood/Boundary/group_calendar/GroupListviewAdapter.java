package com.example.fillinggood.Boundary.group_calendar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

//"내 모임 목록"에서 개별 모임의 관리를 맡는 class로, 밑에 "삭제 버튼"에 대한 코드만 작성해주시면 됩니다
public class GroupListviewAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<>();
    private Context context;

    public GroupListviewAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.group_listview, null);
        }
        //Handle TextView and display string from your list
        TextView groupInfo = (TextView)view.findViewById(R.id.groupInfo);
        groupInfo.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button modifyButton = (Button)view.findViewById(R.id.button1);
        Button deleteButton = (Button)view.findViewById(R.id.button2);
        Button newMeetingButton = (Button)view.findViewById(R.id.button3);
        Button pastMettingButton = (Button)view.findViewById(R.id.button4);

        //모임 수정 버튼
        modifyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //해당 버튼을 누를시, 그룹 정보를 수정하는 class로 이동합니다
                Intent intent = new Intent(v.getContext(), GroupModificationForm.class);
                context.startActivity(intent);
            }
        });

        //모임 삭제 버튼
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //해당 버튼을 누를시, 그룹 정보가 db에서 삭제되도록 코드를 짜주세요
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        //일정 생성 버튼
        newMeetingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //해당 버튼을 누를시, 그룹 내 새로운 일정을 생성하는 클래스로 이동합니다
                Intent intent = new Intent(v.getContext(), GroupScheduleList.class);
                context.startActivity(intent);
            }
        });

        //일정 내역 버튼
        pastMettingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //해당 버튼을 누를시, 그룹이 가졌던 일정 내역을 보여주는 클래스로 이동합니다
                Intent intent = new Intent(v.getContext(), GroupScheduleHistoryForm.class);
                context.startActivity(intent);
            }
        });

        return view;
    }

}

