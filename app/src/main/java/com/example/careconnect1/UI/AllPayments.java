package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.Adapters.AllPaymentsAdapter;
import com.example.careconnect1.Model.PaymentModel;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.example.careconnect1.Utilities.UserData;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class AllPayments extends AppCompatClass {
    private AllPaymentsAdapter adapter;
    private RecyclerView recyclerView;

    private ArrayList<PaymentModel> arrayList;
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_payments);
        setMethods("Payments Card", "");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        recyclerView = findViewById(R.id.recyclerView);
        userData = new UserData(AllPayments.this);
    }

    @Override
    public void setActions() {
        super.setActions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPaymentCards();
    }

    public void getPaymentCards(){
        arrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP + "select_payments_where.php?user_id="+ userData.getId(), response -> {
            int i = 0;
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                while (i < jsonArray.length()) {
                    JSONObject jSONObject = jsonArray.getJSONObject(i);
                    String id = jSONObject.getString("payment_id");
                    String user_id = jSONObject.getString("user_id");
                    String card_nb = jSONObject.getString("card_nb");
                    String expiration_date = jSONObject.getString("expiration_date");
                    String cvc = jSONObject.getString("CVV");
                    String payment_type = jSONObject.getString("payment_type");
                    String holder_name = jSONObject.getString("holder_name");
                    String used = jSONObject.getString("used");
                    arrayList.add(new PaymentModel(id,card_nb,holder_name,expiration_date,cvc,user_id,payment_type, used));
                    i++;
                }

                adapter = new AllPaymentsAdapter(AllPayments.this, arrayList);
                recyclerView.setAdapter(adapter);
            }catch (Exception | Error e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(AllPayments.this, error.toString(), Toast.LENGTH_SHORT).show()){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AllPayments.this);
        requestQueue.add(stringRequest);
    }
}