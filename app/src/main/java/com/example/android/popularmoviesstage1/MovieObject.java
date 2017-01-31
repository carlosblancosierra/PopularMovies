package com.example.android.popularmoviesstage1;

import android.graphics.Bitmap;

/**
 * Created by carlosblanco on 1/30/17.
 */

public class MovieObject {
    private String mTitle;
    private Bitmap mPosterBitmap;
    private String mReleaseDate;
    private String mVoteAverage;
    private String mOverview;


    public MovieObject(String title, String releaseDate, String voteAverage, String overview) {
        mTitle = title;
        mReleaseDate = "(" + releaseDate.substring(0,4) + ")";
        mVoteAverage = voteAverage;
        mOverview = overview;
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

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public String getOverview() {
        return mOverview;
    }
}

