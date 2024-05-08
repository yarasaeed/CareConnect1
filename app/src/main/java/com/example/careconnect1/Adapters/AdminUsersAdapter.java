package com.example.careconnect1.Adapters;

import static com.example.careconnect1.Utilities.Config.IPADMIN;
import static com.example.careconnect1.Utilities.Config.USER_IMAGES_DIR;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.careconnect1.Admin.AllReviews;
import com.example.careconnect1.Admin.AllUsers;
import com.example.careconnect1.Model.UsersModel;
import com.example.careconnect1.R;
import com.example.careconnect1.UI.UserProfile;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminUsersAdapter extends RecyclerView.Adapter<AdminUsersAdapter.recyclerHolder> implements Filterable {
    ArrayList<UsersModel> list;// Original list of users
    ArrayList<UsersModel> listFull; // Copy of the original list (used for filtering)
    Activity activity;

    public AdminUsersAdapter(Activity activity, ArrayList<UsersModel> list) {
        this.list = list;
        this.activity = activity;
        listFull =new ArrayList<>(list);// Initialize the copy of the list
    }

    public static class recyclerHolder extends RecyclerView.ViewHolder {
        public TextView name, role;
        private final ShapeableImageView icon_call;
        private final ShapeableImageView icon_email;
        private final ShapeableImageView icon,icon_delete,icon_review;
        private final LinearLayout layout_tools;
        private final MaterialCardView cardView;

        public recyclerHolder(View v) {
            super(v);
            // Initialize the views
            layout_tools = v.findViewById(R.id.layout_tools);
            cardView = v.findViewById(R.id.cardView);
            name = v.findViewById(R.id.name);
            role = v.findViewById(R.id.role);
            icon_call = v.findViewById(R.id.icon_call);
            icon_email = v.findViewById(R.id.icon_email);
            icon = v.findViewById(R.id.icon);
            icon_delete = v.findViewById(R.id.icon_delete);
            icon_review = v.findViewById(R.id.icon_review);
        }
    }

    @NonNull
    @Override
    public recyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a user item
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_admin_user, parent, false);
        return new recyclerHolder(v);
    }

    @SuppressLint({"SetTextI18n", "ResourceType", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull final recyclerHolder holder, @SuppressLint("RecyclerView") final int position) {
        // Inflate the layout for a user item
        holder.name.setText(list.get(position).getName());
        holder.role.setText(list.get(position).getRole().toUpperCase(Locale.ROOT));
        //if(list.get(position).getRole().toLowerCase(Locale.ROOT).equals("provider")){
        // holder.layout_employee.setVisibility(View.VISIBLE);
        // }else {
        //     holder.layout_employee.setVisibility(View.GONE);
        //  }
        // Set click listeners for call, email, review, and profile actions
        if(list.get(position).getRole().toLowerCase(Locale.ROOT).equals("parent")){
            holder.icon_review.setVisibility(View.VISIBLE);
            holder.layout_tools.setWeightSum(3);
        }else {
            holder.icon_review.setVisibility(View.GONE);
            holder.layout_tools.setWeightSum(2);
        }
        holder.icon_call.setOnClickListener(v -> {
            Intent intent=new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+list.get(position).getPhone()+""));
            activity.startActivity(intent);
        });

        holder.icon_email.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"+list.get(position).getEmail()));
            intent.putExtra(Intent.EXTRA_EMAIL, list.get(position).getEmail());
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            activity.startActivity(intent);

        });
        holder.icon_review.setOnClickListener(v -> {
            Intent intent = new Intent(activity, AllReviews.class);
            intent.putExtra("user_id", list.get(position).getId());
            activity.startActivity(intent);

        });
        // Load user icon using Glide library
        Glide.with(activity).load(USER_IMAGES_DIR + list.get(position).getIcon())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_user)
                .into(holder.icon);

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, UserProfile.class);
            intent.putExtra("user_id", list.get(position).getId());
            activity.startActivity(intent);
        });
        holder.icon_delete.setOnClickListener(v -> delete(list.get(position).getId()));
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
    // Filter for searching/filtering users
    private final Filter filter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<UsersModel> filteredList=new ArrayList<>();
            if(charSequence==null || charSequence.length()==0){ // if list empty
                filteredList.addAll(listFull);  // Add all users from the original list to the filtered list
            }
            else {  // Convert the search query to lowercase and remove leading/trailing whitespaces
                String filterPattern=charSequence.toString().toLowerCase().trim();
                for (UsersModel item: listFull){
                    //  iterate on each user then Check if the user's phone number, email, or name contains the filter pattern
                    if(item.getPhone().toLowerCase().contains(filterPattern)|
                            item.getEmail().toLowerCase().contains(filterPattern)|
                            item.getName().toLowerCase().contains(filterPattern) ){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        @SuppressWarnings("unchecked")

        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            try { // Clear the current list and add all the filtered users
                list.clear();
                list.addAll((ArrayList<UsersModel>)filterResults.values);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    void delete(String id){
        // Method to delete a user
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Delete image");
        builder.setMessage("Are you sure?");
        builder.setIcon(R.drawable.ic_delete);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, IPADMIN + "delete_user.php?id="+ id , response -> {
                Toast.makeText(activity, response.trim(), Toast.LENGTH_SHORT).show();
                ((AllUsers)activity).getUsers();
            }, error -> {
            });

            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(stringRequest);
        });
        builder.setNegativeButton("No" ,null);
        builder.show();
    }

}