package com.example.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable, Comparable<Movie> {
    private final String title;
    private final String moviePoster;
    private final String releaseDate;
    private final String voteAverage;
    private final String plotSynopsis;

    public Movie(String title, String moviePoster, String releaseDate, String voteAverage, String plotSynopsis) {
        this.title = title;
        this.moviePoster = moviePoster;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    private Movie(Parcel parcel) {
        title = parcel.readString();
        moviePoster = parcel.readString();
        releaseDate = parcel.readString();
        voteAverage = parcel.readString();
        plotSynopsis = parcel.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(moviePoster);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
        dest.writeString(plotSynopsis);
    }

    public static final Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>() {
                @Override
                public Movie createFromParcel(Parcel parcel) {
                    return new Movie(parcel);
                }

                @Override
                public Movie[] newArray(int size) {
                    return new Movie[size];
                }
            };

    @Override
    public int compareTo(Movie o) {
        return this.getVoteAverage().compareTo(o.getVoteAverage());
    }
}
