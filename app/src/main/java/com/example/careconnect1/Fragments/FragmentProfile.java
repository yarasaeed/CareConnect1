package com.example.careconnect1.Fragments;

import static com.example.careconnect1.Utilities.Config.IP;
import static com.example.careconnect1.Utilities.Config.USER_IMAGES_DIR;
import static com.example.careconnect1.Utilities.GetImagePath.getRealPath;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.careconnect1.UI.LogIn;
import com.example.careconnect1.UI.MainActivity;
import com.example.careconnect1.UI.ProviderOffers;
import com.example.careconnect1.UI.ProviderReviews;
import com.example.careconnect1.Utilities.UserData;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.example.careconnect1.FileUpload.ImageUploaderClass;
import com.example.careconnect1.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FragmentProfile extends Fragment {
    private TextView text_login;
    private TextInputLayout layout_fname,layout_lname;
    private LinearLayoutCompat layout_provider;
    boolean edit = false;
    private ScrollView layout_update;
    private RelativeLayout layout_login;
    private UserData userData;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ChipGroup chipGroupServices, chipGroupDates, chipGroupCleaningProducts;

    Uri uriImage = null;
    String nameOfImage = "";
    private TextInputEditText fname, lname, email, phone, password, address, bio, gender;
    private TextView text_name, text_email, text_role;
    private ShapeableImageView btn_reviews, btn_offers;
    private ShapeableImageView btn_logout;
    private ShapeableImageView icon, icon_edit,icon_save;

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
        icon = v.findViewById(R.id.icon);
        fname = v.findViewById(R.id.fn_edit);
        lname = v.findViewById(R.id.ln_edit);
        email = v.findViewById(R.id.email_edit);
        phone = v.findViewById(R.id.phone_edit);
        password = v.findViewById(R.id.password_edit);
        text_name = v.findViewById(R.id.text_name);
        text_email = v.findViewById(R.id.text_email);
        text_role = v.findViewById(R.id.text_role);
        bio = v.findViewById(R.id.bio_edit);
        address = v.findViewById(R.id.address_edit);
        gender = v.findViewById(R.id.gender_edit);
        layout_provider = v.findViewById(R.id.layout_provider);
        chipGroupServices = v.findViewById(R.id.chipGroup);
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
       /* text_login.setOnClickListener(v1 -> {
            Intent intent = new Intent(getActivity(), LogIn.class);
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
        });*/
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
                    map.put("gender",gender.getText().toString());
                    map.put("password",password.getText().toString());
                    map.put("phone",phone.getText().toString());
                    map.put("user_id", userData.getId());
                    map.put("icon",nameOfImage);
                    map.put("address",address.getText().toString());
                    map.put("bio",bio.getText().toString());
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        }
    }

    private void getUserData() {
        if (getActivity() != null) {
            chipGroupDates.removeAllViews();
            chipGroupServices.removeAllViews();
            chipGroupCleaningProducts.removeAllViews();
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
                        gender.setText(""+jSONObject.getString("gender") );
                        bio.setText(jSONObject.getString("bio") );
                        address.setText(jSONObject.getString("address") );
                        if (getActivity() != null) {
                            Glide.with(getActivity()).load(USER_IMAGES_DIR + jSONObject.getString("icon"))
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .error(R.drawable.ic_user)
                                    .into(icon);
                        }

                        if(jSONObject.getString("UserRole").equals("provider")){
                            layout_provider.setVisibility(View.GONE);
                            btn_offers.setVisibility(View.GONE);
                            btn_reviews.setVisibility(View.GONE);
                        }
                        /*else   if(jSONObject.getString("UserRole").equals("individual")){
                            layout_parent.setVisibility(View.GONE);
                            btn_offers.setVisibility(View.GONE);
                            gender.setText("0");

                        }else   if(jSONObject.getString("UserRole").equals("company")) {
                            layout_fname.setHint("Name");
                            layout_lname.setVisibility(View.GONE);
                            lname.setText("-");
                        }else{
                            layout_provider.setVisibility(View.GONE);
                            layout_employee.setVisibility(View.GONE);
                            btn_offers.setVisibility(View.GONE);
                            btn_reviews.setVisibility(View.GONE);
                        }*/
                        i++;
                    }
                }catch (Exception | Error ignored){

                }

            }, error -> {

            });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }

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

}
