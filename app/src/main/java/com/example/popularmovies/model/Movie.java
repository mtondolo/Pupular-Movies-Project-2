package com.example.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable, Comparable<Movie> {
    private int id;
    private String title;
    private String moviePoster;
    private String releaseDate;
    private String voteAverage;
    private String plotSynopsis;

    public Movie() {
    }

    public Movie(int id, String title, String moviePoster, String releaseDate, String voteAverage,
                 String plotSynopsis) {
        this.id = id;
        this.title = title;
        this.moviePoster = moviePoster;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public int getId() {
        return id;
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

    private Movie(Parcel parcel) {
        id = parcel.readInt();
        title = parcel.readString();
        moviePoster = parcel.readString();
        releaseDate = parcel.readString();
        voteAverage = parcel.readString();
        plotSynopsis = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
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
