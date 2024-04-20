package com.example.careconnect1.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.careconnect1.Adapters.CustomAdapter;
import com.example.careconnect1.R;

public class ParentsFragment extends Fragment {

    public ParentsFragment() {
        // Required empty public constructor
    }

    public static ParentsFragment newInstance() {
        return new ParentsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_parents_activity, container, false);
        ListView listView = v.findViewById(R.id.listView);

        int[] backgroundImages = {R.drawable.center1, R.drawable.center1, R.drawable.center1, R.drawable.center1};

        String[] data = {"Center 1", "Center 2", "Center 3", "Center 4"}; // Sample data
        String[] id = {"1", "2", "3", "4"};

        CustomAdapter adapter = new CustomAdapter(getActivity(), data, id, backgroundImages, new CustomAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int position) {
                // Handle button click if needed
            }
        });

        listView.setAdapter(adapter);

        return v;
    }
}
