package com.example.fillinggood.Entity;

import android.util.Log;

import com.example.fillinggood.Boundary.DBboundary.DBmanager;
import com.example.fillinggood.Boundary.home.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class GroupSchedule extends Schedule {
    // 필드
    private int choicedTimeRank;
    private int choicedLocationRank;
    private ArrayList<Feed> Feeds;

    // 생성자
    public GroupSchedule() {};
    public GroupSchedule(String groupName, String name, String location, String description, String date,
                         String startTime, String endTime, int choicedTimeRank, int choicedLocationRank) {
        this.groupName = groupName;
        this.name = name;
        this.location = location;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.choicedTimeRank = choicedTimeRank;
        this.choicedLocationRank = choicedLocationRank;
        this.Feeds = DBmanager.getInstance().getFeeds(groupName, date, startTime);
    }

    // GETTERs & SETTERs
    public int getChoicedTimeRank() {return choicedTimeRank;}
    public void setChoicedTimeRank(int choicedTimeRank){this.choicedTimeRank = choicedTimeRank;}

    public int getChoicedLocationRank() {return choicedLocationRank;}
    public void setChoicedLocationRank(int choicedLocationRank){this.choicedLocationRank = choicedLocationRank;}

    public ArrayList<Feed> getFeeds(){return this.Feeds;}

    public static void additionsaveRecommending(String groupName, String name, int ExpectTime){
        ArrayList<GroupMember> groupMembers = DBmanager.getInstance().getGroupMember(groupName);
        GroupMember leader = DBmanager.getInstance().getGroupLeader(groupName);
        DBmanager.getInstance().saveAdditionRecommending(groupName, leader.getID(), name, ExpectTime);
        for(int i = 0; i < groupMembers.size(); i++){
            DBmanager.getInstance().saveAdditionRecommending(groupName, groupMembers.get(i).getID(), name, ExpectTime);
        }
    }
    public static void saveTimeChoiceRecommending(String groupName, String userID, int rank){
        DBmanager.getInstance().saveTimeChoiceRecommending(groupName, userID, rank);
    }
    public static void saveLocChoiceRecommending(String groupName, String userID, int rank){
        DBmanager.getInstance().saveLocChoiceRecommending(groupName, userID, rank);
    }
    public static String[] getRT(String groupName){
        String[] rt = DBmanager.getInstance().getTimeRecommending(groupName, MainActivity.User.getID());
        return rt;
    }

    // [시간추천]
    // 그룹 일정 생성
    public static double[][] createGroupSch(Group g, String startDate){
        // 일주일 단위 모임 일정 생성
        double[][] GS = new double[7][96];
        Date day = null;
        SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");

        // 시작일
        try{
            day = date.parse(startDate);
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        // 모임 일정 가중치 전체 1로 초기화
        for (int i = 0; i < GS.length; i++)
            for (int j = 0; j < GS[i].length; j++)
                GS[i][j] = 1.0;

        ArrayList<GroupMember> members = DBmanager.getInstance().getGroupMember(g.getName());
        String leaderID = DBmanager.getInstance().getGroupLeader(g.getName()).getID();
        members.add(DBmanager.getInstance().getUser(leaderID));
        // 그룹 멤버 개개인 일정 불러오기
        for (int i = 0; i < members.size(); i++) {
            GroupMember m = members.get(i);

            // 개인 일정 시간 각각 분으로 분할 및 날짜 계산
            SimpleDateFormat f = new SimpleDateFormat("HH:mm", Locale.KOREA);
            Log.d("check", "" + members.get(i).getID());
            ArrayList<PersonalSchedule> PS = DBmanager.getInstance().getPersonalSchedule(members.get(i).getID());
            if(PS == null)
                PS = new ArrayList<>();

            Date d1 = null;
            Date d2 = null;
            long diff;  // 날짜 차이 계산
            long diffDays;

            for (int ii = 0; ii < PS.size(); ii++) {
                PersonalSchedule temp = PS.get(ii);

                diff = 0;
                try {
                    diff = day.getTime() - date.parse(temp.getDate()).getTime();  // starttime과 개인일정 date 차이
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                diffDays = diff / (24 * 60 * 60 * 1000);  // 두 날짜의 차이를 일수로 나타냄

                // startdate와 개인 일정의 date가 같은 week가 아닌 경우 continue
                if (diffDays > Schedule.getDateDay(startDate) || diffDays <= -7 + Schedule.getDateDay(startDate)) {
                    continue;
                }

                try {
                    d1 = f.parse(temp.getStartTime());
                    d2 = f.parse(temp.getEndTime());
                    long min = (d2.getTime() - d1.getTime()) / (1000 * 60);
                    long startmin = d1.getTime() / (1000 * 60);

                    int t = (int) Math.ceil(startmin / 15);
                    for (int j = 0; j < (int) Math.ceil(min / 15); j++) {
                        int d = Schedule.getDateDay(temp.getDate());

                        // 모임원 역할에 따른 가중치 부여
                        if (m.getID().equals(leaderID))
                            // 모임장 leader 체크
                            GS[d][t + j] += Double.valueOf(temp.getPriority()) * 1.2;
                        else
                            GS[d][t + j] += Double.valueOf(temp.getPriority()) * 1.0;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        double[][] temp = g.getGroupTimeTable();
        if(temp == null){
            g.getTimeTable(g.getName());
            temp = g.getGroupTimeTable();
        }
        // 시간대 가중치 곱하기
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 96; j++) {
                GS[i][j] *= temp[i][j];
            }
        }

        // 모임 일정 추가
        return GS;
    }
    // 일정 추천 함수 : startTime으로 월요일 날짜가 들어와 해당 주의 월-일 일주일간의 일정 추천
    public static String[] RecommendSchedule(double[][] gs, int min, String startTime, String groupName){
        double[][] schedule = gs;
        double[] temp = new double[schedule.length * schedule[0].length];

        int revised_min = (int) Math.ceil(min / 15);  // 15분 단위
        int[] recommend_index = {-1, -1, -1, -1, -1};
        String[] recommend_date = new String[5];

        double[] recommend_arr = new double[temp.length - revised_min];
        double[] recommend_temp = new double[recommend_arr.length];

        // 2차원 배열 -> 1차원으로
        int k = 0;
        for (int i = 0; i < schedule.length; i++) {
            for (int j = 0; j < schedule[i].length; j++) {
                if (Schedule.getDateDay(startTime) > i) {
                    temp[k] = 90000000.0;  // 그 주의 starttime 이전 요일에 해당하는 일정 추천 못하도록 설정
                }
                else
                    temp[k] = schedule[i][j];
                k++;
            }
        }

        // 추천 시간대 구하기
        double sum = 0;

        for (int i = 0; i < temp.length; i++) {
            sum = 0;  // 초기화

            if (i + revised_min >= temp.length)
                break;

            // 시간대 만큼 가중치 합 계산
            for (int j = i; j < i + revised_min; j++)
                sum += temp[j];

            recommend_arr[i] = sum;
        }

        // arr 복사 및 정렬
        System.arraycopy(recommend_arr, 0, recommend_temp, 0,
                recommend_arr.length);
        Arrays.sort(recommend_arr);

        // 가중치 가장 낮은 index 5개 도출
        double value = 0;
        boolean check = false;
        k = 0;
        for (int i = 0; i < recommend_arr.length; i++) {
            value = recommend_arr[i];
            for (int j = 0; j < recommend_temp.length; j++) {
                // 붙어있는 일정 추천 못하도록 하는 조건. 넣어야 하나?
                if (recommend_temp[j] == value) {
                    if (k == 0 ||
                            (k != 0 && recommend_index[k - 1] + revised_min <= j)) {
                        recommend_index[k] = j;
                        k++;
                    }
                }
                if (k >= recommend_index.length) {
                    check = true;
                    break;
                }
            }
            if (check)
                break;
        }

        // index 기반으로 구체적인 날짜, 시간 계산
        SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd");
        Calendar c = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        try {
            c.setTime(f.parse(startTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  // 해당 일이 포함된 주의 월요일로 날짜 설정

        String startdate = f.format(c.getTime()) + " 00:00";

        Date d = null;
        try {
            d = date.parse(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int day, hour, minute;

        for (int i = 0; i < recommend_index.length; i++) {
            day = recommend_index[i] / 96;
            minute = recommend_index[i] % 96;
            hour = minute / 4;
            minute = (minute % 4) * 15;

            cal.setTime(d);
            cal.add(Calendar.DATE, day);
            cal.add(Calendar.HOUR, hour);
            cal.add(Calendar.MINUTE, minute);

            cal2.setTime(cal.getTime());

            hour = min / 60;
            minute = min % 60;
            cal2.add(Calendar.HOUR, hour);
            cal2.add(Calendar.MINUTE, minute);

            recommend_date[i] = date.format(cal.getTime()) + " ~ " + date.format(cal2.getTime());
            // 2019.11.23 10:30 ~ 2019.11.24 12:00 와 같은 형식의 String이 추가됨
        }
        // 해당 string 정보를 save
        String DATE, START, END;
        ArrayList<GroupMember> members = DBmanager.getInstance().getGroupMember(groupName);
        for(int i = 0; i < recommend_date.length; i++){
            DATE = recommend_date[i].substring(0, 10);
            START = recommend_date[i].substring(11, 16);
            END = recommend_date[i].substring(30);
            for(int j = 0; j < members.size(); j++){
                DBmanager.getInstance().saveTimeRecommending(groupName, members.get(j).getID(), i+1, DATE, START, END );
            }
            DBmanager.getInstance().saveTimeRecommending(groupName, DBmanager.getInstance().getGroupLeader(groupName).getID(),i+1, DATE, START, END);
        }

        return recommend_date;
    }

    // [장소추천]
    // 피어슨 상관계수 구하는 함수
    private static double sim_pearson(HashMap<String, int[]> data, String loc1, String loc2){
        int sumX = 0; //X의 합
        int sumY = 0; //Y의 합
        double sumPowX = 0.0; //X 제곱의 합
        double sumPowY = 0.0; //Y 제곱의 합
        int sumXY = 0; //X * Y의 합
        int count = 0; //그룹 개수
        double result = 0.0; // 결과값

        for (int i = 0; i < data.get(loc1).length; i++) {
            //if (((data.get(loc1))[i] != 0) && ((data.get(loc2))[i] != 0)) {  // 같은 그룹에서 모두 갔을때만
            if(true){
                sumX += (data.get(loc1))[i];
                sumY += (data.get(loc2))[i];
                sumPowX += Math.pow((data.get(loc1))[i], 2);
                sumPowY += Math.pow((data.get(loc2))[i], 2);
                sumXY += (data.get(loc1))[i] * (data.get(loc2))[i];
                count += 1;
            }
        }

        // 피어슨 상관계수 구하는 수식
        if (count != 0 &&
                Math.sqrt((sumPowX - (Math.pow(sumX, 2) / count)) * (sumPowY - (Math.pow(sumY, 2) / count))) != 0)
            result = (sumXY - ((sumX * sumY) / count)) / Math.sqrt((sumPowX - (Math.pow(sumX, 2) / count)) *
                    (sumPowY - (Math.pow(sumY, 2) / count)));

        // t1.append(Integer.toString(count) + " " + Double.toString(result) + "\n");

        return result;
    }
    // 전체 장소와의 피어슨 상관계수 구하고 value순으로 정렬된 key List 반환
    public static ArrayList top_match(HashMap<String, int[]> data, String loc){
        HashMap<String, Double> li = new HashMap<>();
        ArrayList result = new ArrayList();

        for (String key : data.keySet()) {
            //sim_function()을 통해 상관계수를 구하고 li[]에 추가
            li.put(key, sim_pearson(data, loc, key));
        }

        Collection cols = li.values();

        ArrayList temps = new ArrayList(cols);
        Collections.sort(temps, Collections.reverseOrder());

        int count = 0;
        if (temps != null) {
            for (int i = 0; i < temps.size(); i++) {
                for(String key: li.keySet()){
                    if(li.get(key) == temps.get(i)) {
                        result.add(count, key);
                        count++;
                    }
                }
            }
        }

        return result;
    }

    public static void makeGsch(String groupName, String rt, int timerank, int locrank, String name, String loc){
        String Date = rt.substring(0,10);
        String Start = rt.substring(11, 16);
        if(rt.substring(11,12).equals(" "))
            Start = "0" + Start;
        String End = rt.substring(30);
        if(rt.substring(30,31).equals(" "))
            End = "0" + Start;
        Log.d("check", "!!!!!!!" + Start + "!!!!!!" + End);
        GroupSchedule Recomed = new GroupSchedule();
        Recomed.date = Date;
        Recomed.startTime = Start;
        Recomed.endTime = End;
        Recomed.choicedTimeRank = timerank;
        Recomed.choicedLocationRank = locrank;
        Recomed.name = name;
        Recomed.location = loc;
        DBmanager.getInstance().saveOneGroupSchedule(groupName, Recomed);
        //DBmanager.getInstance().saveGroupSchedule(DBmanager.getInstance().getGroupInfo(groupName), groupSchedules);
        // Del Recommending
        DBmanager.getInstance().DelRecommending(groupName);
    }

    // Feedback
    // Add Update Del Feeds
    public static ArrayList<GroupSchedule> getGroupSchedule(String groupName){
        ArrayList<GroupSchedule> groupSchedules = DBmanager.getInstance().getGroupSchedule(groupName);
        if(groupSchedules == null){
            return new ArrayList<>();
        }
        for(int i = 0; i < groupSchedules.size(); i++){
            GroupSchedule g = groupSchedules.get(i);
            g.Feeds = DBmanager.getInstance().getFeeds(g.groupName, g.date, g.startTime);
        }
        return groupSchedules;
    }
    public static GroupSchedule getOneGroupSchedule(String groupName, String date, String startTime){
        String Date = date.replace(".", "");
        String StartTime = startTime.replace(":", "") + "00";
        return DBmanager.getInstance().getOneGroupSch(groupName, Date, StartTime);
    }
    public static ArrayList<Feed> getFeeds(String groupName, String date, String startTime){
        ArrayList<Feed> Feeds = new ArrayList<>();
        Feeds = DBmanager.getInstance().getFeeds(groupName, date, startTime);
        return Feeds;
    }
    public static Feed getMyFeed(String groupName, String date, String startTime, String userID){
        Feed feed = new Feed();
        feed = DBmanager.getInstance().getMyFeed(groupName, date, startTime, userID);
        return feed;
    }

    // saveFeed : insert / update 모두 관할
    public void addFeed(String groupName, String writer, String feed){
        DBmanager.getInstance().saveFeed(groupName, this.date, this.startTime, writer, feed);
    }
    public void updateFeed(String groupName, String writer, String feed){
        DBmanager.getInstance().saveFeed(groupName, this.date, this.startTime, writer, feed);
    }
    public void deleteFeed(String groupName, String writer){
        DBmanager.getInstance().delOneFeed(groupName, this.date, this.startTime, writer);
    }
    // groupName 바탕으로 group 구하기.
    // group 구해서 groupMember 구하기.
    // groupMember 구해서 일정 구하기.
    // 특정 member가 작성한다는 정보가 필요함.
    public static void ReAdjustment() {

    }

    public static class Feed {
        public String writer;
        public String Feed;
        public Feed(String w, String f){
            this.writer = w;
            this.Feed = f;
        }
        public Feed(){}
    }
}
