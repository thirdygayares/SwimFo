package com.example.swimfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Hide the ActionBar (Toolbar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        new Handler().postDelayed(() -> {
                Intent loginActivity = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(loginActivity);
                finishAffinity();
        },3000);

    }
}