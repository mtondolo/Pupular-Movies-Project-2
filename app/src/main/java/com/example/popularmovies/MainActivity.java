package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovies.Utils.JsonUtils;
import com.example.popularmovies.Utils.NetworkUtils;
import com.example.popularmovies.model.Movie;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterOnClickHandler {

    @BindView(R.id.recyclerview_movies)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageTextView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(new ArrayList<Movie>(), this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        loadMoviesData();
    }

    // Method called showMoviesDataView to show the data and hide the error
    private void showMoviesDataView() {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    // Method called showErrorMessage to show the error and hide the data
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if (menuItemThatWasSelected == R.id.action_sort) {
            JsonUtils.sortMoviesByTopRated();
            mMoviesAdapter.notifyDataSetChanged();
            Toast.makeText(this, R.string.sort_order_message, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // This method will tell some background method to get the movies data in the background.
    private void loadMoviesData() {
        new FetchMoviesTask().execute();
    }

    @Override
    public void onListItemClick(Movie clickedMovie) {
        Context context = this;
        Class detailActivity = DetailActivity.class;
        Intent detailActivityIntent = new Intent(context, detailActivity);
        detailActivityIntent.putExtra(Intent.EXTRA_TEXT, clickedMovie);
        startActivity(detailActivityIntent);
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... strings) {
            URL moviesRequestUrl = NetworkUtils.buildUrl();
            try {
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);
                return JsonUtils
                        .getSimpleNewsStringsFromJson(jsonMoviesResponse);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showMoviesDataView();
                mMoviesAdapter.setMoviesData(moviesData);
            } else {
                showErrorMessage();
            }
        }
    }
}
