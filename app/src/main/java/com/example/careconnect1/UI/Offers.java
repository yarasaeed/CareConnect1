package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.Adapters.OfferAdapter;
import com.example.careconnect1.Model.OffersModel;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class Offers extends AppCompatClass {
    private RecyclerView recyclerView;
    private ArrayList<OffersModel> arrayList;
    private String user_id, site_id ;
    private OfferAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        setMethods("Offers","");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        recyclerView = findViewById(R.id.recyclerView);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            user_id = bundle.getString("user_id","");
            site_id = bundle.getString("site_id","");
        }
        
    }


    @Override
    public void setActions() {
        super.setActions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOffers();
    }

    public void getOffers(){
        arrayList = new ArrayList<>();
        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_offers_where.php?user_id="+ user_id, response -> {
            int i = 0;
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                if(jsonArray.length() == 0){
                    Toast.makeText(Offers.this,   "There are no offers", Toast.LENGTH_SHORT).show();
                }

                while (i < jsonArray.length()) {
                    JSONObject jSONObject = jsonArray.getJSONObject(i);
                    String description = jSONObject.getString("description");
                    String date = jSONObject.getString("time");
                    String price = jSONObject.getString("price");
                    String icon = jSONObject.getString("icon");
                    String id_offer = jSONObject.getString("offer_id");
                    String used = jSONObject.getString("used");
                    arrayList.add(new OffersModel(id_offer, icon,description,date,price,user_id,used));
                    i++;
                }
                adapter = new OfferAdapter(Offers.this, arrayList, site_id);
                recyclerView.setAdapter(adapter);
            }catch (Exception | Error ignored){

            }

        }, error -> {

        });
        RequestQueue requestQueue = Volley.newRequestQueue(Offers.this);
        requestQueue.add(stringRequest);
    }

}