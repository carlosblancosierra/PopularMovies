package com.example.android.popularmoviesstage1;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.Utils.MovieJsonUtil;
import com.example.android.popularmoviesstage1.Utils.NetworkUtils;
import com.example.android.popularmoviesstage1.data.FavoriteMoviesContract.FavoriteMoviesEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<ArrayList<String>>> {

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverviewTextView;
    private CheckBox favCheckBox;

    private String title;
    private Bitmap posterBitmap;
    private String releaseDate;
    private String voteAverage;
    private String overview;
    private String mMovieId;

    public String TITLE = "title";
    public String POSTER = "poster";
    public String RELEASE_DATE = "date";
    public String VOTE_AVERAGE = "vote_average";
    public String OVERVIEW = "overview";
    public String MOVIE_ID = "id";

    private VideosAdapter mVideosAdapter;
    private ReviewsAdapter mReviewsAdapter;


    private RecyclerView mVideosRecyclerView;
    private RecyclerView mReviewsRecyclerView;

    private static final int VIDEOS_LOADER_ID = 0;
    private static final int REVIEWS_LOADER_ID = 1;

    private String shareVideoUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        title = intent.getStringExtra(TITLE);
        posterBitmap = intent.getParcelableExtra(POSTER);
        releaseDate = intent.getStringExtra(RELEASE_DATE);
        voteAverage = intent.getStringExtra(VOTE_AVERAGE);
        overview = intent.getStringExtra(OVERVIEW);
        mMovieId = intent.getStringExtra(MOVIE_ID);

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
        mVideosAdapter = new VideosAdapter();
        LinearLayoutManager videosLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mVideosRecyclerView = (RecyclerView) findViewById(R.id.detail_recyclerview_trailers);
        mVideosRecyclerView.setLayoutManager(videosLinearLayoutManager);
        mVideosRecyclerView.setAdapter(mVideosAdapter);
        getSupportLoaderManager().initLoader(VIDEOS_LOADER_ID, null, DetailActivity.this);

        mReviewsAdapter = new ReviewsAdapter();
        LinearLayoutManager reviewsLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.detail_recyclerview_reviews);
        mReviewsRecyclerView.setLayoutManager(reviewsLinearLayoutManager);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
        getSupportLoaderManager().initLoader(REVIEWS_LOADER_ID, null, DetailActivity.this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case (android.R.id.home):
                NavUtils.navigateUpFromSameTask(this);
                break;
            case (R.id.action_share):

                String mimeType = "text/plain";

                // COMPLETED (3) Create a title for the chooser window that will pop up
        /* This is just the title of the window that will pop up when we call startActivity */
                String title = "Learning How to Share";

                // COMPLETED (4) Use ShareCompat.IntentBuilder to build the Intent and start the chooser
        /* ShareCompat.IntentBuilder provides a fluent API for creating Intents */
                ShareCompat.IntentBuilder
                /* The from method specifies the Context from which this share is coming from */
                        .from(this)
                        .setType(mimeType)
                        .setChooserTitle(title)
                        .setText(shareVideoUrl)
                        .startChooser();
                break;

        }
        if (id == android.R.id.home) {
        }

        return super.onOptionsItemSelected(item);
    }

    public void addCurrentMovieToFavorites() {

        ContentValues values = new ContentValues();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        posterBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        values.put(FavoriteMoviesEntry.COLUMN_DATE, releaseDate);
        values.put(FavoriteMoviesEntry.COLUMN_POSTER, byteArray);
        values.put(FavoriteMoviesEntry.COLUMN_RATING, voteAverage);
        values.put(FavoriteMoviesEntry.COLUMN_SYNOPSIS, overview);
        values.put(FavoriteMoviesEntry.COLUMN_TITLE, title);

        getContentResolver().insert(FavoriteMoviesEntry.CONTENT_URI, values);

        Toast.makeText(DetailActivity.this, "Added Movie to Favorites", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        addCurrentMovieToFavorites();
    }


    @Override
    public Loader<ArrayList<ArrayList<String>>> onCreateLoader(final int id, Bundle args) {

        return new AsyncTaskLoader<ArrayList<ArrayList<String>>>(this) {

            ArrayList<ArrayList<String>> data = null;

            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public ArrayList<ArrayList<String>> loadInBackground() {

                String jsonResponse = "";

                switch (id) {
                    case VIDEOS_LOADER_ID:

                        try {
                            jsonResponse = NetworkUtils.getResponseFromHttpUrl
                                    (NetworkUtils.buildVideoUrl(mMovieId, DetailActivity.this));
                            if (jsonResponse != null) {

                                data = MovieJsonUtil.getMovieVideos(jsonResponse);
                            }
                            shareVideoUrl = "http://www.youtube.com/watch?v=" + data.get(0).get(1);
                            return data;

                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }

                    case REVIEWS_LOADER_ID:

                        try {
                            jsonResponse = NetworkUtils.getResponseFromHttpUrl
                                    (NetworkUtils.buildReviewsUrl(mMovieId, DetailActivity.this));
                            if (jsonResponse != null) {

                                data = MovieJsonUtil.getMovieReviews(jsonResponse);
                            }
                            return data;

                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }

                    default:
                        return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ArrayList<String>>> loader,
                               ArrayList<ArrayList<String>> data) {

        switch (loader.getId()) {
            case VIDEOS_LOADER_ID:
                mVideosAdapter.setVideos(data);
                break;
            case REVIEWS_LOADER_ID:
                mReviewsAdapter.setReviews(data);
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ArrayList<String>>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_activity, menu);
        return true;
    }


}
