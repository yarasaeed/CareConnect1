package com.example.careconnect1.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.careconnect1.Model.PaymentModel;
import com.example.careconnect1.R;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;

public class PaymentsCardsSpinner extends BaseAdapter {
    private final ArrayList<PaymentModel> arrayList;
    private final Context context;



    public PaymentsCardsSpinner(Context context, ArrayList<PaymentModel> arrayList) {
        this.arrayList = arrayList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.layout_payment_card, parent, false);
        }
        TextView number = view.findViewById(R.id.number);
        ShapeableImageView icon = view.findViewById(R.id.icon);
        number.setText(arrayList.get(position).getNumber());
        if(arrayList.get(position).getType().equals("credit card")){
            Glide.with(context).load(R.drawable.ic_credit_card).into(icon);
        }else if(arrayList.get(position).getType().equals("debit card")){
            Glide.with(context).load(R.drawable.ic_debit).into(icon);
        }else{
            Glide.with(context).load(R.drawable.ic_paypal).into(icon);
        }

        return view;
    }
}
