package com.example.alanvan.popularmovies.utilities;

import android.content.Context;

import com.example.alanvan.popularmovies.favorite_data.FavRepository;

public class InjectorUtils {

    public static FavRepository provideRepository(Context context) {
        return FavRepository.getInstance(context);
    }
}
