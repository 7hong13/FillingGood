package com.example.fillinggood.Boundary.group_calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//그냥 무시해도 되는 class입니다
public class GroupListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GroupListViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}