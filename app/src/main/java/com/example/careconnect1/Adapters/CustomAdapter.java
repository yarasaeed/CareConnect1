package com.example.careconnect1.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.careconnect1.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> locationList;
    private ArrayList<String> iconUrlList; // Add a new ArrayList to store icon URLs

    public CustomAdapter(Context context, ArrayList<String> locationList, ArrayList<String> iconUrlList) {
        super(context, 0, locationList);
        this.context = context;
        this.locationList = locationList;
        this.iconUrlList = iconUrlList; // Initialize the iconUrlList
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        }

        String location = locationList.get(position);
        String[] locationParts = location.split(": ");

        TextView textViewName = listItem.findViewById(R.id.textViewName);
        TextView textViewLocation = listItem.findViewById(R.id.textViewLocation);
        ImageView iconImageView = listItem.findViewById(R.id.iconImageView);

        if (locationParts.length == 2) {
            textViewName.setText(locationParts[0]);
            textViewLocation.setText(locationParts[1]);
        }

        // Load the icon image using Glide
        String iconUrl = iconUrlList.get(position);
        loadImageWithGlide(iconUrl, iconImageView);

        return listItem;
    }

    private void loadImageWithGlide(String iconUrl, ImageView imageView) {
        // Use Glide to load the image
        Glide.with(context)
                .load(iconUrl) // Load the image from the URL
                .into(imageView);
    }
}
