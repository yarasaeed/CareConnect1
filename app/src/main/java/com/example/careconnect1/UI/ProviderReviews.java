package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.Adapters.ProviderReviewsAdapter;
import com.example.careconnect1.Model.ReviewsModel;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.example.careconnect1.Utilities.UserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProviderReviews extends AppCompatClass {
    private RecyclerView recyclerView;

    private ArrayList<ReviewsModel> arrayList;

    private UserData userData;

    private ProviderReviewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_reviews);
        setMethods("Reviews", "");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        recyclerView = findViewById(R.id.recyclerView);
        userData = new UserData(ProviderReviews.this);
    }

    @Override
    public void setActions() {
        super.setActions();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getReviews();
    }

    public void getReviews(){
        arrayList = new ArrayList<>();
            @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_reviews_where_cleaner.php?cleaner_id="+ userData.getId(), response -> {
                int i = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    if(jsonArray.length() == 0){
                        Toast.makeText(ProviderReviews.this,   "There are no reviews", Toast.LENGTH_SHORT).show();
                    }

                    while (i < jsonArray.length()) {
                        JSONObject jSONObject = jsonArray.getJSONObject(i);
                        String text = jSONObject.getString("ReviewText");
                        String user_id = jSONObject.getString("UserID");
                        String id = jSONObject.getString("ReviewID");
                        String cleaner_id = jSONObject.getString("CleanerID");
                        String book_id = jSONObject.getString("r_booking_id");
                        String customer_name = jSONObject.getString("f_name") +" "+jSONObject.getString("l_name")  ;
                        String customer_icon = jSONObject.getString("icon");
                        String date = jSONObject.getString("date");
                        arrayList.add(new ReviewsModel(id, book_id,user_id,cleaner_id, text,customer_name,customer_icon,date));
                        i++;
                    }
                    adapter = new ProviderReviewsAdapter(ProviderReviews.this, arrayList, false);
                    recyclerView.setAdapter(adapter);
                }catch (Exception | Error ignored){

                }

            }, error -> {

            });
            RequestQueue requestQueue = Volley.newRequestQueue(ProviderReviews.this);
            requestQueue.add(stringRequest);
        }



}