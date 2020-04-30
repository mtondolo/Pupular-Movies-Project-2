package com.example.popularmovies;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.popularmovies.model.AppDatabase;
import com.example.popularmovies.model.MovieEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static String TAG = MainActivity.class.getSimpleName();
    private LiveData<List<MovieEntry>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the movies from the Database");
        movies = database.movieDao().loadAllMovies();

    }

    public LiveData<List<MovieEntry>> getMovies() {
        return movies;
    }
}
