package com.example.popularmovies.Utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String MOVIES_DB_BASE_URL
            = "https://api.themoviedb.org/3/movie/popular";

    final static String QUERY_PARAM = "api_key";
    final static String api_key = "024d46a9e528641235e789659cc35089";

    // Builds the URL used to talk to the the movie db server.
    public static URL buildUrl() {
        Uri buildUri = Uri.parse(MOVIES_DB_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, api_key)
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // This method returns the entire result from the HTTP response.
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
