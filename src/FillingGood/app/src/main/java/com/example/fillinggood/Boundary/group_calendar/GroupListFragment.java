package com.example.fillinggood.Boundary.group_calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.Boundary.personal_calendar.PersonalScheduleAdditionForm;
import com.example.fillinggood.Control.GroupManagementController;
import com.example.fillinggood.Control.GroupManagementController;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.R;


import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

//"내 모임 목록" 클릭시 처음 맞이하게 되는 화면입니다
public class GroupListFragment extends Fragment {
    private int REQUEST_ADD = 1;
    private int REQUEST_MOD = 2;
    private GroupListviewAdapter adapter;

    private GroupListViewModel groupListViewModel;

    private GroupManagementController groupManagementController;
    private ArrayList<Group> list;

    protected ListView lView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        groupListViewModel =
                ViewModelProviders.of(this).get(GroupListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_group_list, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        groupListViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //상단 우측 메뉴 변경
        setHasOptionsMenu(true);

        groupManagementController = new GroupManagementController(MainActivity.User);

        list = groupManagementController.FindAllUserGroup(MainActivity.User.getID());

        //instantiate custom adapter
        adapter = new GroupListviewAdapter(list, getActivity());

        //handle listview and assign adapter
        lView = (ListView)root.findViewById(R.id.groupListview);
        lView.setAdapter(adapter);

        /*
        Intent C = getActivity().getIntent();
        if(C.getBooleanExtra("check", false)){
            onActivityResult(REQUEST_ADD, RESULT_OK, C);
        }*/

        adapter.setOnItemClickListener(new GroupListviewAdapter.OnItemClickListener() {

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }

            @Override
            public void onChangeClick(int position) {
                changeItem(position);
            }
        });

        return root;
    }


    // 실제 삭제
    public void removeItem(int position){

        // position의 그룹명을 알아오고
        // user의 그룹 내역을 수정, 저장하고
        // 해당 그룹에서 user 정보 삭제하고.

        Group delgroup = list.get(position);
        groupManagementController = new GroupManagementController(delgroup.getName(), MainActivity.User);
        groupManagementController.DelGroup(delgroup.getName());

        list.remove(delgroup.getName());
        list.clear();
        list = groupManagementController.FindAllUserGroup(MainActivity.User.getID());
        //adapter = new GroupListviewAdapter(list, getActivity());
        adapter.list = list;
        lView.setAdapter(adapter);
    }
    // 실제 수정 - Modification에서 처리
    public void changeItem(int position) {

        Intent X = new Intent(getActivity(), GroupModificationForm.class);
        X.putExtra("id", position);
        X.putExtra("name",list.get(position).getName());
        X.putExtra("description", list.get(position).getDescription());
        String members = new String();
        for(int i = 0; i < list.get(position).getGroupMembers().size(); i++){
            members += list.get(position).getGroupMembers().get(i).getID() + " ";
        }
        X.putExtra("groupmem", members);

        startActivityForResult(X, REQUEST_MOD);

        Intent B = getActivity().getIntent();
        if (B.getBooleanExtra("check", false)) {
            onActivityResult(REQUEST_MOD, RESULT_OK, B);
        }
        list.clear();
        list = groupManagementController.FindAllUserGroup(MainActivity.User.getID());
        adapter.list = list;
        lView.setAdapter(adapter);
    }




    //상단 우측 메뉴탭 설정하는 함수
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sub2, menu) ;
        super.onCreateOptionsMenu(menu,inflater);
    }

    //상단 우측 탭 메뉴 클릭시, 해당 액티비티(새 모임 등록)로 이동
    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        if (item.getItemId()==R.id.add_new_group){
            Intent intent = new Intent(getActivity(), GroupAdditionForm.class);
            startActivityForResult(intent,REQUEST_ADD);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_ADD){
            // list update

            list = groupManagementController.FindAllUserGroup(MainActivity.User.getID());
            //list.add(new Group(data.getStringExtra("name"), data.getStringExtra("description"), data.getStringArrayListExtra("groupmem")));
            Intent C = getActivity().getIntent();
            if(C.getBooleanExtra("check", false)){
                onActivityResult(REQUEST_ADD, RESULT_OK, C);
            }

            adapter.notifyDataSetChanged();
            lView.setAdapter(adapter);
        }

        if(requestCode == REQUEST_MOD){
            // list update

            list = groupManagementController.FindAllUserGroup(MainActivity.User.getID());

            //int position = data.getIntExtra("id2",1);



            //Group g = list.get(position);
            //g.setName(data.getStringExtra("name"));
            //g.setDescription(data.getStringExtra("description"));
            //g.setGroupMembers(data.getStringArrayListExtra("groupmem"));

            adapter.notifyDataSetChanged();
            lView.setAdapter(adapter);
        }

    }
}