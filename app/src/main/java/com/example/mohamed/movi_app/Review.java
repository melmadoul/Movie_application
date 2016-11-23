package com.example.mohamed.movi_app;

/**
 * Created by Nicolás Carrasco on 22-09-2015.
 */
public class Review {

    private String author;
    private String comment;

    public Review(String author, String comment) {
        this.author = author;
        this.comment = comment;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getComment() {
        return this.comment;
    }
}
