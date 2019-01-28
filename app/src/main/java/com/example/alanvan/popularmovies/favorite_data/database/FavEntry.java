package com.example.alanvan.popularmovies.favorite_data.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;

@Entity(tableName = "fav_table")
public class FavEntry {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movieId")
    private int movieId;

    public FavEntry(int movieId) {
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }
}
