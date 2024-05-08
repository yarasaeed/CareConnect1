package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;
import static com.example.careconnect1.Utilities.Config.USER_IMAGES_DIR;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.LinearLayoutCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.careconnect1.Adapters.PaymentsCardsSpinner;
import com.example.careconnect1.Enumerations.PaymentMethod;
import com.example.careconnect1.Model.PaymentModel;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import com.example.careconnect1.Utilities.UserData;

public class Booking extends AppCompatClass implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private ChipGroup chipGroup;
    private String center_id, type, offer_id, selected_services = "";
    private StringBuilder stringBuilder;
    private boolean isDateSelected = false;
    private String [] disabled_dates ;

    private UserData userData;

    private String selected_date, selected_time;

    private TextView btn_book_date, name, role;

    private ShapeableImageView icon;

    private ArrayList<PaymentModel> arrayList;

    private LinearLayoutCompat layout_services;

    private MaterialButton btn_book;
    private RadioGroup radioGroup;
    private TextView text_add_card;
    private LinearLayoutCompat layout_card;
    private Spinner spinner;
    private PaymentsCardsSpinner adapter;

    ArrayList<Double> service_prices;
    private String payment_type = PaymentMethod.CASH.toString();
    private String payment_info_id = "";
    double amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        setMethods("Booking", "");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        spinner = findViewById(R.id.spinner);
        layout_card = findViewById(R.id.layout_card);
        text_add_card = findViewById(R.id.text_add_card);
        radioGroup = findViewById(R.id.radio_group);
        chipGroup = findViewById(R.id.chipGroup);
        layout_services = findViewById(R.id.layout_services);
        btn_book_date = findViewById(R.id.btn_book_date);
        icon = findViewById(R.id.icon);
        name = findViewById(R.id.name);
        role = findViewById(R.id.role);
        btn_book = findViewById(R.id.btn_book);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            center_id = bundle.getString("center_id","");
            type = bundle.getString("type","");
            offer_id = bundle.getString("offer_id","");
            amount = bundle.getDouble("amount",0.0);
        }

        userData = new UserData(Booking.this);
        stringBuilder = new StringBuilder();
        disabled_dates = new String[]{};
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void setActions() {
        super.setActions();
        getUserData();
        if(type.equals("offer")){
            layout_services.setVisibility(View.GONE);

        }else{
            getServices();
        }

        btn_book_date.setOnClickListener(v -> showDatePicker());
        btn_book.setOnClickListener(v -> beforeSendRequest());

        text_add_card.setOnClickListener(v -> {
            Intent intent = new Intent(Booking.this, PaymentInfo.class);
            intent.putExtra("user_id", center_id);
            startActivity(intent);
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_cash) {
                layout_card.setVisibility(View.GONE);
                payment_type = PaymentMethod.CASH.toString();
            } else if (checkedId == R.id.radio_card) {
                layout_card.setVisibility(View.VISIBLE);
            }
        });



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(radioGroup.getCheckedRadioButtonId() == R.id.radio_card){
                    payment_type = arrayList.get(position).getType();
                    payment_info_id = arrayList.get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPaymentCards();
    }

    private void beforeSendRequest(){
        if(layout_services.getVisibility() == View.VISIBLE){
            stringBuilder = new StringBuilder();
            for(int i=0;i<chipGroup.getChildCount();i++){
                Chip chip2 = (Chip) chipGroup.getChildAt(i);
                if(chip2.isChecked()){
                    amount = amount + service_prices.get(i);
                    stringBuilder.append(chip2.getTag()).append(",");
                }
            }
            if(stringBuilder.length() > 0){
                selected_services = stringBuilder.substring(0, stringBuilder.length() -1);
                if(isDateSelected){
                    sendRequest();
                }else{
                    Toast.makeText(this, "Select booking date and time", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Select at least 1 service", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(isDateSelected){
                sendRequest();
            }else{
                Toast.makeText(this, "Select booking date and time", Toast.LENGTH_SHORT).show();
            }
        }
    }
//in case user added new card we call it on resume to refresh the spinner
    private void getPaymentCards(){
        arrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP + "select_payments_card_where.php?user_id="+ userData.getId(), response -> {
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
                    arrayList.add(new PaymentModel(id,card_nb,holder_name,expiration_date,cvc,user_id,payment_type, ""));
                    i++;
                }

                adapter = new PaymentsCardsSpinner(Booking.this, arrayList);
                spinner.setAdapter(adapter);
            }catch (Exception | Error e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(Booking.this, error.toString(), Toast.LENGTH_SHORT).show()){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Booking.this);
        requestQueue.add(stringRequest);
    }
    private void sendRequest(){
        if(!payment_type.equals("cash")){
            if(payment_info_id.equals("")){
                Toast.makeText(this, "Select payment information", Toast.LENGTH_SHORT).show();
            }else{
              addBook();
            }
        }else {
            addBook();
        }

    }

    private void addBook(){
        Random rand = new Random();
        int max=1000000,min=10000;
        int randomNum = rand.nextInt(max - min + 1) + min;
        String payment_id = ""+ randomNum;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP + "insert_book.php", response -> {
            Toast.makeText(Booking.this, response.trim(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Booking.this, MainActivity.class);
            intent.putExtra("tab","2");
            startActivity(intent);
            finish();
        }, error -> Toast.makeText(Booking.this, error.toString(), Toast.LENGTH_SHORT).show()){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("date",selected_date);
                map.put("time",selected_time);
                map.put("services_ids",selected_services);
                map.put("user_id",userData.getId());
                map.put("offer_id",offer_id);
                map.put("center_id", center_id);
                map.put("payment_type",payment_type);
                map.put("payment_info_id",payment_info_id);
                map.put("amount",""+amount);
                map.put("payment_date",getCurrentDate());
                map.put("payment_id","" + payment_id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Booking.this);
        requestQueue.add(stringRequest);
    }


    private void getUserData() {

        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_user.php?id="+ center_id, response -> {
            int i = 0;
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                while (i < jsonArray.length()) {
                    JSONObject jSONObject = jsonArray.getJSONObject(i);

                    String disabled_date_string = jSONObject.getString("disabled_date").trim();
                     disabled_dates = disabled_date_string.split(",");
                     if(jSONObject.getString("UserRole").toLowerCase(Locale.ROOT).equals("center")){
                         name.setText(jSONObject.getString("f_name"));
                     }else{
                         name.setText(jSONObject.getString("f_name") + " " + jSONObject.getString("l_name"));
                     }
                    role.setText(jSONObject.getString("UserRole").toUpperCase(Locale.ROOT));
                    Glide.with(Booking.this).load(USER_IMAGES_DIR + jSONObject.getString("icon"))
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .error(R.drawable.ic_user)
                            .into(icon);

                    i++;

                }

             }catch (Exception | Error ignored){

            }

        }, error -> {

        });


        RequestQueue requestQueue = Volley.newRequestQueue(Booking.this);
        requestQueue.add(stringRequest);
    }

    private void getServices(){
        service_prices = new ArrayList<>();
        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_services_where.php?user_id="+ center_id, response -> {
            int i = 0;
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                while (i < jsonArray.length()) {
                    JSONObject jSONObject = jsonArray.getJSONObject(i);
                    final Chip chip=(Chip) View.inflate(Booking.this, R.layout.chip_items_select,null);
                    chip.setText(jSONObject.getString("ServiceName")+" - "+jSONObject.getString("ServicePrice"));
                    chip.setTag(jSONObject.getString("ServiceID"));
                    chipGroup.addView(chip);
                    service_prices.add(Double.parseDouble(jSONObject.getString("ServicePrice")));
                    i++;
                }
            }catch (Exception | Error ignored){

            }

        }, error -> {

        });
        RequestQueue requestQueue = Volley.newRequestQueue(Booking.this);
        requestQueue.add(stringRequest);

    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePicker.show(getSupportFragmentManager(), "Datepickerdialog");


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        if(disabled_dates.length > 0){
            if(!disabled_dates[0].equals("")) {
                for (String holiday : disabled_dates) {
                    try {
                        date = sdf.parse(holiday);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    calendar = dateToCalendar(date);
                    List<Calendar> dates = new ArrayList<>();
                    dates.add(calendar);
                    Calendar[] disabledDays1 = dates.toArray(new Calendar[0]);
                    datePicker.setDisabledDays(disabledDays1);

                }
            }}

        //Set min date
        Calendar calendar2 = Calendar.getInstance();
        datePicker.setMinDate(calendar2);

        //Set theme
        datePicker.setAccentColor(getResources().getColor(R.color.purple, null));

    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        selected_date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this,
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.setAccentColor(getResources().getColor(R.color.purple,null));
        timePickerDialog.show(getSupportFragmentManager(), "DateTimePicker");
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        selected_time = hourOfDay+":"+minute;
        btn_book_date.setText(selected_date + " - "+ selected_time);
        isDateSelected = true;
    }

    private String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(c);
    }
}