package com.example.careconnect1.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.Adapters.ProviderReviewsAdapter;
import com.example.careconnect1.Model.ReviewsModel;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.example.careconnect1.Utilities.Config;
import com.example.careconnect1.Utilities.UserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewsFragment extends Fragment {
    private RecyclerView recyclerView;

    private ArrayList<ReviewsModel> arrayList;

    private UserData userData;

    private ProviderReviewsAdapter adapter;

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        userData = new UserData(requireContext());
        getReviews();
    }

    public void getReviews() {
        arrayList = new ArrayList<>();
        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.IP + "select_reviews_where_provider.php?center_id=" + userData.getId(), response -> {
            int i = 0;
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                if (jsonArray.length() == 0) {
                    Toast.makeText(requireContext(), "There are no reviews", Toast.LENGTH_SHORT).show();
                }

                while (i < jsonArray.length()) {
                    JSONObject jSONObject = jsonArray.getJSONObject(i);
                    String text = jSONObject.getString("ReviewText");
                    String user_id = jSONObject.getString("UserID");
                    String id = jSONObject.getString("ReviewID");
                    String cleaner_id = jSONObject.getString("ProviderID");
                    String book_id = jSONObject.getString("r_booking_id");
                    String customer_name = jSONObject.getString("f_name") + " " + jSONObject.getString("l_name");
                    String customer_icon = jSONObject.getString("icon");
                    String date = jSONObject.getString("date");
                    arrayList.add(new ReviewsModel(id, book_id, user_id, cleaner_id, text, customer_name, customer_icon, date));
                    i++;
                }
                adapter = new ProviderReviewsAdapter(requireContext(), arrayList, false);
                recyclerView.setAdapter(adapter);
            } catch (Exception | Error ignored) {

            }

        }, error -> {

        });
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }
}
