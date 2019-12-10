package com.example.fillinggood.Boundary.DBboundary;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.Entity.Group;
import com.example.fillinggood.Entity.GroupMember;
import com.example.fillinggood.Entity.GroupSchedule;
import com.example.fillinggood.Entity.PersonalSchedule;
import com.example.fillinggood.Entity.Schedule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;


public class DBmanager extends AppCompatActivity {
    private static DBmanager instance = new DBmanager();

    public static DBmanager getInstance(){
        return instance;
    }
    public DBmanager(){}

    private static String IP_ADDRESS = "192.168.0.34";
    private static String query = "http://" + IP_ADDRESS + "/query/";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    protected String getData(String uri){
        try {
            AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {
                BufferedReader bufferedReader = null;
                @Override
                protected String doInBackground(String... strings) {
                    try {
                        URL url = new URL(strings[0]);
                        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                        StringBuilder stringBuilder = new StringBuilder();
                        httpURLConnection.connect();
                        bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                        String json;
                        while ( (json = bufferedReader.readLine()) != null){
                            stringBuilder.append(json + "\n");
                        }
                        return stringBuilder.toString().trim();
                    } catch (Exception e){
                        Log.d("check", ""+e);
                        return null;
                    }
                }
            };

            String result = asyncTask.execute(uri).get();
            return result;
        } catch (Exception e){
            return null;
        }
    }

    // 모든 User의 ID 받아옴
    public ArrayList<String> getAllUserID(){
        ArrayList<String> alluser = new ArrayList<>();
        String url = query + "SelAllMemID.php";
        String result = getData(url);

        if(result.equals("결과 없음"))
            return null;
        String[] userInfo = result.split("<br>");
        for(int i = 0; i < userInfo.length; i++){
            alluser.add(userInfo[i]);
        }
        return alluser;
    }
    // 특정 User의 정보 받아옴
    public GroupMember getUser(String userID){
        GroupMember temp = new GroupMember();
        String url = query + "SelMember.php?id=" + userID;

        String result = getData(url);

        if(result.equals("결과 없음"))
            return null;
        String[] userInfo = result.split("<br>");
        for(int i = 0; i < userInfo.length; i++){
            String[] tempS = userInfo[i].split("#");
            temp.setID(tempS[0]);
            temp.setName(tempS[1]);
            temp.setAge(Integer.parseInt(tempS[2]));
            temp.setMajor(tempS[3]);
        }

        return temp;
    }
    // 특정 User의 정보 저장
    public void saveUserInfo(GroupMember user){
        String url = query + "SelMember.php?id=" + user.getID();
        String temp;

        if(getData(url).equals("결과 없음")){
            // insert
            url = query + "InsertMember.php?id=" + user.getID() + "&name=" + user.getName()
                    + "&age=" + user.getAge() + "&major=" + user.getMajor();
            temp = getData(url);
            if(!temp.equals("insert"))
                Log.d("ERROR", "insert fail");
        }
        else{
            // update
            // id는 where에, 나머지는 set에
            url = query + "UpdateMember.php?id=" + user.getID() + "&name=" + user.getName()
                    + "&age=" + user.getAge() + "&major=" + user.getMajor();
            temp = getData(url);
            if(!temp.equals("update"))
                Log.d("ERROR", "update fail");
        }
    }

    // 특정 User가 속한 그룹 정보를 받아옴(GroupMember의 groups 변수)
    public ArrayList<Group> getUserGroup(String userID){
        ArrayList<Group> temp = new ArrayList<Group>();
        String url = query + "SelUserGroup.php?id=" + userID;

        // temps[0]만 할거면.... for 안 돌려도 되지 않을까
        String result = getData(url);
        if(result.equals("결과 없음"))
            return null;
        String[] group = result.split("<br>");
        for(int i = 0; i < group.length; i++){
            String[] temps = group[i].split("#");
            Group tempg;
            tempg = getGroupInfo(temps[0]);
            temp.add(tempg);
        }

        return temp;
    }
    // 특정 User가 속한 그룹 정보를 모두 저장
    public void saveUserGroup(String userID, ArrayList<Group> groups){
        String url = query + "DelAllUserGroup.php?id=" + userID;
        String temp = getData(url);
        String role;
        for(int i = 0; i < groups.size(); i++){
            if(groups.get(i).getGroupLeader().getID() == userID)
                role = "leader";
            else
                role = "member";
            url = query + "InsertUserGroup.php?id=" + userID + "&name=" + groups.get(i).getName()
                    +"&role=" + role;
            temp = getData(url);
            if(temp.equals("insert"))
                return;
            else
                Log.d("ERROR", "UserGroup insert fail");
        }
    }

