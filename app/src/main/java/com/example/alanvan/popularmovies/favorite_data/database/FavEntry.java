package com.example.alanvan.popularmovies.favorite_data.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.alanvan.popularmovies.model.Movie;

@Entity(tableName = "fav_table")
public class FavEntry {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movieId")
    private int movieId;

    @ColumnInfo(name = "movie")
    private Movie movie;

    public FavEntry(Movie movie, int movieId) {
        this.movie = movie;
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }

    public Movie getMovie() {
        return movie;
    }
}