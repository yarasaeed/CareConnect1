package com.example.careconnect1.Fragments;

import static com.example.careconnect1.Utilities.Config.IP;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class MainListFragment extends Fragment {
    List<Provider> providerList;
    RecyclerView recyclerView;

    public static MainListFragment newInstance() {
        return new MainListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);

        // Getting the recyclerview from XML
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initializing the provider list
        providerList = new ArrayList<>();

        // This method will fetch and parse JSON to display it in recyclerview
        loadProviders();

        return view;
    }

    private void loadProviders() {
        String url = IP + "fetch_babysitters.php";

        Log.d("Network Request", "Fetching babysitters from URL: " + url); // Log network request
        // Creating a JsonArrayRequest to fetch data from the server
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
                                    double lat = jsonObject.getDouble("latitude");
                                    double lon = jsonObject.getDouble("longitude");
                                    String role = jsonObject.getString("role");
                                    String name = jsonObject.getString("name");
                                    String icon = jsonObject.getString("icon");

                                    // Create a Provider object
                                    Provider provider = new Provider(new GeoPoint(lat, lon), name, role, icon);
                                    providerList.add(provider);
                                    System.out.println(provider);
                                }
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


    private static class Provider {
        private GeoPoint location;
        private String name;
        private String icon;
        private String phone;

        public Provider(GeoPoint location, String name, String icon, String phone) {
            this.location = location;
            this.name = name;
            this.icon = icon;
            this.phone = phone;
        }

        public GeoPoint getLocation() {
            return location;
        }

        public String getName() {
            return name;
        }

        public String getIcon() {
            return icon;
        }

        public String getPhone() {
            return phone;
        }
    }
}
