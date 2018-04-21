package com.example.android.popularmoviesstage1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This is a utils class that parses the Json and returns the result in the form of an arrayList.
 */

public class MovieJsonUtils {

    public static ArrayList<Movie> getDetailsFromJson (String movieJson){

        ArrayList<Movie> movieDetailsList = new ArrayList<>();

        //checking if the json is null.
        if(movieJson == null){
            return null;
        }

        //Parsing the json.
        try {
            JSONObject movieJsonObject = new JSONObject(movieJson);
            if(movieJsonObject.has("results")){

                JSONArray resultsArray = movieJsonObject.getJSONArray("results");

                for(int i =0; i< resultsArray.length(); i++){
                    JSONObject movieObject = resultsArray.getJSONObject(i);
                    String originalTitle = movieObject.getString("original_title");
                    String imageUrl = movieObject.getString("poster_path");
                    String plot = movieObject.getString("overview");
                    String rating = movieObject.getString("vote_average");
                    String releaseDate = movieObject.getString("release_date");
                    String movieId = movieObject.getString("id");
                    movieDetailsList.add(new Movie(originalTitle, imageUrl, plot, rating, releaseDate, movieId));
                }
            }


            return movieDetailsList;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
