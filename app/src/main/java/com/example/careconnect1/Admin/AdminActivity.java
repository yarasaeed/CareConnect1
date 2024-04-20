package com.example.careconnect1.Admin;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.example.careconnect1.R;
import com.example.careconnect1.UI.AllPayments;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.example.careconnect1.Utilities.UserData;
import com.google.android.material.card.MaterialCardView;

public class AdminActivity extends AppCompatClass {
    private MaterialCardView card_user, card_payments, card_images;

    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setMethods("Administrator","");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        card_user  =findViewById(R.id.card_user);
        card_payments  =findViewById(R.id.card_payments);
        card_images  =findViewById(R.id.card_images);
        userData = new UserData(AdminActivity.this);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void setActions() {
        super.setActions();

        card_user.setOnClickListener(v -> openActivity(AllUsers.class));
        card_payments.setOnClickListener(v -> openActivity(AllPayments.class));
        card_images.setOnClickListener(v -> openActivity(AllImages.class));
    }

    private void openActivity(Class<?> activity){
        Intent intent = new Intent(AdminActivity.this, activity);
        startActivity(intent);
    }
}