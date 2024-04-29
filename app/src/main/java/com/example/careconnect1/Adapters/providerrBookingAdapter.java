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
import android.widget.EditText;
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

public class providerrBookingAdapter extends RecyclerView.Adapter<providerrBookingAdapter.MyHolder>  {
    private final ArrayList<BookingModel> list;
    private final Activity context;
    FragmentRefresh fragmentRefresh; //implementing the FragmentRefresh interface.

    private AlertDialog alertDialog;
    public providerrBookingAdapter(Activity activity, ArrayList<BookingModel> list, FragmentRefresh fragmentRefresh) {
        this.list = list;
        context = activity;
        this.fragmentRefresh = fragmentRefresh;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private final TextView date,payment_info;

        private final TextView parent_name;
        private final TextView status;
        private final TextView action_accept;
        private final TextView action_reject;
        private final TextView action_complete;
        private final ShapeableImageView  icon_offer,icon_parent,icon_info;

        private final TextView offer_info;
        private LinearLayoutCompat layout_offer, layout_service;
        private ChipGroup chipGroup;

        private MaterialCardView cardView;



        public MyHolder(View v) {
            super(v);
            date = v.findViewById(R.id.date);
            cardView = v.findViewById(R.id.cardView);
            icon_info = v.findViewById(R.id.icon_info);
            icon_parent = v.findViewById(R.id.icon_parent);
            payment_info = v.findViewById(R.id.payment_info);
            layout_offer = v.findViewById(R.id.layout_offer);
            chipGroup = v.findViewById(R.id.chipGroup);
            offer_info = v.findViewById(R.id.offer_info);
            layout_service = v.findViewById(R.id.layout_services);
            icon_offer = v.findViewById(R.id.icon_offer);
            parent_name = v.findViewById(R.id.parent_name);
            status = v.findViewById(R.id.status);
            action_accept = v.findViewById(R.id.action_accept);
            action_reject = v.findViewById(R.id.action_reject);
            action_complete = v.findViewById(R.id.action_complete);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_center_booking, parent, false);
        return new MyHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        BookingModel currentItem = list.get(position);
        // Set the date and time
        holder.date.setText(currentItem.getDate() + " - " + currentItem.getTime());

        // Set the customer name
        holder.parent_name.setText(currentItem.getParent_name());
        holder.parent_name.setPaintFlags( holder.parent_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.payment_info.setText((currentItem.getPayment_amount() + "$ ("+currentItem.getPayment_type().toUpperCase(Locale.ROOT) + ")"));
        holder.offer_info.setText(currentItem.getOffer_description());
        //set the corresponding data underlined
        holder.offer_info.setPaintFlags( holder.offer_info.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
// depends of the customer booked an offer or service
        if(currentItem.getOffer_id().equals("")){
            holder.layout_offer.setVisibility(View.GONE);
            holder.layout_service.setVisibility(View.VISIBLE);
        }else{
            holder.layout_offer.setVisibility(View.VISIBLE);
            holder.layout_service.setVisibility(View.GONE);
        }

        for(int i = 0; i<currentItem.getArrayList().size();i++){
            Chip chip=(Chip)View.inflate(context, R.layout.chip_items_read,null);
            chip.setText(currentItem.getArrayList().get(i).getName()+" - "+currentItem.getArrayList().get(i).getPrice());
            holder.chipGroup.addView(chip);
        }
        // Load offer and customer images using Glide
        Glide.with(context).load(OFFER_IMAGES_DIR + currentItem.getOffer_icon())
                .error(R.drawable.ic_image_broke)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.icon_offer);
        Glide.with(context).load(USER_IMAGES_DIR + currentItem.getParent_icon())
                .error(R.drawable.ic_user)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.icon_parent);
        // Set the visibility and text color of status TextView based on the status value
        if(currentItem.getStatus().toLowerCase(Locale.ROOT).equals("pending")){
            holder.action_reject.setVisibility(View.VISIBLE);
            holder.action_complete.setVisibility(View.VISIBLE);
            holder.action_accept.setVisibility(View.VISIBLE);
            holder.icon_info.setVisibility(View.GONE);
            holder.status.setTextColor(context.getResources().getColor(R.color.pink));
        }else  if(currentItem.getStatus().toLowerCase(Locale.ROOT).equals("accepted")){
            holder.status.setTextColor(context.getResources().getColor(R.color.pink));
            holder.action_reject.setVisibility(View.GONE);
            holder.action_accept.setVisibility(View.GONE);
            holder.icon_info.setVisibility(View.GONE);
        }
        // Show a dialog with the reason for rejection when the icon_info is clicked

        else  if(currentItem.getStatus().toLowerCase(Locale.ROOT).equals("rejected")){
            holder.status.setTextColor(context.getResources().getColor(R.color.red, null));
            holder.action_reject.setVisibility(View.GONE);
            holder.action_complete.setVisibility(View.GONE);
            holder.action_accept.setVisibility(View.GONE);
            holder.icon_info.setVisibility(View.VISIBLE);
            holder.icon_info.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Reason of rejection");
                builder.setMessage(currentItem.getReject_cause());
                builder.setPositiveButton("Close", null);
                builder.show();

            });

        }
        else{
            holder.icon_info.setVisibility(View.GONE);
            holder.action_reject.setVisibility(View.GONE);
            holder.action_complete.setVisibility(View.GONE);
            holder.action_accept.setVisibility(View.GONE);
            holder.status.setTextColor(context.getResources().getColor(R.color.purple));
        }
        // Set the status text
        holder.status.setText(currentItem.getStatus());

        holder.action_accept.setOnClickListener(v -> updateBookStatus(list.get(position).getId(), "Accepted",""));
        holder.action_reject.setOnClickListener(v -> showDialog(list.get(position).getId(), "Rejected"));
        holder.action_complete.setOnClickListener(v -> updateBookStatus(list.get(position).getId(), "Completed",""));
        holder.offer_info.setSelected(true);
        holder.parent_name.setSelected(true);
        holder.payment_info.setSelected(true);

        holder.offer_info.setOnClickListener(v -> {
            Intent intent = new Intent(context, Offers.class);
            intent.putExtra("user_id", list.get(position).getCenter_id());
            context.startActivity(intent);
        });

        holder.parent_name.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfile.class);
            intent.putExtra("user_id", list.get(position).getParent_id());
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

    // Create a StringRequest with GET method to update the book status
    void updateBookStatus(String book_id, String status, String cause){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "update_book_status.php?book_id="+ book_id + "&status="+status+ "&cause="+cause, response -> {
            // Display a toast message with the response
            Toast.makeText(context, response.trim(), Toast.LENGTH_SHORT).show();
            fragmentRefresh.refreshBookingProvider();
        }, error -> {
        });// Create a RequestQueue using Volley library

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    // Create an AlertDialog.Builder to show the rejection cause dialog

    public void showDialog(String book_id, String status){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = View.inflate(context, R.layout.layout_rejected_cause, null);
        // Get references to the views in the dialog layout

        TextView btn_ok = dialogView.findViewById(R.id.btn_ok);
        TextView btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        EditText text_cause = dialogView.findViewById(R.id.text_cause);
        // Set the dialog to be cancelable

        builder.setCancelable(true);
        // Set click listener for the button in the dialog

        btn_ok.setOnClickListener(v12 -> {
            alertDialog.dismiss();
            updateBookStatus(book_id, status,text_cause.getText().toString());
        });
        btn_cancel.setOnClickListener(view -> {
            alertDialog.dismiss();

        });
        builder.setView(dialogView);
        alertDialog = builder.create();
        if (dialogView.getParent() != null) {
            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
        }
        alertDialog.show();
    }

}