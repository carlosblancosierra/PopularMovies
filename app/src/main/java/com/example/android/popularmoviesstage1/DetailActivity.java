package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverviewTextView;

    public String TITLE = "title";
    public String POSTER = "poster";
    public String RELEASE_DATE = "date";
    public String VOTE_AVERAGE = "vote_average";
    public String OVERVIEW = "overview";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        String title = intent.getStringExtra(TITLE);
        Bitmap posterBitmap = intent.getParcelableExtra(POSTER);
        String releaseDate = intent.getStringExtra(RELEASE_DATE);
        String voteAverage = intent.getStringExtra(VOTE_AVERAGE);
        String overview = intent.getStringExtra(OVERVIEW);

        mTitleTextView = (TextView) findViewById(R.id.detail_view_movie_title);
        mPosterImageView = (ImageView) findViewById(R.id.detail_view_movie_poster);
        mReleaseDateTextView = (TextView) findViewById(R.id.detail_view_movie_release_date);
        mVoteAverageTextView = (TextView) findViewById(R.id.detail_view_movie_vote_average);
        mOverviewTextView = (TextView) findViewById(R.id.detail_view_movie_overview);

        mTitleTextView.setText(title);
        mPosterImageView.setImageBitmap(posterBitmap);
        mReleaseDateTextView.setText(releaseDate);
        mVoteAverageTextView.setText(voteAverage);
        mOverviewTextView.setText(overview);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
