package com.ilyes.myapplication.network;

import static com.ilyes.myapplication.constants.AppConstant.BASE_URL;

import android.app.Application;
import android.content.Context;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance extends Application {
    final String TAG = getClass().getSimpleName();
    private static Retrofit retrofit = null;
    @Override
    public void onCreate() {
        super.onCreate();
    }



    public static Retrofit getRetrofit(Context mContext) {

        if (retrofit == null) {
            okhttp3.OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new NetworkConnectionInterceptor(mContext)).build();

            retrofit = new Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
        }

        return retrofit;
    }

}
