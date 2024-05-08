package com.example.careconnect1.Model;

public class UsersModel {
    String id, name, role, phone ,email, address,icon;

    public UsersModel(String id, String name, String role, String phone, String email, String address,String icon) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getIcon() {
        return icon;
    }
}
