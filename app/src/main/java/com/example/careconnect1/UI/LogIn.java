package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.example.careconnect1.Utilities.UserData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.Interface.FragmentRefresh;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatClass {
    private MaterialToolbar toolbar;
    FragmentRefresh fragmentRefresh;
    private UserData userData;
    private TextView registerbtn;
    private MaterialButton btn_login;
    private TextInputEditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setMethods("", "");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        toolbar = findViewById(R.id.generalToolbar);
        registerbtn = findViewById(R.id.registerBtn);
        btn_login = findViewById(R.id.loginButton);
        email = findViewById(R.id.email_edit);
        password = findViewById(R.id.password_edit);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void setActions() {
        super.setActions();
        //change icon of tool bar
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close));
        registerbtn.setOnClickListener(v -> {
            Intent intent = new Intent(LogIn.this, UserType.class);
            startActivity(intent);
        });
        btn_login.setOnClickListener(v -> login());
    }

    private void login() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP + "login.php", response -> {
            Log.d("Login Response", response); // Log the response

            if (response.trim().contains(",,")) {
                String[] data = response.trim().split(",,");
                if (data.length >= 2) { // Check if array length is at least 2
                    // LoadingLayout.show(LogIn.this);
                    userData = new UserData(LogIn.this); // save to sharedpref
                    Toast.makeText(LogIn.this, "Success", Toast.LENGTH_SHORT).show();
                    userData.setUserData(data[0], data[1]);
                    Intent intent = new Intent(LogIn.this, MainActivity.class);
                    intent.putExtra("tab", "4");
                    // LoadingLayout.hide(LogIn.this);
                    startActivity(intent);
                    finish();
                } else {
                    // Handle the case where the response doesn't contain enough data
                    Toast.makeText(LogIn.this, "Invalid response format", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle the case where the response is not in the expected format
                Toast.makeText(LogIn.this, response.trim(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("email", email.getText().toString());
                map.put("password", password.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LogIn.this);
        requestQueue.add(stringRequest);
    }
}