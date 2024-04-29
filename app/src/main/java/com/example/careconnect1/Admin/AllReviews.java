package com.example.careconnect1.Admin;

import static com.example.careconnect1.Utilities.Config.IPADMIN;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllReviews extends AppCompatClass {
    private RecyclerView recyclerView;

    private ArrayList<ReviewsModel> arrayList;

    private String user_id= "";

    private ProviderReviewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        setMethods("Reviews", "");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        recyclerView = findViewById(R.id.recyclerView);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            user_id = bundle.getString("user_id", "");
        }
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

            @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, IPADMIN + "select_reviews_where_parent.php?parent_id="+ user_id, response -> {
                int i = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    if(jsonArray.length() == 0){
                        Toast.makeText(AllReviews.this,   "There are no reviews", Toast.LENGTH_SHORT).show();
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

                    adapter = new ProviderReviewsAdapter(AllReviews.this, arrayList, true);
                    recyclerView.setAdapter(adapter);
                }catch (Exception | Error ignored){
                    Toast.makeText(this, ignored.toString(), Toast.LENGTH_SHORT).show();
                }

            }, error -> {
                Toast.makeText(this, error+ "", Toast.LENGTH_SHORT).show();
            });
            RequestQueue requestQueue = Volley.newRequestQueue(AllReviews.this);
            requestQueue.add(stringRequest);
        }



}