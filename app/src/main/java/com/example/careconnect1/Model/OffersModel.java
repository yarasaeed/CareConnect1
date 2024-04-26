package com.example.cleanup.Models;

public class OffersModel {
    String id, icon, description, date, price, cleaner_id;

    String  cleaner_name, cleaner_icon, used;

    public OffersModel(String id, String icon, String description, String date, String price, String cleaner_id, String cleaner_name, String cleaner_icon) {
        this.id = id;
        this.icon = icon;
        this.description = description;
        this.date = date;
        this.price = price;
        this.cleaner_id = cleaner_id;
        this.cleaner_name = cleaner_name;
        this.cleaner_icon = cleaner_icon;

    }

    public String getUsed() {
        return used;
    }

    public String getCleaner_name() {
        return cleaner_name;
    }

    public String getCleaner_icon() {
        return cleaner_icon;
    }

    public OffersModel(String id, String icon, String description, String date, String price, String cleaner_id, String used) {
        this.id = id;
        this.icon = icon;
        this.used = used;
        this.description = description;
        this.date = date;
        this.price = price;
        this.cleaner_id = cleaner_id;
    }

    public String getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public String getCleaner_id() {
        return cleaner_id;
    }
}
