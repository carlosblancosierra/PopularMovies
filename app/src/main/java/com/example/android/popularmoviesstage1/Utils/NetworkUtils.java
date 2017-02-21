package com.example.android.popularmoviesstage1.Utils;

/**
 * Created by carlosblanco on 1/28/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    final static String LOG_TAG = NetworkUtils.class.getSimpleName();

    final static String TMBD_POPULAR_BASE_URL =
            "https://api.themoviedb.org/3/movie/popular?";

    final static String TMBD_TOP_RATED_BASE_URL =
            "https://api.themoviedb.org/3/movie/top_rated?";

    final static String PARAM_LANGUAGE = "language";
    final static String PARAM_LANGUAGE_ENGLISH = "en-US";

    final static String PARAM_PAGE = "page";
    final static String PARAM_PAGE_QUANTITY = "1";

    final static String PARAM_API_KEY = "api_key";
    final static String API_KEY = "f4d478c237eb3ff26f0f228d47c9327f";

    final static String YOUTUBE_BASE_URI = "https://www.youtube.com/watch";
    final static String YOUTUBE_PARAMETER = "v";

    public final static int CODE_POPULAR = 0;
    public final static int CODE_TOP_RATED = 1;
    public final static int CODE_FAVORITES = 2;


    public static URL buildUrl(int preferredOptionId, Context context) {

        String baseUrl = "";

        switch (preferredOptionId) {
            case (CODE_POPULAR):
                baseUrl = TMBD_POPULAR_BASE_URL;
                break;
            case (CODE_TOP_RATED):
                baseUrl = TMBD_TOP_RATED_BASE_URL;
                break;
            default:
                return null;
        }

        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, PARAM_LANGUAGE_ENGLISH)
                .appendQueryParameter(PARAM_PAGE, PARAM_PAGE_QUANTITY)
                .build();

        Log.v(LOG_TAG, "URL: " + builtUri.toString() );


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Bitmap loadImage(String posterUrl) {
        URL imageUrl = null;
        Bitmap bmp = null;
        try {
            imageUrl = new URL(posterUrl);
            bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            return bmp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //https://api.themoviedb.org/3/movie/278/videos?api_key=f4d478c237eb3ff26f0f228d47c9327f&language=en-US&page=1
    public static URL buildVideoUrl(String key, Context context) {

        String baseUrl = "https://api.themoviedb.org/3/movie/" + key + "/videos?";

        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, PARAM_LANGUAGE_ENGLISH)
                .appendQueryParameter(PARAM_PAGE, PARAM_PAGE_QUANTITY)
                .build();

        Log.v(LOG_TAG, "Video URL: " + builtUri.toString() );


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildReviewsUrl(String key, Context context) {

        String baseUrl = "https://api.themoviedb.org/3/movie/" + key + "/reviews?";

        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, PARAM_LANGUAGE_ENGLISH)
                .appendQueryParameter(PARAM_PAGE, PARAM_PAGE_QUANTITY)
                .build();

        Log.v(LOG_TAG, "Reviews URL: " + builtUri.toString() );


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}

