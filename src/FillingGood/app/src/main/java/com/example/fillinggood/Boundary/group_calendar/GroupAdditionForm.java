package com.example.fillinggood.Boundary.group_calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggood.Boundary.DBboundary.DBmanager;
import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.Boundary.personal_calendar.PersonalScheduleModificationForm;
import com.example.fillinggood.R;
import com.example.fillinggood.Control.GroupManagementController;

import org.w3c.dom.Text;

import java.util.ArrayList;

//새 모임을 등록하는 class 입니다
public class GroupAdditionForm extends AppCompatActivity {
    EditText groupName, groupDescription, addedGroupMember;
    ListView groupMembers;
    Button addMember, saveResult;
    ArrayList<String> list = new ArrayList<String>();

    GroupManagementController groupManagementController = new GroupManagementController(MainActivity.User);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_group);


        //모임명으로 입력된 값
        groupName = (EditText)findViewById(R.id.groupName);

        //모임 설명으로 입력된 값
        groupDescription = (EditText)findViewById(R.id.groupDescription);

        //모임 구성원 추가 입력창에 입력된 값(모임원 아이디)
        addedGroupMember = (EditText)findViewById(R.id.groupMember);

        //현재 추가된 모임 구성원 이름들
        groupMembers = (ListView)findViewById(R.id.groupMembers);

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
                String memID = addedGroupMember.getText().toString();

                int findmember = groupManagementController.FindMember(memID);
                if(findmember == -1){ // 존재하지 않는 ID
                    Toast.makeText(GroupAdditionForm.this, "존재하지 않는 ID입니다", Toast.LENGTH_SHORT).show();
                    addedGroupMember.setText("");
                } else if(findmember == -2){
                    Toast.makeText(GroupAdditionForm.this, "본인을 추가할 수 없습니다", Toast.LENGTH_SHORT).show();
                    addedGroupMember.setText("");
                }
                else if(findmember == 1){ // 이미 추가된 구성원입니다.
                    int addmember = groupManagementController.AddMember(memID);
                    if(addmember == -1) {
                        Toast.makeText(GroupAdditionForm.this, "이미 추가된 구성원입니다", Toast.LENGTH_SHORT).show();
                        addedGroupMember.setText("");
                    } else if(addmember == 1){
                        list.add(memID);
                        addedGroupMember.setText("");
                    }
                }

                //list.add(addedGroupMember.getText().toString());
                //addedGroupMember.setText("");
                //instantiate custom adapter
                GroupMemberListviewAdapter adapter = new GroupMemberListviewAdapter(list, GroupAdditionForm.this);
                //handle listview and assign adapter
                ListView lView = (ListView)findViewById(R.id.groupMembers);
                lView.setAdapter(adapter);
                setListViewHeightBasedOnChildren(lView);
            }
        });

        saveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = groupName.getText().toString();
                String description = groupDescription.getText().toString();
                int findgroup = groupManagementController.FindGroup(name);
                if(findgroup == 1){
                    Toast.makeText(GroupAdditionForm.this, "이미 존재하는 모임명입니다", Toast.LENGTH_SHORT).show();
                } else if(findgroup == -1){
                    if(name.equals("")){
                        Toast.makeText(GroupAdditionForm.this, "모임 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                    else if(list.size() == 0){
                        Toast.makeText(GroupAdditionForm.this, "모임 구성원을 추가해주세요", Toast.LENGTH_SHORT).show();
                    }else {
                        // period는..
                        for(int i = 0; i < list.size(); i++){
                            if(list.get(i).equals(MainActivity.User.getID()))
                                list.remove(i);
                        }
                        groupManagementController.saveGroup(MainActivity.User.getID(), name, description, list);
                        // list > 멤버로 저장
                        // 나user > 리더로 저장

                        Intent intent = new Intent(GroupAdditionForm.this, GroupListFragment.class);
                        intent.putExtra("name", name);
                        intent.putExtra("description", description);
                        intent.putExtra("check", true);
                        intent.putExtra("groupmem", list);

                        setResult(RESULT_OK, intent);
                        finish();

                        Toast.makeText(GroupAdditionForm.this, "모임이 등록되었습니다", Toast.LENGTH_SHORT).show();
                    }
                }
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
