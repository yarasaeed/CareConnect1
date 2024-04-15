package com.example.careconnect1.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.careconnect1.Fragments.Page1;
import com.example.careconnect1.Fragments.Page2;

public class ViewPagerAdapter extends FragmentStateAdapter {
    //use FragmentActivity fragment, when use activity
    // Total number of pages (fragments) in the ViewPager2
    final int pages_nb=2;
    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Determine which fragment should be displayed for each position

        switch (position){
            case 0:
                return Page1.newInstance();
            case 1:
                return Page2.newInstance();

        }
        return null;
    }
    @Override        // Return the total number of pages in the ViewPager2

    public int getItemCount() {
        return pages_nb;
    }
}
