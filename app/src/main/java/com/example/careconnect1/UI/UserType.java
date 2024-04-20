package com.example.careconnect1.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
//appears in sign up choose the role before sign up
public class UserType extends AppCompatClass {
    private MaterialButton btn_baby_sitter, btn_childcare_center, btn_parent;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        setMethods("","");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        btn_parent = findViewById(R.id.btn_parent);
        btn_childcare_center = findViewById(R.id.btn_childcare_center);
        btn_baby_sitter = findViewById(R.id.btn_baby_sitter);
        toolbar = findViewById(R.id.generalToolbar);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void setActions() {
        super.setActions();
        btn_childcare_center.setOnClickListener(v -> openLogin("center"));
        btn_baby_sitter.setOnClickListener(v -> openLogin("individual"));
        btn_parent.setOnClickListener(v -> openLogin("customer"));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close));
    }

    private void openLogin(String type){
        Intent intent = new Intent(UserType.this, SignUp.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}