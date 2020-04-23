package com.example.popularmovies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.popularmovies.model.AppDatabase;
import com.example.popularmovies.model.MovieEntry;

public class DetailViewModel extends ViewModel {
    private LiveData<MovieEntry> movie;

    public DetailViewModel(AppDatabase database, int movieId) {
        movie = database.movieDao().loadMovieById(movieId);
    }

    public LiveData<MovieEntry> getMovie() {
        return movie;
    }
}
