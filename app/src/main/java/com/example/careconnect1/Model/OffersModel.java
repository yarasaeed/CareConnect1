package com.example.careconnect1.Model;

public class OffersModel {
    String id, icon, description, date, price, provider_id;

    String  provider_name, provider_icon, used;

    public OffersModel(String id, String icon, String description, String date, String price, String provider_id, String provider_name, String provider_icon) {
        this.id = id;
        this.icon = icon;
        this.description = description;
        this.date = date;
        this.price = price;
        this.provider_id = provider_id;
        this.provider_name = provider_name;
        this.provider_icon = provider_icon;

    }

    public String getUsed() {
        return used;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public String getProvider_icon() {
        return provider_icon;
    }

    public OffersModel(String id, String icon, String description, String date, String price, String provider_id, String used) {
        this.id = id;
        this.icon = icon;
        this.used = used;
        this.description = description;
        this.date = date;
        this.price = price;
        this.provider_id = provider_id;
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

    public String getProvider_id() {
        return provider_id;
    }
}
