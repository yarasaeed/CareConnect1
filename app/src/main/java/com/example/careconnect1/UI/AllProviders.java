package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.Adapters.ProvidersAdapter;
import com.example.careconnect1.Model.ProvidersModel;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.google.android.material.appbar.MaterialToolbar;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class AllProviders extends AppCompatClass {
    private RecyclerView recyclerView;
    private ProvidersAdapter adapter;
    private MaterialToolbar toolbar;
    private SearchView searchView;
    private ArrayList<ProvidersModel> arrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_providers);
        setMethods("Providers","");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        toolbar = findViewById(R.id.generalToolbar);
        recyclerView = findViewById(R.id.recyclerView);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
    }


    @Override
    public void setActions() {
        super.setActions();
        toolbar.inflateMenu(R.menu.menu_search);
        MenuItem item = toolbar.getMenu().findItem(R.id.item_search);
        searchView = (SearchView) item.getActionView();
        getProviders();

    }

    private void getProviders(){
        arrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_providers.php?limit=1000", response -> {

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
                        String gender =jSONObject.getString("gender");
                        arrayList.add(new ProvidersModel(user_id,f_name,l_name,role,bio,address,email,phone_nb,icon,gender));

                        i++;
                    }
                    adapter = new ProvidersAdapter(AllProviders.this, arrayList);
                    recyclerView.setAdapter(adapter);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            adapter.getFilter().filter(newText);
                            return true;
                        }
                    });

                }catch (Exception | Error ignored){}
            }

        }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()){ };

        RequestQueue requestQueue = Volley.newRequestQueue(AllProviders.this);
        requestQueue.add(stringRequest);
    }
}