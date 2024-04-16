package com.example.careconnect1.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.careconnect1.Fragments.JobSeekersFragment;
import com.example.careconnect1.Fragments.ParentsFragment;
import com.example.careconnect1.R;

public class Home extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Find the ViewPager
        viewPager = findViewById(R.id.viewPager);

        // Set up ViewPager adapter
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Find the buttons
        Button btnParents = findViewById(R.id.btnParents);
        Button btnJobSeekers = findViewById(R.id.btnJobSeekers);

        // Set click listener for the Parents button
        btnParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to the Parents fragment
                viewPager.setCurrentItem(0);
            }
        });

        // Set click listener for the Job Seekers button
        btnJobSeekers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to the Job Seekers fragment
                viewPager.setCurrentItem(1);
            }
        });
    }

    // Define the PagerAdapter class
    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 2; // Number of pages (fragments)

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // Return the fragment for the corresponding page position
            switch (position) {
                case 0:
                    return new ParentsFragment();
                case 1:
                    return new JobSeekersFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Return the total number of pages
            return NUM_PAGES;
        }
    }
}
