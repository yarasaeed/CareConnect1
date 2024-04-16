package com.example.careconnect1.UI;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.careconnect1.Adapters.NewViewPagerAdapter;
import com.example.careconnect1.R;

public class Home extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Button btnParents, btnJobSeekers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Find views
        viewPager = findViewById(R.id.viewPager);
        btnParents = findViewById(R.id.btnParents);
        btnJobSeekers = findViewById(R.id.btnJobSeekers);

        // Set up ViewPager
        NewViewPagerAdapter adapter = new NewViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Set default fragment to ParentsFragment
        viewPager.setCurrentItem(0);

        // Set up button click listeners
        btnParents.setOnClickListener(v -> viewPager.setCurrentItem(0));
        btnJobSeekers.setOnClickListener(v -> viewPager.setCurrentItem(1));
    }
}