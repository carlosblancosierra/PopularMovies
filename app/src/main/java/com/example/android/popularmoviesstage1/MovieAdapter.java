package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by carlosblanco on 1/30/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList<MovieObject> mMovieObjects;


    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();

        int layoutIdForItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForItem, parent, shouldAttachToParentImmediately);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieAdapterViewHolder holder, final int position) {

        final MovieObject currentMovie = mMovieObjects.get(position);
        final Context context = holder.itemView.getContext();

        final Bitmap currentMovieBitmap = currentMovie.getPoster();

        holder.mMoviePosterImageView.setImageBitmap(currentMovieBitmap);

        holder.itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.TITLE, currentMovie.getTitle());
                intent.putExtra(DetailActivity.POSTER, currentMovieBitmap);
                intent.putExtra(DetailActivity.RELEASE_DATE, currentMovie.getReleaseDate());
                intent.putExtra(DetailActivity.VOTE_AVERAGE, currentMovie.getVoteAverage());
                intent.putExtra(DetailActivity.OVERVIEW, currentMovie.getOverview());
                intent.putExtra(DetailActivity.MOVIE_ID, currentMovie.getMovieId());
                intent.putExtra(DetailActivity.INTENT_TYPE, DetailActivity.INTENT_TYPE_TMDB);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mMovieObjects == null) return 0;
        return mMovieObjects.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        private ImageView mMoviePosterImageView;

        private MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.list_item_movie_poster);
        }

    }


    public void setMovieObjects(ArrayList<MovieObject> movieObjects) {
        mMovieObjects = movieObjects;
        notifyDataSetChanged();
    }
}
