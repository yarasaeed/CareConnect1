package com.example.careconnect1.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


public class LogIn extends AppCompatClass {
    private MaterialToolbar toolbar;
    private TextView registerBtn;
    private MaterialButton loginButton;
    private TextInputEditText email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setMethods("","");

    }

        @Override
        public void setInitialize() {
            super.setInitialize();
            toolbar = findViewById(R.id.generalToolbar);
            registerBtn = findViewById(R.id.registerBtn);
            loginButton = findViewById(R.id.loginButton);
            email = findViewById(R.id.email_edit);
            password = findViewById(R.id.password_edit);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void setActions() {
            super.setActions();
            //change icon of tool bar
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close));
            registerBtn.setOnClickListener(v -> {
                Intent intent = new Intent(LogIn.this, UserType.class);
                startActivity(intent);
            });

        }


    }
