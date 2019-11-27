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

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.fillinggood.Boundary.personal_calendar.PersonalScheduleAdditionForm;
import com.example.fillinggood.R;

import java.security.acl.Group;
import java.util.ArrayList;

//"내 모임 목록" 클릭시 처음 맞이하게 되는 화면입니다
public class GroupListFragment extends Fragment {

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
        ArrayList<String> list = new ArrayList<String>();
        list.add("모임명: 4조 \n모임 설명: 융합소프트웨어 종합설계 팀플조 \n모임원: 김동욱, 김지환, 김지현, 김형준, 함형우");
        list.add("item1");
        list.add("item2");

        //instantiate custom adapter
        GroupListviewAdapter adapter = new GroupListviewAdapter(list, getActivity());

        //handle listview and assign adapter
        ListView lView = (ListView)root.findViewById(R.id.groupListview);
        lView.setAdapter(adapter);

        return root;
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
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}