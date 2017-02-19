package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstage1.data.FavoriteMoviesContract;

/**
 * Created by carlosblanco on 2/18/17.
 */

public class MoviesCursorAdapter extends RecyclerView.Adapter<MoviesCursorAdapter.MoviesViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;
    private String LOG_TAG = "CursorAdapter";


    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public MoviesCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new MoviesViewHolder that holds the view for each task
     */
    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_list_item, parent, false);

        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {

        if (mCursor.getCount() != 0) {

            mCursor.moveToPosition(position);
            int posterColumnIndex = mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER);
            Log.v(LOG_TAG, "posterColumnIndex: " + posterColumnIndex);

            Log.v(LOG_TAG, "mCursor count: " + mCursor.getCount());
            Log.v(LOG_TAG, "mCursor columnCount: " + mCursor.getColumnCount());


            byte[] posterByte = mCursor.getBlob(posterColumnIndex);

            Bitmap posterBitmap = BitmapFactory.decodeByteArray(posterByte, 0, posterByte.length);

            holder.posterImageView.setImageBitmap(posterBitmap);
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    // Inner class for creating ViewHolders
    class MoviesViewHolder extends RecyclerView.ViewHolder {

        // Class variable
        ImageView posterImageView;

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public MoviesViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.list_item_movie_poster);

        }
    }
}
