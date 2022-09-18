package com.ilyes.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ilyes.myapplication.R;
import com.ilyes.myapplication.databinding.ActivityFirstBinding;
import com.ilyes.myapplication.databinding.ActivityMainBinding;

public class FirstActivity extends AppCompatActivity {
    ActivityFirstBinding activityFirstBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFirstBinding = ActivityFirstBinding.inflate(getLayoutInflater());
        setContentView(activityFirstBinding.getRoot());

        activityFirstBinding.suivant.setOnClickListener(view ->
        {
            Intent intent = new Intent(FirstActivity.this,MainActivity.class);
            startActivity(intent);
        });

    }
}