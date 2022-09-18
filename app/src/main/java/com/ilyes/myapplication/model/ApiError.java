package com.ilyes.myapplication.model;

import okhttp3.Protocol;

public class ApiError extends CityWeather {
    int code;
    String url;

    public ApiError( int code, String url) {

        this.code = code;
        this.url = url;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
