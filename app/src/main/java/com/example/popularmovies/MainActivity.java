package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.popularmovies.Utils.NetworkUtils;
import com.example.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.MovieAdapterOnClickHandler {

    @BindView(R.id.recyclerview_movies)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageTextView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    private MoviesAdapter mMoviesAdapter;

    private static List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        movieList = new ArrayList<>();
        mMoviesAdapter = new MoviesAdapter(movieList, this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        getMoviesData();
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
            Toast.makeText(this, R.string.sort_order_message, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(Movie clickedMovie) {
        Context context = this;
        Class detailActivity = DetailActivity.class;
        Intent detailActivityIntent = new Intent(context, detailActivity);
        detailActivityIntent.putExtra(Intent.EXTRA_TEXT, clickedMovie);
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

                                Movie movie = new Movie();

                                movie.setTitle(jsonObject.getString("original_title"));
                                movie.setMoviePoster(jsonObject.getString("poster_path"));
                                movie.setReleaseDate(jsonObject.getString("release_date"));
                                movie.setVoteAverage(jsonObject.getString("vote_average"));
                                movie.setPlotSynopsis(jsonObject.getString("overview"));

                                movieList.add(movie);

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
        Collections.sort(movieList, Collections.<Movie>reverseOrder());
    }
}

