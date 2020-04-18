package com.example.popularmovies.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY id")
    List<MovieEntry> loadAllMovies();

    @Insert
    void insertBook(MovieEntry movieEntry);

    @Delete
    void deleteBook(MovieEntry movieEntry);
}
