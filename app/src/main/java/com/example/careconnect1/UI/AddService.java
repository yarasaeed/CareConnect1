package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;
import android.os.Bundle;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.example.careconnect1.Utilities.UserData;

public class AddService extends AppCompatClass {
    private UserData userData;
    private TextInputEditText name, price;
    private MaterialButton btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        setMethods("Add service", "");
    }

    @Override
    public void setActions() {
        super.setActions();
        btn_add.setOnClickListener(v -> add());
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        userData = new UserData(AddService.this);
        name= findViewById(R.id.name_edit);
        price= findViewById(R.id.price_edit);
        btn_add= findViewById(R.id.btn_add);
    }

    private void add(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP + "add_service.php", response -> {
            if(!response.toLowerCase(Locale.ROOT).contains("success")){
                Toast.makeText(AddService.this, response.trim(), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AddService.this, "New service add successfully", Toast.LENGTH_SHORT).show();
                name.setText("");
                price.setText("");

            }
        }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("name",name.getText().toString());
                map.put("price",price.getText().toString());
                map.put("user_id",userData.getId());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddService.this);
        requestQueue.add(stringRequest);
    }
}