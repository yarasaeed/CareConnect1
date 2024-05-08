package com.example.careconnect1.Fragments;// Import statements

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.careconnect1.Fragments.MapViewFragment;
import com.example.careconnect1.R;

public class MapButtonsFragment extends Fragment {

    private Button btnViewNearbyBabysitters;

    public MapButtonsFragment() {
        // Required empty public constructor
    }
    public static MapButtonsFragment newInstance() {
        return new MapButtonsFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_buttons, container, false);

        // Initialize the button
        btnViewNearbyBabysitters = rootView.findViewById(R.id.btnViewNearbyBabysitters);

        // Set click listener for the button
        btnViewNearbyBabysitters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to another fragment
                navigateToMapViewFragment();
            }
        });

        return rootView;
    }

    private void navigateToMapViewFragment() {
        // Navigate to the MapViewFragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container2, MapViewFragment.newInstance()); // Use newInstance() method
        transaction.addToBackStack(null);  // Optional, to allow back navigation
        transaction.commit();
    }

}
