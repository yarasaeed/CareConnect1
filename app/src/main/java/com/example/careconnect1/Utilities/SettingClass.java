//sed to configure the settings of a toolbar in an Android
// activity.sed to configure the settings of a toolbar in an Android activity.

package com.example.careconnect1.Utilities;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.example.careconnect1.R;
import com.google.android.material.appbar.MaterialToolbar;


public class SettingClass {
    private final Activity activity;
    private final String title;
    private final String subtitle;

    public SettingClass(Activity activity,String title, String subtitle) {
        this.activity = activity;
        this.title = title;
        this.subtitle = subtitle;

    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public void setSetting(){
        MaterialToolbar materialToolbar = activity.findViewById(R.id.generalToolbar);
        materialToolbar.setNavigationIcon(activity.getResources().getDrawable(R.drawable.ic_back));
        materialToolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
        materialToolbar.setTitle(title);
        materialToolbar.setSubtitle(subtitle);
        materialToolbar.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));

    }
}
