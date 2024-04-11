package com.example.careconnect1.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.File;

public class AppCompatClass extends AppCompatActivity implements MyClass {

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    // this  is called when the activity is being attached to the context
    public void setSetting(String title, String subtitle){
        SettingClass settingClass = new SettingClass(AppCompatClass.this, title, subtitle);
        settingClass.setSetting();
    }
    // implementations of the methods defined in the MyClass interface.
    @Override
    public void setInitialize() {

    }

    @Override
    public void setActions() {

    }

    public void setMethods(String title, String subTitle){
        setInitialize();
        setSetting(title, subTitle);
        setActions();
    }
}
