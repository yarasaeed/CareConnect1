package com.example.careconnect1.Adapters;

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
import com.example.careconnect1.R;
import com.example.careconnect1.UI.Booking;
import com.example.careconnect1.UI.UserProfile;
import com.example.careconnect1.Utilities.UserData;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProvidersAdapter extends RecyclerView.Adapter<ProvidersAdapter.MyHolder> implements Filterable {
    private final ArrayList<ProvidersModel> list;
    private final ArrayList<ProvidersModel> listFull;
    private final Context context;
    private UserData userData;

    public ProvidersAdapter(Context activity, ArrayList<ProvidersModel> list) {
        this.list = list;
        context = activity;
        listFull =new ArrayList<>(list);
        userData = new UserData(context);
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView role;
        private final TextView address;
        private final MaterialCardView cardView;
        private final ShapeableImageView icon, icon_book;

        public MyHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            role = v.findViewById(R.id.role);
            icon = v.findViewById(R.id.icon);
            address = v.findViewById(R.id.address);
            cardView = v.findViewById(R.id.cardView);
            icon_book = v.findViewById(R.id.icon_book);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_center, parent, false);
        return new MyHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        ProvidersModel currentItem = list.get(position);
        holder.role.setText(currentItem.getRole().toUpperCase(Locale.ROOT));
        if(userData.getId().equals("")){
            holder.icon_book.setVisibility(View.GONE);
        }else{
            if(userData.getRole().equals("parent")){
                holder.icon_book.setVisibility(View.VISIBLE);
            }else{
                holder.icon_book.setVisibility(View.GONE);
            }
        }
        if(currentItem.getRole().toLowerCase(Locale.ROOT).equals("center")){
            holder.name.setText(currentItem.getFname() );
            holder.name.setVisibility(View.VISIBLE);
        }else{
            holder.name.setVisibility(View.GONE);
            holder.name.setText(currentItem.getFname() + " " + currentItem.getLname());
        }

        if(!currentItem.getAddress().trim().equals("")){

            holder.address.setText(currentItem.getAddress());

            holder.address.setVisibility(View.VISIBLE);
        }else {
            holder.address.setVisibility(View.GONE);
        }
        Glide.with(context).load(USER_IMAGES_DIR +
                        currentItem.getIcon())
                .error(R.drawable.ic_user)
                .into(holder.icon);

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfile.class);
            intent.putExtra("user_id", list.get(position).getProvider_id());
            intent.putExtra("offer_id", "");
            intent.putExtra("type", "service");
            context.startActivity(intent);
        });

        holder.icon_book.setOnClickListener(v -> {
            Intent intent = new Intent(context, Booking.class);
            intent.putExtra("provider_id", list.get(position).getProvider_id());
            intent.putExtra("offer_id", "");
            intent.putExtra("type", "service");
            intent.putExtra("amount", 0.0);
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
            List<ProvidersModel> list1=new ArrayList<>();
            if(charSequence==null || charSequence.length()==0){
                list1.addAll(listFull);
            }
            else {
                String filterPattern=charSequence.toString().toLowerCase().trim();
                for (ProvidersModel item: listFull){
                    if(
                            item.getAddress().toLowerCase().contains(filterPattern)|
                                    item.getFname().toLowerCase().contains(filterPattern)|
                                    item.getLname().toLowerCase().contains(filterPattern)|
                                    item.getRole().toLowerCase().contains(filterPattern) ){
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
            try {
                list.clear();
                list.addAll((ArrayList<ProvidersModel>)filterResults.values);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
}