package com.wisewolf.midhilaarts.ui.myCourse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyCourseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyCourseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MyCourse fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}