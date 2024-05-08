package com.example.careconnect1.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.careconnect1.Fragments.MainListFragment;
import com.example.careconnect1.Fragments.MapButtonsFragment;

public class NewViewPagerAdapter extends FragmentStateAdapter {
    // Total number of pages (fragments) in the ViewPager2
    private final int pages_nb = 2;

    public NewViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Determine which fragment should be displayed for each position
        if (position == 0) {
            return MainListFragment.newInstance();
        } else {
            return MapButtonsFragment.newInstance();
        }
    }

    @Override
    // Return the total number of pages in the ViewPager2
    public int getItemCount() {
        return pages_nb;
    }
}