    // 모든 Group의 Name 받아옴
    public ArrayList<String> getAllGroupName(){
        ArrayList<String> allgroup = new ArrayList<>();
        String url = query + "SelAllGroupName.php";
        String result = getData(url);

        if(result.equals("결과 없음"))
            return null;
        String[] groupname = result.split("<br>");
        for(int i = 0; i < groupname.length; i++){
            allgroup.add(groupname[i]);
        }
        return allgroup;
    }
    // 특정 Group의 정보를 받아옴
    public Group getGroupInfo(String groupName){
        Group temp = new Group();
        String url = query + "SelGroup.php?name=" + groupName;

        String result = getData(url);
        if(result.equals("결과 없음"))
            return null;
        String[] group = result.split("<br>");
        for(int i = 0; i < group.length; i++){
            String[] temps = group[i].split("#");
            temp.setName(temps[0]);
            temp.setDescription(temps[1]);
            //temp.setStartSchedulePeriod(temps[2]);
            //temp.setEndSchedulePeriod(temps[3]);
            // 그룹 멤버 가져오기
            temp.setGroupLeader(getGroupLeader(temps[0]));
            temp.setGroupMembers(getGroupMember(temps[0]));
            // 그룹 스케줄 가져오기...
        }

        return temp;
    }
    // 특정 Group의 일정 생성 시작 날짜와 끝 날짜를 받아옴

    // 특정 그룹의 기본정보 + 구성원 정보 저장
    public void saveGroupInfoMember(Group group){
        String url = query + "SelGroup.php?name=" + group.getName();
        String temp;
        if(getData(url).equals("결과 없음")){
            // insert
            url = query + "InsertGroup.php?name=" + group.getName() + "&description=" + group.getDescription()
                    + "&start=" + group.getStartSchedulePeriod() + "&end=" + group.getEndSchedulePeriod();
            temp = getData(url);
            if(!temp.equals("insert"))
                Log.d("ERROR", "GroupInfo insert fail");
        }
        else{
            // update
            // name은 where에, 나머지는 set에
            url = query + "UpdateGroup.php?name=" + group.getName() + "&description=" + group.getDescription()
                    + "&start=" + group.getStartSchedulePeriod() + "&end=" + group.getEndSchedulePeriod();
            temp = getData(url);
            if(!temp.equals("update"))
                Log.d("ERROR", "GroupInfo update fail");
        }

        url = query + "DelGroupsMembers.php?name=" + group.getName();
        ArrayList<GroupMember> members = group.getGroupMembers();
        temp = getData(url);
        for(int i = 0; i < members.size(); i++){
            url = query + "InsertGroupMember.php?id=" + members.get(i).getID() + "&name=" + group.getName()
                    +"&role=member";
            temp = getData(url);
            if(!temp.equals("insert"))
                Log.d("ERROR", "GroupMember insert fail");
        }
        GroupMember leader = group.getGroupLeader();
        url = query + "InsertGroupMember.php?id=" + leader.getID() + "&name=" + group.getName()
                +"&role=leader";
        temp = getData(url);
        if(!temp.equals("insert"))
            Log.d("ERROR", "GroupLeader insert fail");
    }
    // 특정 그룹의 기본정보(이름, 설명) 저장
    public void saveGroupInfo(String groupName, String description){
        String url = query + "SelGroup.php?name=" + groupName;
        String temp;
        if(getData(url).equals("결과 없음")){
            // insert
            url = query + "InsertGroupInfo.php?name=" + groupName + "&description=" + description;
            temp = getData(url);
            if(!temp.equals("insert"))
                Log.d("ERROR", "GroupInfo insert fail");
        }
        else{
            // update
            // name은 where에, 나머지는 set에
            url = query + "UpdateGroupInfo.php?name=" + groupName + "&description=" + description;
            temp = getData(url);
            if(!temp.equals("update"))
                Log.d("ERROR", "GroupInfo update fail");
        }
    }
    // 특정 그룹의 일정 생성 시작 날짜와 끝 날짜를 저장
    public void saveGroupSEperiod(String groupName, String startPeriod, String endPeriod){
        String start = startPeriod.replace(".", "");
        String end = startPeriod.replace(".", "");
        String url = query + "UpdateGroupSEperiod.php?name=" + groupName + "&start=" + start + "&end=" + end;
        String temp = getData(url);
        if(temp.equals("update"))
            return;
        else
            Log.d("ERROR", "SEperiod update fail");
    }

