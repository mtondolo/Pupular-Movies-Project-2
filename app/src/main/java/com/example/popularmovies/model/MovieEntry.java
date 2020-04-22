package com.example.popularmovies.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class MovieEntry implements Comparable<MovieEntry>{

    @PrimaryKey()
    private int id;
    private String title;
    private String moviePoster;
    private String releaseDate;
    private String voteAverage;
    private String plotSynopsis;
    private String favourite;

    @Ignore
    public MovieEntry(String title, String moviePoster, String releaseDate, String voteAverage, String plotSynopsis, String favourite) {
        this.title = title;
        this.moviePoster = moviePoster;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
        this.favourite = favourite;
    }

    public MovieEntry(int id, String title, String moviePoster, String releaseDate, String voteAverage, String plotSynopsis, String favourite) {
        this.id = id;
        this.title = title;
        this.moviePoster = moviePoster;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
        this.favourite = favourite;
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

    public void setId(int id) {
        this.id = id;
    }

    public String getFavourite() {
        return favourite;
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

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    @Override
    public int compareTo(MovieEntry o) {
        return this.getVoteAverage().compareTo(o.getVoteAverage());
    }
}
