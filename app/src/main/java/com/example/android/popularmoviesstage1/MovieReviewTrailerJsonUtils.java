package com.example.android.popularmoviesstage1;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MovieReviewTrailerJsonUtils {
    public static ArrayList<MovieTrailer> getDetailsFromTrailerJson (String trailerJson){

        ArrayList<MovieTrailer> trailerUrls = new ArrayList<>();

        //checking if the json is null.
        if(trailerJson == null){
            return null;
        }

        JSONObject trailerJsonObject = null;
        try {
            trailerJsonObject = new JSONObject(trailerJson);
            if(trailerJsonObject.has("results")){
                JSONArray resultsArray = trailerJsonObject.getJSONArray("results");

                for(int i =0; i< resultsArray.length(); i++){
                    JSONObject trailerObject = resultsArray.getJSONObject(i);
                    String youtubeKey = trailerObject.getString("key");
                    URL trailerURL = idToTrailerUrlConverter(youtubeKey);
                    URL imageURL = idToImageURLConverter(youtubeKey);
                    trailerUrls.add(new MovieTrailer(trailerURL, imageURL));
                }

                return trailerUrls;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<MovieReview> getDetailsFromReviewJson(String reviewJson){
        ArrayList<MovieReview> reviews = new ArrayList<>();

        //checking if the json is null.
        if(reviewJson == null){
            return null;
        }

        JSONObject reviewJsonObject = null;
        try {
            reviewJsonObject = new JSONObject(reviewJson);
            if(reviewJsonObject.has("results")){
                JSONArray resultsArray = reviewJsonObject.getJSONArray("results");

                for(int i =0; i< resultsArray.length(); i++){
                    JSONObject reviewObject = resultsArray.getJSONObject(i);
                    String authorName = reviewObject.getString("author");
                    String review = reviewObject.getString("content");
                    reviews.add(new MovieReview(authorName, review));
                }

                return reviews;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static URL idToTrailerUrlConverter(String id){
        String SCHEME = "https";
        String BASE_URL = "www.youtube.com";
        String BASE_PATH = "watch";
        String V = "v";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(BASE_URL)
                .appendPath(BASE_PATH)
                .appendQueryParameter(V, id);
        String stringUrl = builder.build().toString();
        URL url = null;
        try {
            url = new URL(stringUrl);
            Log.v("trailerUtils", url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL idToImageURLConverter(String id){

        String SCHEME = "https";
        String BASE_URL = "img.youtube.com";
        String BASE_PATH = "vi";
        String END_PATH = "hqdefault.jpg";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(BASE_URL)
                .appendPath(BASE_PATH)
                .appendPath(id)
                .appendPath(END_PATH);
        String stringUrl = builder.build().toString();
        URL url = null;
        try {
            url = new URL(stringUrl);
            Log.v("imageUtils", url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

}
