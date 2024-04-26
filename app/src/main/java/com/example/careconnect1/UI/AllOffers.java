package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cleanup.Adapters.AllOffersAdapter;
import com.example.cleanup.Models.OffersModel;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.google.android.material.appbar.MaterialToolbar;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;

public class AllOffers extends AppCompatClass {
    private RecyclerView  recyclerViewOffers;
    private MaterialToolbar toolbar;
    private SearchView searchView;
   // private AllOffersAdapter allOffersAdapter;
    private ArrayList<OffersModel> arrayListOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_offers);
        setMethods("Offers", "");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        toolbar = findViewById(R.id.generalToolbar);
        recyclerViewOffers = findViewById(R.id.recyclerView);

    }

    @Override
    public void setActions() {
        super.setActions();
        toolbar.inflateMenu(R.menu.menu_search);
        MenuItem item = toolbar.getMenu().findItem(R.id.item_search);
        searchView = (SearchView) item.getActionView();
        getOffers();
    }

    private void getOffers() {
        arrayListOffers = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_offers.php?limit=10000", response -> {

            {

                int i = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    while (i < jsonArray.length()) {
                        JSONObject jSONObject = jsonArray.getJSONObject(i);
                        String offer_id = jSONObject.getString("offer_id");
                        String o_cleaner_id = jSONObject.getString("o_provider_id");
                        String description = jSONObject.getString("description");
                        String icon = jSONObject.getString("icon");
                        String price = jSONObject.getString("price");
                        String date = jSONObject.getString("time");
                        String f_name = jSONObject.getJSONObject("provider_info").getString("f_name");
                        String l_name = jSONObject.getJSONObject("provider_info").getString("l_name");
                        String cleaner_icon = jSONObject.getJSONObject("provider_info").getString("icon");
                        String user_role = jSONObject.getJSONObject("provider_info").getString("UserRole");

                        String name = "";
                        if (!user_role.toLowerCase(Locale.ROOT).equals("center")) {
                            name = f_name + " " + l_name;
                        } else {
                            name = f_name;
                        }
                        arrayListOffers.add(new OffersModel(offer_id, icon, description, date, price, o_provider_id, name, provider_icon));
                        i++;
                    }

                  //  allOffersAdapter = new AllOffersAdapter(AllOffers.this, arrayListOffers);
                    GridLayoutManager layoutManager = new GridLayoutManager(AllOffers.this, 2);
                    recyclerViewOffers.setLayoutManager(layoutManager);
                   // recyclerViewOffers.setAdapter(allOffersAdapter);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            allOffersAdapter.getFilter().filter(newText);
                            return true;
                        }
                    });

                } catch (Exception | Error ignored) {

                }

            }

        }, error -> Toast.makeText(AllOffers.this, error.toString(), Toast.LENGTH_SHORT).show()) {
        };


        RequestQueue requestQueue = Volley.newRequestQueue(AllOffers.this);
        requestQueue.add(stringRequest);

    }
}