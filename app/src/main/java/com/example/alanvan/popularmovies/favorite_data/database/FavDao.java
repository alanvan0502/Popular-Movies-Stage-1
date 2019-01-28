package com.example.alanvan.popularmovies.favorite_data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavDao {
    @Query("SELECT * FROM fav_table")
    LiveData<List<FavEntry>> loadAllFavEntries();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addFavorite(FavEntry favEntry);

    @Query("DELETE FROM fav_table WHERE movieId = :id")
    void deleteFavEntry(int id);
}
