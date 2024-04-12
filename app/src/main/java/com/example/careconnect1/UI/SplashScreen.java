package com.example.careconnect1.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
//This is an abstract class in the Android framework that provides the basic functionality for an activity.
//It is a subclass of the FragmentActivity class, which provides support for fragments.
@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatClass {
    private Intent intent;
    private SharedPreferences sharedPreferences;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setMethods("", "");

        try {
            // Set flags to achieve a full-screen immersive experience
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception | Error ignored) {
            // Handle any exceptions or errors that occur while setting window flags
        }
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        sharedPreferences = getSharedPreferences("firstTime", MODE_PRIVATE);
    }

    @Override
    public void setActions() {
        super.setActions();

        // Create a new Handler and post a delayed runnable to start the desired activity
        new Handler().postDelayed(() -> {
            // Check if it's the first time the app is launched
            if (sharedPreferences.getBoolean("firstTime", true)) {
                intent = new Intent(SplashScreen.this, OnBoardingScreens.class);
            } else {
                intent = new Intent(SplashScreen.this, LogIn.class);
            }
            startActivity(intent);
            finish();
        }, 1000);
    }
}
