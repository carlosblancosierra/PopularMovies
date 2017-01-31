package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        mTitleTextView = (TextView) findViewById(R.id.detail_view_movie_title);
        mTitleTextView.setText(title);

        Bitmap posterBitmap = intent.getParcelableExtra("poster");
        mPosterImageView = (ImageView) findViewById(R.id.detail_view_movie_poster);
        mPosterImageView.setImageBitmap(posterBitmap);


    }
}
