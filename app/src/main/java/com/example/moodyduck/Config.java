package com.example.moodyduck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Config extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MoodyDuckSecondary);
        setContentView(R.layout.activity_config);
    }
}