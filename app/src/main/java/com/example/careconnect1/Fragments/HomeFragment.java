package com.example.careconnect1.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.careconnect1.R;
import com.example.careconnect1.UI.ViewActivity;

public class HomeFragment extends Fragment {
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find your button by id
        button = view.findViewById(R.id.searchView);

        // Set an OnClickListener to your button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the login activity
                startActivity(new Intent(getActivity(), ViewActivity.class));
            }
        });

        return view;
    }
}