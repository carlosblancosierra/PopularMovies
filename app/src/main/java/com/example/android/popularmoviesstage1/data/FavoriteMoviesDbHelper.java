package com.example.android.popularmoviesstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.popularmoviesstage1.data.FavoriteMoviesContract.FavoriteMoviesEntry;

/**
 * Created by carlosblanco on 2/7/17.
 */

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 4;

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WEATHER_TABLE =
                "CREATE TABLE " + FavoriteMoviesEntry.TABLE_NAME + " (" +

                        FavoriteMoviesEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        FavoriteMoviesEntry.COLUMN_TITLE    + " STRING, " +

                        FavoriteMoviesEntry.COLUMN_MOVIE_ID    + " REAL, " +

                        FavoriteMoviesEntry.COLUMN_TRAILER_URL + " STRING, " +

                        FavoriteMoviesEntry.COLUMN_DATE        + " STRING, " +

                        FavoriteMoviesEntry.COLUMN_POSTER      + " BLOB NOT NULL, " +

                        FavoriteMoviesEntry.COLUMN_RATING      + " REAL, " +

                        FavoriteMoviesEntry.COLUMN_SYNOPSIS    + " STRING);";

        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
