package com.example.android.popularmoviesstage1;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by carlosblanco on 2/17/17.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosAdapterViewHolder> {

    private ArrayList<ArrayList<String>> mVideosArray;

    @Override
    public VideosAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();

        int layoutIdForItem = R.layout.videos_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForItem, parent, shouldAttachToParentImmediately);

        return new VideosAdapter.VideosAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideosAdapterViewHolder holder, int position) {

        final ArrayList<String> currentVideo = mVideosArray.get(position);
        holder.mName.setText(currentVideo.get(0));


        holder.mActionPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = holder.itemView.getContext();
                final String key = currentVideo.get(1);

                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));

                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + key));
                try {
                    context.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        if (mVideosArray == null) return 0;
        return mVideosArray.size();
    }

    public class VideosAdapterViewHolder extends RecyclerView.ViewHolder{

        private TextView mName;
        private TextView mActionPlay;

        public VideosAdapterViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.videos_list_item);
            mActionPlay = (TextView) itemView.findViewById(R.id.videos_list_action_play_trailer);
        }
    }

    public void setVideos(ArrayList<ArrayList<String>> videos) {
        mVideosArray = videos;
        notifyDataSetChanged();
    }
}
