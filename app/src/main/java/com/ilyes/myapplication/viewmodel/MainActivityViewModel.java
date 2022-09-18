package com.ilyes.myapplication.viewmodel;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilyes.myapplication.R;
import com.ilyes.myapplication.model.CityWeather;
import com.ilyes.myapplication.repository.CityWeatherRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private CityWeatherRepository cityWeatherRepository;
    private MutableLiveData<List<CityWeather>> mCitiesWeather;
    String[] citiesList;


    public MainActivityViewModel() {
    }

    public void setCitiesList(String[] citiesList) {
        this.citiesList = citiesList;
        this.cityWeatherRepository = new CityWeatherRepository(this.citiesList);
    }

    public LiveData<List<CityWeather>> getCitiesWeather(Context mContext) {

        if (mCitiesWeather == null) {
            mCitiesWeather = new MutableLiveData<>();
            mCitiesWeather = cityWeatherRepository.requestCitiesWeather(mCitiesWeather,mContext);

        }
        return this.mCitiesWeather;

    }

    public void resetCitiesWeather(LifecycleOwner lifecycleOwner) {
       if (cityWeatherRepository!=null && cityWeatherRepository.getTimer()!=null) cityWeatherRepository.getTimer().cancel();
       if(mCitiesWeather !=null && mCitiesWeather.hasObservers()) mCitiesWeather.removeObservers(lifecycleOwner);
        mCitiesWeather = new MutableLiveData<>();
    }


    public static final long START_TIME_IN_MILLIS = 60000;//60000
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mEndTime;

    public boolean getTimerRunning() {
        return mTimerRunning;
    }

    public long getTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mTimerRunning = true;
    }

    public void restoreTimer(boolean timerRunning, long timeLeftInMillis, long endTime) {
        this.mTimerRunning = timerRunning;
        this.mTimeLeftInMillis = timeLeftInMillis;
        this.mEndTime = endTime;
    }

    public void finish() {
        mTimerRunning = false;
    }

    public void restart() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;

    }

    public void updateCounter(long millisUntilFinished) {
        mTimeLeftInMillis = millisUntilFinished;
    }

    public boolean remainingTime() {
        return mTimeLeftInMillis < START_TIME_IN_MILLIS;
    }




}
