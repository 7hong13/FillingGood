package com.example.fillinggood.Control;

import com.example.fillinggood.Entity.PersonalSchedule;

public class PersonalScheduleController {

    public static int addSchedule(String name, String location, String description, String priority,
                                   String date, String startTime, String endTime, long mScheduleID) {
        PersonalSchedule schedule = new PersonalSchedule(name, location, description, priority, date, startTime, endTime);
        return PersonalSchedule.save(mScheduleID);
    }

    public static int modifySchedule(String name, String location, String description, String priority,
                                   String date, String startTime, String endTime, long mScheduleID) {
        PersonalSchedule schedule = new PersonalSchedule(name, location, description, priority, date, startTime, endTime);
        return PersonalSchedule.modify(mScheduleID);
    }

    //read랑 delete도 적절히 채워주세요
}
