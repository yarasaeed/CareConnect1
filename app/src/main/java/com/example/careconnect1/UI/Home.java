package com.example.careconnect1.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.careconnect1.Adapters.NewViewPagerAdapter;
import com.example.careconnect1.R;

public class Home extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Button btnParents, btnJobSeekers;
    boolean clicked=true;

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

    public void Change(View view) {
        System.out.println("enter");
//        if(clicked==true){
//            view.setBackgroundResource(R.drawable.pink);
//            clicked=false;
//        }
    }
}