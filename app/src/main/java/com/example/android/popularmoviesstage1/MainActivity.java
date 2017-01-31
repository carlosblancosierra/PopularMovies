package com.example.android.popularmoviesstage1;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmoviesstage1.Utils.MovieJsonUtil;
import com.example.android.popularmoviesstage1.Utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<MovieObject>> {

    private int LOADER_ID = 1;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private MovieAdapter mMovieAdapter;
    private String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

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

            @Override
            protected void onStartLoading() {
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
                    jsonResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl());
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
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieObject>> loader) {

    }
}
