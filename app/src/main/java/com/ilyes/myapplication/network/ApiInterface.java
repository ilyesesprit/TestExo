package com.ilyes.myapplication.network;

import com.ilyes.myapplication.model.CityWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("data/2.5/weather")
    Call<CityWeather> getWeatherThatMoment(@Query("lat") String lat,
                                           @Query("lon") String lon,
                                           @Query("appid") String appid,
                                           @Query("units") String units,
                                           @Query("lang") String lang);

}