    // 특정 Group의 구성원 정보를 받아옴
    public ArrayList<GroupMember> getGroupMember(String groupName){
        ArrayList<GroupMember> temp = new ArrayList<GroupMember>();
        String url = query + "SelGroupMember.php?name=" + groupName + "&role=member";

        String result = getData(url);
        if(result.equals("결과 없음"))
            return null;
        String[] mems = result.split("<br>");
        for(int i = 0; i < mems.length; i++){
            String[] temps = mems[i].split("#");
            GroupMember tempmem = getUser(temps[0]);
            temp.add(tempmem);
        }

        return temp;
    }
    public GroupMember getGroupLeader(String groupName){
        GroupMember temp = new GroupMember();
        String url = query + "SelGroupMember.php?name=" + groupName + "&role=leader";

        String result = getData(url);
        if(result.equals("결과 없음"))
            return null;
        temp = getUser(result);

        return temp;
    }
    // 특정 그룹의 멤버 저장
    public void saveGroupMembers(String groupName, String uesrID, ArrayList<String> members){
        String url = query + "DelGroupsMembers.php?name=" + groupName;
        String temp = getData(url);
        for(int i = 0; i < members.size(); i++){
            url = query + "InsertGroupMember.php?id=" + members.get(i) + "&name=" + groupName
                    +"&role=member";
            temp = getData(url);
            if(!temp.equals("insert"))
                Log.d("ERROR", "GroupMember insert fail");
        }
        url = query + "InsertGroupMember.php?id=" + uesrID + "&name=" + groupName
                +"&role=leader";
        temp = getData(url);
        if(!temp.equals("insert"))
            Log.d("ERROR", "GroupLeader insert fail");
    }

