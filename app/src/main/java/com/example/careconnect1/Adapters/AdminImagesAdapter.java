package com.example.careconnect1.Adapters;

import static com.example.careconnect1.Utilities.Config.IPADMIN;
import static com.example.careconnect1.Utilities.Config.MAIN_IMAGES_DIR;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.careconnect1.Admin.AllImages;
import com.example.careconnect1.R;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;

public class AdminImagesAdapter extends RecyclerView.Adapter<AdminImagesAdapter.recyclerHolder> {
    ArrayList<String> list; // List to store the image URLs
    Activity activity; // Reference to the activity using this adapter

    public AdminImagesAdapter(Activity activity, ArrayList<String> list) {
        this.list = list;
        this.activity = activity;
    }
    // ViewHolder class for holding views of each item in the RecyclerView
    public static class recyclerHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView icon, icon_delete;

        public recyclerHolder(View v) {
            super(v);
            icon = v.findViewById(R.id.icon);// Reference to the image view for displaying the image
            icon_delete = v.findViewById(R.id.icon_delete);// Reference to the image view for deletion
        }

    }


    @NonNull
    @Override
    public recyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_images, parent, false);
        return new recyclerHolder(v);
        // Inflate the layout for each item in the RecyclerView
    }

    @SuppressLint({"SetTextI18n", "ResourceType", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull final recyclerHolder holder, @SuppressLint("RecyclerView") final int position) {
        // Load the image into the icon ImageView using Glide library
        Glide.with(activity).load(MAIN_IMAGES_DIR + list.get(position))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.image_broke)
                .into(holder.icon);

        holder.icon_delete.setOnClickListener(v -> delete(list.get(position)));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void delete(String name){ // Method for deleting an image
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Delete image");
        builder.setMessage("Are you sure?");
        builder.setIcon(R.drawable.delete);
        builder.setPositiveButton("Yes", (dialog, which) -> {         // Make a  request to delete the image on the server

            StringRequest stringRequest = new StringRequest(Request.Method.GET, IPADMIN + "delete_image.php?name="+ name , response -> {
                Toast.makeText(activity, response.trim(), Toast.LENGTH_SHORT).show();
                ((AllImages)activity).getImages();
            }, error -> { // Error handling
            });

            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(stringRequest);
        });
        builder.setNegativeButton("No" ,null);
        builder.show();
    }
}