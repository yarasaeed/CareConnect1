package com.example.careconnect1.Adapters;

import static com.example.careconnect1.Utilities.Config.IP;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
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
import com.example.careconnect1.Enumerations.PaymentMethod;
import com.example.careconnect1.Model.PaymentModel;
import com.example.careconnect1.R;
import com.example.careconnect1.UI.AllPayments;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;
import java.util.Locale;

public class AllPaymentsAdapter extends RecyclerView.Adapter<AllPaymentsAdapter.MyHolder> {
    private final ArrayList<PaymentModel> list;
    private final Context context;

    public AllPaymentsAdapter(Context activity, ArrayList<PaymentModel> list) {
        this.list = list;
        context = activity;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private final TextView name, date, number,  delete_cause;

        private final ShapeableImageView icon_delete, icon;

        public MyHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            date = v.findViewById(R.id.date);
            number = v.findViewById(R.id.number);
            icon = v.findViewById(R.id.icon);
            icon_delete = v.findViewById(R.id.icon_delete);
            delete_cause = v.findViewById(R.id.delete_cause);


        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_all_payments, parent, false);
        return new MyHolder(v);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        PaymentModel currentItem = list.get(position);
        holder.name.setText(currentItem.getName().toUpperCase(Locale.ROOT));
        holder.number.setText("XXXX XXXX XXXX "+currentItem.getNumber().substring(currentItem.getNumber().length() - 4));
        holder.date.setText(currentItem.getDate());


        if (currentItem.getUsed().equals("0")) {
            holder.icon_delete.setEnabled(true);
            holder.icon_delete.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.black, null)));
            holder.delete_cause.setText("");
        } else {
            holder.icon_delete.setEnabled(false);
            holder.icon_delete.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.silver, null)));
            holder.delete_cause.setText("Card used, can't be deleted now");
        }

        if(currentItem.getType().equals(PaymentMethod.CREDIT.toString())){
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.credit_card,null));

        }else if(currentItem.getType().equals(PaymentMethod.DEBIT.toString())){
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.debit,null));

        }else{
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.paypal,null));
        }


        holder.icon_delete.setOnClickListener(v -> {
            delete(list.get(position).getId());
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    void delete(String site_id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Card");
        builder.setMessage("Are you sure?");
        builder.setIcon(R.drawable.delete);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "delete_payment.php?id="+ site_id , response -> {
                Toast.makeText(context, response.trim(), Toast.LENGTH_SHORT).show();
                ((AllPayments)context).getPaymentCards();
            }, error -> {
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        });
        builder.setNegativeButton("No" ,null);
        builder.show();
    }

}