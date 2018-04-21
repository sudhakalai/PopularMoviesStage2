package com.example.android.popularmoviesstage1;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.Data.FavoriteMovieContract.FavoriteMovieEntry;

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, FavoriteMovieCursorAdapter.FavMovieOnClickListener {

    private FavoriteMovieCursorAdapter movieCursorAdapter;
    private RecyclerView mRecyclerView;
    private TextView mErrorTv;
    private FavoriteMovieCursorAdapter.FavMovieOnClickListener movieOnClickListener;
    private static final int TASK_LOADER_ID = 0;
    private static final String BUNDLE_RECYCLER_LAYOUT = "FavActivity.recycler.layout";
    Parcelable savedRecyclerLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mRecyclerView = findViewById(R.id.rv_fav_movies);
        mErrorTv = findViewById(R.id.tv_error_message);
        movieOnClickListener = this;

        Context context = this;

        setTitle(getResources().getString(R.string.favorite_movies));

        GridLayoutManager layoutManager;

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager= new GridLayoutManager(context, 2);
        }
        else{
            layoutManager= new GridLayoutManager(context, 3);
        }

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        movieCursorAdapter = new FavoriteMovieCursorAdapter(this, movieOnClickListener);

        mRecyclerView.setAdapter(movieCursorAdapter);

       // getLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null)
        {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }
    @Override
    protected void onResume() {
        super.onResume();

        getLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorTv.setVisibility(View.INVISIBLE);
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor movieData= null;

            @Override
            protected void onStartLoading() {

                if (movieData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(movieData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(FavoriteMovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e("Cursor_loadInBackground", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                movieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if((cursor == null) || (cursor.getCount() ==0)){
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorTv.setVisibility(View.VISIBLE);
        }

        mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);

        movieCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        movieCursorAdapter.swapCursor(null);

    }


    @Override
    public void onClick(String name) {
        Log.v("favMovieTest", name);
        Intent intent = new Intent(FavoritesActivity.this, FavMovieDetailsActivity.class);
        intent.putExtra("movieName", name);
        startActivity(intent);

    }
}
