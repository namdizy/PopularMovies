package com.example.android.popularmovies.data;

import android.app.PendingIntent;
import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.midi.MidiOutputPort;
import android.preference.PreferenceManager;

import com.example.android.popularmovies.Models.Movies;

import java.net.URL;
import java.util.ArrayList;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.utils.PopularMoviesJsonUtils;
import com.example.android.popularmovies.utils.NetworkUtils;

/**
 * Created by Nnamdi on 12/3/2017.
 */

public class FetchMoviesTask extends AsyncTaskLoader<ArrayList<Movies>> {

    private static final String TAG = "FetchMoviesTask";
    private Context context;

    private ArrayList<Movies> mData;

    private static final String FETCH_POPULAR_MOVIES = "popular_movies";
    private static final String FETCH_TOP_RATED_MOVIES = "top_rated_movies";
    private static final String FETCH_FAVORITE_MOVIES = "favorite_movies";

    public static String[] mProjections = {
            MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID,
            MovieContract.FavoriteMoviesEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.FavoriteMoviesEntry.COLUMN_OVERVIEW,
            MovieContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH,
            MovieContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE,
            MovieContract.FavoriteMoviesEntry.COLUMN_VOTE_AVERAGE
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_ORIGINAL_TITLE = 1;
    public static final int INDEX_OVERVIEW = 2;
    public static final int INDEX_POSTER_PATH = 3;
    public static final int INDEX_RELEASE_DATE = 4;
    public static final int INDEX_VOTE_AVERAGE = 5;


    public FetchMoviesTask(Context ctx)
    {
        super(ctx);
        context = ctx;
    }

    @Override
    protected void onStartLoading() {
        if(mData != null){
            deliverResult(mData);
        }
        else{
            forceLoad();
        }
    }

    @Override
    public ArrayList<Movies> loadInBackground() {

        URL moviesURL;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String mQueryString = pref.getString(context.getString(R.string.filter_movies_flag),null);
        if(mQueryString == null){
            mQueryString = FETCH_POPULAR_MOVIES;
        }

        switch (mQueryString){
            case FETCH_POPULAR_MOVIES:
                moviesURL = NetworkUtils.buildURL();
                return getMovies(moviesURL);
            case FETCH_TOP_RATED_MOVIES:
                moviesURL = NetworkUtils.buildURLTopRated();
                return getMovies(moviesURL);
            case FETCH_FAVORITE_MOVIES:
                return getFavoriteMovies();
            default:
                return null;
        }
    }
    public ArrayList<Movies> getFavoriteMovies(){

        ArrayList<Movies> moviesArrayList = new ArrayList<>();

        ContentResolver mContentResolver = context.getContentResolver();
        Cursor mCursor = mContentResolver.query(MovieContract.FavoriteMoviesEntry.CONTENT_URI,
                mProjections,
                null,
                null,
                null);

        if(mCursor.getCount() > 0){
            while(mCursor.moveToNext())
            {
                int weatherId = mCursor.getInt(INDEX_MOVIE_ID);
                String posterPath = mCursor.getString(INDEX_POSTER_PATH);
                String releaseDate = mCursor.getString(INDEX_RELEASE_DATE);
                String overview = mCursor.getString(INDEX_OVERVIEW);
                int voteAverage = mCursor.getInt(INDEX_VOTE_AVERAGE);
                String originalTitle = mCursor.getString(INDEX_ORIGINAL_TITLE);

                Movies movie = new Movies();
                movie.setId(weatherId);
                movie.setPosterPath(posterPath);
                movie.setReleaseDate(releaseDate);
                movie.setOverview(overview);
                movie.setVoteAverage(voteAverage);
                movie.setOriginalTitle(originalTitle);

                moviesArrayList.add(movie);
            }
            mCursor.close();
            return moviesArrayList;
        }
        else{
            return null;
        }
    }

    public ArrayList<Movies> getMovies(URL url){
        try{
            String jsonMoviesResponse = NetworkUtils
                    .getResponseFromHttpUrl(url);
            //Log.v(TAG, jsonMoviesResponse);

            ArrayList<Movies> moviesList = PopularMoviesJsonUtils.getMoviesFromJson(jsonMoviesResponse);

            mData = moviesList;

            return moviesList;
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
            return null;
        }
    }
    @Override
    public void deliverResult(ArrayList<Movies> data) {
        mData = data;
        super.deliverResult(mData);
    }

    @Override protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(ArrayList<Movies> data) {
        super.onCanceled(data);
    }
}

