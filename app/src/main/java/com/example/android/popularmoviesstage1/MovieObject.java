package com.example.android.popularmoviesstage1;

import android.graphics.Bitmap;

/**
 * Created by carlosblanco on 1/30/17.
 */

public class MovieObject {
    private String mTitle;
    private Bitmap mPosterBitmap;

    public MovieObject(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public Bitmap getPoster() {
        return mPosterBitmap;
    }

    public void setPoster(Bitmap posterBitmap) {
        mPosterBitmap = posterBitmap;
    }
}

