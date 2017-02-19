package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.Utils.MovieJsonUtil;
import com.example.android.popularmoviesstage1.Utils.NetworkUtils;
import com.example.android.popularmoviesstage1.data.FavoriteMoviesContract;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private MovieAdapter mMovieAdapter;
    private MoviesCursorAdapter mCursorAdapter;
    private String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private String PAGE_TITE;
    private TextView mPageTitleTextView;
    private int loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mPageTitleTextView = (TextView) findViewById(R.id.app_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mMovieAdapter = new MovieAdapter();
        mCursorAdapter = new MoviesCursorAdapter(this);

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(manager);
        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);

        loader = getPreferredList();
        getSupportLoaderManager().initLoader(loader, null, this);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        setUpPageTitle();


    }

    public void showProgressBar() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void showResults() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader onCreateLoader(final int loaderId, Bundle args) {


        return new AsyncTaskLoader(this) {

            ArrayList<MovieObject> mMovieObjects = null;
            Cursor mMoviesCursor = null;


            @Override
            protected void onStartLoading() {

                setUpPageTitle();
                showProgressBar();

                switch (loaderId) {
                    case NetworkUtils.CODE_TOP_RATED:
                    case NetworkUtils.CODE_POPULAR:
                        if (mMovieObjects != null) {
                            deliverResult(mMovieObjects);
                        } else {
                            forceLoad();
                        }
                        break;
                    case NetworkUtils.CODE_FAVORITES:
                        if (mMoviesCursor != null) {
                            // Delivers any previously loaded data immediately
                            deliverResult(mMoviesCursor);
                        } else {
                        showProgressBar();
                        forceLoad();
                        }
                        break;
                }
            }

            @Override
            public Object loadInBackground() {

                switch (loaderId) {
                    case NetworkUtils.CODE_TOP_RATED:
                    case NetworkUtils.CODE_POPULAR:
                        String jsonResponse = "";
                        try {
                            jsonResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl(loaderId, MainActivity.this));
                            if (jsonResponse != null) {

                                mMovieObjects = MovieJsonUtil.getMovieObjects(jsonResponse);
                                ArrayList<String> moviesPosterArray = MovieJsonUtil.getPicturesURLs(jsonResponse);

                                for (int i = 0; i < moviesPosterArray.size(); i++) {
                                    String moviePosterFile = moviesPosterArray.get(i);
                                    Bitmap currentPosterBitmap = NetworkUtils.loadImage(moviePosterFile);
                                    mMovieObjects.get(i).setPoster(currentPosterBitmap);
//                                    Log.v(LOG_TAG, "Movie Object: " + mMovieObjects.get(i).getTitle() + " poster: "
//                                            + moviePosterFile);
                                }
                            }
                            return mMovieObjects;

                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }

                    case NetworkUtils.CODE_FAVORITES:

                        try {
                            mMoviesCursor = getContentResolver().query(
                                    FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    null);
                            return mMoviesCursor;
                        } catch (Exception e) {
                            Log.e(TAG, "Failed to asynchronously load data.");
                            e.printStackTrace();
                            return null;
                        }
                    default:
                        return null;
                }

            }

            @Override
            public void deliverResult(Object data) {
                switch (loaderId) {
                    case NetworkUtils.CODE_TOP_RATED:
                    case NetworkUtils.CODE_POPULAR:
                        mMovieObjects = (ArrayList<MovieObject>) data;
                        break;
                    case NetworkUtils.CODE_FAVORITES:
                        mMoviesCursor = (Cursor) data;
                        break;
                }
                super.deliverResult(data);
            }
        };

//        return new AsyncTaskLoader<ArrayList<MovieObject>>(this) {
//
//            ArrayList<MovieObject> movieObjects = null;
//
//            @Override
//            protected void onStartLoading() {
//
//                setUpPageTitle();
//                if (movieObjects != null) {
//                    deliverResult(movieObjects);
//                } else {
//                    showProgressBar();
//                    forceLoad();
//                }
//            }
//
//            @Override
//            public ArrayList<MovieObject> loadInBackground() {
//
//                String jsonResponse = "";
//                try {
//                    jsonResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl(loaderId, MainActivity.this));
//                    if (jsonResponse != null) {
//
//                        movieObjects = MovieJsonUtil.getMovieObjects(jsonResponse);
//                        ArrayList<String> moviesPosterArray = MovieJsonUtil.getPicturesURLs(jsonResponse);
//
//                        for (int i = 0; i < moviesPosterArray.size(); i++) {
//                            String moviePosterFile = moviesPosterArray.get(i);
//                            Bitmap currentPosterBitmap = NetworkUtils.loadImage(moviePosterFile);
//                            movieObjects.get(i).setPoster(currentPosterBitmap);
//                            Log.v(LOG_TAG, "Movie Object: " + movieObjects.get(i).getTitle() + " poster: "
//                                    + moviePosterFile);
//                        }
//                    }
//                    return movieObjects;
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//
//            @Override
//            public void deliverResult(ArrayList<MovieObject> data) {
//                movieObjects = data;
//                super.deliverResult(data);
//            }
//        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        showResults();

        switch (loader.getId()) {
            case NetworkUtils.CODE_TOP_RATED:
            case NetworkUtils.CODE_POPULAR:

                ArrayList movies = (ArrayList) data;
                mMovieAdapter.setMovieObjects(movies);
                mRecyclerView.setAdapter(mMovieAdapter);

                break;

            case NetworkUtils.CODE_FAVORITES:
                Cursor cursor = (Cursor) data;
                mCursorAdapter.swapCursor(cursor);
                mRecyclerView.setAdapter(mCursorAdapter);
                break;
        }

        setUpPageTitle();


    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d(TAG, "onStart: preferences were updated");
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            loader = getPreferredList();
            showProgressBar();
            getSupportLoaderManager().restartLoader(loader, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }

//        setUpPageTitle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setUpPageTitle() {

        if (PAGE_TITE != null) {
            mPageTitleTextView.setText(PAGE_TITE);
        }
    }

    public int getPreferredList() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        String preferenceListKey = getString(R.string.pref_list_key);

        String preferredListDefault = getString(R.string.pref_list_options_popular_value);
        String preferredListTopRated = getString(R.string.pref_list_options_top_rated_value);
        String preferredListFavorites = getString(R.string.pref_list_options_favorites_value);

        String preferredList = sharedPreferences.getString(preferenceListKey, preferredListDefault);


        int preferredListID;

        if (preferredList.equals(preferredListDefault)) {
            PAGE_TITE = getString(R.string.pref_list_options_popular_label);
            preferredListID = NetworkUtils.CODE_POPULAR;

        } else if (preferredList.equals(preferredListTopRated)) {
            PAGE_TITE = getString(R.string.pref_list_options_top_rated_label);
            preferredListID = NetworkUtils.CODE_TOP_RATED;

        } else {
            PAGE_TITE = getString(R.string.pref_list_options_favorites_label);
            preferredListID = NetworkUtils.CODE_FAVORITES;
        }

        Log.v(LOG_TAG, "List value: " + preferredList + ", id: " + preferredListID);
        return preferredListID;
    }
}
