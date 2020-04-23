package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

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
import com.example.popularmovies.Utils.AppExecutors;
import com.example.popularmovies.Utils.NetworkUtils;
import com.example.popularmovies.model.AppDatabase;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieEntry;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private String TAG = DetailActivity.class.getSimpleName();

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
    @BindView(R.id.unfavorite_btn)
    TextView mUnfavoriteButton;
    //private Movie mMovie;
    private Trailer mTrailer;
    private static List<Trailer> mTrailerList;
    private static List<Review> mReviewList;
    private Review mReview;

    // Member variable for the Database
    private AppDatabase mDb;
    private String mTitle;
    private String mMoviePoster;
    private String mReleaseDate;
    private String mVoteAverage;
    private String mPlotSynopsis;
    private String mFavourite;

    public static final String EXTRA_MOVIE_ID = "extraMovieId";
    public static final String INSTANCE_MOVIE_ID = "instanceMovieId";
    private static final int DEFAULT_MOVIE_ID = -1;
    private int mMovieId = DEFAULT_MOVIE_ID;
    private MovieEntry movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        // Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_MOVIE_ID)) {
            mMovieId = savedInstanceState.getInt(INSTANCE_MOVIE_ID, DEFAULT_MOVIE_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_MOVIE_ID))
            if (mMovieId == DEFAULT_MOVIE_ID)
                mMovieId = intent.getIntExtra(EXTRA_MOVIE_ID, DEFAULT_MOVIE_ID);
        final LiveData<MovieEntry> movie = mDb.movieDao().loadMovieById(mMovieId);
        movie.observe(this, new Observer<MovieEntry>() {
            @Override
            public void onChanged(MovieEntry movieEntry) {
                movie.removeObserver(this);
                Log.v(TAG, "Receiving database update from LiveData");
                populateUI(movieEntry);
            }
        });

        mTrailerList = new ArrayList<>();
        mReviewList = new ArrayList<>();

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
                markMovieAsFavourite();
            }
        });

        mUnfavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAsFavourite();
            }
        });
    }

    private void removeAsFavourite() {
        final MovieEntry movieEntry = new MovieEntry(mTitle, mMoviePoster, mReleaseDate,
                mVoteAverage, mPlotSynopsis, mFavourite);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mMovieId != DEFAULT_MOVIE_ID) {
                    //update movie
                    movieEntry.setFavourite(null);
                    mDb.movieDao().updateMovie(movieEntry);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showFavouriteMovieButton();
                        }
                    });
                }
                finish();
            }
        });
    }

    private void markMovieAsFavourite() {
        final MovieEntry movieEntry = new MovieEntry(mTitle, mMoviePoster, mReleaseDate,
                mVoteAverage, mPlotSynopsis, mFavourite);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mMovieId != DEFAULT_MOVIE_ID) {
                    //update book
                    movieEntry.setFavourite("Favourite");
                    mDb.movieDao().updateMovie(movieEntry);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showRemoveAsFavouriteMovieButton();
                        }
                    });
                }
                finish();
            }
        });
    }

    private void populateUI(MovieEntry movie) {

        if (movie == null) {
            return;
        }

        setTitle(movie.getTitle());
        Glide.with(getApplicationContext())
                .load(String.valueOf(NetworkUtils.buildPosterUrl(movie.getMoviePoster())))
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .into(mPosterImageView);
        String rating = movie.getVoteAverage();
        mRatingsTextView.setText(rating);
        String releaseDate = movie.getReleaseDate();
        mReleaseTextView.setText(releaseDate);
        String plotSynopsis = movie.getPlotSynopsis();
        mPlotSynopsisTextView.setText(plotSynopsis);
    }

    public void getTrailer() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest
                = new JsonObjectRequest(Request.Method.GET,
                NetworkUtils.buildTrailerUrl(movie.getId()).toString(), null,
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
                NetworkUtils.buildReviewUrl(movie.getId()).toString(), null,
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

    public void showFavouriteMovieButton() {
        mFavoriteButton.setVisibility(View.VISIBLE);
        mUnfavoriteButton.setVisibility(View.INVISIBLE);
    }

    public void showRemoveAsFavouriteMovieButton() {
        mUnfavoriteButton.setVisibility(View.VISIBLE);
        mFavoriteButton.setVisibility(View.INVISIBLE);
    }
}
