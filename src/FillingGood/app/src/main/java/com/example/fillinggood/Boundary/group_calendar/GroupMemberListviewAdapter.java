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

import com.example.fillinggood.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

//모임 추가 혹은 모임 수정을 할 때 구성원을 추가 및 삭제하는 class로, 밑에 "삭제 버튼"에 대한 코드만 작성해주시면 됩니다
public class GroupMemberListviewAdapter  extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list  = new ArrayList<>();;
    private Context context;

    public GroupMemberListviewAdapter(ArrayList<String> list, Context context) {
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
            view = inflater.inflate(R.layout.group_members_listview, null);
        }

        //Handle TextView and display string from your list
        TextView groupMemberName = (TextView) view.findViewById(R.id.groupMemberName);
        groupMemberName.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button removeButton = (Button) view.findViewById(R.id.removeButton);

        //모임 구성원 삭제 버튼
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //해당 버튼을 누를시, 해당 모임원이 그룹 내에서 삭제되도록 코드를 짜주세요
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });
        return view;
    }
}
