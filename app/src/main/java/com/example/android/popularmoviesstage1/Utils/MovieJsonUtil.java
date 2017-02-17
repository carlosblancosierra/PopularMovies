package com.example.android.popularmoviesstage1.Utils;

import android.util.Log;

import com.example.android.popularmoviesstage1.MovieObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by carlosblanco on 1/30/17.
 */

public class MovieJsonUtil {

    private static String LOG_TAG = MovieJsonUtil.class.getSimpleName();

    public static ArrayList<String> getPicturesURLs(String JsonResponse){

        ArrayList<String> moviePostersArray = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(JsonResponse);
            JSONArray results = root.getJSONArray("results");

            for (int i = 0; i < results.length(); i++){
                JSONObject movieObject = results.getJSONObject(i);
                String moviePosterFile = movieObject.getString("poster_path");

                String basePosterUrl = "https://image.tmdb.org/t/p/";
                String imageFormatSize = "/w154";

                String moviePosterPath = basePosterUrl + imageFormatSize + moviePosterFile;
                Log.v(LOG_TAG, "Path: " + moviePosterPath);

                moviePostersArray.add(moviePosterPath);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return moviePostersArray;
    }

    public static ArrayList<MovieObject> getMovieObjects(String JsonResponse){

        ArrayList<MovieObject> movieObjectArrayList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(JsonResponse);
            JSONArray results = root.getJSONArray("results");

            for (int i = 0; i < results.length(); i++){
                JSONObject movieObject = results.getJSONObject(i);

                String title = movieObject.getString("title");
                String releaseDate = movieObject.getString("release_date");
                String voteAverage = movieObject.getString("vote_average");
                String overview = movieObject.getString("overview");
                String movieId = movieObject.getString("id");

                movieObjectArrayList.add(new MovieObject(title, releaseDate, voteAverage, overview, movieId));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieObjectArrayList;
    }

    public static ArrayList<ArrayList<String>> getMovieVideos(String jsonVideos){

        ArrayList<ArrayList<String>> videosArrayList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonVideos);
            JSONArray results = root.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject videoObject = results.getJSONObject(i);

                String name = videoObject.getString("name").trim();
                String key = videoObject.getString("key").trim();

                ArrayList<String> video = new ArrayList<>();

                video.add(name);
                video.add(key);
                videosArrayList.add(video);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videosArrayList;
    }

    public static ArrayList<ArrayList<String>> getMovieReviews(String jsonVideos){

        ArrayList<ArrayList<String>> reviewsArrayList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonVideos);
            JSONArray results = root.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject videoObject = results.getJSONObject(i);

                String name = videoObject.getString("author").trim();
                String key = videoObject.getString("content").trim();

                ArrayList<String> review = new ArrayList<>();

                review.add(name);
                review.add(key);
                reviewsArrayList.add(review);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewsArrayList;
    }
}
