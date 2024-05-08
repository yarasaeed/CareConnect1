package com.example.careconnect1.Admin;

import static com.example.careconnect1.Utilities.Config.IPADMIN;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.Adapters.AdminUsersAdapter;
import com.example.careconnect1.Model.UsersModel;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class AllUsers extends AppCompatClass {
    private RecyclerView recyclerView;
    private AdminUsersAdapter adapter;

    private ArrayList<UsersModel> arrayList;
    private SearchView searchView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_common);
        setMethods("Users","");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.generalToolbar);


    }

    @Override
    public void setActions() {
        super.setActions();
        //toolbar.inflateMenu(R.menu.menu_search);
       // MenuItem item = toolbar.getMenu().findItem(R.id.item_search);
        //searchView = (SearchView) item.getActionView();
        getUsers();

    }
    public void getUsers(){
        arrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IPADMIN + "select_users.php", response -> {

            {
                int i = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    while (i < jsonArray.length()) {
                        toolbar.setTitle(jsonArray.length() + " users registered");
                        JSONObject jSONObject = jsonArray.getJSONObject(i);
                        String user_id = jSONObject.getString("user_id");
                        String fname = jSONObject.getString("f_name");
                        String lname = jSONObject.getString("l_name");
                        String email = jSONObject.getString("email");
                        String phone = jSONObject.getString("phone_nb");
                        String role  = jSONObject.getString("UserRole");
                        String icon = jSONObject.getString("icon");
                        String address = ""+jSONObject.getString("address");

                        String name;
                        if(role.toLowerCase(Locale.ROOT).equals("center")){
                            name = fname;
                        }else {
                            name = fname + lname;
                        }
                        arrayList.add(new UsersModel(user_id, name, role, phone, email,address,icon));
                        i++;
                    }

                    adapter = new AdminUsersAdapter(AllUsers.this, arrayList);
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
                }catch (Exception | Error ignored){

                }
            }

        }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()){ };

        RequestQueue requestQueue = Volley.newRequestQueue(AllUsers.this);
        requestQueue.add(stringRequest);
    }
}