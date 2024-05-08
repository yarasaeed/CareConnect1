package com.example.careconnect1.UI;


import static com.example.careconnect1.Utilities.Config.IP;
import static com.example.careconnect1.Utilities.GetImagePath.getRealPath;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.careconnect1.FileUpload.ImageUploaderClass;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;


import com.example.careconnect1.Utilities.Config;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.careconnect1.Utilities.UserData;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatClass {
    private TextView text_login;
    private MaterialToolbar toolbar;
    private TextInputEditText fname, lname, email, phone, password;
    private TextInputLayout layout_fname, layout_lname, address_layout;
    private MaterialButton btn_signup, btn_open_map;
    private UserData userData;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    Uri uriImage = null;
    String nameOfImage = "", typeOfImage = "";
    private String user_role = "";
    private ShapeableImageView icon;
    private double latitude = 0, longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setMethods(" ", " ");

    }


    @Override
    public void setInitialize() {
        super.setInitialize();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user_role = bundle.getString("type", "");
        Log.e("SignUpActivity", "HI" + user_role);
        if (bundle != null) {
            Log.e("SignUpActivity", "Bundle content: " + bundle.toString());
            if (bundle.containsKey("type")) {
                user_role = bundle.getString("type", "");
                Log.e("SignUpActivity", "User role: " + user_role);
            } else {
                Log.e("SignUpActivity", "Bundle does not contain 'type' key");
            }
        } else {
            Log.e("SignUpActivity", "Bundle is null");
        }
        icon = findViewById(R.id.icon);
        toolbar = findViewById(R.id.generalToolbar);
        text_login = findViewById(R.id.text_login);
        fname = findViewById(R.id.fn_edit);
        lname = findViewById(R.id.ln_edit);
        email = findViewById(R.id.email_edit);
        phone = findViewById(R.id.phone_edit);
        password = findViewById(R.id.password_edit);
        btn_signup = findViewById(R.id.btn_signup);
        layout_lname = findViewById(R.id.ln_layout);
        layout_fname = findViewById(R.id.fn_layout);
        btn_open_map = findViewById(R.id.btn_open_map);
        address_layout = findViewById(R.id.address_layout);
    }


    @Override
    public void setActions() {
        super.setActions();
        if(user_role.equals("center")){
            layout_fname.setHint("Name");
            layout_lname.setVisibility(View.GONE);
            lname.setText("-");
        }else if(user_role.equals("babysitter")){
            layout_fname.setHint("Name");
            layout_lname.setHint("Last Name");
        }
        else if(user_role.equals("parent")){
            layout_fname.setHint("Name");
            layout_lname.setHint("Last Name");
            address_layout.setVisibility(View.GONE);
        }
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close));
        text_login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, LogIn.class);
            startActivity(intent);
        });
        btn_open_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the map activity or fragment
                Intent intent = new Intent(SignUp.this, MapsActivity.class);
                startActivityForResult(intent, 1);


            }
        });
        btn_signup.setOnClickListener(v -> {
            if (user_role.equals("parent")) {
                // For parents, proceed with signup without checking for location
                if (uriImage != null) {
                    uploadImage();
                    insertUser();
                } else {
                    Toast.makeText(SignUp.this, "Image is required", Toast.LENGTH_SHORT).show();
                }
            } else {
                // For babysitters and centers, enforce the location requirement
                if (uriImage != null && latitude != 0 && longitude != 0) {
                    uploadImage();
                    insertUser();
                } else {
                    Toast.makeText(SignUp.this, "Image and location are both required", Toast.LENGTH_SHORT).show();
                }
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
        }
    }


    private void insertUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.IP + "signup.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SignUpActivity", "hi response");
                if (!response.trim().contains(",,")) {//hek befare2 id aan userrole
                    Toast.makeText(SignUp.this, response.trim(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("SignUpActivity", "hi entered else");
                    userData = new UserData(SignUp.this);
                    Toast.makeText(SignUp.this, "Success", Toast.LENGTH_SHORT).show();
                    String[] data = response.trim().split(",,");
                    userData.setUserData(data[0], data[1]);//aam ysayev userid be data0 w userrole be data1
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    intent.putExtra("tab", "4");

                    startActivity(intent);
                    finish();
                }


            }
        }, error -> {

        }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("fname", fname.getText().toString());
                map.put("lname", lname.getText().toString());
                map.put("email", email.getText().toString());
                map.put("phone", phone.getText().toString());
                map.put("user_role", user_role);
                map.put("password", password.getText().toString());
                map.put("icon", nameOfImage);
                map.put("latitude", String.valueOf(latitude));
                map.put("longitude", String.valueOf(longitude));
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
        requestQueue.add(stringRequest);
    }

    //Settings to choose image from gallery
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            openGallery();
        } else {
            Toast.makeText(SignUp.this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(SignUp.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher.launch(intent);
            } else {
                ActivityCompat.requestPermissions(SignUp.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(SignUp.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher.launch(intent);
            } else {
                ActivityCompat.requestPermissions(SignUp.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(SignUp.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                ActivityCompat.requestPermissions(SignUp.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            }
        } else {

            if (ActivityCompat.checkSelfPermission(SignUp.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                ActivityCompat.requestPermissions(SignUp.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }

    }


    private void uploadImage() {
        String filePath = getRealPath(this, uriImage);
        ImageUploaderClass.uploadImage(filePath, nameOfImage, "images/users", new ImageUploaderClass.onSuccessfulTask() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String error) {

            }
        });
    }


}