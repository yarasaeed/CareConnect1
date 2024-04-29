package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;
import static com.example.careconnect1.Utilities.Config.USER_IMAGES_DIR;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.careconnect1.Adapters.BookReviewsAdapter;
import com.example.careconnect1.Model.ReviewsModel;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.example.careconnect1.Utilities.LoadingLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class UserProfile extends AppCompatClass {
    private TextView text_name, text_role, text_address, text_bio,text_no_reviews;
    private LottieAnimationView btn_offers;
    private ChipGroup chipGroup;
    private  ArrayList<ReviewsModel> arrayList;
    private LinearLayoutCompat layout_reviews;
    RecyclerView recyclerView;
    private BookReviewsAdapter adapter;
    private ShapeableImageView icon_email, icon_phone, icon, icon_eco;
    private LinearLayoutCompat layout_eco, layout_address,layout_services, layout_bio,layout_provider;
    private String user_id = "";
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setMethods("Profile", "");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        text_no_reviews = findViewById(R.id.text_no_reviews);
        layout_reviews = findViewById(R.id.layout_reviews);
        layout_eco = findViewById(R.id.layout_eco);
        icon_eco = findViewById(R.id.icon_eco);
        text_name = findViewById(R.id.text_name);
        icon = findViewById(R.id.icon);
        icon_email = findViewById(R.id.icon_mail);
        icon_phone = findViewById(R.id.icon_call);
        text_role = findViewById(R.id.text_role);
        btn_offers = findViewById(R.id.btn_offers);
        layout_provider = findViewById(R.id.layout_provider);
        text_address = findViewById(R.id.address);
        chipGroup = findViewById(R.id.chipGroup);
        layout_bio = findViewById(R.id.layout_bio);
        layout_services = findViewById(R.id.layout_services);
        text_bio = findViewById(R.id.bio);
        layout_address = findViewById(R.id.layout_address);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        arrayList = new ArrayList<>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle !=null){
            user_id = bundle.getString("user_id", "");
        }
    }


    @Override
    public void setActions() {
        super.setActions();
        getUserData();
        btn_offers.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfile.this, Offers.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        });

    }

    private void getUserData() {
        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_user.php?id="+ user_id, response -> {
            int i = 0;
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                while (i < jsonArray.length()) {
                    JSONObject jSONObject = jsonArray.getJSONObject(i);
                    String email = jSONObject.getString("email");
                    String phone = jSONObject.getString("phone_nb");
                    int offers_count = jSONObject.getInt("offers_count");

                    //calculateEco(prd);
                    icon_phone.setOnClickListener(view -> {
                        Intent intent=new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+phone+""));
                        startActivity(intent);
                    });

                    icon_email.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"+email));
                        intent.putExtra(Intent.EXTRA_EMAIL, email);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                        startActivity(intent);

                    });


                    text_role.setText(jSONObject.getString("UserRole") );
                    text_address.setText(jSONObject.getString("address") );
                    if(jSONObject.getString("UserRole").toLowerCase(Locale.ROOT).equals("company")){
                        text_name.setText(jSONObject.getString("f_name"));
                        layout_provider.setVisibility(View.VISIBLE);
                        text_bio.setText(jSONObject.getString("bio") );
                        if(offers_count == 0){
                            btn_offers.setVisibility(View.GONE);
                        }else {
                            btn_offers.setVisibility(View.VISIBLE);
                        }

                    }else  if(jSONObject.getString("UserRole").toLowerCase(Locale.ROOT).equals("individual")){
                        text_name.setText(jSONObject.getString("f_name") +" "+ jSONObject.getString("l_name"));
                        layout_provider.setVisibility(View.VISIBLE);
                        text_bio.setText(jSONObject.getString("bio") );
                        btn_offers.setVisibility(View.GONE);
                    }else{
                        text_name.setText(jSONObject.getString("f_name") +" "+ jSONObject.getString("l_name"));
                        layout_bio.setVisibility(View.GONE);
                        layout_address.setVisibility(View.VISIBLE);
                        layout_reviews.setVisibility(View.GONE);
                        btn_offers.setVisibility(View.GONE);
                        layout_services.setVisibility(View.GONE);
                        layout_eco.setVisibility(View.GONE);
                    }

                    Glide.with(UserProfile.this).load(USER_IMAGES_DIR + jSONObject.getString("icon"))
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .error(R.drawable.ic_user)
                            .into(icon);


                    i++;
                }
                getServices();
                getReviews();
            }catch (Exception | Error ignored){

            }

        }, error -> {

        });


        RequestQueue requestQueue = Volley.newRequestQueue(UserProfile.this);
        requestQueue.add(stringRequest);
    }

    private void getServices(){

        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_services_where.php?user_id="+ user_id, response -> {
            int i = 0;
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                while (i < jsonArray.length()) {
                    JSONObject jSONObject = jsonArray.getJSONObject(i);
                    final Chip chip=(Chip)View.inflate(UserProfile.this, R.layout.chip_items_read,null);
                    chip.setText(jSONObject.getString("ServiceName")+" - "+jSONObject.getString("ServicePrice"));
                    chipGroup.addView(chip);
                    i++;
                }
            }catch (Exception | Error ignored){

            }

        }, error -> {

        });
        RequestQueue requestQueue = Volley.newRequestQueue(UserProfile.this);
        requestQueue.add(stringRequest);

    }

    public void getReviews(){
        arrayList = new ArrayList<>();
        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_reviews_where_provider.php?provider_id="+ user_id, response -> {
            int i = 0;

            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                if(jsonArray.length() == 0){
                    text_no_reviews.setVisibility(View.VISIBLE);

                }else {
                    text_no_reviews.setVisibility(View.GONE);
                }

                while (i < jsonArray.length()) {
                    JSONObject jSONObject = jsonArray.getJSONObject(i);
                    String text = jSONObject.getString("ReviewText");
                    String user_id = jSONObject.getString("UserID");
                    String id = jSONObject.getString("ReviewID");
                    String provider_id = jSONObject.getString("ProviderID");
                    String book_id = jSONObject.getString("r_booking_id");
                    String provider_name = jSONObject.getString("f_name") +" "+jSONObject.getString("l_name")  ;
                    String provider_icon = jSONObject.getString("icon");
                    String date = jSONObject.getString("date");
                    arrayList.add(new ReviewsModel(id, book_id,user_id,provider_id, text,provider_name,provider_icon,date));
                    i++;
                }

                adapter = new BookReviewsAdapter(UserProfile.this, arrayList);
                recyclerView.setAdapter(adapter);

            }catch (Exception | Error ignored){

            }

        }, error -> {

        });
        RequestQueue requestQueue = Volley.newRequestQueue(UserProfile.this);
        requestQueue.add(stringRequest);
    }
}