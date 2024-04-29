package com.example.careconnect1.Fragments;

import static com.example.careconnect1.Utilities.Config.IP;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.Adapters.providerrBookingAdapter;
import com.example.careconnect1.Interface.FragmentRefresh;
import com.example.careconnect1.Model.BookingModel;
import com.example.careconnect1.Model.ServiceModel;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.UserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentBookingProvider extends Fragment implements FragmentRefresh {

    private TextView text_no_book;
    private UserData userData;

    private RecyclerView recyclerView;

    private ArrayList<BookingModel> arrayList;

    private providerrBookingAdapter adapter;

    public FragmentBookingProvider() {
    }


    public static FragmentBookingProvider newInstance() {
        return new FragmentBookingProvider();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_booking_provider, container, false);
        setInitialize(v);
        setActions(v);

        return v;
    }

    private void setInitialize(View v){
        text_no_book = v.findViewById(R.id.text_no_book);
        recyclerView = v.findViewById(R.id.recyclerView);
    }

    private void setActions(View v){
        if(userData.isLogin()){

            getBooking();
        }
    }



    public void getBooking(){


        arrayList = new ArrayList<>();
        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_cleaner_booking.php?cleaner_id="+ userData.getId(), response -> {
            int i = 0;
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                if(jsonArray.length() == 0){
                    text_no_book.setVisibility(View.VISIBLE);
                    text_no_book.setText("You didn't receive any booking request");
                }else {
                    text_no_book.setVisibility(View.GONE);
                }

                while (i < jsonArray.length()) {
                    JSONObject jSONObject = jsonArray.getJSONObject(i);
                    String bookingTime = jSONObject.getString("bookingTime");
                    String bookingDate = jSONObject.getString("bookingDate");
                    String id_book = jSONObject.getString("bookingID");
                    String book_status = jSONObject.getString("book_status");
                    String rejected_cause = jSONObject.getString("rejected_cause");

                    String center_id = jSONObject.getString("user_id");
                    String parent_email = "", parent_id="", parent_name="", parent_icon="";

                    if(!jSONObject.isNull("customer_info")) {

                        parent_email = jSONObject.getJSONObject("parent_info").getString("email");
                        parent_name = jSONObject.getJSONObject("parent_info").getString("f_name") + " " + jSONObject.getJSONObject("parent_info").getString("l_name");
                        parent_id = jSONObject.getJSONObject("parent_info").getString("user_id");
                        parent_icon = jSONObject.getJSONObject("parent_info").getString("icon");
                    }
                    String offer_id="", offer_price="",offer_time="",offer_description="",offer_icon="";
                    if(!jSONObject.isNull("offer_info")){
                        offer_id = jSONObject.getJSONObject("offer_info").getString("offer_id");
                        offer_description = jSONObject.getJSONObject("offer_info").getString("description");
                        offer_price = jSONObject.getJSONObject("offer_info").getString("price");
                        offer_time = jSONObject.getJSONObject("offer_info").getString("time");
                        offer_icon = jSONObject.getJSONObject("offer_info").getString("icon");
                    }
                    String payment_id = "", payemnt_type = "", payment_amount = "";
                    if(!jSONObject.isNull("payment_info")) {
                        payment_id = jSONObject.getJSONObject("payment_info").getString("PaymentID");
                        payemnt_type = jSONObject.getJSONObject("payment_info").getString("PaymentMethod");
                        payment_amount = jSONObject.getJSONObject("payment_info").getString("AmountPaid");
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


                    arrayList.add(new BookingModel(id_book,bookingDate,bookingTime,parent_id, parent_name, parent_email,parent_icon,
                            center_id, "", "", "",book_status, offer_id, offer_price,
                            offer_time, offer_description, offer_icon, payment_id,payemnt_type,payment_amount,rejected_cause,arrayService));
                    i++;
                }

                adapter = new providerrBookingAdapter(getActivity(), arrayList, this);
                recyclerView.setAdapter(adapter);




            }catch (Exception | Error ignored){


            }


        }, error -> {

        });
        if(getActivity() != null){
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }

    }

    @Override
    public void refreshBookingProvider() {
        getBooking();
    }

    @Override
    public void refreshProfile() {

    }

    @Override
    public void refreshBookingParent() {

    }
}