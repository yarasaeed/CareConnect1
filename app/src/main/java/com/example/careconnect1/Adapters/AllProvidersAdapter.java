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
import com.example.careconnect1.Model.ProvidersModel;
import com.example.careconnect1.R;
import com.example.careconnect1.UI.Booking;
import com.example.careconnect1.UI.UserProfile;
import com.example.careconnect1.Utilities.UserData;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;
import java.util.Locale;

public class AllProvidersAdapter extends RecyclerView.Adapter<AllProvidersAdapter.MyHolder> {
    private final ArrayList<ProvidersModel> list;
    private final Context context;
    private UserData userData;

    public AllProvidersAdapter(Context activity, ArrayList<ProvidersModel> list) {
        this.list = list;
        context = activity;
        userData = new UserData(context);
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView role;
        private final MaterialCardView cardView;
        private final ShapeableImageView icon, icon_book;

        public MyHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            role = v.findViewById(R.id.role);
            icon = v.findViewById(R.id.icon);
            cardView = v.findViewById(R.id.cardView);
            icon_book = v.findViewById(R.id.icon_book);
        }
    }

    @NonNull
    @Override
    // Inflate the layout for each item in the list
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_all_providers, parent, false);
        return new MyHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    // Set the role text to uppercase
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        ProvidersModel currentItem = list.get(position);
        holder.role.setText(currentItem.getRole().toUpperCase(Locale.ROOT));
        // Check if the user is logged in and has the role of "parent" to show the booking icon else hide it
        if(userData.getId().equals("")){
            holder.icon_book.setVisibility(View.GONE);
        }else{
            if(userData.getRole().equals("parent")){
                holder.icon_book.setVisibility(View.VISIBLE);
            }else{
                holder.icon_book.setVisibility(View.GONE);
            }
        }
        // Set the name of the provider
        if (currentItem.getRole().toLowerCase(Locale.ROOT).equals("center")) {
            holder.name.setText(currentItem.getFname());
        } else {
            holder.name.setText(currentItem.getFname() + " " + currentItem.getLname());
        }

        // Load the cleaner's icon using Glide library

        Glide.with(context).load(USER_IMAGES_DIR +
                        currentItem.getIcon())
                .error(R.drawable.ic_user)
                .into(holder.icon);
        // Set a click listener on the card view to open the user's profile
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfile.class);
            intent.putExtra("user_id", list.get(position).getProvider_id());
            intent.putExtra("offer_id", "");
            intent.putExtra("type", "service");
            context.startActivity(intent);
        });
        // Set a click listener on the booking icon to initiate the booking process
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


}