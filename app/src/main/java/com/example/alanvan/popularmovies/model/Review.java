package com.example.alanvan.popularmovies.model;

public class Review {
    private String reviewText;
    private String reviewAuthor;

    public Review(String reviewText, String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
        this.reviewText = reviewText;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public String getReviewText() {
        return reviewText;
    }
}
