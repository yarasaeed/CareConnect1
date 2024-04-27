package com.example.careconnect1.Admin;

import static com.example.careconnect1.Utilities.Config.IPADMIN;
import static com.example.careconnect1.Utilities.GetImagePath.getRealPath;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.careconnect1.Adapters.AdminImagesAdapter;
import com.example.careconnect1.FileUpload.ImageUploaderClass;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllImages extends AppCompatClass {
    private RecyclerView recyclerView;
    private AdminImagesAdapter adapter;
    private TextView text_add;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    Uri uriImage = null;
    String nameOfImage="", typeOfImage="";
    private ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_images);
        setMethods("Main Images","");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        recyclerView = findViewById(R.id.recyclerView);
        text_add = findViewById(R.id.text_add);


    }

    @Override
    public void setActions() {
        super.setActions();
        getImages();
        text_add.setOnClickListener(v -> checkPermission());
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
                            nameOfImage = "icon_"+System.currentTimeMillis()+"."+typeOfImage.replace("image/","");
                            uploadImage();

                        }
                    }
                });

    }
    public void getImages(){
        arrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IPADMIN + "select_images.php", response -> {

            {
                int i = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    while (i < jsonArray.length()) {
                        JSONObject jSONObject = jsonArray.getJSONObject(i);
                        String name = jSONObject.getString("FileName");

                        arrayList.add(name);
                        i++;
                    }

                    adapter = new AdminImagesAdapter(AllImages.this, arrayList);
                    recyclerView.setAdapter(adapter);

                }catch (Exception | Error ignored){

                }
            }

        }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()){ };

        RequestQueue requestQueue = Volley.newRequestQueue(AllImages.this);
        requestQueue.add(stringRequest);
    }


    //Settings to choose image from gallery
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            openGallery();
        } else {
            Toast.makeText(AllImages.this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
    public void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(AllImages.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher.launch(intent);
            } else {
                ActivityCompat.requestPermissions(AllImages.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            }
        }else{
            if (ActivityCompat.checkSelfPermission(AllImages.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher.launch(intent);
            } else {
                ActivityCompat.requestPermissions(AllImages.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }



    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(AllImages.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }else{
                ActivityCompat.requestPermissions(AllImages.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            }
        }else{

            if (ActivityCompat.checkSelfPermission(AllImages.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                ActivityCompat.requestPermissions(AllImages.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }

    }

    private void uploadImage(){
        String filePath = getRealPath(this, uriImage);
        ImageUploaderClass.uploadImage(filePath, nameOfImage, "images/main", new ImageUploaderClass.onSuccessfulTask() {
            @Override
            public void onSuccess() {

                insertImage();
            }

            @Override
            public void onFailed(String error) {

            }
        });
    }

    private void insertImage(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IPADMIN + "insert_image.php", response -> {
           Toast.makeText(AllImages.this, response.trim(), Toast.LENGTH_SHORT).show();
             getImages();

        }, error -> {

        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("name",nameOfImage);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AllImages.this);
        requestQueue.add(stringRequest);
    }


}