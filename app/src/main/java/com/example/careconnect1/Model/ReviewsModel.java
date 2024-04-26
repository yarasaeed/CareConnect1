package com.example.careconnect1.Model;

public class ReviewsModel {
    String id, book_id, user_id ,provider_id, text, date;

    String parent_name, parent_icon;

    public ReviewsModel(String id, String book_id, String user_id, String provider_id, String text, String parent_name, String parent_icon, String date) {
        this.id = id;
        this.book_id = book_id;
        this.user_id = user_id;
        this.provider_id = provider_id;
        this.text = text;
        this.parent_name = parent_name;
        this.parent_icon = parent_icon;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getCustomer_name() {
        return parent_name;
    }

    public String getCustomer_icon() {
        return parent_icon;
    }

    public String getId() {
        return id;
    }

    public String getBook_id() {
        return book_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public String getText() {
        return text;
    }
}
