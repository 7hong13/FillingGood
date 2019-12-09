package com.example.fillinggood.Entity;

import com.example.fillinggood.Boundary.DBboundary.DBmanager;
import com.example.fillinggood.Control.RecommendationController;

public class TimeTable {
    private double[][] time_rating;
    RecommendationController recommendationController;

    public TimeTable(String groupName) {
        this.time_rating = new double[7][96];  // 시간대별 가중치: 일주일 7, 하루 4*24
        if(DBmanager.getInstance().findTT(groupName) != false) // 해당 그룹의 가중치 정보가 DB에 담겨있지 않으면
            TimeTableInit(groupName);  // 초기화
        else // 해당 그룹의 가중치 정보가 DB에 담겨져 있으면
            this.time_rating = DBmanager.getInstance().getTimeRating(groupName);
    }

    public void TimeTableInit(String groupName){  // 시간 가중치 default 값으로 초기화하는 함수
        for(int i = 0; i < this.time_rating.length; i++){
            for(int k = 0; k < this.time_rating[0].length; k++) {
                if (i==5 || i==6){  // 주말 가중치 1.2
                    if (k <= 28 || k > 88)  // 오전 7시 이전, 밤 10시 이후
                        this.time_rating[i][k] = 1.2 * 1.2;
                    else
                        this.time_rating[i][k] = 1.2;
                }
                else{  // 평일 가중치 1.0
                    if (k <= 28 || k > 88)  // 오전 7시 이전, 밤 10시 이후
                        this.time_rating[i][k] = 1.2;
                    else
                        this.time_rating[i][k] = 1.0;
                }
            }
        }
        DBmanager.getInstance().saveInitTimeTable();
    }

    // 2019.11.20 14:50 ~ 2019.11.20 18:50
    // 이런 식으로 추천 결과 시간을 입력하면, 해당하는 index 범위를 찾아, 그 값을 반환하는 함수
    public double[][] getFeedTimeTable(String timename, double[][] timetb){  // (추천 시간, 가중치 테이블)
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

    public double[][] getTimeTable(){
        return time_rating;
    }
}
