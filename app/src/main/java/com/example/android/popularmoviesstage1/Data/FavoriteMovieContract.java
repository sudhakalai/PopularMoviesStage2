package com.example.android.popularmoviesstage1.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/*
** This is the contract class.
 */

public class FavoriteMovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesstage1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static final class FavoriteMovieEntry implements BaseColumns{

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE = "name";

        public static final String COLUMN_PLOT = "plot";

        public static final String COLUMN_RELEASE_DATE = "releasedate";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_IMAGE = "image";

        public static final String COLUMN_MOVIE_ID = "movieidentity";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIE);


    }
}
