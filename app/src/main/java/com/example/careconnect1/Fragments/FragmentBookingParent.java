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
import com.example.careconnect1.Adapters.ParentBookingAdapter;
import com.example.careconnect1.Interface.FragmentRefresh;
import com.example.careconnect1.Model.BookingModel;
import com.example.careconnect1.Model.ServiceModel;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.UserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class FragmentBookingParent extends Fragment implements FragmentRefresh {

    private TextView  text_no_book;
    private UserData userData;

    private RecyclerView recyclerView;

    private ParentBookingAdapter adapter;

    private ArrayList<BookingModel> arrayList;

    private TextView btn_book, btn_book_2;
    public FragmentBookingParent() {
    }


    public static FragmentBookingParent newInstance() {
        return new FragmentBookingParent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_booking, container, false);
        setInitialize(v);
        return v;
    }

    private void setInitialize(View v){
        text_no_book = v.findViewById(R.id.text_no_book);
        btn_book = v.findViewById(R.id.btn_book);
        btn_book_2 = v.findViewById(R.id.btn_book_2);
        recyclerView = v.findViewById(R.id.recyclerView);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() != null){
            userData = new UserData(getActivity());
            if(userData.isLogin()){
                getBooking();
            }
        }
    }
    public void getBooking(){
        arrayList = new ArrayList<>();
        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_parent_booking.php?user_id=" + userData.getId(), response -> {
            int i = 0;
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                if (jsonArray.length() == 0) {
                    text_no_book.setVisibility(View.VISIBLE);
                    btn_book_2.setVisibility(View.VISIBLE);
                    btn_book.setVisibility(View.GONE);
                    text_no_book.setText("You didn't send any booking request");
                } else {
                    text_no_book.setVisibility(View.GONE);
                    btn_book_2.setVisibility(View.GONE);
                    btn_book.setVisibility(View.VISIBLE);
                }

                while (i < jsonArray.length()) {
                    JSONObject jSONObject = jsonArray.getJSONObject(i);
                    String bookingTime = jSONObject.getString("bookingTime");
                    String bookingDate = jSONObject.getString("bookingDate");
                    String id_book = jSONObject.getString("bookingID");
                    String book_status = jSONObject.getString("book_status");
                    String rejected_cause = jSONObject.getString("rejected_cause");

                    String parent_name = jSONObject.getString("f_name") +
                            " " + jSONObject.getString("l_name");
                    String parent_email = jSONObject.getString("email");
                    String parent_id = jSONObject.getString("UserID");
                    String parent_icon = jSONObject.getString("icon");

                    String center_email = jSONObject.getJSONObject("center_info").getString("email");
                    String center_name;
                    if (jSONObject.getJSONObject("center_info").getString("UserRole").toLowerCase(Locale.ROOT).equals("center")) {
                        center_name = jSONObject.getJSONObject("center_info").getString("f_name");
                    } else {
                        center_name = jSONObject.getJSONObject("center_info").getString("f_name") + " " + jSONObject.getJSONObject("center_info").getString("l_name");
                    }

                    String center_id = jSONObject.getJSONObject("center_info").getString("user_id");
                    String center_icon = jSONObject.getJSONObject("center_info").getString("icon");


                    String offer_id = "", offer_price = "", offer_time = "", offer_description = "", offer_icon = "";
                    if (!jSONObject.isNull("offer_info")) {
                        offer_id = jSONObject.getJSONObject("offer_info").getString("offer_id");
                        offer_description = jSONObject.getJSONObject("offer_info").getString("description");
                        offer_price = jSONObject.getJSONObject("offer_info").getString("price");
                        offer_time = jSONObject.getJSONObject("offer_info").getString("time");
                        offer_icon = jSONObject.getJSONObject("offer_info").getString("icon");
                    }

                    String payment_id = "", payemnt_type = "", payment_amount = "";
                    if (!jSONObject.isNull("payment_info")) {
                        payment_id = jSONObject.getJSONObject("payment_info").getString("PaymentID");
                        payemnt_type = jSONObject.getJSONObject("payment_info").getString("PaymentMethod");
                        payment_amount = jSONObject.getJSONObject("payment_info").getString("AmountPaid");
                    }
                    ArrayList<ServiceModel> arrayService = new ArrayList<>();
                    if (!jSONObject.getJSONArray("services").isNull(0)) {
                        int j = 0;
                        JSONArray jsonArrayServices = new JSONArray(jSONObject.getString("services"));

                        while (j < jsonArrayServices.length()) {
                            JSONObject jb = jsonArrayServices.getJSONObject(j);
                            String id = jb.getString("ServiceID");
                            String name = jb.getString("ServiceName");
                            String price = jb.getString("ServicePrice");
                            String s_center_id = jb.getString("s_center_id");
                            arrayService.add(new ServiceModel(id, name, price, s_center_id));
                            j++;
                        }
                    }


                    arrayList.add(new BookingModel(id_book, bookingDate, bookingTime, parent_id, parent_name, parent_email,
                            parent_icon, center_id, center_name, center_email, center_icon, book_status, offer_id, offer_price,
                            offer_time, offer_description, offer_icon, payment_id, payemnt_type, payment_amount, rejected_cause, arrayService));
                    i++;
                }
                adapter = new ParentBookingAdapter(getActivity(), arrayList, this);
                recyclerView.setAdapter(adapter);

            } catch (Exception | Error ignored) {
            }

        }, error -> {

        });
        if (getActivity() != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }

    }

    @Override
    public void refreshBookingProvider() {

    }

    @Override
    public void refreshProfile() {

    }

    @Override
    public void refreshBookingParent() {
        getBooking();

    }
}