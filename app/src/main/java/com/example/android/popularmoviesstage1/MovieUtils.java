package com.example.android.popularmoviesstage1;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * This is a Utils class to form the Url from the selected search and return a json
 */

public class MovieUtils {

    /**
     * Declaring the static variables.
     */
    final static String SCHEME = "http";

    final static String BASE_URL = "api.themoviedb.org";

    final static String BASE_PATH_1 = "3";

    final static String BASE_PATH_2 = "movie";

    final static String POPULAR_PATH = "popular";

    final static String TOP_RATED_PATH = "top_rated";

    final static String TRAILER_PATH = "videos";

    final static String REVIEW_PATH = "reviews";

    final static String API_KEY = "api_key";

    final static String key = ""; // enter your api key

    /*****
     *
     * @param pathIdentifier
     * @return URL
     * This method takes the path identifier "popular" or "most rated" and returns the query URL.
     */

    public static URL buildUrl (int pathIdentifier){
        Uri.Builder builder = new Uri.Builder();
        switch (pathIdentifier){
            case 1:
                builder.scheme(SCHEME)
                        .authority(BASE_URL)
                        .appendPath(BASE_PATH_1)
                        .appendPath(BASE_PATH_2)
                        .appendPath(POPULAR_PATH)
                        .appendQueryParameter(API_KEY, key);
                break;
            case 2:
                builder.scheme(SCHEME)
                        .authority(BASE_URL)
                        .appendPath(BASE_PATH_1)
                        .appendPath(BASE_PATH_2)
                        .appendPath(TOP_RATED_PATH)
                        .appendQueryParameter(API_KEY, key);
                break;

        }

        String stringUrl = builder.build().toString();
        URL url = null;
        try {
            url = new URL(stringUrl);
            Log.v("MovieUtils", url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     *
     * @param pathIdentifier, movieId
     * @return URL
     * This method takes the path identifier "trailer" or "review" and movieId and returns the query URL.
     */

    public static URL buildUrlWithId (int pathIdentifier, String movieId){
        Uri.Builder builder = new Uri.Builder();
        switch (pathIdentifier){
            case 1:
                builder.scheme(SCHEME)
                        .authority(BASE_URL)
                        .appendPath(BASE_PATH_1)
                        .appendPath(BASE_PATH_2)
                        .appendPath(movieId)
                        .appendPath(TRAILER_PATH)
                        .appendQueryParameter(API_KEY, key);
                break;
            case 2:
                builder.scheme(SCHEME)
                        .authority(BASE_URL)
                        .appendPath(BASE_PATH_1)
                        .appendPath(BASE_PATH_2)
                        .appendPath(movieId)
                        .appendPath(REVIEW_PATH)
                        .appendQueryParameter(API_KEY, key);
                break;

        }

        String stringUrl = builder.build().toString();
        URL url = null;
        try {
            url = new URL(stringUrl);
            Log.v("MovieUtils", url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    /**
     *
     * @param url
     * @return json as a String
     * @throws IOException
     * This method takes in a query Url, connects to the internet and returns a json as a string.
     */

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        // Checks if the Url is null
        if(url == null){
            return null;
        }
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
