package com.example.android.popularmoviesstage1.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.popularmoviesstage1.Data.FavoriteMovieContract.FavoriteMovieEntry;

/*
    * This is the DbHelper class which creates the database and upgrades it if necessary.
 */

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "favorite.db";
    public static int DATABASE_VERSION = 1;

    public FavoriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + FavoriteMovieEntry.TABLE_NAME + "("
                + FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FavoriteMovieEntry.COLUMN_MOVIE + " TEXT NOT NULL, "
                + FavoriteMovieEntry.COLUMN_PLOT + " TEXT, "
                + FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT, "
                + FavoriteMovieEntry.COLUMN_RATING + " TEXT, "
                + FavoriteMovieEntry.COLUMN_MOVIE_ID + " TEXT, "
                + FavoriteMovieEntry.COLUMN_IMAGE + " BLOB );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);

        Log.v("databasequery", SQL_CREATE_FAVORITES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
