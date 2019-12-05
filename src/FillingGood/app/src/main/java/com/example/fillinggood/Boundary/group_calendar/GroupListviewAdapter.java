package com.example.fillinggood.Boundary.group_calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.Boundary.personal_calendar.PersonalScheduleModificationForm;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

//"내 모임 목록"에서 개별 모임의 관리를 맡는 class로, 밑에 "삭제 버튼"에 대한 코드만 작성해주시면 됩니다
public class GroupListviewAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Group> list = new ArrayList<>();
    private Context context;

    public GroupListviewAdapter(ArrayList<Group> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onDeleteClick(int position);
        void onChangeClick(int position);

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
        TextView groupde = (TextView)view.findViewById(R.id.groupde);
        TextView groupmem = (TextView)view.findViewById(R.id.groupmem);
        groupInfo.setText(list.get(position).getName());
        groupde.setText(list.get(position).getDescription());
        groupmem.setText(list.get(position).getGroupMembers().toString());

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
                /*Intent intent = new Intent(v.getContext(), GroupModificationForm.class);
                intent.putExtra("groupInfo", list.get(position));
                context.startActivity(intent);*/
                if(mListener != null) {
                    mListener.onChangeClick(position);
                }
            }
        });

        //모임 삭제 버튼
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //if 모임장 아니면
                /*if (getGroupLeader(list.get(position).getName())!=userID) {
                    Toast.makeText(context, "모임장만 접근이 가능합니다", Toast.LENGTH_SHORT).show();
                }*/

                //else, 아래 전체 수행
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("모임 삭제")        // 제목 설정
                        .setMessage("해당 모임을 삭제하겠습니까?")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                            public void onClick(DialogInterface dialog, int whichButton){
                                //원하는 클릭 이벤트를 넣으시면 됩니다.
                                //list.remove(position); //or some other task
                                //notifyDataSetChanged();
                                //해당 버튼을 누를시, 그룹 정보가 db에서 삭제되도록 코드를 짜주세요
                                if(mListener != null) {
                                    mListener.onDeleteClick(position);
                                }
                                Toast.makeText(context, "일정이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                            // 취소 버튼 클릭시 설정, 왼쪽 버튼입니다.
                            public void onClick(DialogInterface dialog, int whichButton){
                                //원하는 클릭 이벤트를 넣으시면 됩니다.

                            }
                        });
                final AlertDialog dialog = builder.create();    // 알림창 객체 생성

                dialog.setOnShowListener( new DialogInterface.OnShowListener()
                { @Override public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK); } });

                dialog.show();    // 알림창 띄우기
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

