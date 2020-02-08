package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.example.popularmovies.Utils.JsonUtils;
import com.example.popularmovies.Utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_movies_data)
    TextView mMovieDisplayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadMoviesData();
    }

    // This method will tell some background method to get the movies data in the background.
    private void loadMoviesData() {
        new FetchMoviesTask().execute();
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... strings) {
            URL moviesRequestUrl = NetworkUtils.buildUrl();
            try {
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);
                String[] simpleJsonMoviesData = JsonUtils
                        .getSimpleNewsStringsFromJson(MainActivity.this, jsonMoviesResponse);
                return simpleJsonMoviesData;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] moviesData) {
            if (moviesData != null) {
                // Iterate through the array and append the Strings to the TextView.
                for (String moviesString : moviesData) {
                    mMovieDisplayTextView.append((moviesString) + "\n\n\n");
                }
            }
        }
    }
}
