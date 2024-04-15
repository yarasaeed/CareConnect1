package com.example.careconnect1.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.careconnect1.R;
import com.example.careconnect1.UI.LogIn;

public class Page2 extends Fragment {



    public Page2() {
        // Required empty public constructor
    }

    public static Page2 newInstance() {

        return new Page2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page2, container, false);

        // Add a click listener to the page 2 view
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the login activity
                startActivity(new Intent(getActivity(), LogIn.class));
            }
        });

        return v;
    }
}