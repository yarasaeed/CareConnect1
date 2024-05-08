package com.example.careconnect1.UI;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.careconnect1.Adapters.NewViewPagerAdapter;
import com.example.careconnect1.R;

public class ViewActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnListView, btnMapView;
    boolean clicked = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_activity);

        // Find views
        viewPager = findViewById(R.id.viewPager);
        btnListView = findViewById(R.id.btnListView);
        btnMapView = findViewById(R.id.btnMapView);

        // Set up ViewPager
        NewViewPagerAdapter adapter = new NewViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Set default fragment to ParentsFragment
        viewPager.setCurrentItem(0);
        btnListView.setBackgroundColor(getResources().getColor(R.color.pink));
        btnMapView.setBackgroundColor(getResources().getColor(R.color.purple));

        // Set up button click listeners
        btnListView.setOnClickListener(v -> {
            viewPager.setCurrentItem(0);


        });
        btnMapView.setOnClickListener(v -> {
            viewPager.setCurrentItem(1);

        });
    }

}


