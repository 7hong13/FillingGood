package com.example.fillinggood.Entity;

import android.util.Log;

import com.example.fillinggood.Boundary.DBboundary.DBmanager;

import java.util.ArrayList;
import java.util.HashMap;

public class Group {
    private String name;
    private String description;
    private String startSchedulePeriod;
    private String endSchedulePeriod;
    private  GroupMember groupLeader;
    private ArrayList<GroupMember> groupMembers;
    private ArrayList<GroupSchedule> groupSchedules;
    private double[][] timetable;
    private HashMap<String, String[]> location_priority = new HashMap<>();
    // DB로 뺄 것!
    String[] location = {
            "PA관 1층(파관 라운지 / 파관 투썸 앞)", "PA관 4층 스터디룸",
            "마관 3층 휴게실", "J관 세미나실(J관 1층 스터디룸)",
            "J관 4층 휴게공간(J관 문과대 휴게실 맞은편 휴게 공간)",
            "경카 스터디룸", "만레사 스터디룸(도서관 스터디룸)",
            "도라지(도서관 라운지)", "GA관 스터디룸",
            "AS관 로비(AS관 5층 공대 휴게실)", "빈 강의실",
            "과방", "GN관 계단", "우정관 카페 옆", "커브",
            "굿투데이", "정문 스타벅스", "신촌 카페", "플리즈 커피",
            "스터디 카페", "테이스트", "뗴이야르관 8층 전자과 전용 스터디룸",
            "다산관로비", "마관 5층휴게실", "K관 1층카페", "술탄커피"};

    public void setLocation_priority(HashMap<String, String[]> location_priority) {
        this.location_priority = location_priority;
    }
    public HashMap<String, String[]> getLocation_priority(){
        return location_priority;
    }

    public void Location_priorityInit(){
        // location : 모든 추천 가능한 장소 이름 String 으로 구성된 1차원 배열 불러왔다고 가정
        String[] empty = {};
        for(int i = 0; i < location.length; i++){
            // key: 장소이름, value: 빈 String[] 인 HashMap 생성
            this.location_priority.put(location[i], empty);
        }
    }

