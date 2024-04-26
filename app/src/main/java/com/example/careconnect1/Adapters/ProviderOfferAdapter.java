package com.example.careconnect1.Adapters;

import static com.example.careconnect1.Utilities.Config.IP;
import static com.example.careconnect1.Utilities.Config.OFFER_IMAGES_DIR;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
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
import com.example.cleanup.Models.OffersModel;
import com.example.careconnect1.R;
import com.example.careconnect1.UI.ProviderOffers;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;

public class ProviderOfferAdapter extends RecyclerView.Adapter<ProviderOfferAdapter.MyHolder>  {
    private final ArrayList<OffersModel> list;
    private final Context context;

    public ProviderOfferAdapter(Context activity, ArrayList<OffersModel> list) {
        this.list = list;
        context = activity;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private final TextView date;
        private final TextView description;
        private final TextView price,delete_cause;
        private final ShapeableImageView icon;
        private final ShapeableImageView icon_delete;

        public MyHolder(View v) {
            super(v);
            date = v.findViewById(R.id.date);
            description = v.findViewById(R.id.description);
            price = v.findViewById(R.id.price);
            icon = v.findViewById(R.id.icon);
            icon_delete = v.findViewById(R.id.icon_delete);
            delete_cause = v.findViewById(R.id.delete_cause);


        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_provider_offer, parent, false);
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
        holder.icon_delete.setOnClickListener(v -> deleteOffer(list.get(position).getId(), list.get(position).getIcon()));
        if (currentItem.getUsed().equals("0")) {
            holder.icon_delete.setEnabled(true);
            holder.icon_delete.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.black, null)));
            holder.delete_cause.setText("");
        } else {
            holder.icon_delete.setEnabled(false);
            holder.icon_delete.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.silver, null)));
            holder.delete_cause.setText("Offer used, can't be deleted now");
        }

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    private void deleteOffer(String offer_id, String icon){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "delete_offer.php?id="+ offer_id + "&icon="+icon, response -> {
            Toast.makeText(context, "Offer deleted", Toast.LENGTH_SHORT).show();
            ((ProviderOffers)context).getOffers();
        }, error -> {
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}