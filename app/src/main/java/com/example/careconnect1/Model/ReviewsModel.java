package com.example.careconnect1.Model;

public class ReviewsModel {
    String id, book_id, user_id ,cleaner_id, text, date;

    String customer_name, customer_icon;

    public ReviewsModel(String id, String book_id, String user_id, String cleaner_id, String text, String customer_name, String customer_icon, String date) {
        this.id = id;
        this.book_id = book_id;
        this.user_id = user_id;
        this.cleaner_id = cleaner_id;
        this.text = text;
        this.customer_name = customer_name;
        this.customer_icon = customer_icon;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getCustomer_icon() {
        return customer_icon;
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

    public String getCleaner_id() {
        return cleaner_id;
    }

    public String getText() {
        return text;
    }
}
