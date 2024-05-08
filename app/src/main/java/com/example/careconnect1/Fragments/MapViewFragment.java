package com.example.careconnect1.Fragments;

import static com.example.careconnect1.Utilities.Config.IP;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MapViewFragment extends Fragment implements MapListener, Marker.OnMarkerClickListener {

    private static final int REQUEST_LOCATION_PERMISSION = 123;
    private MapView mMap;
    private IMapController controller;
    private MyLocationNewOverlay mMyLocationOverlay;
    private GeoPoint parentLocation;
    private InfoWindow mCurrentInfoWindow;
    private List<Babysitter> babysitters = new ArrayList<>();


    public static MapViewFragment newInstance() {
        return new MapViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
        mMap = rootView.findViewById(R.id.osmmap);
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.setMultiTouchControls(true);
        controller = mMap.getController();
        controller.setZoom(15.0);
        mMyLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), mMap);
        mMyLocationOverlay.enableMyLocation();
        mMyLocationOverlay.enableFollowLocation();
        mMyLocationOverlay.setDrawAccuracyEnabled(true);
        mMap.getOverlays().add(mMyLocationOverlay);
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        Log.e("Empty Response", "Empty responseeeee");
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getDeviceLocation();
        }

        mMap.addMapListener(this);
        fetchBabysitters();
        return rootView;
    }

    private void fetchBabysitters() {
        String url = IP + "fetch_locations.php";

        Log.d("Network Request", "Fetching babysitters from URL: " + url); // Log network request

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Network Response", "Response received: " + response.toString()); // Log network response
                        if (response != null && response.length() > 0) {
                            // Non-empty response, parse the JSON data
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    double lat = Double.parseDouble(jsonObject.getString("latitude"));
                                    double lon = Double.parseDouble(jsonObject.getString("longitude"));
                                    String role = jsonObject.getString("role");
                                    String name = jsonObject.getString("name");
                                    //  String icon = jsonObject.optString("icon");

                                    // Create a Babysitter object
                                    Babysitter babysitter = new Babysitter(new GeoPoint(lat, lon), name, role);
                                    babysitters.add(babysitter);
                                    System.out.println(babysitter);
                                }
                                updateMapMarkers();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // Handle JSON parsing error
                                Log.e("JSON Parsing Error", "Error parsing JSON data: " + e.getMessage());
                            }
                        } else {
                            // Empty response
                            Log.e("Empty Response", "Empty response received from the server");
                            // Handle empty response scenario
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle Volley error
                        Log.e("Volley Error", "Error fetching data: " + error.getMessage());

                        // Handle server error scenario
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest);
    }

    private void downloadAndSaveIcon(String iconUrl) {
        // Use Volley or another networking library to download the icon image
        ImageRequest imageRequest = new ImageRequest(iconUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // Save the downloaded icon image to local storage
                saveIconToStorage(response);
            }
        }, 0, 0, null, null);

        // Add the request to the RequestQueue
        Volley.newRequestQueue(requireContext()).add(imageRequest);
    }

    private void saveIconToStorage(Bitmap iconBitmap) {
        // Save the iconBitmap to local storage (internal or external)
        // Example: Save to internal storage
        String filename = "icon_image.jpg";
        FileOutputStream outputStream;
        try {
            outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            iconBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateMapMarkers() {
        mMap.getOverlays().clear();
        mMap.getOverlays().add(mMyLocationOverlay);
        Log.e("Empty Response", "Empty responseeeee");
        // Add marker for parent's location
        if (parentLocation != null) {
            Marker parentMarker = new Marker(mMap);
            parentMarker.setPosition(parentLocation);
            parentMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mMap.getOverlays().add(parentMarker);
        }

        // Add markers for babysitters' locations
        for (Babysitter babysitter : babysitters) {
            Marker marker = new Marker(mMap);
            marker.setPosition(babysitter.getLocation());
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            // marker.setIcon(ContextCompat.getDrawable(requireContext(), Integer.parseInt(babysitter.getIcon())));
            marker.setOnMarkerClickListener(this);
            mMap.getOverlays().add(marker);
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        for (Babysitter babysitter : babysitters) {
            if (babysitter.getLocation().equals(marker.getPosition())) {
                showBabysitterInfo(babysitter);
                return true;
            }
        }
        return false;
    }

    private void showBabysitterInfo(Babysitter babysitter) {
        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.marker_info_window, null);
        TextView textName = popup.findViewById(R.id.textName);
        TextView textPhone = popup.findViewById(R.id.textPhone);
        textName.setText(babysitter.getName());
        textPhone.setText(babysitter.getPhone());
        InfoWindow infoWindow = new InfoWindow(popup, mMap) {
            @Override
            public void onOpen(Object item) {
            }

            @Override
            public void onClose() {
            }
        };
        infoWindow.open(popup, babysitter.getLocation(), 0, 0);
    }

    @Override
    public boolean onScroll(ScrollEvent event) {
        Log.e("TAG", "onScroll: Latitude " + event.getSource().getMapCenter().getLatitude());
        Log.e("TAG", "onScroll: Longitude " + event.getSource().getMapCenter().getLongitude());
        return true;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        Log.e("TAG", "onZoom: Zoom level " + event.getZoomLevel() + " Source " + event.getSource());
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceLocation();
            }
        }
    }

    private void getDeviceLocation() {
        mMyLocationOverlay.runOnFirstFix(() -> {
            parentLocation = mMyLocationOverlay.getMyLocation();
            if (parentLocation != null) {
                // Create a handler to post a Runnable to the main UI thread
                new Handler(Looper.getMainLooper()).post(() -> {
                    // Add a marker for the parent's location
                    Marker parentMarker = new Marker(mMap);
                    parentMarker.setPosition(parentLocation);
                    parentMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    mMap.getOverlays().add(parentMarker);
                    Log.e("Empty Response", "Empty responseeeee");
                    // Center the map around the parent's location
                    controller.animateTo(parentLocation);

                    // Update the map markers to include the parent marker
                    updateMapMarkers();
                });
            }
        });
    }


    // Custom class to hold babysitter information
    private static class Babysitter {
        private GeoPoint location;
        private String name;
        private int icon;
        private String phone; // Add phone field
        private String info;
        private double distance; // Distance from the user's location

        public Babysitter(GeoPoint location, String name, String phone) {
            this.location = location;
            this.name = name;
            this.icon = icon;
            this.phone = phone; // Initialize phone field with the parameter value
            this.info = info;   // Initialize info field with the parameter value
        }

        public GeoPoint getLocation() {
            return location;
        }

        public String getName() {
            return name;
        }

        public int getIcon() {
            return icon;
        }

        public String getPhone() {
            return phone; // Return phone value
        }

        public String getInfo() {
            return info; // Return info value
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }
}