    // 특정 User의 특정 개인 일정을 받아옴
    public PersonalSchedule getOnePersonalSchedule(String userID, String date, String startTime){
        PersonalSchedule temp = new PersonalSchedule();
        String url = query + "SelOnePersonalSch.php?id=" + userID + "&date=" + date + "&start=" + startTime;

        String result = getData(url);
        if(result.equals("결과 없음"))
            return null;
        String[] temps = result.split("#");
        String Date = temps[0].replace("-", ".");
        temp.setDate(Date);
        String start = temps[1].substring(0,5);
        temp.setStartTime(start);
        String end = temps[2].substring(0,5);
        temp.setEndTime(end);
        temp.setName(temps[3]);
        temp.setDescription(temps[4]);
        temp.setLocation(temps[5]);
        temp.setPriority(Integer.parseInt(temps[6]));

        return temp;
    }
    // 특정 User의 모든 개인 일정을 받아옴
    public ArrayList<PersonalSchedule> getPersonalSchedule(String userID){
        ArrayList<PersonalSchedule> temp = new ArrayList<PersonalSchedule>();
        String url = query + "SelPersonalSch.php?id=" + userID;

        String result = getData(url);
        if(result.equals("결과 없음"))
            return null;
        String[] psch = result.split("<br>");
        for(int i = 0; i < psch.length; i++){
            String[] temps = psch[i].split("#");
            PersonalSchedule tempps = new PersonalSchedule();
            String date = temps[0].replace("-", ".");
            tempps.setDate(date);
            String start = temps[1].substring(0,5);
            tempps.setStartTime(start);
            String end = temps[2].substring(0,5);
            tempps.setEndTime(end);
            tempps.setName(temps[3]);
            tempps.setDescription(temps[4]);
            tempps.setLocation(temps[5]);
            tempps.setPriority(Integer.parseInt(temps[6]));
            temp.add(tempps);
        }

        return temp;
    }
    // 특정 User의 특정 개인 일정을 저장함
    public void saveOnePersonalSchedule(String userID, PersonalSchedule psch){
        String date = psch.getDate().replace(".", "");
        String start = psch.getStartTime().replace(":", "").replace(" ","") + "00";
        String end = psch.getEndTime().replace(":", "").replace(" ","") + "00";
        String url = query + "InsertPersonalSch.php?id=" + userID + "&date=" + date + "&start=" + start
                + "&end=" + end + "&name=" + psch.getName() + "&descrip=" + psch.getDescription()
                + "&loc=" + psch.getLocation() + "&prior=" + Integer.toString(psch.getPriority());
        String temp = getData(url);
        if(temp.equals("insert"))
            return;
        else
            Log.d("ERROR", "PSCH insert fail");
    }
    // 특정 User의 모든 개인 일정을 저장함
    public void savePersonalSchedule(GroupMember user, ArrayList<PersonalSchedule> pschs){
        String userID = user.getID();
        String url = query + "DelAllPersonalSch.php?id=" + userID;
        String temp = getData(url);

        for(int i = 0; i < pschs.size(); i++){
            PersonalSchedule tempps = pschs.get(i);
            String date = tempps.getDate().replace(".", "");
            String start = tempps.getStartTime().replace(":", "").replace(" ","") + "00";
            String end = tempps.getEndTime().replace(":", "").replace(" ","") + "00";
            url = query + "InsertPersonalSch.php?id=" + userID + "&date=" + date + "&start=" + start
                    + "&end=" + end + "&name=" + tempps.getName() + "&descrip=" + tempps.getDescription()
                    + "&loc=" + tempps.getLocation() + "&prior=" + Integer.toString(tempps.getPriority());
            temp = getData(url);
            if(!temp.equals("insert"))
                Log.d("ERROR", "PSCH insert fail");
        }
    }

