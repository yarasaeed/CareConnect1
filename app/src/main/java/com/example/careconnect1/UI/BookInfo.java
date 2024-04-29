package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;
import static com.example.careconnect1.Utilities.Config.OFFER_IMAGES_DIR;
import static com.example.careconnect1.Utilities.Config.USER_IMAGES_DIR;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.careconnect1.Adapters.BookReviewsAdapter;
import com.example.careconnect1.Model.ReviewsModel;
import com.example.careconnect1.Model.ServiceModel;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class BookInfo extends AppCompatClass {
    private  TextView date, payment_info, text_no_reviews,status;
    private String book_id;
    private  ArrayList<ReviewsModel> arrayList;
    private  TextView provider_name, parent_name;
    private  ShapeableImageView ic_help,icon_provider, icon_offer, icon_parent
            ,icon_call_provider,icon_call_parent, icon_email_provider, icon_email_parent;
    private  TextView offer_info;
    private LinearLayoutCompat layout_offer, layout_service ;
    RelativeLayout layout_provider, layout_parent;
    private ChipGroup chipGroup;

    RecyclerView recyclerView;
    private BookReviewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        setMethods("Book info","");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        ic_help =findViewById(R.id.ic_help);
        status =findViewById(R.id.status);
        text_no_reviews =findViewById(R.id.text_no_reviews);
        layout_provider =findViewById(R.id.layout_provider);
        layout_parent =findViewById(R.id.layout_parent);
        icon_email_parent =findViewById(R.id.icon_email_parent);
        icon_email_provider =findViewById(R.id.icon_email_provider);
        icon_call_parent=findViewById(R.id.icon_call_parent);
        icon_call_provider =findViewById(R.id.icon_call_provider);
        recyclerView =findViewById(R.id.recyclerView);
        icon_parent =findViewById(R.id.icon_parent);
        parent_name =findViewById(R.id.customer_name);
        payment_info =findViewById(R.id.payment_info);
        date =findViewById(R.id.booking_date);
        provider_name =findViewById(R.id.provider_name);
        icon_provider =findViewById(R.id.icon_provider);
        icon_offer =findViewById(R.id.icon_offer);
        offer_info =findViewById(R.id.offer_info);
        layout_offer =findViewById(R.id.layout_offer);
        layout_service =findViewById(R.id.layout_services);
        chipGroup =findViewById(R.id.chipGroup);
        recyclerView.setNestedScrollingEnabled(false);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){ // from customer booking adapter
            book_id = bundle.getString("book_id","");
        }
    }

    @Override
    public void setActions() {
        super.setActions();
        getInfo();
    }

    public void getInfo(){
        arrayList = new ArrayList<>();
        @SuppressLint("SetTextI18n")
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_book.php?book_id="+ book_id, response -> {
            int i = 0;
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));


                while (i < jsonArray.length()) {
                    JSONObject jSONObject = jsonArray.getJSONObject(i);
                    String bookingTime = jSONObject.getString("bookingTime");
                    String bookingDate = jSONObject.getString("bookingDate");
                    String id_book = jSONObject.getString("bookingID");
                    String book_status = jSONObject.getString("book_status");
                    String rejection_cause = jSONObject.getString("rejected_cause");

                    String parent_name = jSONObject.getJSONObject("user_info").getString("f_name") +
                            " "+  jSONObject.getJSONObject("user_info").getString("l_name");
                    String parent_email = jSONObject.getJSONObject("user_info").getString("email");
                    String parent_id = jSONObject.getJSONObject("user_info").getString("user_id");
                    String ic_cu = jSONObject.getJSONObject("user_info").getString("icon");
                    String customer_phone = jSONObject.getJSONObject("user_info").getString("phone_nb");

                    String cleaner_email = jSONObject.getJSONObject("center_info").getString("email");
                    String cleaner_phone = jSONObject.getJSONObject("center_info").getString("phone_nb");
                    String provider_name ;
                    if(jSONObject.getJSONObject("center_info").getString("UserRole").toLowerCase(Locale.ROOT).equals("center")){
                        provider_name = jSONObject.getJSONObject("center_info").getString("f_name");
                    }else{
                        provider_name = jSONObject.getJSONObject("center_info").getString("f_name") + " " + jSONObject.getJSONObject("center_info").getString("l_name") ;
                    }

                    String ic_cl = jSONObject.getJSONObject("center_info").getString("icon");
                    String provider_id = jSONObject.getJSONObject("center_info").getString("user_id");


                    String offer_id="", offer_price="",offer_time="",offer_description="",offer_icon="";
                    if(!jSONObject.isNull("offer_info")){
                        offer_id = jSONObject.getJSONObject("offer_info").getString("offer_id");
                        offer_description = jSONObject.getJSONObject("offer_info").getString("description");
                        offer_price = jSONObject.getJSONObject("offer_info").getString("price");
                        offer_time = jSONObject.getJSONObject("offer_info").getString("time");
                        offer_icon = jSONObject.getJSONObject("offer_info").getString("icon");
                    }

                    String payment_id = "", payemnt_type = "", payment_amount="";
                    if(!jSONObject.isNull("payment_info")) {
                        payment_amount = jSONObject.getJSONObject("payment_info").getString("AmountPaid");
                        payment_id = jSONObject.getJSONObject("payment_info").getString("PaymentID");
                        payemnt_type = jSONObject.getJSONObject("payment_info").getString("PaymentMethod");
                    }
                    ArrayList<ServiceModel> arrayService = new ArrayList<>();
                    if(!jSONObject.getJSONArray("services").isNull(0)) {
                        int j =0;
                        JSONArray jsonArrayServices = new JSONArray(jSONObject.getString("services"));

                        while (j < jsonArrayServices.length()) {
                            JSONObject jb = jsonArrayServices.getJSONObject(j);
                            String id =  jb.getString("ServiceID");
                            String name =  jb.getString("ServiceName");
                            String price =  jb.getString("ServicePrice");
                            String s_cleaner_id =  jb.getString("s_cleaner_id");
                            arrayService.add(new ServiceModel(id, name, price,s_cleaner_id));
                            j++;
                        }
                    }
                    status.setText(book_status.toUpperCase(Locale.ROOT));
                    if(book_status.toLowerCase(Locale.ROOT).equals("pending")){
                        status.setTextColor(getResources().getColor(R.color.pink));
                        ic_help.setVisibility(View.GONE);
                    }else  if(book_status.toLowerCase(Locale.ROOT).equals("accepted")){
                        status.setTextColor(getResources().getColor(R.color.purple));
                        ic_help.setVisibility(View.GONE);
                    }
                    else  if(book_status.toLowerCase(Locale.ROOT).equals("rejected")){
                        ic_help.setVisibility(View.VISIBLE);
                        status.setTextColor(getResources().getColor(R.color.red, null));
                        ic_help.setOnClickListener(v -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookInfo.this);
                            builder.setTitle("Reason of rejection");
                            builder.setMessage(rejection_cause);
                            builder.setPositiveButton("Close", null);
                            builder.show();
                        });
                    }
                    else{
                        ic_help.setVisibility(View.GONE);
                        status.setTextColor(getResources().getColor(R.color.purple));
                    }
                    layout_provider.setOnClickListener(view -> {
                        Intent intent = new Intent(BookInfo.this, UserProfile.class);
                        intent.putExtra("user_id", provider_id);
                        startActivity(intent);
                    });
                    layout_parent.setOnClickListener(view -> {
                        Intent intent = new Intent(BookInfo.this, UserProfile.class);
                        intent.putExtra("user_id", parent_id);
                        startActivity(intent);
                    });
                    icon_call_provider.setOnClickListener(view -> {
                        Intent intent=new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+cleaner_phone+""));
                        startActivity(intent);
                    });
                    icon_call_parent.setOnClickListener(view -> {
                        Intent intent=new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+customer_phone+""));
                        startActivity(intent);
                    });

                    icon_email_parent.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"+provider_email));
                        intent.putExtra(Intent.EXTRA_EMAIL, parent_email);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                        startActivity(intent);

                    });

                    icon_email_provider.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"+cleaner_email));
                        intent.putExtra(Intent.EXTRA_EMAIL, cleaner_email);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                        startActivity(intent);

                    });

                    this.offer_info.setText(offer_description + " (" + offer_price + "$)");
                    this.offer_info.setPaintFlags( this.offer_info.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    this.date.setText(bookingDate);
                    this.provider_name.setText(provider_name);
                    this.parent_name.setText(parent_name);

                    this.payment_info.setText(payment_amount + "$ ("+payemnt_type.toUpperCase(Locale.ROOT) + ")");
                    if(offer_id.equals("")){
                        layout_offer.setVisibility(View.GONE);
                        layout_service.setVisibility(View.VISIBLE);
                    }else{
                        layout_offer.setVisibility(View.VISIBLE);
                        layout_service.setVisibility(View.GONE);
                    }
                    for(int j = 0; j<arrayService.size();j++){
                        Chip chip=(Chip)View.inflate(BookInfo.this, R.layout.chip_items_read,null);
                        chip.setText(arrayService.get(j).getName()+" - "+arrayService.get(i).getPrice());
                        this.chipGroup.addView(chip);
                    }

                    Glide.with(BookInfo.this).load(OFFER_IMAGES_DIR + offer_icon)
                            .error(R.drawable.ic_image_broke)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(this.icon_offer);
                    Glide.with(BookInfo.this).load(USER_IMAGES_DIR + ic_cu)
                            .error(R.drawable.ic_image_broke)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(icon_parent);
                    Glide.with(BookInfo.this).load(USER_IMAGES_DIR + ic_cl)
                            .error(R.drawable.ic_image_broke)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(icon_provider);
                    i++;
                }

                getReviews();

            }catch (Exception | Error ignored){

            }

        }, error -> {

        });

        RequestQueue requestQueue = Volley.newRequestQueue(BookInfo.this);
        requestQueue.add(stringRequest);


    }

    public void getReviews(){
        arrayList = new ArrayList<>();
        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_review_book.php?book_id="+ book_id, response -> {
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
                    String center_id = jSONObject.getString("CleanerID");
                    String book_id = jSONObject.getString("r_booking_id");
                    String customer_name = jSONObject.getString("f_name") +" "+jSONObject.getString("l_name")  ;
                    String customer_icon = jSONObject.getString("icon");
                    String date = jSONObject.getString("date");
                    arrayList.add(new ReviewsModel(id, book_id,user_id,center_id, text,customer_name,customer_icon,date));
                    i++;
                }

                adapter = new BookReviewsAdapter(BookInfo.this, arrayList);
                recyclerView.setAdapter(adapter);
            }catch (Exception | Error ignored){

            }

        }, error -> {

        });
        RequestQueue requestQueue = Volley.newRequestQueue(BookInfo.this);
        requestQueue.add(stringRequest);
    }

}