    public Group(){
        groupMembers = new ArrayList<>();
        groupSchedules = new ArrayList<>();
    } // DB에서 넘겨줄 객체 생성시에 사용
    public Group(String name, String description, ArrayList<GroupMember> groupMembers){
        this.name = name;
        this.description = description;
        this.groupMembers = groupMembers;
        getTimeTable(this.name); // init TT || get TT from DB

    }

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}

    public String getStartSchedulePeriod(){return startSchedulePeriod;}
    public void setStartSchedulePeriod(String startSchedulePeriod){this.startSchedulePeriod = startSchedulePeriod;}
    public String getEndSchedulePeriod(){return endSchedulePeriod;}
    public void setEndSchedulePeriod(String endSchedulePeriod){this.endSchedulePeriod = endSchedulePeriod;}

    public GroupMember getGroupLeader(){return groupLeader;}
    public void setGroupLeader(GroupMember groupLeader){this.groupLeader = groupLeader;}
    public ArrayList<GroupMember> getGroupMembers(){return groupMembers;}
    public void setGroupMembers(ArrayList<GroupMember> groupMembers){this.groupMembers = groupMembers;}

    public double[][] getGroupTimeTable(){return timetable;}
    public void setGroupTimeTable(double[][] timetable){this.timetable = timetable;}

    public ArrayList<GroupSchedule> getGroupSchedules(){return groupSchedules;}

    // Util
    public static Group getGroup(String groupName){
        return DBmanager.getInstance().getGroupInfo(groupName);
    }
    public static ArrayList<String> getAllGroupName(){
        return DBmanager.getInstance().getAllGroupName();
    }
    public static ArrayList<Group> getAllUsersGroup(String userID){
        ArrayList<Group> groups = DBmanager.getInstance().getUserGroup(userID);
        if(groups == null)
            return new ArrayList<>();
        ArrayList<GroupMember> members;
        GroupMember leader;
        for(int i = 0; i < groups.size(); i++){
            Group temp = groups.get(i);
            members = DBmanager.getInstance().getGroupMember(temp.getName());
            if(members == null)
                temp.setGroupMembers(new ArrayList<GroupMember>());
            else
                temp.setGroupMembers(members);
            leader = DBmanager.getInstance().getGroupLeader(temp.getName());
            if(leader == null)
                temp.setGroupLeader(new GroupMember());
            else
                temp.setGroupLeader(leader);
        }
        return groups;
    }
    public static void saveGroupInfo(String groupName, String groupDescription){
        DBmanager.getInstance().saveGroupInfo(groupName, groupDescription);
    }
    public static void saveGroupSEperiod(String groupName, String startPeriod, String endPeriod){
        DBmanager.getInstance().saveGroupSEperiod(groupName, startPeriod, endPeriod);
    }
    public static void saveGroupMember(String groupName, String userID, ArrayList<String> members){
        DBmanager.getInstance().saveGroupMembers(groupName, userID, members);
    }

    // 그룹 관리
    public int AddMember(String memID){
        for(int i = 0; i < groupMembers.size(); i++){
            if(groupMembers.get(i).getID().equals(memID))
                return -1;
        }
        GroupMember member = DBmanager.getInstance().getUser(memID);
        groupMembers.add(member);
        return 1;
    }
    public void DelMember(String memID){
        for(int i = 0; i < groupMembers.size(); i++){
            if(groupMembers.get(i).getID().equals(memID)) {
                groupMembers.remove(i);
                ArrayList<String> temps = new ArrayList<>();
                for(int j = 0; j < groupMembers.size(); j++){
                    temps.add(groupMembers.get(j).getName());
                }
                DBmanager.getInstance().saveGroupMembers(this.name, this.groupLeader.getID(), temps);
                return;
            }
        }
    }
    public static void delGroup(String groupName){
        DBmanager.getInstance().delGroup(groupName);
    }

    // Time Table : 시간대 가중치 값
    public void getTimeTable(String groupName){
        if(DBmanager.getInstance().findTT(groupName) == false) {
            TimeTableInit(groupName);
        }
        else {
            Log.d("check", "true");
            this.timetable = DBmanager.getInstance().getTimeTable(groupName);
            Log.d("check", "" + timetable);
        }
    }

    public void TimeTableInit(String groupName){  // 시간 가중치 default 값으로 초기화하는 함수
        this.timetable = new double[7][96];
        for(int i = 0; i < this.timetable.length; i++){
            for(int k = 0; k < this.timetable[0].length; k++) {
                if (i==5 || i==6){  // 주말 가중치 1.2
                    if (k <= 28 || k > 88)  // 오전 7시 이전, 밤 10시 이후
                        this.timetable[i][k] = 1.2 * 1.2;
                    else
                        this.timetable[i][k] = 1.2;
                }
                else{  // 평일 가중치 1.0
                    if (k <= 28 || k > 88)  // 오전 7시 이전, 밤 10시 이후
                        this.timetable[i][k] = 1.2;
                    else
                        this.timetable[i][k] = 1.0;
                }
            }
        }
        DBmanager.getInstance().saveTimeTable(groupName, this.timetable);
    }
    // 2019.11.20 14:50 ~ 2019.11.20 18:50
    // 이런 식으로 추천 결과 시간을 입력하면, 해당하는 index 범위를 찾아, 그 가중치 값을 반환하는 함수
    public static double[][] getFeedTimeTable(String timename, double[][] timetb){  // (추천 시간, 가중치 테이블)
        double[][] temp = timetb;
        String[] dateset = timename.split(" ~ ");  // 공백으로 구분

        String[] date1 = dateset[0].split("\\s");
        String[] date2 = dateset[1].split("\\s");

        int index_1_1 = Schedule.getDateDay(date1[0]);  // 시작 요일 index
        int index_2_1 = Schedule.getDateDay(date2[0]);  // 끝 요일 index

        String[] time1 = date1[1].split(":");
        String[] time2 = date2[1].split(":");

        int index_1_2 = Integer.parseInt(time1[0])*60 + Integer.parseInt(time1[1]);
        index_1_2 = (int) Math.ceil(index_1_2 / 15); // 시작 시간 index
        int index_2_2 = Integer.parseInt(time2[0])*60 + Integer.parseInt(time2[1]);  // 끝 시간
        index_2_2 = (int) Math.ceil(index_2_2 / 15); // 시작 시간 index

        int[][] index_set = new int[2][2];
        index_set[0][0] = index_1_1;  // 시작 요일 index
        index_set[0][1] = index_1_2;  // 시작 시간 index
        index_set[1][0] = index_2_1;  // 끝 요일 index
        index_set[1][1] = index_2_2;  // 끝 시간 index

        for(int i = index_set[0][0]; i <= index_set[1][0]; i++){
            for(int j = index_set[0][1]; j <= index_set[1][1]; j++){
                temp[i][j] -= 0.1;  // 추천 받은 시간대 가중치 0.1 만큼 낮춤
            }
        }

        return temp;
    }
}
