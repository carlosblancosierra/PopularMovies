package com.example.android.popularmoviesstage1.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by carlosblanco on 2/7/17.
 */

public class FavoriteMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesstage1";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";

    public static final class FavoriteMoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIES)
                .build();

        public static final String TABLE_NAME = "weather";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_POSTER = "poster";

        public static final String COLUMN_SYNOPSIS = "synopsis";

        public static final String COLUMN_RATING = "rating";


    }

}
