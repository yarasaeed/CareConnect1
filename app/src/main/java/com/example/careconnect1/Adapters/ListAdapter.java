package com.example.careconnect1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.careconnect1.R;

import org.osmdroid.util.GeoPoint;

import java.security.Provider;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ProviderViewHolder> {

    private Context mCtx;
    private List<Provider> providerList;

    public ListAdapter(Context mCtx, List<Provider> providerList) {
        this.mCtx = mCtx;
        this.providerList = providerList;
    }

    public ListAdapter(FragmentActivity activity, List<java.security.Provider> providerList) {
    }

    @NonNull
    @Override
    public ProviderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.fragment_list_view, parent, false);
        return new ProviderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderViewHolder holder, int position) {
        Provider provider = providerList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(provider.getIcon())
                .into(holder.imageView);

        holder.name.setText(provider.getName());
        holder.textViewShortDesc.setText("Phone: " + provider.getPhone());
        // Assuming there is no rating or price for providers
    }

    @Override
    public int getItemCount() {
        return providerList.size();
    }

    static class ProviderViewHolder extends RecyclerView.ViewHolder {

        TextView name, textViewShortDesc;
        ImageView imageView;

        public ProviderViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textViewName);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }



private static class Provider {
        private GeoPoint location;
        private String name;
        private String icon;
        private String phone;

        public Provider(GeoPoint location, String name, String icon, String phone) {
            this.location = location;
            this.name = name;
            this.icon = icon;
            this.phone = phone;
        }

        public GeoPoint getLocation() {
            return location;
        }

        public String getName() {
            return name;
        }

        public String getIcon() {
            return icon;
        }

        public String getPhone() {
            return phone;
        }
    }}