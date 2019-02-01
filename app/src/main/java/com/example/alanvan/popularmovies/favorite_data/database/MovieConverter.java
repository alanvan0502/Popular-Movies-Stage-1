package com.example.alanvan.popularmovies.favorite_data.database;

import android.arch.persistence.room.TypeConverter;

import com.example.alanvan.popularmovies.model.Movie;
import com.example.alanvan.popularmovies.utilities.JsonUtils;

public class MovieConverter {
    @TypeConverter
    public static Movie toMovie(String jsonString) {
        return JsonUtils.convertToMovieObject(jsonString);
    }

    @TypeConverter
    public static String fromMovie(Movie movie) {
        return JsonUtils.convertToJsonString(movie);
    }
}