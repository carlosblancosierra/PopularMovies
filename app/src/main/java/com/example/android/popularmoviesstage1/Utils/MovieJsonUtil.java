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

                movieObjectArrayList.add(new MovieObject(title, releaseDate, voteAverage, overview));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieObjectArrayList;
    }
}
