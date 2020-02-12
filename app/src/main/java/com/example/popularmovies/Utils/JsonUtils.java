package com.example.popularmovies.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonUtils {

    private static List<Movie> movieList;

    // This method parses JSON from a web response and returns an array of Strings
    public static List<Movie> getSimpleNewsStringsFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(moviesJsonStr)) {
            return null;
        }

        JSONObject moviesJsonObject = new JSONObject(moviesJsonStr);
        JSONArray moviesJsonArray = moviesJsonObject.getJSONArray("results");

        movieList = new ArrayList<>();

        for (int i = 0; i < moviesJsonArray.length(); i++) {
            JSONObject movieJsonObject = moviesJsonArray.getJSONObject(i);

            String title = movieJsonObject.optString("original_title");
            String moviePoster = movieJsonObject.optString("poster_path");
            String releaseDate = movieJsonObject.optString("release_date");
            String voteAverage = movieJsonObject.optString("vote_average");
            String plotSynopsis = movieJsonObject.optString("overview");

            Movie movie = new Movie(title, moviePoster, releaseDate, voteAverage, plotSynopsis);
            movieList.add(movie);
        }
        return movieList;
    }

    public static void sortMoviesByTopRated() {
        Collections.sort(movieList, Collections.<Movie>reverseOrder());
    }
}
