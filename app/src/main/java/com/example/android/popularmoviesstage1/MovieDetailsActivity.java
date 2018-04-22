package com.example.android.popularmoviesstage1;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.Data.FavoriteMovieContract;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.android.popularmoviesstage1.Data.FavoriteMovieContract.FavoriteMovieEntry;

/**
 * This class displays the details of the movie being clicked.
 */

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnclickListener{

    //Declaring the variables
    private TextView nameTv;
    private TextView plotTv;
    private TextView ratingTv;
    private TextView releaseTv;
    private ImageView posterIv;
    private ScrollView movieDetailsSv;
    private ImageButton favButton;
    private boolean isEnable;
    private RecyclerView trailerRecyclerView;
    private TrailerAdapter trailerAdapter;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private ArrayList<MovieTrailer> trailers = new ArrayList<>();
    private ArrayList<MovieReview> reviews = new ArrayList<>();
    private String movieId;
    private TrailerAdapter.TrailerAdapterOnclickListener mTrialerClickHandler;
    private LinearLayoutManager trailerLayoutManager;
    private LinearLayoutManager reviewLayoutManager;
    private Context context;
    private Uri newUri = null;
    private String CALLBACKS_BOOLEAN_KEY = "callbacks";
    private String movieName;
    private int id;
    private static final String BUNDLE_RECYCLER_TRAILER = "Details.trailer.recycler.layout";
    Parcelable savedRecyclerTrailerState;
    private static final String BUNDLE_RECYCLER_REVIEW = "Details.review.recycler.layout";
    Parcelable savedRecyclerReviewState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        //Initialising the views
        nameTv = findViewById(R.id.tv_movie_name);
        plotTv = findViewById(R.id.tv_movie_plot);
        ratingTv = findViewById(R.id.tv_movie_rating);
        releaseTv = findViewById(R.id.tv_movie_release);
        posterIv = findViewById(R.id.iv_poster);
        movieDetailsSv = findViewById(R.id.sv_fav_movie);
        mTrialerClickHandler = this;
        context = this;


        favButton = findViewById(R.id.ib_star);

        Log.v("buttonTest", "oncreate is called");




        //Getting the value from the intent
        Intent intent = getIntent();

        if (intent != null) {

            if (intent.hasExtra("MovieObject")) {
                Movie movie = (Movie) intent.getSerializableExtra("MovieObject");

                //Setting the values to the views
                movieName = movie.getOriginalTitle();
                nameTv.setText(movieName);
                plotTv.setText(movie.getPlotAnalysis());
                ratingTv.setText(movie.getVoteAverage());
                releaseTv.setText(movie.getReleaseDate());

                movieId = movie.getMovieId();
                Log.v("movieId", movieId);


                String posterPath = "http://image.tmdb.org/t/p/w780/" + movie.getImageUrl();

                //Drawing the poster using picasso library.
                Picasso.with(this)
                        .load(posterPath)
                        .into(posterIv);

           }

           new TrailerAsyncTask().execute(movieId);
            new ReviewAsyncTask().execute(movieId);

           try {
                URL trailerURL = new URL("https://www.youtube.com/watch?v=GpAuCG6iUcA");
                URL imageURL = new URL("\"https://img.youtube.com/vi/GpAuCG6iUcA/default.jpg");
                trailers.add(new MovieTrailer(trailerURL, imageURL));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            checkIfFav();
                favButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.v("buttonTest", "onclick is called");
                        if (isEnable){
                            favButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                            deleteFavMovie();
                            isEnable = false;
                        }else{
                            favButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                            saveFavoriteMovie();
                            isEnable = true;
                        }
                    }
                });

            trailerRecyclerView = findViewById(R.id.rv_trailers);
            trailerAdapter = new TrailerAdapter(trailers, mTrialerClickHandler);
            trailerLayoutManager =
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            trailerRecyclerView.setLayoutManager(trailerLayoutManager);
            trailerRecyclerView.setHasFixedSize(true);
            trailerRecyclerView.setAdapter(trailerAdapter);


            reviewRecyclerView = findViewById(R.id.rv_reviews);
            reviewAdapter = new ReviewAdapter(reviews);
            reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            reviewRecyclerView.setLayoutManager(reviewLayoutManager);
            reviewRecyclerView.setHasFixedSize(true);
            reviewRecyclerView.setAdapter(reviewAdapter);

        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null)
        {

            if (savedInstanceState.containsKey(CALLBACKS_BOOLEAN_KEY)) {
                Log.v("buttonTest", "savedinstance is called");
                Boolean favState = savedInstanceState
                        .getBoolean(CALLBACKS_BOOLEAN_KEY);
                if (favState){
                    favButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                    isEnable = true;
                }else{
                    favButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                    isEnable = false;
                }
            }
            savedRecyclerTrailerState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_TRAILER);
            trailerRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerTrailerState);

            savedRecyclerReviewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_REVIEW);
            reviewRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerReviewState);
        }

    }


    public void checkIfFav(){
        isEnable = false;
        Log.v("buttonTest", "check fav is called");
        String[] columns = { FavoriteMovieEntry._ID, FavoriteMovieEntry.COLUMN_MOVIE};
        Cursor cursor = getContentResolver().query(FavoriteMovieEntry.CONTENT_URI,
                columns,
                null,
                null,
                null);
        Log.v("columns", String.valueOf(cursor.getCount()));

       Log.v("columns", movieName);

       cursor.moveToFirst();
        for(int i =0; i<cursor.getCount(); i++){

            String fav = cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE));
            Log.v("columns", fav);
            if(fav.contains(movieName)){
                favButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                id = cursor.getInt(cursor.getColumnIndex(FavoriteMovieEntry._ID));
                isEnable = true;
            }
        cursor.moveToNext();
        }

        cursor.close();
   }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Boolean b = isEnable;
        outState.putBoolean(CALLBACKS_BOOLEAN_KEY, b);
        outState.putParcelable(BUNDLE_RECYCLER_TRAILER, trailerRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(BUNDLE_RECYCLER_REVIEW, reviewRecyclerView.getLayoutManager().onSaveInstanceState());

    }

    @Override
    public void onClick(MovieTrailer movieTrailer) {
        URL trailerUrl = movieTrailer.getTrailerURL();
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(trailerUrl.toString()));
            startActivity(webIntent);
    }

    public void saveFavoriteMovie(){

        String movieName = nameTv.getText().toString().trim();
        String plot = plotTv.getText().toString().trim();
        String rating = ratingTv.getText().toString().trim();
        String releaseDate = releaseTv.getText().toString().trim();

        BitmapDrawable bitmapDrawable = ((BitmapDrawable) posterIv.getDrawable());
        Bitmap bitmap = bitmapDrawable .getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();

        if(movieName.isEmpty()){
            return;
        }



        ContentValues values = new ContentValues();
        values.put(FavoriteMovieEntry.COLUMN_MOVIE, movieName);
        values.put(FavoriteMovieEntry.COLUMN_PLOT, plot);
        values.put(FavoriteMovieEntry.COLUMN_RATING, rating);
        values.put(FavoriteMovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        values.put(FavoriteMovieEntry.COLUMN_IMAGE, imageInByte);
        values.put(FavoriteMovieEntry.COLUMN_MOVIE_ID, movieId);

        newUri = getContentResolver().insert(FavoriteMovieEntry.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(this, getString(R.string.movie_not_added),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.movie_added),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteFavMovie(){

        if(newUri == null){
        newUri = ContentUris.withAppendedId(FavoriteMovieEntry.CONTENT_URI, id);
       }

       Log.v("newUri", newUri.toString());

            int rowsDeleted = getContentResolver().delete(newUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_success),
                        Toast.LENGTH_SHORT).show();
            }
    }


   //Getting trailers
    public class TrailerAsyncTask extends AsyncTask<String, Void, ArrayList<MovieTrailer>>{

        @Override
        protected ArrayList<MovieTrailer> doInBackground(String... strings) {
            String movieId = strings[0];
            ArrayList<MovieTrailer> urls = new ArrayList<>();
            try {
                String trailerJson = MovieUtils.getResponseFromHttpUrl(MovieUtils.buildUrlWithId(1, movieId)) ;
                urls = MovieReviewTrailerJsonUtils.getDetailsFromTrailerJson(trailerJson);
                Log.v("arraylist", urls.toString());
                return urls;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieTrailer> movieTrailers) {
            super.onPostExecute(movieTrailers);
            trailers = movieTrailers;
            trailerAdapter.setTrailerList(trailers);
            trailerRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerTrailerState);

        }
    }

    //Getting reviews
    public class ReviewAsyncTask extends AsyncTask<String, Void, ArrayList<MovieReview>>{

        @Override
        protected ArrayList<MovieReview> doInBackground(String... strings) {
            String movieId = strings[0];
            ArrayList<MovieReview> reviews = new ArrayList<>();
            try {
                String reviewJson = MovieUtils.getResponseFromHttpUrl(MovieUtils.buildUrlWithId(2, movieId));
                reviews = MovieReviewTrailerJsonUtils.getDetailsFromReviewJson(reviewJson);
                Log.v("arraylist", reviews.toString());
                return reviews;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieReview> movieReviews) {
            super.onPostExecute(movieReviews);
            reviews = movieReviews;
            reviewAdapter.setReviewList(reviews);
            reviewRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerReviewState);

        }
    }


}
