package com.example.android.popularmovies.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.example.android.popularmovies.BuildConfig;

/**
 * Created by Nnamdi on 11/26/2017.
 */

public class NetworkUtils {

    private static final String MOVIEDB_API_KEY = BuildConfig.API_KEY;
    private static final String MOVIEDB_BASE_URL_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated";
    private static final String MOVIEDB_BASE_URL_POPULAR = "http://api.themoviedb.org/3/movie/popular";
    private static final String MOVIEDB_BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    private static final String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";


    private static  final String IMAGE_SIZE = "w185";

    private static final String REVIEWS = "reviews";
    private static final String TRAILERS = "videos";

    private static final String api = "api_key";
    private static final String youtubeQuery = "v";


    public static URL buildURL(){
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL_POPULAR).buildUpon()
                .appendQueryParameter(api, MOVIEDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildURLTopRated(){
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL_TOP_RATED).buildUpon()
                .appendQueryParameter(api, MOVIEDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    public static URL buildURLReview(String movieId){
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(REVIEWS)
                .appendQueryParameter(api, MOVIEDB_API_KEY)
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());

        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }
    public static  URL buildURLTrailer(String movieId){
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(TRAILERS)
                .appendQueryParameter(api, MOVIEDB_API_KEY)
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());

        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildYoutubeURL(String key){
        Uri builtUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(youtubeQuery, key)
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }


    public static URL buildImageURL(String imageString){

        imageString = imageString.substring(1);
        Uri builtUri = Uri.parse(MOVIEDB_BASE_IMAGE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendPath(imageString)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

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

    public static boolean checkConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null &&
                networkInfo.isConnectedOrConnecting();
    }

}
