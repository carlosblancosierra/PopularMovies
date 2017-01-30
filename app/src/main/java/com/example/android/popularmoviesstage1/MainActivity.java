package com.example.android.popularmoviesstage1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.Utils.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private TextView textView;
    private int LOADER_ID = 1;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ImageView testImageView;
    private Bitmap[] bmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
//
//        GridLayoutManager layoutManager
//                = new GridLayoutManager(this, GridLayoutManager.DEFAULT_SPAN_COUNT);
//        // COMPLETED (41) Set the layoutManager on mRecyclerView
//        mRecyclerView.setLayoutManager(layoutManager);
//
//        mRecyclerView.setHasFixedSize(true);
//
//        mMovieAdapter = new MovieAdapter();
//
//        mRecyclerView.setAdapter(mMovieAdapter);

        testImageView = (ImageView) findViewById(R.id.test_image_view);

        bmp = new Bitmap[1];


    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
            forceLoad();
            }

            @Override
            public String loadInBackground() {
                URL imageUrl = null;
                try {
                    imageUrl = new URL("https://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg\n");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bmp[0] = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                URL url = NetworkUtils.buildUrl();
                try {
                    String data = NetworkUtils.getResponseFromHttpUrl(url);
                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        testImageView.setImageBitmap(bmp[0]);

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
