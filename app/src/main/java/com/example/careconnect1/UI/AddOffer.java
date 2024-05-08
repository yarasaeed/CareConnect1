package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;
import static com.example.careconnect1.Utilities.GetImagePath.getRealPath;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.careconnect1.FileUpload.ImageUploaderClass;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.example.careconnect1.Utilities.UserData;

public class AddOffer extends AppCompatClass {
    private UserData userData;
    private TextInputEditText description,  price;


    private ShapeableImageView icon;
    private MaterialButton btn_add,date;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    Uri uriImage = null;
    String nameOfImage="", typeOfImage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);
        setMethods("Add Offer", "");
    }

    @Override
    public void setActions() {
        super.setActions();
        btn_add.setOnClickListener(v -> {
            if(uriImage != null){
                uploadImage();
                add();
            }else {
                Toast.makeText(AddOffer.this, "Choose image", Toast.LENGTH_SHORT).show();
            }
        });
        icon.setOnClickListener(v -> checkPermission());
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            uriImage = result.getData().getData();
                            ContentResolver cr = this.getContentResolver();
                            typeOfImage = cr.getType(uriImage);

                        }
                        if (uriImage != null) {
                            Glide.with(this).load(uriImage).into(icon);
                            nameOfImage = "icon_"+System.currentTimeMillis()+"."+typeOfImage.replace("image/","");

                        }
                    }
                });
        date.setOnClickListener(v -> {
            Calendar c=Calendar.getInstance();
            int mYear=c.get(Calendar.YEAR);
            int mMonth=c.get(Calendar.MONTH);
            int mDay=c.get(Calendar.DAY_OF_MONTH);
            @SuppressLint("SetTextI18n") final DatePickerDialog datePickerDialog=new DatePickerDialog(AddOffer.this, (datePicker, year, monthOfYear, dayOfMonth) -> {
                int a=Integer.parseInt(String.valueOf(monthOfYear))+1;
                date.setText(year+"-"+a+"-"+dayOfMonth);
            },mYear,mMonth,mDay);
            datePickerDialog.show();


        });
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        userData = new UserData(AddOffer.this);
        price= findViewById(R.id.price_edit);
        icon= findViewById(R.id.icon);
        date= findViewById(R.id.date_edit);
        description= findViewById(R.id.description_edit);
        btn_add= findViewById(R.id.btn_add);
    }

    private void add(){
        @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"}) StringRequest stringRequest = new StringRequest(Request.Method.POST, IP + "add_offer.php", response -> {
            if(!response.toLowerCase(Locale.ROOT).contains("success")){
                Toast.makeText(AddOffer.this, response.trim(), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AddOffer.this, "New offer add successfully", Toast.LENGTH_SHORT).show();
                date.setText("");
                price.setText("");
                description.setText("");
                date.setText("Select date");
                icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_image));

            }
        }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("description",description.getText().toString());
                map.put("price",price.getText().toString());
                map.put("date",date.getText().toString().toLowerCase(Locale.ROOT).replace("select date",""));
                map.put("user_id",userData.getId());
                map.put("icon",nameOfImage);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddOffer.this);
        requestQueue.add(stringRequest);
    }
    //Settings to choose image from gallery
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            openGallery();
        } else {
            Toast.makeText(AddOffer.this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
    public void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(AddOffer.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher.launch(intent);
            } else {
                ActivityCompat.requestPermissions(AddOffer.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            }
        }else{
            if (ActivityCompat.checkSelfPermission(AddOffer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher.launch(intent);
            } else {
                ActivityCompat.requestPermissions(AddOffer.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(AddOffer.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }else{
                ActivityCompat.requestPermissions(AddOffer.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            }
        }else{

            if (ActivityCompat.checkSelfPermission(AddOffer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                ActivityCompat.requestPermissions(AddOffer.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }

    }

    private void uploadImage(){
        String filePath = getRealPath(this, uriImage);
        ImageUploaderClass.uploadImage(filePath, nameOfImage, "images/offers", new ImageUploaderClass.onSuccessfulTask(){
            @Override
            public void onSuccess() {

            }
            @Override
            public void onFailed(String error) {

            }
        });
    }

}