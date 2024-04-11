package com.example.careconnect1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.careconnect1.R;

public class Page3 extends Fragment {



    public Page3() {
        // Required empty public constructor
    }

    public static Page3 newInstance() {

        return new Page3();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=  inflater.inflate(R.layout.fragment_page3, container, false);

        return v;
    }
}