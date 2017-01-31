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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private ArrayList<Bitmap> mMovieDataBitmapArray;
    private ArrayList<MovieObject> mMovieObjects;


    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();

        int layoutIdForItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForItem, parent, shouldAttachToParentImmediately);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                context.startActivity(intent);
            }
        });
        return new MovieAdapterViewHolder(view);    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieAdapterViewHolder holder, int position) {
        Bitmap currentMovieBitmap = mMovieDataBitmapArray.get(position);
        holder.mMoviePosterImageView.setImageBitmap(currentMovieBitmap);

    }

    @Override
    public int getItemCount() {
        if (mMovieDataBitmapArray == null ) return 0;
        return mMovieDataBitmapArray.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView mMoviePosterImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.list_item_movie_poster);
        }

    }

    public void setMovieData(ArrayList<Bitmap>movieData){
        mMovieDataBitmapArray = movieData;
        notifyDataSetChanged();
    }

    public void setMovieObjects (ArrayList<MovieObject> movieObjects){
        mMovieObjects = movieObjects;
    }
}
