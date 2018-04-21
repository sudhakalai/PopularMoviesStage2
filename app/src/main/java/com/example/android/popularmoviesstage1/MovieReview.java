package com.example.android.popularmoviesstage1;

public class MovieReview {

    private String mAuthorName;
    private String mReview;

    MovieReview(String authorName, String review){
        mAuthorName = authorName;
        mReview = review;
    }

    public String getAuthorName(){ return mAuthorName; }
    public String getReview(){ return mReview; }
}
