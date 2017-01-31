package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<MovieObject>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private String TAG = MainActivity.class.getSimpleName();
    private int LOADER_ID = 1;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private MovieAdapter mMovieAdapter;
    private String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private String PAGE_TITE;
    private TextView mPageTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mPageTitleTextView = (TextView) findViewById(R.id.app_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mMovieAdapter = new MovieAdapter();

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(manager);
        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

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
    public Loader<ArrayList<MovieObject>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<MovieObject>>(this) {

            ArrayList<MovieObject> movieObjects = null;
            String preferredList;

            @Override
            protected void onStartLoading() {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String preferenceListKey = getString(R.string.pref_list_key);
                String preferredListDefault = getString(R.string.pref_list_options_popular_value);
                preferredList = sharedPreferences.getString(preferenceListKey, preferredListDefault);

                if (preferredList.equals(preferredListDefault)){
                    PAGE_TITE = getString(R.string.pref_list_options_popular_label);
                } else {
                    PAGE_TITE = getString(R.string.pref_list_options_top_rated_label);
                }
                setUpPageTitle();
                if (movieObjects != null) {
                    deliverResult(movieObjects);
                } else {
                    showProgressBar();
                    forceLoad();
                }
            }

            @Override
            public ArrayList<MovieObject> loadInBackground() {



                String jsonResponse = "";


                try {
                    jsonResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl(preferredList, MainActivity.this));
                    if (jsonResponse != null) {

                        movieObjects = MovieJsonUtil.getMovieObjects(jsonResponse);
                        ArrayList<String> moviesPosterArray = MovieJsonUtil.getPicturesURLs(jsonResponse);

                        for (int i = 0; i < moviesPosterArray.size(); i++) {
                            String moviePosterFile = moviesPosterArray.get(i);
                            Bitmap currentPosterBitmap = NetworkUtils.loadImage(moviePosterFile);
                            movieObjects.get(i).setPoster(currentPosterBitmap);
                            Log.v(LOG_TAG, "Movie Object: " + movieObjects.get(i).getTitle() + " poster: "
                                    + moviePosterFile);
                        }
                    }
                    return movieObjects;

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(ArrayList<MovieObject> data) {
                movieObjects = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieObject>> loader, ArrayList<MovieObject> data) {
        showResults();
        mMovieAdapter.setMovieObjects(data);
        setUpPageTitle();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieObject>> loader) {

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
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }

        setUpPageTitle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setUpPageTitle(){
        if (PAGE_TITE != null){
            mPageTitleTextView.setText(PAGE_TITE);
        }
    }
}
