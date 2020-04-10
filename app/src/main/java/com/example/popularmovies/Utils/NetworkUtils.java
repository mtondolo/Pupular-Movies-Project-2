package com.example.popularmovies.Utils;

import android.app.Application;
import android.net.Uri;

import com.example.popularmovies.R;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {
    private static final String MOVIES_DB_BASE_URL
            = "https://api.themoviedb.org/3/movie/popular";

    private static final String MOVIES_DB_POSTER_URL
            = "https://image.tmdb.org/t/p/";

    private static final String MOVIES_DB_TRAILER_URL
            = "https://api.themoviedb.org/3/movie/";

    private final static String QUERY_PARAM = "api_key";
    private final static ThreadLocal<String> api_key = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            try {
                return getApplicationUsingReflection().getString(R.string.THE_MOVIE_DB_API_TOKEN);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return String.valueOf(api_key);
        }
    };
    private final static String logo_size = "w185";
    private final static String video_path = "videos";

    // Builds the URL used to talk to the the movie db server.
    public static URL buildUrl() {
        Uri buildUri = Uri.parse(MOVIES_DB_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, api_key.get())
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // Builds the URL used to talk to the the movie db server to get the movie poster.
    public static URL buildPosterUrl(String pathParameter) {
        Uri buildPosterUri = Uri.parse(MOVIES_DB_POSTER_URL)
                .buildUpon()
                .appendEncodedPath(logo_size)
                .appendEncodedPath(pathParameter)
                .build();
        URL url = null;
        try {
            url = new URL(buildPosterUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildTrailerUrl(String movie_id) {
        Uri buildTrailerUrl = Uri.parse(MOVIES_DB_TRAILER_URL)
                .buildUpon()
                .appendEncodedPath(movie_id)
                .appendEncodedPath(video_path)
                .appendQueryParameter(QUERY_PARAM, api_key.get())
                .build();
        URL url = null;
        try {
            url = new URL(buildTrailerUrl.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static Application getApplicationUsingReflection() throws Exception {
        return (Application) Class.forName("android.app.AppGlobals")
                .getMethod("getInitialApplication").invoke(null, (Object[]) null);
    }
}
