package com.example.popularmovies.model;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY id")
    LiveData<List<MovieEntry>> loadAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MovieEntry movieEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieEntry movieEntry);

    @Delete
    void deleteBook(MovieEntry movieEntry);

    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<MovieEntry> loadMovieById(int id);

    @Query("SELECT * FROM movies WHERE id = :id")
    MovieEntry loadMovieByIdForFavourite(int id);

    @Query("SELECT * FROM movies ORDER BY voteAverage DESC")
    LiveData<List<MovieEntry>> sortMoviesByTopRated();
}
