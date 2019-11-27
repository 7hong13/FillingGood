package com.example.fillinggood.Boundary.group_calendar;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

//그룹 정보를 수정하는 class 입니다
public class GroupModificationForm extends AppCompatActivity {
    EditText groupName, groupDescription, addedGroupMember;
    ListView groupMembers;
    Button addMember, saveResult;
    ArrayList<String> list = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_group_information);

        //GUI 구성을 보이기 위한 코드로 db 구축 후 지우고 사용해주세요
        ArrayList<String> names = new ArrayList<>();
        names.add("abc1");
        names.add("abc2");
        names.add("abc3");
        names.add("abc4");
        names.add("abc5");
        Group groupInfo = new Group("4조", "융합소프트웨어 종합설계 팀플조", names);

        //모임명으로 입력된 값
        groupName = (EditText)findViewById(R.id.groupName);
        groupName.setText(groupInfo.getName());

        //모임 설명으로 입력된 값
        groupDescription = (EditText)findViewById(R.id.groupDescription);
        groupDescription.setText(groupInfo.getDescription());

        //모임 구성원 추가 입력창에 입력된 값(모임원 아이디)
        addedGroupMember = (EditText)findViewById(R.id.groupMember);

        //현재 추가된 모임 구성원 이름들
        groupMembers = (ListView)findViewById(R.id.groupMembers);
        //기존 모임 구성원들이 화면상 추가돼 있도록 보여주는 코드
        Iterator<String> iter = names.iterator();
        while (iter.hasNext()){
            String s = iter.next();
            list.add(s);
        }
        GroupMemberListviewAdapter adapter = new GroupMemberListviewAdapter(list, GroupModificationForm.this);
        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.groupMembers);
        lView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lView);

        //모임 구성원 추가 버튼
        addMember = (Button)findViewById(R.id.addMember);

        //모임 등록 버튼
        saveResult = (Button)findViewById(R.id.saveResult);

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //아이디를 db에서 검색 후, 그 아이디를 모임 구성원으로 추가하고
                //그에 해당하는 이름을 화면에 출력하는 코드를 작성해 주세요
                //아래는 list라는 string array-list(상단에 선언)에 add한 값을 화면상에 출력하는 코드입니다
                list.add(addedGroupMember.getText().toString());
                addedGroupMember.setText("");
                //instantiate custom adapter
                GroupMemberListviewAdapter adapter = new GroupMemberListviewAdapter(list, GroupModificationForm.this);
                //handle listview and assign adapter
                ListView lView = (ListView)findViewById(R.id.groupMembers);
                lView.setAdapter(adapter);
                setListViewHeightBasedOnChildren(lView);
            }
        });

        saveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //모임 정보를 db로 올리는 코드를 작성해주세요
            }
        });

    }
    //gui 상 에러 잡는 코드로 신경 안 쓰셔도 됩니다
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
