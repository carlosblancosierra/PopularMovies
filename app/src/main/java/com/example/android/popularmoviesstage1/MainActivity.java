package com.example.android.popularmoviesstage1;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmoviesstage1.Utils.MovieJsonUtil;
import com.example.android.popularmoviesstage1.Utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Bitmap>> {

    private int LOADER_ID = 1;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(manager);

         /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter();

        mRecyclerView.setAdapter(mMovieAdapter);


        getSupportLoaderManager().initLoader(LOADER_ID, null, this);



    }

    public void showProgressBar () {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void showResults () {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<ArrayList<Bitmap>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Bitmap>>(this) {
            ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();

            @Override
            protected void onStartLoading() {
                showProgressBar();
                forceLoad();
            }

            @Override
            public ArrayList<Bitmap> loadInBackground() {

                String jsonResponse = "";


                try {
                    jsonResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (jsonResponse != null) {
                    ArrayList<String> moviesPosterArray = MovieJsonUtil.getPicturesURLs(jsonResponse);

                    for (int i = 0; i < moviesPosterArray.size(); i++) {

                        String moviePosterFile = moviesPosterArray.get(i);
                        Bitmap currentPosterBitmap = NetworkUtils.loadImage(moviePosterFile);
                        bitmapArrayList.add(currentPosterBitmap);
                    }
                }

                return bitmapArrayList;
            }

            @Override
            public void deliverResult(ArrayList<Bitmap> data) {
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Bitmap>> loader, ArrayList<Bitmap> data) {
        showResults();
        mMovieAdapter.setMovieData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Bitmap>> loader) {

    }

}