    // 특정 그룹의 모든 일정을 받아옴
    public ArrayList<GroupSchedule> getGroupSchedule(String groupName){
        ArrayList<GroupSchedule> temp = new ArrayList<GroupSchedule>();
        String url = query + "SelGroupSch.php?gname=" + groupName;

        String result = getData(url);
        if(result.equals("결과 없음"))
            return null;
        String[] gsch = result.split("<br>");
        for(int i = 0; i < gsch.length; i++){
            String[] temps = gsch[i].split("#");
            GroupSchedule tempgs = new GroupSchedule();
            String Date = temps[0].replace("-", ".");
            tempgs.setDate(Date);
            String start = temps[1].substring(0,5);
            tempgs.setStartTime(start);
            String end = temps[2].substring(0,5);
            tempgs.setEndTime(end);
            tempgs.setName(temps[3]);
            tempgs.setDescription(temps[4]);
            tempgs.setLocation(temps[5]);
            if(temps.length > 6)
                tempgs.setChoicedTimeRank(Integer.parseInt(temps[6]));
            if(temps.length > 7)
                tempgs.setChoicedLocationRank(Integer.parseInt(temps[7]));
            temp.add(tempgs);
        }

        return temp;
    }
    // 특정 그룹의 모든 일정을 저장
    public void saveOneGroupSchedule(String groupName, GroupSchedule gs){
        String date = gs.getDate().replace(".", "");
        String start = gs.getStartTime().replace(":", "") + "00";
        String end = gs.getEndTime().replace(":", "") + "00";
        String url = query + "InsertGroupSch.php?gname=" + groupName + "&date=" + date + "&start=" + start
                + "&end=" + end + "&name=" + gs.getName() + "&descrip=" + gs.getDescription() + "&loc=" + gs.getLocation();
        if(gs.getChoicedTimeRank() > 0)
            url += "&trank=" + gs.getChoicedTimeRank();
        if(gs.getChoicedLocationRank() > 0)
            url += "&lrank=" + gs.getChoicedLocationRank();
        String temp = getData(url);
        Log.d("check", ""+temp);
        if(temp.equals("insert"))
            return;
        else
            Log.d("ERROR", "GSCH insert fail");
    }
    public void saveGroupSchedule(Group group, ArrayList<GroupSchedule> gschs){
        String groupName = group.getName();
        String url = query + "DelAllGroupSch.php?name=" + groupName;
        String temp = getData(url);

        String date, start, end;

        for(int i = 0; i < gschs.size(); i++){
            GroupSchedule tempgs = gschs.get(i);
            date = tempgs.getDate().replace(".", "");
            start = tempgs.getStartTime().replace(":", "") + "00";
            end = tempgs.getEndTime().replace(":", "") + "00";
            if(end.length() < 5)
                end = "0" + end;
            url = query + "InsertGroupSch.php?gname=" + groupName + "&date=" + date + "&start=" + start
                    + "&end=" + end + "&name=" + tempgs.getName() + "&descrip=" + tempgs.getDescription()
                    + "&loc=" + tempgs.getLocation();
            if(tempgs.getChoicedTimeRank() > 0)
                url += "&trank=" + tempgs.getChoicedTimeRank();
            if(tempgs.getChoicedLocationRank() > 0)
                url += "&lrank=" + tempgs.getChoicedLocationRank();
            temp = getData(url);
            Log.d("check", ""+temp);
            if(temp.equals("insert"))
                return;
            else
                Log.d("ERROR", "GSCH insert fail");
        }
    }
    // 특정 그룹의 특정 일정을 받아옴
    public GroupSchedule getOneGroupSch(String groupName, String date, String starttime){
        GroupSchedule temp = new GroupSchedule();
        String url = query + "SelOneGroupSch.php?gname=" + groupName + "&date=" + date + "&start=" + starttime;
        Log.d("check", url);
        String result = getData(url);
        if(result.equals("결과 없음"))
            return null;
        String[] temps = result.split("#");
        temp.setName(temps[0]);String Date = temps[0].replace("-", ".");
        temp.setDate(Date);
        String start = temps[1].substring(0,5);
        temp.setStartTime(start);
        String end = temps[2].substring(0,5);
        temp.setEndTime(end);
        temp.setDescription(temps[4]);
        temp.setLocation(temps[5]);
        if(temps.length > 6)
            temp.setChoicedTimeRank(Integer.parseInt(temps[6]));
        if(temps.length > 7)
            temp.setChoicedLocationRank(Integer.parseInt(temps[7]));
        return temp;
    }

