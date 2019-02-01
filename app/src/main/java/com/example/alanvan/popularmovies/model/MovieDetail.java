package com.example.alanvan.popularmovies.model;

import java.util.List;

public class MovieDetail {
    private int duration;
    private List<String> videoLink;
    private List<Review> reviews;

    public MovieDetail(int duration, List<String> videoLink, List<Review> reviews) {
        this.duration = duration;
        this.videoLink = videoLink;
        this.reviews = reviews;
    }

    public int getDuration() {
        return duration;
    }

    public List<String> getVideoLink() {
        return videoLink;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
