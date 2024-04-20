package com.example.careconnect1.Utilities;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.example.careconnect1.R;

public class LoadingLayout {

    public static void show(Activity activity){
        FrameLayout layout_loading = activity.findViewById(R.id.layout_loading);
        layout_loading.setVisibility(View.VISIBLE);
    }

    public static void hide(Activity activity){
        FrameLayout layout_loading = activity.findViewById(R.id.layout_loading);
        layout_loading.setVisibility(View.GONE);

    }
}
