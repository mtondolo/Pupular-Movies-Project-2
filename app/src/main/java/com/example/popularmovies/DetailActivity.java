package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.popularmovies.Utils.NetworkUtils;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieEntry;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.ratings_tv)
    TextView mRatingsTextView;
    @BindView(R.id.release_date_tv)
    TextView mReleaseTextView;
    @BindView(R.id.plot_synopsis_tv)
    TextView mPlotSynopsisTextView;
    @BindView(R.id.image_iv)
    ImageView mPosterImageView;
    @BindView(R.id.trailer_iv)
    ImageView mTrailerImageView;
    @BindView(R.id.reviews_tv)
    TextView mReviewsTextView;
    @BindView(R.id.favorite_btn)
    TextView mFavoriteButton;
    private Movie mMovie;
    private Trailer mTrailer;
    private static List<Trailer> mTrailerList;
    private static List<Review> mReviewList;
    private Review mReview;

    // Member variable for the Database
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        // Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getApplicationContext());

        mTrailerList = new ArrayList<>();
        mReviewList = new ArrayList<>();

        Intent intent = getIntent();
        mMovie = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            setTitle(mMovie.getTitle());
            loadDetailUIPoster();
            populateUI();
        }

        mTrailerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTrailer();
            }
        });
        mReviewsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReviews();
            }
        });
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteAMovie();
            }
        });

    }

    private void favoriteAMovie() {

        // Get data to save
        String apiId = mMovie.getMovieId();
        String title = mMovie.getTitle();

        MovieEntry movieEntry = new MovieEntry(apiId, title);
        // Save data with room
        mDb.movieDao().insertBook(movieEntry);
        finish();
    }

    private void populateUI() {
        String rating = mMovie.getVoteAverage();
        mRatingsTextView.setText(rating);
        String releaseDate = mMovie.getReleaseDate();
        mReleaseTextView.setText(releaseDate);
        String plotSynopsis = mMovie.getPlotSynopsis();
        mPlotSynopsisTextView.setText(plotSynopsis);
    }

    private void loadDetailUIPoster() {
        Glide.with(getApplicationContext())
                .load(String.valueOf(NetworkUtils.buildPosterUrl(mMovie.getMoviePoster())))
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .into(mPosterImageView);
        setTitle(mMovie.getTitle());
    }

    public void getTrailer() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest
                = new JsonObjectRequest(Request.Method.GET,
                NetworkUtils.buildTrailerUrl(mMovie.getMovieId()).toString(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray responseJSONArray = response.getJSONArray("results");
                            for (int i = 0; i == 0; i++) {
                                JSONObject jsonObject = responseJSONArray.getJSONObject(0);
                                mTrailer = new Trailer();
                                mTrailer.setKey(jsonObject.getString("key"));
                                mTrailerList.add(mTrailer);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        watchYoutubeVideo();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getReviews() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest
                = new JsonObjectRequest(Request.Method.GET,
                NetworkUtils.buildReviewUrl(mMovie.getMovieId()).toString(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray responseJSONArray = response.getJSONArray("results");
                            for (int i = 0; i == 0; i++) {
                                JSONObject jsonObject = responseJSONArray.getJSONObject(0);
                                mReview = new Review();
                                mReview.setUrl(jsonObject.getString("url"));
                                mReviewList.add(mReview);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        openWebPage(mReview.getUrl());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void watchYoutubeVideo() {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + mTrailer.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + mTrailer.getKey()));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    public void openWebPage(String url) {
        Context context = this;

        // Launch the ReviewActivity using an explicit Intent
        Class destinationClass = ReviewActivity.class;
        Intent intentToStartReviewActivity = new Intent(context, destinationClass);
        intentToStartReviewActivity.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(intentToStartReviewActivity);
    }
}
