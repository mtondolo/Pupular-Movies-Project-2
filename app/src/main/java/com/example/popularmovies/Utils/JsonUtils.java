package com.example.popularmovies.Utils;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    // This method parses JSON from a web response and returns an array of Strings
    public static String[] getSimpleNewsStringsFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(moviesJsonStr)) {
            return null;
        }

        JSONObject moviesJsonObject = new JSONObject(moviesJsonStr);
        JSONArray moviesJsonArray = moviesJsonObject.getJSONArray("results");

        // String array to hold each movie item String
        String[] parsedMovieData = null;
        parsedMovieData = new String[moviesJsonArray.length()];

        for (int i = 0; i < moviesJsonArray.length(); i++) {
            JSONObject movieJsonObject = moviesJsonArray.getJSONObject(i);

            String title = movieJsonObject.optString("original_title");
            String moviePoster = movieJsonObject.optString("poster_path");
            String releaseDate = movieJsonObject.optString("release_date");
            String voteAverage = movieJsonObject.optString("vote_average");
            String plotSynopsis = movieJsonObject.optString("overview");

            parsedMovieData[i] = title + " - " + moviePoster + " - " + releaseDate + " - "
                    + voteAverage + " - " + plotSynopsis;
        }
        return parsedMovieData;
    }
}