    // 특정 그룹 일정의 특정 피드(user feed) 받아옴
    public GroupSchedule.Feed getMyFeed(String groupName, String date, String startTime, String userID){
        String Date = date.replace(".","");
        String start = startTime.replace(":","").replace(" ", "") + "00";
        String url = query + "SelMyFeed.php?name=" + groupName + "&date=" + Date + "&start=" + start + "&feedmem=" + userID;

        String result = getData(url);
        if(result.equals("결과 없음"))
            return null;
        String[] temps = result.split("#");
        GroupSchedule.Feed feed = new GroupSchedule.Feed(temps[0], temps[1]);
        return feed;
    }
    // 특정 그룹 일정의 모든 피드 받아옴
    public ArrayList<GroupSchedule.Feed> getFeeds(String groupName, String date, String startTime){
        ArrayList<GroupSchedule.Feed> temp = new ArrayList<GroupSchedule.Feed>();
        String Date = date.replace(".","");
        String start = startTime.replace(":","").replace(" ", "") + "00";
        String url = query + "SelFeeds.php?name=" + groupName + "&date=" + Date
                + "&start=" + start;

        String result = getData(url);
        if(result.equals("결과 없음"))
            return null;
        String[] feeds = result.split("<br>");
        for(int i = 0; i < feeds.length; i++){
            // temps[0] : feedmem, temps[1] : feed
            String[] temps = feeds[i].split("#");
            GroupSchedule.Feed tempf = new GroupSchedule.Feed(temps[0], temps[1]);
            temp.add(tempf);
        }

        return temp;
    }
    // 특정 그룹 일정의 모든 피드 저장
    public void saveFeeds(GroupSchedule gs, ArrayList<GroupSchedule.Feed> feeds){
        String gname = gs.getGroupName();
        String gdate = gs.getDate();
        String gstart = gs.getStartTime();
        String url = query + "DelAllFeeds.php?gname=" + gname + "&date=" + gdate + "&start=" + gstart;
        String temp = getData(url);

        for(int i = 0; i < feeds.size(); i++){
            GroupSchedule.Feed tempfs = feeds.get(i);
            url = query + "InsertFeed.php?gname=" + gname + "&date=" + gdate + "&start=" + gstart
                    + "&writer=" + tempfs.writer + "&feed=" + tempfs.Feed;

            temp = getData(url);
            if(temp.equals("insert"))
                return;
            else
                Log.d("ERROR", "Feeds insert fail");
        }
    }
    // 특정 그룹 일정의 특정 피드 저장
    public void saveFeed(String groupName, String date, String startTime, String feedmem, String feed){
        String Date = date.replace(".","");
        String start = startTime.replace(":","").replace(" ", "") + "00";

        String url = query + "SelFeeds.php?name=" + groupName + "&date=" + Date + "&start=" + start;
        String temp;
        if(getData(url).equals("결과 없음")){
            // insert
            url = query + "InsertOneFeed.php?name=" + groupName + "&date=" + Date + "&start=" + start + "&feedmem=" + feedmem + "&feed=" + feed;
            temp = getData(url);
            if(!temp.equals("insert"))
                Log.d("ERROR", "Feed insert fail");
        }
        else{
            // update
            // name은 where에, 나머지는 set에
            url = query + "UpdateOneFeed.php?name=" + groupName + "&date=" + Date + "&start=" + start + "&feedmem=" + feedmem + "&feed=" + feed;
            temp = getData(url);
            if(!temp.equals("update"))
                Log.d("ERROR", "Feed update fail");
        }
        Log.d("check", "" + temp);
    }

    // 특정 그룹의 이미 만들어진 timetable이 있는지 확인
    public boolean findTT(String groupName){
        String url = query + "FindTT.php?gname=" + groupName;

        String result = getData(url);
        if(result.equals("결과 없음")) // 만들어진 time table 없음
            return false;
        else
            return true;
    }

    // 특정 그룹의 시간대 가중치 받아옴
    public double[][] getTimeTable(String groupName){
        double[][] timetable = new double[7][96];
        String url = query + "SelTT.php?gname=" + groupName;

        String result = getData(url);
        if(result.equals("결과 없음"))
            return null;
        String[] temp = result.split("<br>");
        int day, time;
        for (int i = 0; i < temp.length; i++){
            String[] temps = temp[i].split("#");
            // temptt day 판별 > time, value 값 넣기
            // temps[0] : day
            // temps[1] : time index
            // temps[2] : value
            switch (temps[0]){
                case "mon" : day = 0; break;
                case "tue" : day = 1; break;
                case "wed" : day = 2; break;
                case "thu" : day = 3; break;
                case "fri" : day = 4; break;
                case "sat" : day = 5; break;
                case "sun" : day = 6; break;
                default: day = -1; break;
            }
            time = Integer.parseInt(temps[1]);
            timetable[day][time] = Double.parseDouble(temps[2]);
        }

        return timetable;
    }
    // 특정 그룹의 timetable 저장
    public void saveTimeTable(String groupName, double[][] timetable){
        // index > day 변환, time, value랑 같이 저장
        String url = query + "FindTT.php?gname=" + groupName;

        String result = getData(url);
        String day;
        String suburl;
        if(result.equals("결과 없음")){
            url = query + "InsertTT.php?gname=" + groupName;
        }
        else{
            url = query + "UpdateTT.php?gname=" + groupName;
        }
        Log.d("check", "" + url);
        int count = 0;
        for(int i = 0; i < 7; i++){
            switch (i){
                case 0 : day = "mon"; break;
                case 1 : day = "tue"; break;
                case 2 : day = "wed"; break;
                case 3 : day = "thu"; break;
                case 4 : day = "fri"; break;
                case 5 : day = "sat"; break;
                case 6 : day = "sun"; break;
                default: day = ""; break;
            }
            Log.d("check", "" + day);
            for(int j = 0; j < 96; j++){
                suburl = url + "&day=" + day + "&time=" + j + "&value=" + timetable[i][j];
                result = getData(suburl);
                if(!result.equals("insert") || !result.equals("update")){
                    Log.d("check", "" + result);
                }
                count++;
            }
        }
    }
    // 특정 그룹의 특정 시간대 가중치 정보 저장
    public void saveSpecificTimeTable(Group group, String day, String time, int value){
        // update
        String url = query + "DelTT.php?gname=" + group.getName() + "&day=" + day + "&time=" + time;
        String temp = getData(url);

        url = query + "InsertTT.php?gname=" + group.getName() + "&day=" + day + "&time=" + time + "&value=" + value;
        temp = getData(url);
        if(temp.equals("insert"))
            return;
        else
            Log.d("ERROR", "TimeTable insert fail");
    }


