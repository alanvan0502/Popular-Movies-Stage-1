package com.example.alanvan.popularmovies.model;

import java.util.List;

public class MovieDetail {
    private int duration;
    private List<String> videoLink;

    public MovieDetail(int duration, List<String> videoLink) {
        this.duration = duration;
        this.videoLink = videoLink;
    }

    public int getDuration() {
        return duration;
    }

    public List<String> getVideoLink() {
        return videoLink;
    }
}
