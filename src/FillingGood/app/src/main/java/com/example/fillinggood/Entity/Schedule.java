package com.example.fillinggood.Entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Schedule {
    protected String groupName;
    protected String name;
    protected String location;
    protected String description;
    protected String date;
    protected String startTime;
    protected String endTime;


    public String getName() { return name;}
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    // [시간추천]
    // 날짜 요일 계산 함수 => 요일 계산할 때에 사용
    public static int getDateDay(String date){
        int day = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date nDate = null;
        try {
            nDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = 6;  // 일
                break;
            case 2:
                day = 0;  // 월
                break;
            case 3:
                day = 1;  // 화
                break;
            case 4:
                day = 2;  // 수
                break;
            case 5:
                day = 3;  // 목
                break;
            case 6:
                day = 4;  // 금
                break;
            case 7:
                day = 5;  // 토
                break;
        }
        return day;
    }

    // new schedule(this) 과 기존 schedule을 비교
    public boolean isOverlap_add(Schedule PS){
        String temp;
        boolean overlap = true;
        long newPS_startTime, newPS_endTime, PS_startTime, PS_endTime;

        temp = this.getDate() + this.getStartTime();
        temp = temp.replace(".", "");
        temp = temp.replace(":", "");
        temp = temp.replace(" ", "");
        newPS_startTime = Long.parseLong(temp);

        temp = this.getDate() + this.getEndTime();
        temp = temp.replace(".", "");
        temp = temp.replace(":", "");
        temp = temp.replace(" ", "");
        newPS_endTime = Long.parseLong(temp);

        temp = PS.getDate() + PS.getStartTime();
        temp = temp.replace(".", "");
        temp = temp.replace(":", "");
        temp = temp.replace(" ", "");
        PS_startTime = Long.parseLong(temp);

        temp = PS.getDate() + PS.getEndTime();
        temp = temp.replace(".", "");
        temp = temp.replace(":", "");
        temp = temp.replace(" ", "");
        PS_endTime = Long.parseLong(temp);

        if(newPS_endTime < PS_startTime) {
            overlap = false;
        } else if(PS_endTime < newPS_startTime) {
            overlap = false;
        }
        return overlap;
    }

    public boolean isOverlap_mod(Schedule previous, Schedule PS){
        String temp;
        boolean overlap = true;
        long from_startTime, from_endTime, to_startTime, to_endTime, PS_startTime, PS_endTime;

        // from은 변경되기 전의 일정입니다
        temp = previous.getDate() + previous.getStartTime();
        temp = temp.replace(".", "");
        temp = temp.replace(":", "");
        temp = temp.replace(" ", "");
        from_startTime = Long.parseLong(temp);

        temp = previous.getDate() + previous.getEndTime();
        temp = temp.replace(".", "");
        temp = temp.replace(":", "");
        temp = temp.replace(" ", "");
        from_endTime = Long.parseLong(temp);

        // to는 변경된 후의 일정입니다
        temp = this.getDate() + this.getStartTime();
        temp = temp.replace(".", "");
        temp = temp.replace(":", "");
        temp = temp.replace(" ", "");
        to_startTime = Long.parseLong(temp);

        temp = this.getDate() + this.getEndTime();
        temp = temp.replace(".", "");
        temp = temp.replace(":", "");
        temp = temp.replace(" ", "");
        to_endTime = Long.parseLong(temp);

        // PS는 시간대가 겹치는지 비교 대상이 되는 일정입니다
        // 현재 DB에 있는 모든 일정과 겹치는지 비교하는 것이라면 언젠가 PS에 from이 포함되게 됩니다
        temp = PS.getDate() + PS.getStartTime();
        temp = temp.replace(".", "");
        temp = temp.replace(":", "");
        temp = temp.replace(" ", "");
        PS_startTime = Long.parseLong(temp);

        temp = PS.getDate() + PS.getEndTime();
        temp = temp.replace(".", "");
        temp = temp.replace(":", "");
        temp = temp.replace(" ", "");
        PS_endTime = Long.parseLong(temp);

        // 만약 from이라는 일정을 to라는 일정으로 변경하려는데,
        // 비교하려는 PS라는 일정이 from과 같다면, from 일정은 수정 뒤 사라질 일정이므로 false 리
        if(from_startTime == PS_startTime && from_endTime == PS_endTime) {
            return false;
        }

        if(to_endTime < PS_startTime) {
            overlap = false;
        } else if(PS_endTime < to_startTime) {
            overlap = false;
        }

        return overlap;
    }
}
