package com.example.alanvan.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.alanvan.popularmovies.favorite_data.FavRepository;
import com.example.alanvan.popularmovies.favorite_data.database.FavEntry;
import com.example.alanvan.popularmovies.utilities.InjectorUtils;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private final FavRepository repository;
    private final LiveData<List<FavEntry>> mAllFavoriteEntries;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        repository = InjectorUtils.provideRepository(application.getApplicationContext());
        mAllFavoriteEntries = repository.getAllFavEntries();
    }

    LiveData<List<FavEntry>> getAllFavoriteEntries() {
        return mAllFavoriteEntries;
    }
}
