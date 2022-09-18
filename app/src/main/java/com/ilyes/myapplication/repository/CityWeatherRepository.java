package com.ilyes.myapplication.repository;

import static com.ilyes.myapplication.constants.AppConstant.APPID;
import static com.ilyes.myapplication.constants.AppConstant.UNITS;
import static com.ilyes.myapplication.constants.AppConstant.LANG;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.ilyes.myapplication.R;
import com.ilyes.myapplication.model.CityWeather;
import com.ilyes.myapplication.network.ApiInterface;
import com.ilyes.myapplication.network.NetworkConnectionInterceptor;
import com.ilyes.myapplication.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityWeatherRepository {

    private final String TAG = getClass().getSimpleName();
    Timer timer;
    private int indexVille = 0;
    String[] citiesList;

    public CityWeatherRepository(String[] citiesLis) {
        this.citiesList = citiesLis;

    }


    public MutableLiveData<List<CityWeather>> requestCitiesWeather(MutableLiveData<List<CityWeather>> mutableLiveData, Context mContext) {

        ApiInterface apiService = RetrofitInstance.getRetrofit(mContext).create(ApiInterface.class);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (indexVille < 5) {
                    String[] latlong = citiesList[indexVille++].split(":");
                    System.out.println("lat : "+latlong[0]+" long:"+latlong[1]);
                    apiService.getWeatherThatMoment(latlong[0], latlong[1], APPID, UNITS,LANG)
                            .enqueue(new Callback<CityWeather>() {
                                @Override
                                public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        List<CityWeather> cityWeatherList = mutableLiveData.getValue();
                                        if (cityWeatherList == null) {
                                            cityWeatherList = new ArrayList<>();
                                        }
                                        CityWeather cityWeather = response.body();
                                        System.out.println(cityWeather);
                                        if (cityWeather != null
                                                && cityWeather.name != null
                                                && cityWeather.weather != null) {
                                            cityWeatherList.add(response.body());
                                            mutableLiveData.postValue(cityWeatherList);
                                        }
                                    }else
                                        getTimer().cancel();

                                }

                                @Override
                                public void onFailure(Call<CityWeather> call, Throwable t) {

                                    if(t instanceof NetworkConnectionInterceptor.NoConnectivityException) {
                                        System.out.println("NoConnectivityException " + t.getMessage());
                                        getTimer().cancel();
                                    }else {

                                        System.out.println("sdkfjskljf " + t.getMessage());

                                    }


                                }
                            });


                } else
                    getTimer().cancel();
                //Call Service Multiple times
                //At EVery 10 seconds

            }
        }, 0, 10000);


        return mutableLiveData;
    }

    public Timer getTimer() {
        return timer;
    }
}
