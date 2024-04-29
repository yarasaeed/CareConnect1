package com.example.careconnect1.Adapters;

import static com.example.careconnect1.Utilities.Config.IP;
import static com.example.careconnect1.Utilities.Config.OFFER_IMAGES_DIR;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.careconnect1.Model.OffersModel;
import com.example.careconnect1.R;
import com.example.careconnect1.UI.Booking;
import com.example.careconnect1.UI.LogIn;
import com.example.careconnect1.Utilities.UserData;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyHolder>  {
    private final ArrayList<OffersModel> list;
    private final Context context;
    private UserData userData;

    public OfferAdapter(Context activity, ArrayList<OffersModel> list) {
        this.list = list;
        context = activity;
        userData = new UserData(context);
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private final TextView date;
        private final TextView description;
        private final TextView price, btn_book;
        private final ShapeableImageView icon;

        public MyHolder(View v) {
            super(v);
            date = v.findViewById(R.id.date);
            description = v.findViewById(R.id.description);
            price = v.findViewById(R.id.price);
            icon = v.findViewById(R.id.icon);
            btn_book = v.findViewById(R.id.btn_book);


        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_provider_offer_2, parent, false);
        return new MyHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        OffersModel currentItem = list.get(position);
        holder.description.setText(currentItem.getDescription());
        holder.date.setText(currentItem.getDate());
        holder.price.setText(currentItem.getPrice() + "$");
        Glide.with(context).load(OFFER_IMAGES_DIR +
                        currentItem.getIcon())
                .into(holder.icon);

        if(userData.getId().equals("")){
            holder.btn_book.setVisibility(View.VISIBLE);
            holder.btn_book.setText("Log in to avail the offer");
            holder.btn_book.setOnClickListener(v -> {
                Intent intent = new Intent(context, LogIn.class);
                context.startActivity(intent);
            });
        }else{
            if(userData.getRole().equals("parent")){
                holder.btn_book.setText("Book now");
                holder.btn_book.setVisibility(View.VISIBLE);
                holder.btn_book.setOnClickListener(v -> {
                    Intent intent = new Intent(context, Booking.class);
                    intent.putExtra("provider_id", list.get(position).getProvider_id());
                    intent.putExtra("offer_id", list.get(position).getId());
                    intent.putExtra("type", "offer");
                    intent.putExtra("amount",Double.parseDouble(list.get(position).getPrice()) );
                    context.startActivity(intent);
                });
            }else{
                holder.btn_book.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void deleteOffer(String offer_id, String icon){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "delete_offer.php?id="+ offer_id + "&icon="+icon, response -> {
            Toast.makeText(context, "Offer deleted", Toast.LENGTH_SHORT).show();
            ((com.example.careconnect1.UI.ProviderOffers)context).getOffers();
        }, error -> {
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}