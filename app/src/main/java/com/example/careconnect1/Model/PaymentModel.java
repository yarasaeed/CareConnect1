package com.example.careconnect1.Model;

public class PaymentModel {
    String id, number, name, date, cvc, user_id, type, used;

    public PaymentModel(String id, String number, String name, String date,  String cvc, String user_id, String type, String used) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.date = date;
        this.cvc = cvc;
        this.user_id = user_id;
        this.type = type;
        this.used = used;
    }

    public String getUsed() {
        return used;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }


    public String getCvc() {
        return cvc;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getType() {
        return type;
    }
}
