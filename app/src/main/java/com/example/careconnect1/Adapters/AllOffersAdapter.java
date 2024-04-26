package com.example.careconnect1.Adapters;

import static com.example.careconnect1.Utilities.Config.OFFER_IMAGES_DIR;
import static com.example.careconnect1.Utilities.Config.USER_IMAGES_DIR;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.careconnect1.Model.ProvidersModel;
import com.example.cleanup.Models.OffersModel;
import com.example.careconnect1.R;
import com.example.careconnect1.UI.AllOffers;
import com.example.careconnect1.UI.Offers;
import com.example.careconnect1.UI.UserProfile;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;
import java.util.List;

public class AllOffersAdapter extends RecyclerView.Adapter<AllOffersAdapter.MyHolder> implements Filterable {
    private final ArrayList<OffersModel> list;
    private final ArrayList<OffersModel> listFull;
    private final Context context;

    public AllOffersAdapter(Context activity, ArrayList<OffersModel> list) {
        this.list = list;
        context = activity;
        listFull =new ArrayList<>(list);
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private final TextView name, description, price;
        private final MaterialCardView cardView;
        private final ShapeableImageView icon, icon_offer;


        //holder is used to access and update the views within each item of the RecyclerView.
        public MyHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            description = v.findViewById(R.id.description);
            price = v.findViewById(R.id.price);
            icon = v.findViewById(R.id.icon);
            icon_offer = v.findViewById(R.id.icon_offer);
            cardView = v.findViewById(R.id.cardView);


        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_all_offers, parent, false);
        return new MyHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    //sed to set the values and define click listeners for the corresponding views in the item layout.
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        OffersModel currentItem = list.get(position);
        holder.name.setText(currentItem.getCleaner_name());
        holder.price.setText("USD "+currentItem.getPrice());
        holder.description.setText(currentItem.getDescription());
        // Load the cleaner's icon using Glide library
        Glide.with(context).load(USER_IMAGES_DIR +
                        currentItem.getCleaner_icon())
                .error(R.drawable.ic_user)
                .into(holder.icon);
        // Load the offer icon using Glide library
        Glide.with(context).load(OFFER_IMAGES_DIR +
                        currentItem.getIcon())
                .error(R.drawable.ic_image_broke)
                .into(holder.icon_offer);

        // Open the user profile when the name is clicked
        holder.name.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfile.class);
            intent.putExtra("site_id", "");
            intent.putExtra("user_id", list.get(position).getCleaner_id());
            intent.putExtra("offer_id", "");
            intent.putExtra("type", "service");
            context.startActivity(intent);
        });
        // Open the offers activity when the card is clicked
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Offers.class);
            intent.putExtra("user_id", list.get(position).getCleaner_id());
            intent.putExtra("site_id", "");
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    @Override
    public Filter getFilter() {
        return filter;
    }
    // filter android widgets
    private final Filter filter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<OffersModel> list1=new ArrayList<>();
            if(charSequence==null || charSequence.length()==0){
                list1.addAll(listFull);
            }
            else {
                String filterPattern=charSequence.toString().toLowerCase().trim();
                for (OffersModel item: listFull){
                    if(   // Filter the list based on the description, cleaner name, date, and price
                            item.getDescription().toLowerCase().contains(filterPattern)|
                                    item.getCleaner_name().toLowerCase().contains(filterPattern)|
                                    item.getDate().toLowerCase().contains(filterPattern)|
                                    item.getPrice().toLowerCase().contains(filterPattern) ){
                        list1.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=list1;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            try {  // Update the list with the filtered results
                list.clear();
                list.addAll((ArrayList<OffersModel>)filterResults.values);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
}