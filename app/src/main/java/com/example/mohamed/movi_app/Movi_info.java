package com.example.mohamed.movi_app;

/**
 * Created by Mohamed on 07/11/2016.
 */

public class Movi_info {

    int id;
    String title;
    String poster;
    String relese_year;
    String user_rating;
    String over_view;

    public Movi_info(){}

    public Movi_info(int id, String title, String poster, String relese_year, String user_rating, String over_view) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.relese_year = relese_year;
        this.user_rating = user_rating;
        this.over_view = over_view;
    }

    public void setid(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setRelese_year(String relese_year) {
        this.relese_year = relese_year;
    }

    public void setUser_rating(String user_rating) {
        this.user_rating = user_rating;
    }

    public void setOver_view(String over_view) {
        this.over_view = over_view;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOver_view() {
        return over_view;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public String getRelese_year() {
        return relese_year;
    }

    public String getPoster() {
        return poster;
    }
}
