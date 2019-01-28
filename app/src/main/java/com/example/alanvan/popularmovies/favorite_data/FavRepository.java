package com.example.alanvan.popularmovies.favorite_data;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.alanvan.popularmovies.favorite_data.database.AppDatabase;
import com.example.alanvan.popularmovies.favorite_data.database.FavDao;
import com.example.alanvan.popularmovies.favorite_data.database.FavEntry;

import java.util.List;

public class FavRepository {
    private final FavDao mFavDao;
    private final LiveData<List<FavEntry>> mFavEntries;
    private static final Object LOCK = new Object();
    private static FavRepository sInstance;

    private FavRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        mFavDao = db.favDao();
        mFavEntries = mFavDao.loadAllFavEntries();
    }

    public synchronized static FavRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new FavRepository(context);
            }
        }
        return sInstance;
    }

    public synchronized LiveData<List<FavEntry>> getAllFavEntries() {
        return mFavEntries;
    }

    public synchronized void insertFavEntry(FavEntry favEntry) {
        mFavDao.addFavorite(favEntry);
    }

    public void deleteFavEntry(int id) {
        mFavDao.deleteFavEntry(id);
    }
}