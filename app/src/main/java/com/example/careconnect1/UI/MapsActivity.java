package com.example.careconnect1.UI;

import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.careconnect1.R;
import com.example.careconnect1.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private LatLng selectedLocation;
    private ActivityMapsBinding binding;
    private MaterialButton btnSaveLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        btnSaveLocation = findViewById(R.id.btn_save_location);
        btnSaveLocation.setOnClickListener(v -> saveLocation());

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Set initial camera position to Lebanon
        LatLng lebanon = new LatLng(33.8547, 35.8623);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lebanon, 8f));

        googleMap.setOnMapClickListener(latLng -> {
            if (selectedLocation != null) {
                googleMap.clear();
            }
            selectedLocation = latLng;
            googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
        });
    }

    private void saveLocation() {
        if (selectedLocation != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("latitude", selectedLocation.latitude);
            resultIntent.putExtra("longitude", selectedLocation.longitude);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }}
