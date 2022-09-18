package com.ilyes.myapplication.repository;

import static com.ilyes.myapplication.constants.AppConstant.APPID;
import static com.ilyes.myapplication.constants.AppConstant.UNITS;
import static com.ilyes.myapplication.constants.AppConstant.LANG;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.ilyes.myapplication.R;
import com.ilyes.myapplication.model.ApiError;
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
                    String[] latlong = citiesList[indexVille].split(":");
                    apiService.getWeatherThatMoment(latlong[0], latlong[1], APPID, UNITS, LANG)
                            .enqueue(new Callback<CityWeather>() {
                                @Override
                                public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        List<CityWeather> cityWeatherList = mutableLiveData.getValue();
                                        if (cityWeatherList == null) {
                                            cityWeatherList = new ArrayList<>();
                                        }
                                        CityWeather cityWeather = response.body();
                                        if (cityWeather != null
                                                && cityWeather.name != null
                                                && cityWeather.weather != null) {
                                            cityWeatherList.add(response.body());
                                            mutableLiveData.postValue(cityWeatherList);
                                            indexVille++;
                                        }
                                    } else {
                                        List<CityWeather> cityWeatherList = new ArrayList<>();
                                        CityWeather cWError = new ApiError(response.raw().code(), response.raw().message());
                                        cityWeatherList.add(cWError);
                                        mutableLiveData.postValue(cityWeatherList);

                                        getTimer().cancel();

                                    }

                                }

                                @Override
                                public void onFailure(Call<CityWeather> call, Throwable t) {
                                    if (t instanceof NetworkConnectionInterceptor.NoConnectivityException) {
                                        List<CityWeather> cityWeatherList = new ArrayList<>();
                                        CityWeather cWError = new ApiError(404, t.getMessage());
                                        cityWeatherList.add(cWError);
                                        mutableLiveData.postValue(cityWeatherList);
                                        getTimer().cancel();
                                    }


                                }
                            });


                } else
                    getTimer().cancel();
                //Appele le service plusieurs fois
                //Toutes les 10 secondes

            }
        }, 0, 10000);


        return mutableLiveData;
    }

    public Timer getTimer() {
        return timer;
    }
}
