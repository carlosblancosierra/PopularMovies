package com.example.android.popularmoviesstage1.Utils;

/**
 * Created by carlosblanco on 1/28/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmoviesstage1.R;

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


    /**
     * Builds the URL used to query Github.
     *
     *
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String preferedOption, Context context) {

        String optionTopRated = context.getString(R.string.pref_list_options_top_rated_value);
        String optionMostPopular = context.getString(R.string.pref_list_options_popular_value);

        String baseUrl = "";
        if (preferedOption.equals(optionMostPopular)){
            baseUrl = TMBD_POPULAR_BASE_URL;
        } else if (preferedOption.equals(optionTopRated)){
            baseUrl = TMBD_TOP_RATED_BASE_URL;
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
}