
package com.example.careconnect1.UI;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.example.careconnect1.Utilities.Config;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatClass {
    private TextView login_button;
    private TextInputEditText fname, lname, email, phone, password;
    private TextInputLayout layout_fname, layout_lname;
    private MaterialButton btn_signup;
    private UserData userData;
    private String user_role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setMethods("Signup As", "SignUp");

    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
             user_role = bundle.getString("type", "");
        }
        login_button = findViewById(R.id.loginButton);
        fname = findViewById(R.id.fn_edit);
        lname = findViewById(R.id.ln_edit);
        email = findViewById(R.id.email_edit);
        phone = findViewById(R.id.phone_edit);
        password = findViewById(R.id.password_edit);
        btn_signup = findViewById(R.id.btn_signup);
        layout_lname = findViewById(R.id.ln_layout);
        layout_fname = findViewById(R.id.fn_layout);
    }

    @Override
    public void setActions() {
        super.setActions();
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }

    private void signUp() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.IP + "signup.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(SignUp.this, "Signup successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, error -> {
            Toast.makeText(SignUp.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("fname", fname.getText().toString());
                map.put("lname", lname.getText().toString());
                map.put("email", email.getText().toString());
                map.put("password", password.getText().toString());
                map.put("phone", phone.getText().toString());
                map.put("user_role", user_role); // Assuming user_role is set earlier
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
        requestQueue.add(stringRequest);
    }

    // You can implement any other necessary methods here
}
