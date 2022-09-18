package com.ilyes.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.ilyes.myapplication.R;
import com.ilyes.myapplication.model.ApiError;
import com.ilyes.myapplication.network.NetworkConnectionInterceptor;
import com.ilyes.myapplication.view.adapter.CitiesWeatherListAdapter;
import com.ilyes.myapplication.databinding.ActivityMainBinding;
import com.ilyes.myapplication.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {
    private CountDownTimer mCountDownTimer;
    int percentage;


    final String TAG = getClass().getSimpleName();
    ActivityMainBinding mainBinding;
    CitiesWeatherListAdapter citiesWeatherListAdapter;
    MainActivityViewModel viewModel;
    boolean stop = false;
    NetworkConnectionInterceptor interceptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        interceptor = new NetworkConnectionInterceptor(this);

        initDataBinding();
        initViewModel();
        mainBinding.recommencer.setOnClickListener(view -> {
            mainBinding.progressBarz.setVisibility(View.VISIBLE);
            mainBinding.progressBarz.setProgressBar(0);
            mainBinding.progressBarz.setTimer();
            mainBinding.citiesWeather.setVisibility(View.GONE);
            mainBinding.recommencer.setVisibility(View.GONE);
            this.getViewModelStore().clear();
            viewModel.resetCitiesWeather(this);
            initViewModel();
        });


    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        if (interceptor.isConnected()) {
            setErrorView(false);
            viewModel.setCitiesList(getResources().getStringArray(R.array.cities));
            viewModel.getCitiesWeather(this).observe(MainActivity.this, cityWeathers -> {
                if (cityWeathers.size() == 1 && cityWeathers.get(0) instanceof ApiError) {
                    mainBinding.progressBarz.getTimer().cancel();
                    mainBinding.messageError.setText(((ApiError) cityWeathers.get(0)).getUrl());
                    viewModel.restart();
                    setErrorView(true);
                    mCountDownTimer.cancel();
                    stop = true;
                    viewModel.resetCitiesWeather(this);

                } else {
                    citiesWeatherListAdapter.setCityWeatherList(cityWeathers);
                }
            });
            if (viewModel.getTimerRunning()) {
                pauseTimer();
            } else {
                startTimer();
            }
        } else {
            mainBinding.progressBarz.getTimer().cancel();
            setErrorView(true);
        }
    }

    private void setErrorView(boolean show) {
        mainBinding.progressBarz.setVisibility(show ? View.GONE : View.VISIBLE);
        mainBinding.citiesWeather.setVisibility(View.GONE);
        mainBinding.recommencer.setVisibility(!show ? View.GONE : View.VISIBLE);
        mainBinding.messageError.setVisibility(!show ? View.GONE : View.VISIBLE);

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel.getTimerRunning()) {
            startTimer();
        }
    }

    @Override
    protected void onDestroy() {
        viewModel.finish();
        super.onDestroy();
    }

    private void startTimer() {
        viewModel.startTimer();

        mCountDownTimer = new CountDownTimer(viewModel.getTimeLeftInMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                if (!stop) {
                    viewModel.updateCounter(millisUntilFinished);
                    long finishedSeconds = MainActivityViewModel.START_TIME_IN_MILLIS - millisUntilFinished;
                    percentage = (int) (((float) finishedSeconds / (float) MainActivityViewModel.START_TIME_IN_MILLIS) * 100.0);
                    mainBinding.progressBarz.setProgressBar(percentage);
                    mainBinding.progressBarz.setPercentageText(new StringBuilder().append(percentage).append("%").toString());
                } else {
                    this.cancel();
                    stop = false;

                }

            }

            @Override
            public void onFinish() {
                mainBinding.progressBarz.setProgressBar(100);
                mainBinding.progressBarz.getTimer().cancel();
                mainBinding.progressBarz.setVisibility(View.GONE);
                mainBinding.citiesWeather.setVisibility(View.VISIBLE);
                mainBinding.recommencer.setVisibility(View.VISIBLE);
                viewModel.finish();

            }
        }
                .start();


    }

    private void pauseTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        viewModel.finish();
    }

    private void resetTimer() {
        viewModel.restart();
    }


    private void initDataBinding() {
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        mainBinding.citiesWeather.setLayoutManager(new LinearLayoutManager(this));
        citiesWeatherListAdapter = new CitiesWeatherListAdapter(this);
        mainBinding.citiesWeather.setAdapter(citiesWeatherListAdapter);
    }

    @Override
    public void onBackPressed() {
        mCountDownTimer.cancel();
        mainBinding.progressBarz.getTimer().cancel();
        stop = true;
        this.getViewModelStore().clear();
        viewModel.resetCitiesWeather(this);
        MainActivity.this.finish();
    }


}





