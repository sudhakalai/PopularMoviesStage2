package com.example.android.popularmoviesstage1.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.popularmoviesstage1.Data.FavoriteMovieContract.FavoriteMovieEntry;

import com.example.android.popularmoviesstage1.Movie;

/*
This is the content provider class which has the CRUD functions.
 */

public class FavoriteMovieProvider extends ContentProvider {

    private FavoriteMovieDbHelper mDbHelper;
    private String TAG_NAME = FavoriteMovieProvider.class.getSimpleName();

    private static final int FAV_MOVIE = 100;
    private static final int FAV_MOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(FavoriteMovieContract.CONTENT_AUTHORITY, FavoriteMovieContract.PATH_MOVIE, FAV_MOVIE);
        sUriMatcher.addURI(FavoriteMovieContract.CONTENT_AUTHORITY, FavoriteMovieContract.PATH_MOVIE + "/#", FAV_MOVIE_ID);
    }

    //Initialises the FavoriteMovieDbHelper
    @Override
    public boolean onCreate() {

        mDbHelper = new FavoriteMovieDbHelper(getContext());
        return true;
    }

    //Query method
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match){
            case FAV_MOVIE:
                cursor = db.query(FavoriteMovieEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                Log.v("testCursor", String.valueOf(cursor.getCount()));
                break;
            case FAV_MOVIE_ID:
                s = FavoriteMovieEntry._ID + "=?";
                strings1 = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(FavoriteMovieEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                Log.v(TAG_NAME, cursor.toString());
                break;
            default:
                throw new IllegalArgumentException("cannot query unknown uri "+ uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    //insert method
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case FAV_MOVIE:
                long id = db.insert(FavoriteMovieEntry.TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(FavoriteMovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                return returnUri;
            default:
                throw new IllegalArgumentException("Insertion is not supported" + uri);
        }

    }

    //delete method
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;

        switch (match){
            case FAV_MOVIE_ID:
                String id = uri.getPathSegments().get(1);
                rowsDeleted = db.delete(FavoriteMovieEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new IllegalArgumentException("Deletion is not suppported " +uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
