package com.dyna.dyna.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dyna.dyna.R;

public class SplashActivity extends AppCompatActivity {
    private static int splash_time = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashActivity.this, MapsActivity.class);
                startActivity(splashIntent);
                finish();
            }
        },splash_time);
    }
}
