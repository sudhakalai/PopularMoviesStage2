package com.example.android.popularmoviesstage1;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class MovieTrailer {

    private URL mTrailerUrl;
    private URL mImageUrl;

    MovieTrailer(URL trailerUrl, URL imageUrl){
        mTrailerUrl = trailerUrl;
        mImageUrl = imageUrl;
    }

    public URL getTrailerURL(){ return mTrailerUrl; }
    public URL getmImageUrl() { return mImageUrl; }
}
