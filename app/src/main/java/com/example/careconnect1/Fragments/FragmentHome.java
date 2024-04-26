package com.example.careconnect1.Fragments;
import static com.example.careconnect1.Utilities.Config.IP;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.careconnect1.Adapters.AllProvidersAdapter;
import com.example.careconnect1.Adapters.AllOffersAdapter;
import com.example.careconnect1.Adapters.MainImagesAdapter;
//import com.example.careconnect1.Model.ProvidersModel;
import com.example.cleanup.Models.OffersModel;
import com.example.careconnect1.R;
//import com.example.careconnect1.UI.AllProviders;
import com.example.careconnect1.UI.AllOffers;
import com.example.careconnect1.Utilities.LoadingLayout;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class FragmentHome extends Fragment {
    private RecyclerView recyclerViewCleaners, recyclerViewOffers;
    private AllOffersAdapter allOffersAdapter;
   // private AllProvidersAdapter allProvidersAdapter;
   // private ArrayList<ProvidersModel> arrayListProviders;
    private ArrayList<OffersModel> arrayListOffers;

    private  Runnable runnable=null;
    private Handler handler;
    private DotsIndicator dotsIndicator;
    private ViewPager viewPager;

    final int sliderTime = 3000;
    private FrameLayout   layout_loading;
    public FragmentHome() {
        // Required empty public constructor
    }


    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        setInitialize(v);
        getCleaners();
        return v;
    }

    private void setInitialize(View v){
        layout_loading = v.findViewById(R.id.layout_loading);
        recyclerViewCleaners = v.findViewById(R.id.recyclerView);
        recyclerViewOffers = v.findViewById(R.id.recyclerViewOffers);
        TextView show_more_offers = v.findViewById(R.id.show_more_offers);
        TextView show_more_cleaner = v.findViewById(R.id.show_more_cleaners);
        handler=new Handler();
        dotsIndicator =v.findViewById(R.id.activity_welcome);
        viewPager = v.findViewById(R.id.viewPager);
        show_more_offers.setOnClickListener(v1 -> {
            Intent intent = new Intent(getActivity(), AllOffers.class);
            startActivity(intent);
        });
        show_more_cleaner.setOnClickListener(v1 -> {
            Intent intent = new Intent(getActivity(), AllProviders.class);
            startActivity(intent);
        });
    }

    private void getOffers(){
        arrayListOffers = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_offers.php?limit=8", response -> {

            {
                int i = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    while (i < jsonArray.length()) {
                        JSONObject jSONObject = jsonArray.getJSONObject(i);
                        String offer_id =jSONObject.getString("offer_id");
                        String o_cleaner_id =jSONObject.getString("o_provider_id");
                        String description =jSONObject.getString("description");
                        String icon =jSONObject.getString("icon");
                        String price =jSONObject.getString("price");
                        String date =jSONObject.getString("time");
                        String f_name =jSONObject.getJSONObject("provider_info").getString("f_name");
                        String l_name =jSONObject.getJSONObject("provider_info").getString("l_name");
                        String cleaner_icon =jSONObject.getJSONObject("provider_info").getString("icon");
                        String user_role =jSONObject.getJSONObject("provider_info").getString("UserRole");

                        String name = "";
                        if(user_role.toLowerCase(Locale.ROOT).equals("center")){
                            name = f_name;
                        }else{
                            name = f_name + " " + l_name;
                        }
                        arrayListOffers.add(new OffersModel(offer_id, icon,description,date,price,o_cleaner_id,name,cleaner_icon));
                        i++;
                    }
                    allOffersAdapter = new AllOffersAdapter(getActivity(), arrayListOffers);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
                    recyclerViewOffers.setLayoutManager(layoutManager);
                    recyclerViewOffers.setAdapter(allOffersAdapter);
                    recyclerViewOffers.setNestedScrollingEnabled(false);
                    layout_loading.setVisibility(View.GONE);
                }catch (Exception | Error ignored){}
                if(getActivity() !=null){
                    layout_loading.setVisibility(View.GONE);
                }
            }

        }, error -> Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show()){ };

        if(getActivity()  != null){
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        }
    }
    private void getCleaners(){
        layout_loading.setVisibility(View.VISIBLE);
        //arrayListProviders = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_providers.php?limit=8", response -> {

            {
                int i = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    while (i < jsonArray.length()) {
                        JSONObject jSONObject = jsonArray.getJSONObject(i);
                        String user_id =jSONObject.getString("user_id");
                        String email =jSONObject.getString("email");
                        String phone_nb =jSONObject.getString("phone_nb");
                        String address =jSONObject.getString("address");
                        String icon =jSONObject.getString("icon");
                        String role =jSONObject.getString("UserRole");
                        String f_name =jSONObject.getString("f_name");
                        String l_name =jSONObject.getString("l_name");
                        String bio =jSONObject.getString("bio");
                        String employee_nb =jSONObject.getString("gender");
                       // arrayListProviders.add(new ProvidersModel(user_id,f_name,l_name,role,bio,address,email,phone_nb,icon,gender));

                        i++;
                    }
                    //allProviderAdapter = new AllProviderAdapter(getActivity(), arrayListProviders);
                    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                    recyclerViewCleaners.setLayoutManager(layoutManager);
                    //recyclerViewCleaners.setAdapter(allProviderAdapter);
                    recyclerViewCleaners.setNestedScrollingEnabled(false);
                    loadImages(getActivity());



                }catch (Exception | Error ignored){}

            }

        }, error -> Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show()){ };

        if(getActivity()  != null){
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        }
    }
    public  void loadImages(Activity activity){
        ArrayList<String> arrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_main_images.php", response -> {
            try {
                int i = 0;
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                while (i < jsonArray.length()) {
                    JSONObject jSONObject = jsonArray.getJSONObject(i);
                    String name = jSONObject.getString("FileName");
                    arrayList.add(name);
                    i++;

                }
                Collections.reverse(arrayList);
                MainImagesAdapter adapter = new MainImagesAdapter(activity, arrayList);
                viewPager.setAdapter(adapter);
                dotsIndicator.setViewPager(viewPager);
                getOffers();
                resetHandler();
                try {
                    startAutoSlider(adapter.getCount());
                } catch (Error | Exception ignored) {
                }

            } catch (Exception | Error ignored) {
            }
        },error -> {}
        );
        if(getActivity() != null){
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }

    public void resetHandler(){
        try {
            handler.removeCallbacks(runnable);
        }catch (Error | Exception ignored){}

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            resetHandler();
        }catch (Exception | Error ignored){}
    }

    @Override
    public void onStop() {

        try {
            super.onStop();
            resetHandler();
        }catch (Exception | Error ignored){}

    }

    private void startAutoSlider(final int count) {
        try {
            runnable = () -> {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, sliderTime);
            };
            handler.postDelayed(runnable, sliderTime);

        }catch (Error | Exception ignored){}
    }
}