    // 특정 그룹의 Recommended 받아옴(필요?)
    // 특정 그룹의 Recommended 저장

    // 특정 그룹의 Recommending 찾기
    public boolean FindRecommending(String groupName){
        String url = query + "FindTimeRecing.php?gname=" + groupName;
        String result = getData(url);
        if(result.equals("결과 없음")) // 추천 중인 일정 없음
            return false;
        else // 추천 중인 일정 있음
            return true;
    }
    public String FindRecommendingName(String groupName){
        String url = query + "SelReingName.php?gname=" + groupName;
        String result = getData(url);
        return result;
    }
    public boolean FindRecommendingTime(String groupName){
        String url = query + "FindRecingTime.php?gname=" + groupName;
        String result = getData(url).replace("<br>", "");
        Log.d("check", "" + result);
        if(result.equals("생성되지 않음")) // 추천 중인 시간대 없음
            return false;
        else // 추천 중인 일정 있음
            return true;
    }
    public boolean FindRecommendingLoc(String groupName){
        String url = query + "FindRecingLoc.php?gname=" + groupName;
        String result = getData(url);
        if(result.equals("생성되지 않음")) // 추천 중인 시간대 없음
            return false;
        else // 추천 중인 일정 있음
            return true;
    }
    public int getExpectTime(String groupName){
        String url = query + "getExpectTime.php?gname=" + groupName;
        String result = getData(url).replace("<br>", "");
        int expect = Integer.parseInt(result);
        return expect;
    }
    // 특정 그룹의 Recommending 받아옴
    public String[] getTimeRecommending(String groupName, String userID){
        String[] rt = new String[5];
        String url = query + "SelTimeRecing.php?gname=" + groupName + "&id=" + userID;

        String result = getData(url);
        if(result.equals("결과 없음"))
            return null;
        String[] temps = result.split("<br>");
        String rtt;
        int rank;
        String Date, Start, End;
        for(int i = 0; i < 5; i++){
            String[] tempss = temps[i].split("#");
            rank = Integer.parseInt(tempss[0])-1;
            Date = tempss[1].replace("-", ".");
            Start = tempss[2].substring(0,5);
            End = tempss[3].substring(0,5);
            rtt = Date + " " + Start + " ~ " + Date + " " + End;
            rt[rank] = rtt;
        }

        return rt;
    }
    // 특정 그룹의 Recommending 초기 저장
    public void saveAdditionRecommending(String groupName, String userID, String name, int expectedTime){
        String url = query + "InsertAddRing.php?gname=" + groupName + "&id=" + userID + "&name=" + name + "&time=" + expectedTime;
        String suburl;
        String temp;
        for(int i = 1; i <= 5; i++){
            suburl = url + "&rank=" + i;
            temp = getData(suburl);
            if(!temp.equals("insert"))
                Log.d("check", "" + temp);
        }
        Log.d("check", "Insert AddRing :" + expectedTime);
    }
    // 특정 그룹의 Recommending(time) 저장
    public void saveTimeRecommending(String groupName, String userID, int rank, String date, String starTime, String endTime){
        String name;
        int expectTime;
        String url = query + "SelNameExpect.php?gname=" + groupName + "&id=" + userID + "&rank=" + rank;
        String result = getData(url);
        result.replace("<br>", "");
        String[] temp = result.split("#");
        expectTime = Integer.parseInt(temp[0]);
        name = temp[1];
        Log.d("check", "" + expectTime);

        url = query + "DelTimeRankRecomming.php?name=" + groupName + "&id=" + userID + "&rank=" + rank;
        result = getData(url);

        String Date = date.replace(".", "");
        String start = starTime.replace(":", "").replace(" ","") + "00";
        String end = endTime.replace(":", "").replace(" ","") + "00";
        Log.d("check", "start/Start : " + starTime + "/" + start);
        Log.d("check", "end/End : " + endTime + "/" + end);
        url = query + "InsertTimeRing.php?gname=" + groupName + "&id=" + userID + "&rank=" + rank + "&date=" + Date
                + "&start=" + start + "&end=" + end + "&name=" + name + "&time=" + expectTime;

        result = getData(url);
        if(result.equals("insert"))
            return;
        else
            Log.d("ERROR", "TimeRecing insert fail ");
    }
    public void saveTimeChoiceRecommending(String groupName, String userID, int rank){
        String url = query + "UpdateTimeChoiceRecing.php?gname=" + groupName + "&id=" + userID + "&rank=" + rank;
        String temp = getData(url);
        if(temp.equals("update"))
            return;
        else
            Log.d("ERROR", "TimeChoice update fail");
    }
    public void saveTimeChoice(String groupName, String userID, int rank){
        String url = query + "InsertTimeSelect.php?gname=" + groupName + "&id=" + userID + "&rank=" + rank;
        String result = getData(url);
        if(result.equals("insert"))
            return;
        else
            Log.d("ERROR", "Choicetime insert fail");
    }
    // 특정 그룹의 Recommending(lco) 저장
    public void saveLocRecommending(String groupName, String userID, int rank, String location){
        String url = query + "InsertLocRecom.php?gname=" + groupName + "&id=" + userID + "&rank=" + rank + "&loc=" + location;
        String result = getData(url);
        if(result.equals("insert"))
            return;
        else
            Log.d("ERROR", "Loc insert fail");
    }
    public void saveLocChoiceRecommending(String groupName, String userID, int rank){
        String url = query + "UpdateLocChoiceRecing.php?gname=" + groupName + "&id=" + userID + "&rank=" + rank;
        String temp = getData(url);
        if(temp.equals("update"))
            return;
        else
            Log.d("ERROR", "LocChoice update fail");
    }

