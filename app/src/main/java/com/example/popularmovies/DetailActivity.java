package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.popularmovies.Utils.NetworkUtils;
import com.example.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

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
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mMovie = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            setTitle(mMovie.getTitle());
            loadDetailUIPoster();
            populateUI();
        }
    }

    private void loadDetailUIPoster() {
        Glide.with(getApplicationContext())
                .load(String.valueOf(NetworkUtils.buildPosterUrl(mMovie.getMoviePoster())))
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .into(mPosterImageView);
        setTitle(mMovie.getTitle());
    }

    private void populateUI() {
        String rating = mMovie.getVoteAverage();
        mRatingsTextView.setText(rating);
        String releaseDate = mMovie.getReleaseDate();
        mReleaseTextView.setText(releaseDate);
        String plotSynopsis = mMovie.getPlotSynopsis();
        mPlotSynopsisTextView.setText(plotSynopsis);
    }
}
