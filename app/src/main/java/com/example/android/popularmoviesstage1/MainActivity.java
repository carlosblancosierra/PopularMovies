package com.example.android.popularmoviesstage1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.android.popularmoviesstage1.Utils.MovieJsonUtil;
import com.example.android.popularmoviesstage1.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Bitmap>> {

    private int LOADER_ID = 1;
    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_view);
        imageView2 = (ImageView) findViewById(R.id.image_view2);
        imageView3 = (ImageView) findViewById(R.id.image_view3);
        imageView4 = (ImageView) findViewById(R.id.image_view4);
        imageView5 = (ImageView) findViewById(R.id.image_view5);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    public void showProgressBar () {
        imageView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void showResults () {
        mProgressBar.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }

    private Bitmap loadImage(String posterUrl) {
        URL imageUrl = null;
        Bitmap bmp = null;
        try {
            imageUrl = new URL(posterUrl);
            bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            return bmp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
                        Bitmap currentPosterBitmap = loadImage(moviePosterFile);
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
        imageView.setImageBitmap(data.get(0));
        imageView2.setImageBitmap(data.get(1));
        imageView3.setImageBitmap(data.get(2));
        imageView4.setImageBitmap(data.get(3));
        imageView5.setImageBitmap(data.get(4));

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Bitmap>> loader) {

    }
}
