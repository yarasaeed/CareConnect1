package com.example.careconnect1.UI;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.careconnect1.Adapters.ViewPagerAdapter;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class OnBoardingScreens extends AppCompatClass {
    private ViewPager2 viewPager2;
    private DotsIndicator dotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setMethods("","");
    }

    @Override
    public void setActions() {
        super.setActions();
        try {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception | Error ignored) {
        }
        FragmentStateAdapter fragmentStateAdapter = new ViewPagerAdapter(OnBoardingScreens.this);
        viewPager2.setAdapter(fragmentStateAdapter);
        dotsIndicator.setViewPager2(viewPager2);

        //add animation to viewpager2
        viewPager2.setPageTransformer((page, position) -> {
            if(position<-1){
                viewPager2.setAlpha(0);
            }
            else if (position<=1){
                float max = Math.max(0.65f, 1 - Math.abs(position));
                viewPager2.setScaleX(max);
                viewPager2.setScaleY(max);
                viewPager2.setAlpha(Math.max(0.3f,1-Math.abs(position)));
            }
            else {
                viewPager2.setAlpha(0);
            }
        });
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        viewPager2 = findViewById(R.id.viewPager);
        dotsIndicator = findViewById(R.id.dotsIndicator);
    }
}