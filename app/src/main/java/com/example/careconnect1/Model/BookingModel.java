package com.example.careconnect1.Model;

import java.util.ArrayList;

public class BookingModel {
    String id, date, time,service_id,service_name, service_price, parent_id,parent_name, parent_email, parent_icon,
            center_id,center_icon, center_name, center_email, status, offer_id, offer_price,offer_time, offer_description, offer_icon
            ,payment_id, payment_type, payment_amount, reject_cause;

    ArrayList<ServiceModel> arrayList;



    public BookingModel(String id, String date, String time, String parent_id, String parent_name , String parent_email, String parent_icon,
                        String center_id, String center_name, String center_email,String center_icon,
                        String status, String offer_id, String offer_price, String offer_time,
                        String offer_description, String offer_icon, String payment_id, String payment_type, String payment_amount,String reject_cause,ArrayList<ServiceModel> arrayList) {
        this.reject_cause = reject_cause;
        this.payment_amount = payment_amount;
        this.payment_id = payment_id;
        this.payment_type = payment_type;
        this.parent_icon = parent_icon;
        this.id = id;
        this.offer_icon = offer_icon;
        this.center_icon = center_icon;
        this.date = date;
        this.time = time;
        this.parent_id = parent_id;
        this.parent_name = parent_name;
        this.parent_email = parent_email;
        this.center_id = center_id;
        this.center_name = center_name;
        this.center_email = center_email;
        this.status = status;
        this.offer_id = offer_id;
        this.offer_price = offer_price;
        this.offer_time = offer_time;
        this.offer_description = offer_description;
        this.arrayList = arrayList;
    }

    public String getParent_icon() {
        return parent_icon;
    }

    public String getOffer_icon() {
        return offer_icon;
    }


    public ArrayList<ServiceModel> getArrayList() {
        return arrayList;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getCenter_icon() {
        return center_icon;
    }

    public String getReject_cause() {
        return reject_cause;
    }

    public String getTime() {
        return time;
    }

    public String getService_id() {
        return service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public String getService_price() {
        return service_price;
    }

    public String getParent_id() {
        return parent_id;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public String getParent_name() {
        return parent_name;
    }

    public String getParent_email() {
        return parent_email;
    }

    public String getCenter_id() {
        return center_id;
    }

    public String getCenter_name() {
        return center_name;
    }

    public String getCenter_email() {
        return center_email;
    }

    public String getStatus() {
        return status;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public String getOffer_time() {
        return offer_time;
    }

    public String getOffer_description() {
        return offer_description;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public String getPayment_type() {
        return payment_type;
    }
}
