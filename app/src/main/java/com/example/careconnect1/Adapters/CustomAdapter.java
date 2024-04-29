package com.example.careconnect1.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.careconnect1.R;

public class CustomAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] textData;
    private final String[] temp;
    private final int[] mBackgroundImages;
    private final OnButtonClickListener mListener;


    public CustomAdapter(Activity context, String[] textData, String[] temp, int[] backgroundImages, OnButtonClickListener listener) {
        super(context, R.layout.row, textData);
        this.context = context;
        this.textData = textData;
        this.temp = temp;
        this.mBackgroundImages = backgroundImages;
        this.mListener = listener;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row, parent, false);
            holder = new Holder();
            holder.textView1 = convertView.findViewById(R.id.center_name);
            holder.textView2 = convertView.findViewById(R.id.center_address);
            holder.textView3 = convertView.findViewById(R.id.center_distance);
            holder.imageView = convertView.findViewById(R.id.center_image);
            holder.relativeLayout = convertView.findViewById(R.id.Center1); // Replace with your RelativeLayout's ID
            holder.ratingBar = convertView.findViewById(R.id.center_rating);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.textView1.setText(textData[position]);
        holder.textView2.setText(temp[position] + " \u2103");

        int backgroundIndex = position % mBackgroundImages.length;
        holder.relativeLayout.setBackgroundResource(mBackgroundImages[backgroundIndex]);

        holder.ratingBar.setRating(4.5f); // Set rating here
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onButtonClick(position);
                }
                Intent intent = new Intent(CustomAdapter.this.getContext(), BookingDetailsActivity.class);
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    public interface OnButtonClickListener {
        void onButtonClick(int position);
    }

    static class Holder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        ImageView imageView;
        RelativeLayout relativeLayout;
        RatingBar ratingBar;
    }
}
