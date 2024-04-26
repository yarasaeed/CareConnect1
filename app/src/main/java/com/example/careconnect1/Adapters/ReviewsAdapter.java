package com.example.careconnect1.Adapters;

import static com.example.careconnect1.Utilities.Config.IPADMIN;
import static com.example.careconnect1.Utilities.Config.USER_IMAGES_DIR;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.careconnect1.Admin.AllReviews;
import com.example.careconnect1.Model.ReviewsModel;
import com.example.careconnect1.R;
import com.example.careconnect1.UI.BookInfo;
import com.example.careconnect1.UI.UserProfile;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyHolder>  {
    private final ArrayList<ReviewsModel> list;
    private final Context context;

    private boolean showDelete;

    public ReviewsAdapter(Context activity, ArrayList<ReviewsModel> list, boolean showDelete) {
        this.list = list;
        context = activity;
        this.showDelete = showDelete;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private final TextView name, book_id, text, date;
        private final ShapeableImageView icon, icon_delete;
        public MyHolder(View v) {
            super(v);
            text = v.findViewById(R.id.text);
            name = v.findViewById(R.id.name);
            book_id = v.findViewById(R.id.book_id);
            icon = v.findViewById(R.id.icon);
            date = v.findViewById(R.id.date);
            icon_delete = v.findViewById(R.id.icon_delete);


        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reviews, parent, false);
        return new MyHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        ReviewsModel currentItem = list.get(position);
        holder.text.setText(currentItem.getText());
        if(showDelete){
            holder.icon_delete.setVisibility(View.VISIBLE);
            holder.name.setText(currentItem.getCustomer_name().replace("-",""));
        }else {
            holder.name.setText(currentItem.getCustomer_name());
            holder.icon_delete.setVisibility(View.GONE);
        }

        holder.date.setText(currentItem.getDate());
        holder.book_id.setText("Book "+currentItem.getBook_id());
        Glide.with(context).load(USER_IMAGES_DIR +
                        currentItem.getCustomer_icon())
                .into(holder.icon);

        holder.name.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfile.class);
            intent.putExtra("site_id", "");
            intent.putExtra("user_id", list.get(position).getUser_id());
            intent.putExtra("offer_id", "");
            intent.putExtra("type", "service");
            context.startActivity(intent);
        });

        holder.book_id.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookInfo.class);
            intent.putExtra("book_id", list.get(position).getBook_id());
            context.startActivity(intent);
        });
        holder.book_id.setPaintFlags(     holder.book_id.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.icon_delete.setOnClickListener(v -> delete(list.get(position).getId()));
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    void delete(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete review");
        builder.setMessage("Are you sure?");
        builder.setIcon(R.drawable.ic_delete);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, IPADMIN + "delete_review.php?id="+ id , response -> {
                Toast.makeText(context, response.trim(), Toast.LENGTH_SHORT).show();
                ((AllReviews)context).getReviews();
            }, error -> {
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        });
        builder.setNegativeButton("No" ,null);
        builder.show();
    }

}