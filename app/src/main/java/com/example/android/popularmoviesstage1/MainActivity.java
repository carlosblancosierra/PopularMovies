package com.example.android.popularmoviesstage1;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private TextView textView;
    private int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.tv_test);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
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
        textView.setText(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
