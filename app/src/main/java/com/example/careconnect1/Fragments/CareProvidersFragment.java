package com.example.careconnect1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.careconnect1.R;

public class CareProvidersFragment extends Fragment {



    public CareProvidersFragment() {
        // Required empty public constructor
    }

    public static CareProvidersFragment newInstance() {

        return new CareProvidersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_care_providers_activity, container, false);



        return v;
    }
}