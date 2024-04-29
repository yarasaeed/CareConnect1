package com.example.careconnect1.Adapters;

import static com.example.careconnect1.Utilities.Config.USER_IMAGES_DIR;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.careconnect1.Model.ReviewsModel;
import com.example.careconnect1.R;
import com.example.careconnect1.UI.BookInfo;
import com.example.careconnect1.UI.UserProfile;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class BookReviewsAdapter extends RecyclerView.Adapter<BookReviewsAdapter.MyHolder>  {
    private final ArrayList<ReviewsModel> list;
    private final Context context;

    public BookReviewsAdapter(Context activity, ArrayList<ReviewsModel> list) {
        this.list = list;
        context = activity;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private final TextView name, book_id, text, date;
        private final ShapeableImageView icon;
        public MyHolder(View v) {
            super(v);
            text = v.findViewById(R.id.text);
            name = v.findViewById(R.id.name);
            book_id = v.findViewById(R.id.book_id);
            icon = v.findViewById(R.id.icon);
            date = v.findViewById(R.id.date);


        }
    }

    @NonNull
    @Override
    // Create the view holder for the recycler view
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_provider_reviews, parent, false);
        return new MyHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        ReviewsModel currentItem = list.get(position);
        holder.text.setText(currentItem.getText());
        holder.name.setText(currentItem.getParent_name());
        holder.date.setText(currentItem.getDate());
        holder.book_id.setText("Book "+currentItem.getBook_id());
        Glide.with(context).load(USER_IMAGES_DIR +
                        currentItem.getParent_icon())
                .into(holder.icon);

        holder.name.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfile.class);
            intent.putExtra("user_id", list.get(position).getUser_id());
            intent.putExtra("offer_id", "");
            intent.putExtra("type", "service");
            context.startActivity(intent);
        });

        holder.book_id.setVisibility(View.GONE);
        holder.book_id.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookInfo.class);
            intent.putExtra("book_id", list.get(position).getBook_id());
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }



}