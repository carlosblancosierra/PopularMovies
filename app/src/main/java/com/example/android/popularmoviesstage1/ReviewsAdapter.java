package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by carlosblanco on 2/17/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {

    private ArrayList<ArrayList<String>> mReviewsArray;

    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final Context context = parent.getContext();
        int layoutIdForItem = R.layout.reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForItem, parent, shouldAttachToParentImmediately);

        return new ReviewsAdapter.ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapterViewHolder holder, int position) {

        final ArrayList<String> currentReview = mReviewsArray.get(position);
        holder.mAuthor.setText(currentReview.get(0));
        holder.mContent.setText(currentReview.get(1));

    }

    @Override
    public int getItemCount() {
        if (mReviewsArray == null) return 0;
        return mReviewsArray.size();
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder{

        private TextView mAuthor;
        private TextView mContent;

        public ReviewsAdapterViewHolder(View itemView) {
            super(itemView);
            mAuthor = (TextView) itemView.findViewById(R.id.textViewReviewAuthor);
            mContent = (TextView) itemView.findViewById(R.id.textViewReviewContent);
        }
    }

    public void setReviews(ArrayList<ArrayList<String>> reviews) {
        mReviewsArray = reviews;
        notifyDataSetChanged();
    }

}
