package com.example.careconnect1.Adapters;

import static com.example.careconnect1.Utilities.Config.IP;
import static com.example.careconnect1.Utilities.Config.OFFER_IMAGES_DIR;
import static com.example.careconnect1.Utilities.Config.USER_IMAGES_DIR;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.careconnect1.Interface.FragmentRefresh;
import com.example.careconnect1.Model.BookingModel;
import com.example.careconnect1.R;
import com.example.careconnect1.UI.BookInfo;
import com.example.careconnect1.UI.Offers;
import com.example.careconnect1.UI.UserProfile;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Locale;

public class ParentBookingAdapter extends RecyclerView.Adapter<ParentBookingAdapter.MyHolder>  {
    private final ArrayList<BookingModel> list;
    private final Activity context;

    private FragmentRefresh fragmentRefresh;
    public ParentBookingAdapter(Activity activity, ArrayList<BookingModel> list, FragmentRefresh fragmentRefresh) {
        this.list = list;
        context = activity;
        this.fragmentRefresh = fragmentRefresh;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private final TextView date, payment_info;
        private final TextView center_name;
        private final ShapeableImageView icon_center, icon_offer, icon_review;
        private final TextView status;
        private final TextView action_cancel;
        private final TextView offer_info;
        private LinearLayoutCompat layout_offer, layout_service;
        private ChipGroup chipGroup;
        private MaterialCardView cardView;


        public MyHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.cardView);
            icon_review = v.findViewById(R.id.icon_review);
            payment_info = v.findViewById(R.id.payment_info);
            date = v.findViewById(R.id.booking_date);
            center_name = v.findViewById(R.id.center_name);
            icon_center = v.findViewById(R.id.icon_center);
            icon_offer = v.findViewById(R.id.icon_offer);
            status = v.findViewById(R.id.status);
            action_cancel = v.findViewById(R.id.action_cancel);
            offer_info = v.findViewById(R.id.offer_info);
            layout_offer = v.findViewById(R.id.layout_offer);
            layout_service = v.findViewById(R.id.layout_services);
            chipGroup = v.findViewById(R.id.chipGroup);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_parent_booking, parent, false);
        return new MyHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        BookingModel currentItem = list.get(position);
        holder.date.setText(currentItem.getDate() + " - " + currentItem.getTime());
        holder.center_name.setText(currentItem.getCenter_name());
        holder.center_name.setPaintFlags( holder.center_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        holder.payment_info.setText((currentItem.getPayment_amount() + "$ ("+currentItem.getPayment_type().toUpperCase(Locale.ROOT) + ")"));
        Glide.with(context).load(USER_IMAGES_DIR + currentItem.getCenter_icon())
                .error(R.drawable.ic_user)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.icon_center);

        holder.offer_info.setText(currentItem.getOffer_description());
        holder.offer_info.setPaintFlags( holder.offer_info.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if(currentItem.getOffer_id().equals("")){ // if the user booked a service not offer
            holder.layout_offer.setVisibility(View.GONE);
            holder.layout_service.setVisibility(View.VISIBLE);
        }else{
            holder.layout_offer.setVisibility(View.VISIBLE);
            holder.layout_service.setVisibility(View.GONE);
        }
        Glide.with(context).load(OFFER_IMAGES_DIR + currentItem.getOffer_icon())
                .error(R.drawable.ic_image_broke)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.icon_offer);


        for(int i = 0; i<currentItem.getArrayList().size();i++){
            Chip chip=(Chip)View.inflate(context, R.layout.chip_items_read,null);
            chip.setText(currentItem.getArrayList().get(i).getName()+" - "+currentItem.getArrayList().get(i).getPrice());
            holder.chipGroup.addView(chip);
        }

// if it is still pending not rejected or accepted by company customer can cancel it
        if(currentItem.getStatus().toLowerCase(Locale.ROOT).equals("pending")){
            holder.action_cancel.setVisibility(View.VISIBLE);

            holder.action_cancel.setOnClickListener(v -> cancelRequest(list.get(position).getId()));
            holder.icon_review.setVisibility(View.GONE);
            holder.status.setTextColor(context.getResources().getColor(R.color.purple));
        }else  if(currentItem.getStatus().toLowerCase(Locale.ROOT).equals("accepted")){
            holder.status.setTextColor(context.getResources().getColor(R.color.pink));
            holder.action_cancel.setVisibility(View.GONE);
            holder.icon_review.setVisibility(View.GONE);
        }
        else  if(currentItem.getStatus().toLowerCase(Locale.ROOT).equals("rejected")){
            holder.status.setTextColor(context.getResources().getColor(R.color.red, null));
            holder.action_cancel.setVisibility(View.GONE);
            holder.icon_review.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.drawable.ic_help).into(holder.icon_review);
            holder.icon_review.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Reason of rejection");
                builder.setMessage(currentItem.getReject_cause());
                builder.setPositiveButton("Close", null);
                builder.show();
            });
        }
        else{
            holder.action_cancel.setVisibility(View.GONE);
            holder.status.setTextColor(context.getResources().getColor(R.color.purple));
            holder.icon_review.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.drawable.ic_review).into(holder.icon_review);
           // holder.icon_review.setOnClickListener(v -> {
              //  Intent intent = new Intent(context, AddReview.class);
               // intent.putExtra("cleaner_id", list.get(position).getCenter_id());
               // intent.putExtra("book_id", list.get(position).getId());
              //  context.startActivity(intent);
           // });
        } //chip and this
       holder.status.setText(currentItem.getStatus());


        holder.center_name.setSelected(true);
        holder.payment_info.setSelected(true);
        holder.offer_info.setSelected(true);

        holder.center_name.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfile.class);
            intent.putExtra("user_id", list.get(position).getCenter_id());
            context.startActivity(intent);
        });
        holder.offer_info.setOnClickListener(v -> {
            Intent intent = new Intent(context, Offers.class);
            intent.putExtra("user_id", list.get(position).getCenter_id());
            context.startActivity(intent);
        });


        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookInfo.class);
            intent.putExtra("book_id", list.get(position).getId());
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    void cancelRequest(String book_id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cancel Request");
        builder.setMessage("Are you sure?");
        builder.setIcon(R.drawable.logo);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "delete_book.php?id="+ book_id , response -> {
                Toast.makeText(context, response.trim(), Toast.LENGTH_SHORT).show();
                fragmentRefresh.refreshBookingProvider(); // to update the booking list
            }, error -> {
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        });
        builder.setNegativeButton("No" ,null);

        builder.show();
    }


}