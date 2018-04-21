package com.example.android.popularmoviesstage1;

import java.io.Serializable;

/**
 * This is the Movie object class
 */
@SuppressWarnings("serial")
public class Movie implements Serializable {

    //Declaring member variables
    private String mOriginalTitle;
    private String mImageUrl;
    private String mPlotAnalysis;
    private String mVoteAverage;
    private String mReleaseDate;
    private String mMovieId;

    //Constructor
    public Movie(String originalTitle, String imageUrl, String plotAnalysis, String voteAverage, String releaseDate, String movieId){
        mOriginalTitle = originalTitle;
        mImageUrl = imageUrl;
        mPlotAnalysis = plotAnalysis;
        mVoteAverage = voteAverage;
        mReleaseDate = releaseDate;
        mMovieId = movieId;
    }

    //Getter methods
    public String getOriginalTitle(){ return mOriginalTitle; }
    public String getImageUrl(){ return mImageUrl; }
    public String getPlotAnalysis(){return mPlotAnalysis; }
    public String getVoteAverage(){ return mVoteAverage; }
    public String getReleaseDate(){ return mReleaseDate; }

    public String getMovieId(){ return mMovieId; }
}
