package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.Data.FavoriteMovieContract;
import com.example.android.popularmoviesstage1.Data.FavoriteMovieContract.FavoriteMovieEntry;

public class FavMovieDetailsActivity extends AppCompatActivity {

    private TextView nameTv;
    private TextView plotTv;
    private TextView ratingTv;
    private TextView releaseTv;
    private ImageView posterIv;
    private ScrollView favMovieSv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_movie_details);

        //Initialising the views
        nameTv = findViewById(R.id.tv_fav_movie_name);
        plotTv = findViewById(R.id.tv_fav_movie_plot);
        ratingTv = findViewById(R.id.tv_fav_movie_rating);
        releaseTv = findViewById(R.id.tv_fav_movie_release);
        posterIv = findViewById(R.id.iv_fav_poster);
        favMovieSv = findViewById(R.id.sv_fav_movie);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("movieName")) {
                String movieName = intent.getStringExtra("movieName");

                String selection = FavoriteMovieEntry.COLUMN_MOVIE + "=?";
                String[] selectionArgs = {movieName};
                Cursor cursor = getContentResolver().query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                        null,
                        selection,
                        selectionArgs,
                        null);
                cursor.moveToFirst();
                if(cursor != null){

                    nameTv.setText(cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE)));
                    plotTv.setText(cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_PLOT)));
                    ratingTv.setText(cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_RATING)));
                    releaseTv.setText(cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_RELEASE_DATE)));

                    byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_IMAGE));
                    Bitmap bmp = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
                    posterIv.setImageBitmap(bmp);
                }
            }
        }
    }


}
