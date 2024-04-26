package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.Enumerations.PaymentMethod;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.example.careconnect1.Utilities.UserData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentInfo extends AppCompatClass {
    private TextInputEditText number,name, mm,yy,cvc;
    private  String type = PaymentMethod.CREDIT.toString();
    private RadioGroup radioGroup;
    private MaterialButton btn_save;
    private UserData userData;
    private TextInputLayout layout_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_info);
        setMethods("Payment Information","");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        radioGroup = findViewById(R.id.radio_group);
        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        mm = findViewById(R.id.mm);
        yy = findViewById(R.id.yy);
        cvc = findViewById(R.id.cvc);
        btn_save = findViewById(R.id.btn_save);
        layout_number = findViewById(R.id.layout_number);
        userData = new UserData(PaymentInfo.this);

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void setActions() {
        super.setActions();
        btn_save.setOnClickListener(v -> addCard());
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.credit_card) {
                Toast.makeText(PaymentInfo.this, "Credit card selected", Toast.LENGTH_SHORT).show();
                type = PaymentMethod.CREDIT.toString();
                layout_number.setEndIconDrawable(getResources().getDrawable(R.drawable.ic_credit_card, null));
            }
            else if(checkedId == R.id.debit_card) {
                Toast.makeText(PaymentInfo.this, "Debit card selected", Toast.LENGTH_SHORT).show();
                type = PaymentMethod.DEBIT.toString();
                layout_number.setEndIconDrawable(getResources().getDrawable(R.drawable.ic_debit, null));
            }
            else if(checkedId == R.id.paypal){
                    Toast.makeText(PaymentInfo.this, "Paypal selected", Toast.LENGTH_SHORT).show();
                    type = PaymentMethod.PAYPAL.toString();
                    layout_number.setEndIconDrawable(getResources().getDrawable(R.drawable.ic_paypal, null));
            }
        });
    }

    private void addCard(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP + "add_card_info.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.toLowerCase(Locale.ROOT).trim().contains("added")){
                    finish();
                }else{

                }
                Toast.makeText(PaymentInfo.this, response.trim(), Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(PaymentInfo.this, error.toString(), Toast.LENGTH_SHORT).show()){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", userData.getId());
                map.put("mm",mm.getText().toString());
                map.put("yy",yy.getText().toString());
                map.put("number",number.getText().toString());
                map.put("name",name.getText().toString());
                map.put("cvc",cvc.getText().toString());
                map.put("type", type + "");
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PaymentInfo.this);
        requestQueue.add(stringRequest);

    }
}