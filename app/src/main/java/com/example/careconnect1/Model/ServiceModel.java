package com.example.careconnect1.Model;

public class ServiceModel {
    String id, name, price, center_id;

    public ServiceModel(String id, String name, String price, String center_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.center_id = center_id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCenter_id() {
        return center_id;
    }
}
