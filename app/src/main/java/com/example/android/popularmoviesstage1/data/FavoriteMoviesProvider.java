package com.example.android.popularmoviesstage1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.popularmoviesstage1.data.FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME;

/**
 * Created by carlosblanco on 2/7/17.
 */

public class FavoriteMoviesProvider extends ContentProvider {

    public static final int CODE_FAVORITE_MOVIES = 100;
    public static final int CODE_FAVORITE_MOVIES_WITH_ID = 101;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteMoviesDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(
                FavoriteMoviesContract.CONTENT_AUTHORITY,
                FavoriteMoviesContract.PATH_FAVORITE_MOVIES,
                CODE_FAVORITE_MOVIES);

        matcher.addURI(
                FavoriteMoviesContract.CONTENT_AUTHORITY,
                FavoriteMoviesContract.PATH_FAVORITE_MOVIES + "/#",
                CODE_FAVORITE_MOVIES_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new FavoriteMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case CODE_FAVORITE_MOVIES:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        long id = 0;
        Uri returnUri; // URI to be returned

        switch (match) {
            case CODE_FAVORITE_MOVIES:
                id = db.insert(TABLE_NAME, null, contentValues);
                if (id >= 0) {
                    returnUri = ContentUris.withAppendedId(
                            FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case CODE_FAVORITE_MOVIES_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                Log.v("PROVIDER ", "DELETED URI = " + uri);
                Log.v("PROVIDER ", "DELETED ID = " + id);
                // Use selections/selectionArgs to filter for this ID
                rowsDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (rowsDeleted != 0) {
            // A task was deleted, set notification

            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }


}
