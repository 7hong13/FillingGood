package com.example.fillinggood.Boundary.personal_calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//그냥 무시해도 되는 class 입니다
public class PersonalCalendarViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PersonalCalendarViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}