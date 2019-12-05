package com.example.fillinggood.Boundary.group_calendar;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.fillinggood.Boundary.personal_calendar.PersonalScheduleAdditionForm;
import com.example.fillinggood.Control.GroupController;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.R;


import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

//"내 모임 목록" 클릭시 처음 맞이하게 되는 화면입니다
public class GroupListFragment extends Fragment {
    private int REQUEST_ADD = 1;
    private int REQUEST_MOD = 2;
    private GroupController ctrl;

    private GroupListViewModel groupListViewModel;

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




        //GUI 구성을 보이기 위한 arraylist로, db 구축 후 적절한 코드로 대체해주세요
        //list에 add한 정보들이 화면상에 뜨게 되는 구조입니다
        /*
        ArrayList<Group> list = getUserGroup(userID);
        (아래 코드 지워주세요)
        }
        * */
        ArrayList<Group> list = new ArrayList<Group>();
        ArrayList<String> memlist = new ArrayList<String>();
        memlist.add("함형우");
        memlist.add("김지환");
        memlist.add("김형준");
        memlist.add("김지현");
        memlist.add("김동욱");
        list.add(new Group("융종설 4팀","망할 융종설",memlist));


        //instantiate custom adapter
        GroupListviewAdapter adapter = new GroupListviewAdapter(list, getActivity());

        ctrl = new GroupController(list, adapter);
        //handle listview and assign adapter

        ListView lView = (ListView)root.findViewById(R.id.groupListview);
        lView.setAdapter(ctrl.getMPA());


        ctrl.getMPA().setOnItemClickListener(new GroupListviewAdapter.OnItemClickListener() {

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


    //
    public void removeItem(int position){
        ctrl.getMdatas().remove(position);
        ctrl.getMPA().notifyDataSetChanged();
    }
    public void changeItem(int position) {


        Intent X = new Intent(getActivity(), GroupModificationForm.class);
        X.putExtra("id", position);
        X.putExtra("name",ctrl.getMdatas().get(position).getName());
        X.putExtra("description", ctrl.getMdatas().get(position).getDescription());
        X.putExtra("groupmem", ctrl.getMdatas().get(position).getGroupMembers());

        startActivityForResult(X, REQUEST_MOD);



        Intent B = getActivity().getIntent();
        if (B.getBooleanExtra("check", false)) {
            onActivityResult(REQUEST_MOD, RESULT_OK, B);
        }
        ctrl.getMPA().notifyDataSetChanged();
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
            ctrl.getMdatas().add(new Group(data.getStringExtra("name"), data.getStringExtra("description"), data.getStringArrayListExtra("groupmem")));
            Intent C = getActivity().getIntent();
            if(C.getBooleanExtra("check", false)){
                onActivityResult(REQUEST_ADD, RESULT_OK, C);
            }

            ctrl.getMPA().notifyDataSetChanged();
        }

        if(requestCode == REQUEST_MOD){
            int position = data.getIntExtra("id2",1);



            Group g = ctrl.getMdatas().get(position);
            g.setName(data.getStringExtra("name"));
            g.setDescription(data.getStringExtra("description"));
            g.setGroupMembers(data.getStringArrayListExtra("groupmem"));

            ctrl.getMPA().notifyDataSetChanged();

        }

    }
}