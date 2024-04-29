package com.example.careconnect1.Fragments;

import static com.example.careconnect1.Utilities.Config.IP;
import static com.example.careconnect1.Utilities.Config.USER_IMAGES_DIR;
import static com.example.careconnect1.Utilities.GetImagePath.getRealPath;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.careconnect1.FileUpload.ImageUploaderClass;
import com.example.careconnect1.Interface.FragmentRefresh;
import com.example.careconnect1.R;
import com.example.careconnect1.UI.AddService;
import com.example.careconnect1.UI.Offers;
import com.example.careconnect1.UI.ProviderOffers;
import com.example.careconnect1.UI.ProviderReviews;
import com.example.careconnect1.UI.Reviews;
import com.example.careconnect1.UI.LogIn;
import com.example.careconnect1.UI.MainActivity;
import com.example.careconnect1.Utilities.LoadingLayout;
import com.example.careconnect1.Utilities.UserData;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FragmentProfile extends Fragment implements FragmentRefresh, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private ChipGroup chipGroupServices;
    private TextView text_login;
    private TextInputLayout layout_fname,layout_lname;
    private LinearLayoutCompat layout_provider;
    boolean edit = false;
    private ScrollView layout_update;
    private RelativeLayout layout_login;
    private UserData userData;
    private boolean isDateSelected = false;
    private ArrayList<String> disabled_dates,products_ids ;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    Uri uriImage = null;
    String nameOfImage = "";
    private TextInputEditText fname, lname, email, phone, password, address, bio, gender;
    private TextView text_name, text_email, text_role;

    private ShapeableImageView btn_reviews, btn_offers;
    private ShapeableImageView btn_logout;
    private TextView btn_add_service,btn_add_date;

    private ShapeableImageView icon, icon_edit,icon_save;

    public FragmentProfile() {
    }


    public static FragmentProfile newInstance() {
        return new FragmentProfile();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        setInitialize(v);
        setActions(v);


        return v;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void edit(){
        fname.setEnabled(edit);
        lname.setEnabled(edit);
        phone.setEnabled(edit);
        address.setEnabled(edit);
        bio.setEnabled(edit);
        gender.setEnabled(edit);
        fname.setSelected(true);
        fname.setFocusable(true);
        if(edit){
            icon_save.setVisibility(View.VISIBLE);
            icon_edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_2,null));
        }else {
            icon_save.setVisibility(View.INVISIBLE);
            icon_edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit,null));

        }



    }

    private void setInitialize(View v) {
        icon_edit = v.findViewById(R.id.icon_edit);
        icon_save = v.findViewById(R.id.icon_save);
        layout_update = v.findViewById(R.id.layout_update);
        text_login = v.findViewById(R.id.text_login);
        layout_login = v.findViewById(R.id.layout_login);
        icon = v.findViewById(R.id.icon);
        text_login = v.findViewById(R.id.text_login);
        fname = v.findViewById(R.id.fn_edit);
        lname = v.findViewById(R.id.ln_edit);
        email = v.findViewById(R.id.email_edit);
        phone = v.findViewById(R.id.phone_edit);
        password = v.findViewById(R.id.password_edit);

        text_name = v.findViewById(R.id.text_name);
        text_email = v.findViewById(R.id.text_email);
        text_role = v.findViewById(R.id.text_role);
        btn_add_service = v.findViewById(R.id.btn_add_service);
        bio = v.findViewById(R.id.bio_edit);
        address = v.findViewById(R.id.address_edit);
        gender = v.findViewById(R.id.gender_edit);
        layout_provider = v.findViewById(R.id.layout_provider);
        layout_lname = v.findViewById(R.id.ln_layout);
        layout_fname = v.findViewById(R.id.fn_layout);
        btn_offers = v.findViewById(R.id.btn_offers);
        btn_reviews = v.findViewById(R.id.btn_reviews);
        btn_logout = v.findViewById(R.id.btn_logout);
        if(getActivity() !=null){
            userData = new UserData(getActivity());
        }


    }

    private void setActions(View v) {
        icon_edit.setOnClickListener(v16 -> {
            edit = !edit;
            edit();
        });
        icon.setOnClickListener(v2 -> checkPermission());
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            uriImage = result.getData().getData();
                        }
                        if (uriImage != null) {
                            Glide.with(this).load(uriImage).into(icon);
                            uploadImage();

                        }
                    }
                });
        text_login.setOnClickListener(v1 -> {
            Intent intent = new Intent(getActivity(), LogIn.class);
            startActivity(intent);
        });

        btn_add_service.setOnClickListener(v12 -> {
            Intent intent = new Intent(getActivity(), AddService.class);
            startActivity(intent);
        });

        btn_offers.setOnClickListener(v12 -> {
            Intent intent = new Intent(getActivity(), ProviderOffers.class);
            startActivity(intent);
        });
        btn_reviews.setOnClickListener(v12 -> {
            Intent intent = new Intent(getActivity(), ProviderReviews.class);
            startActivity(intent);
        });
        icon_save.setOnClickListener(v13 -> {
            edit = !edit;
            edit();
            updateProfile();
        });
        btn_logout.setOnClickListener(v14 -> {
            if(getActivity() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Logout");
                builder.setMessage("Are you sure?");
                builder.setIcon(R.drawable.ic_logout);
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    userData.logout();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("tab", "1");
                    startActivity(intent);
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }


        });
        btn_add_date.setOnClickListener(v15 -> showDatePicker());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            userData = new UserData(getActivity());
            if (userData.isLogin()) {
                layout_login.setVisibility(View.GONE);
                layout_update.setVisibility(View.VISIBLE);
                getUserData();
            }else {
                layout_login.setVisibility(View.VISIBLE);
                layout_update.setVisibility(View.GONE);
            }
        }
    }

    private void getUserData() {
        if (getActivity() != null) {
            LoadingLayout.show(getActivity());
            disabled_dates = new ArrayList<>();
            products_ids = new ArrayList<>();
            chipGroupServices.removeAllViews();
            @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_user.php?id="+ userData.getId(), response -> {
                int i = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    while (i < jsonArray.length()) {
                        JSONObject jSONObject = jsonArray.getJSONObject(i);
                        fname.setText(jSONObject.getString("f_name"));
                        lname.setText(jSONObject.getString("l_name"));
                        email.setText(jSONObject.getString("email"));
                        phone.setText(jSONObject.getString("phone_nb"));
                        password.setText(jSONObject.getString("password"));
                        text_email.setText(jSONObject.getString("email"));
                        text_name.setText(jSONObject.getString("f_name") +" "+ jSONObject.getString("l_name"));
                        text_role.setText(jSONObject.getString("UserRole") );
                        nameOfImage = jSONObject.getString("icon");
                        bio.setText(jSONObject.getString("bio") );
                        address.setText(jSONObject.getString("address") );
                        if (getActivity() != null) {
                            Glide.with(getActivity()).load(USER_IMAGES_DIR + jSONObject.getString("icon"))
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .error(R.drawable.ic_user)
                                    .into(icon);
                        }

                        if(jSONObject.getString("UserRole").equals("parent")){
                            layout_provider.setVisibility(View.GONE);
                            btn_offers.setVisibility(View.GONE);
                            btn_reviews.setVisibility(View.GONE);
                        }else   if(jSONObject.getString("UserRole").equals("individual")){
                            btn_offers.setVisibility(View.GONE);

                        }else   if(jSONObject.getString("UserRole").equals("center")) {
                            layout_fname.setHint("Name");
                            layout_lname.setVisibility(View.GONE);
                            lname.setText("-");
                        }else{
                            layout_provider.setVisibility(View.GONE);
                            btn_offers.setVisibility(View.GONE);
                            btn_reviews.setVisibility(View.GONE);
                        }
                        i++;
                    }
                    getServices();
                }catch (Exception | Error ignored){

                }

            }, error -> {

            });


            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }


    private void getServices(){
        if (getActivity() != null) {
            @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_services_where.php?user_id="+ userData.getId(), response -> {
                int i = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    while (i < jsonArray.length()) {
                        JSONObject jSONObject = jsonArray.getJSONObject(i);
                        final Chip chip=(Chip)View.inflate(getActivity(), R.layout.chip_items,null);
                        chip.setText(jSONObject.getString("ServiceName")+" - "+jSONObject.getString("ServicePrice"));
                        chipGroupServices.addView(chip);
                        chip.setOnCloseIconClickListener(view1 -> {chipGroupServices.removeView(chip);
                            try {
                                removeService(jSONObject.getString("ServiceID"));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        i++;
                    }
                    LoadingLayout.hide(getActivity());
                }catch (Exception | Error ignored){

                }

            }, error -> {

            });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }

    private void removeService(String service_id){
        if (getActivity() != null) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "delete_service.php?id="+ service_id, response -> {
                Toast.makeText(getActivity(), response.trim(), Toast.LENGTH_SHORT).show();
            }, error -> {
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }


    private void updateProfile(){
        if(getActivity() != null){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP + "update_user.php",
                response -> Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show(), error -> {

        }){


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("fname",fname.getText().toString());
                map.put("lname",lname.getText().toString());
                map.put("email",email.getText().toString());
                map.put("password",password.getText().toString());
                map.put("phone",phone.getText().toString());
                map.put("user_id", userData.getId());
                map.put("icon",nameOfImage);
                map.put("address",address.getText().toString());
                map.put("bio",bio.getText().toString());
                map.put("products_ids", products_ids.toString().replace(" ","").replace("[","").replace("]",""));
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

        }
    }

    
    //Change Image
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            openGallery();
        } else {
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
    public void openGallery() {
        if(getActivity() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    activityResultLauncher.launch(intent);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
                }
            } else {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    activityResultLauncher.launch(intent);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }
            }
        }
    }



    public void checkPermission() {
        if(getActivity() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
                }
            } else {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }
            }
        }

    }
    private void uploadImage(){
        String filePath = getRealPath(getActivity(), uriImage);
        ImageUploaderClass.uploadImage(filePath, nameOfImage, "images/users", new ImageUploaderClass.onSuccessfulTask() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "Image changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String error) {

            }
        });
    }



    @Override
    public void refreshBookingProvider() {

    }

    @Override
    public void refreshProfile() {


    }

    @Override
    public void refreshBookingParent() {

    }



    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePicker.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        if(disabled_dates.size() > 0){
            if(!disabled_dates.get(0).equals("")) {
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

    private String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(c);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }
}