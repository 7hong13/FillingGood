package com.example.fillinggood.Entity;

public class TimeTable {
    private String timeName;
    private int[] timeArray;

    private class Hours{
        String timeName;
        int timePriority;
    }

    private Hours[] Hours;

    public int getHoursLength(){
        return Hours.length;
    }
    // 14:50 이런 식으로 시간을 입력하면, 해당하는 index를 찾아, 그 값을 반환하는 함수
    public int getTimeIndex(String timename){

        return 0;
    }
    public String getTimeName(int idx){
        return Hours[idx].timeName;
    }
    public int getTimePriority(int idx){
        return Hours[idx].timePriority;
    }
}
