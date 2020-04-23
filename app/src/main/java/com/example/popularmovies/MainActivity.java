package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.popularmovies.Utils.AppExecutors;
import com.example.popularmovies.Utils.NetworkUtils;
import com.example.popularmovies.model.AppDatabase;
import com.example.popularmovies.model.MovieEntry;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ConstraintLayout constraintLayout;

    @BindView(R.id.recyclerview_movies)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageTextView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    private MoviesAdapter mMoviesAdapter;

    // Member variable for the Database
    private AppDatabase mDb;
    private static List<MovieEntry> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        constraintLayout = findViewById(R.id.constraintlayout);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this, this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        getMoviesData();

        // Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getApplicationContext());
        retrieveMovies();
    }

    private void retrieveMovies() {
        Log.d(TAG, "Actively retrieving the books from the database");
        final LiveData<List<MovieEntry>> movies = mDb.movieDao().loadAllMovies();
        movies.observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(List<MovieEntry> movieEntries) {
                Log.d(TAG, "Receiving database update from LiveData");
                mMoviesAdapter.setMovies(movieEntries);
            }
        });
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
            sortMoviesByTopRated();
            mMoviesAdapter.notifyDataSetChanged();
            Snackbar.make(constraintLayout, R.string.sort_order_message, Snackbar.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int id) {
        Intent detailActivityIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailActivityIntent.putExtra(DetailActivity.EXTRA_MOVIE_ID, id);
        startActivity(detailActivityIntent);
    }

    public void getMoviesData() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest
                = new JsonObjectRequest(Request.Method.GET, NetworkUtils.buildUrl().toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray responseJSONArray = response.getJSONArray("results");

                            for (int i = 0; i < responseJSONArray.length(); i++) {

                                JSONObject jsonObject = responseJSONArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String title = jsonObject.getString("original_title");
                                String moviePoster = jsonObject.getString("poster_path");
                                String release = jsonObject.getString("release_date");
                                String voteAverage = jsonObject.getString("vote_average");
                                String plotSynopsis = jsonObject.getString("overview");
                                String favourite = jsonObject.optString("");

                                final MovieEntry movieEntry = new MovieEntry(id, title, moviePoster,
                                        release, voteAverage, plotSynopsis, favourite);

                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDb.movieDao().insertMovie(movieEntry);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mLoadingIndicator.setVisibility(View.INVISIBLE);
                        }
                        mMoviesAdapter.notifyDataSetChanged();
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        showMoviesDataView();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                showErrorMessage();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public static void sortMoviesByTopRated() {
        Collections.sort(movies, Collections.<MovieEntry>reverseOrder());
    }
}

