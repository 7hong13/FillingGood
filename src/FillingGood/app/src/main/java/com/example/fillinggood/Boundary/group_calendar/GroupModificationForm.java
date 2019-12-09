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
import com.example.fillinggood.Control.GroupManagementController;
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
    int pos;

    GroupManagementController groupManagementController;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_group_information);

        //GUI 구성을 보이기 위한 코드로 db 구축 후 지우고 사용해주세요
        Intent A = getIntent();

        pos=A.getIntExtra("id",1);

        //모임명으로 입력된 값
        groupName = (EditText)findViewById(R.id.groupName);
        groupName.setText(A.getStringExtra("name"));
        groupManagementController = new GroupManagementController(groupName.getText().toString(), MainActivity.User);
        final String tempName = groupName.getText().toString();

        //모임 설명으로 입력된 값
        groupDescription = (EditText)findViewById(R.id.groupDescription);
        groupDescription.setText(A.getStringExtra("description"));

        //모임 구성원 추가 입력창에 입력된 값(모임원 아이디)
        addedGroupMember = (EditText)findViewById(R.id.groupMember);

        //현재 추가된 모임 구성원 이름들
        groupMembers = (ListView)findViewById(R.id.groupMembers);
        //기존 모임 구성원들이 화면상 추가돼 있도록 보여주는 코드
        String[] mems = A.getStringExtra("groupmem").split(" ");
        for(int i = 0; i < mems.length; i++)
            list.add(mems[i]);
        //Iterator<String> iter = A.getStringArrayListExtra("groupmem").iterator();
        //while (iter.hasNext()){
        //    String s = iter.next();
        //    list.add(s);
        //}
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
                String memID = addedGroupMember.getText().toString();

                int findmember = groupManagementController.FindMember(memID);
                if(findmember == -1){ // 존재하지 않는 ID
                    Toast.makeText(GroupModificationForm.this, "존재하지 않는 ID입니다", Toast.LENGTH_SHORT).show();
                    addedGroupMember.setText("");
                } else if(findmember == -2){
                    Toast.makeText(GroupModificationForm.this, "본인을 추가할 수 없습니다", Toast.LENGTH_SHORT).show();
                    addedGroupMember.setText("");
                }
                else if(findmember == 1){ // 이미 추가된 구성원입니다.
                    int addmember = groupManagementController.AddMember(memID);
                    if(addmember == -1) {
                        Toast.makeText(GroupModificationForm.this, "이미 추가된 구성원입니다", Toast.LENGTH_SHORT).show();
                        addedGroupMember.setText("");
                    } else if(addmember == 1){
                        list.add(memID);
                        addedGroupMember.setText("");
                    }
                }
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
                int findgroup = groupManagementController.FindGroup(groupName.getText().toString());
                if (findgroup == -1) {
                    Toast.makeText(GroupModificationForm.this, "모임명은 변경할 수 없습니다", Toast.LENGTH_LONG).show();
                    groupName.setText(tempName);
                } else if (findgroup == 1) {

                    //else일 때 아래 두줄 실행
                    AlertDialog.Builder builder = new AlertDialog.Builder(GroupModificationForm.this);
                    builder.setTitle("모임 정보 수정")        // 제목 설정
                            .setMessage("모임 정보를 수정하겠습니까?")        // 메세지 설정
                            .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    String name = groupName.getText().toString();
                                    String description = groupDescription.getText().toString();
                                    if(list.size() == 0){
                                        Toast.makeText(GroupModificationForm.this, "모임 구성원을 추가해주세요", Toast.LENGTH_SHORT).show();
                                    } else {

                                        groupManagementController.saveGroup(MainActivity.User.getID(), name, description, list);

                                        Intent intent = new Intent(GroupModificationForm.this, GroupListFragment.class);
                                        intent.putExtra("name", name);
                                        intent.putExtra("description", description);
                                        intent.putExtra("check", true);
                                        intent.putExtra("groupmem", list);
                                        intent.putExtra("id2", pos);
                                        setResult(RESULT_OK, intent);
                                        finish();

                                        Toast.makeText(GroupModificationForm.this, "모임이 수정되었습니다", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                // 취소 버튼 클릭시 설정, 왼쪽 버튼입니다.
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //원하는 클릭 이벤트를 넣으시면 됩니다.

                                }
                            });
                    final AlertDialog dialog = builder.create();    // 알림창 객체 생성

                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                        }
                    });
                    dialog.show();    // 알림창 띄우기
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