    public void DelRecommending(String groupName){
        String url = query + "DelTimeRecomming.php?gname=" + groupName;
        String result = getData(url);
        url = query + "DelLocRecomming.php?gname=" + groupName;
        result = getData(url);
    }

    public void delGroup(String groupName){
        String url = query + "DelGroupInfo.php?name=" + groupName;
        String temp = getData(url);
        Log.d("check", "DelGroupInfo : " + temp);
        url = query + "DelGroupsMembers.php?name=" + groupName;
        temp = getData(url);
        Log.d("check", "DelGroupsMembers : " + temp);

        url = query + "DelGroupSch.php?name=" + groupName;
        temp = getData(url);
        url = query + "DelGroupFeed.php?name=" + groupName;
        temp = getData(url);
        url = query + "DelGroupRecommed.php?name=" + groupName;
        temp = getData(url);
        url = query + "DelGroupRecomming.php?name=" + groupName;
        temp = getData(url);
        url = query + "DelGroupTT.php?name=" + groupName;
    }

    public void delOnePsch(String userID, String date, String startTime){
        String url = query + "DelOnePersonalSch.php?id=" + userID + "&date=" + date + "&start=" + startTime;
        String result = getData(url);
    }

    public void delOneFeed(String groupName, String date, String startTime, String feedmem){
        String Date = date.replace(".","");
        String start = startTime.replace(":","").replace(" ", "") + "00";

        String url = query + "DelOneFeed.php?name=" + groupName + "&date=" + Date + "&start=" + start + "&feedmem=" + feedmem;
        String result = getData(url);
    }
}
