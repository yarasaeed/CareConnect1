package com.example.careconnect1.Model;

public class ProvidersModel {
    String cleaner_id, fname, lname, gender, role, bio, address, email, phone,icon;

    public ProvidersModel(String cleaner_id, String fname, String lname, String gender, String role, String bio, String address, String email, String phone, String icon) {
        this.cleaner_id = cleaner_id;
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.role = role;
        this.bio = bio;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.icon = icon;
    }


    public String getCleaner_id() {
        return cleaner_id;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getGender() {
        return gender;
    }

    public String getRole() {
        return role;
    }

    public String getBio() {
        return bio;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getIcon() {
        return icon;
    }
}
