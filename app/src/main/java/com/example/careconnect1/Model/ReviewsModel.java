package com.example.careconnect1.Model;

public class ReviewsModel {
    String id, book_id, user_id ,center_id, text, date;

    String parent_name, parent_icon;

    public ReviewsModel(String id, String book_id, String user_id, String center_id, String text, String parent_name, String parent_icon, String date) {
        this.id = id;
        this.book_id = book_id;
        this.user_id = user_id;
        this.center_id = center_id;
        this.text = text;
        this.parent_name = parent_name;
        this.parent_icon = parent_icon;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getParent_name() {
        return parent_name;
    }
    public String getParent_icon() {return parent_icon;}

    public String getId() {
        return id;
    }

    public String getBook_id() {
        return book_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCenter_id() {
        return center_id;
    }

    public String getText() {
        return text;
    }
}
