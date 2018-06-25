package com.example.android.popularmovies.data;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.net.URL;
import java.util.ArrayList;
import com.example.android.popularmovies.Models.Review;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.example.android.popularmovies.utils.PopularMoviesJsonUtils;

/**
 * Created by Nnamdi on 1/13/2018.
 */

public class FetchMovieReviewsTask extends AsyncTaskLoader<ArrayList<Review>> {

    private long movieId;
    private ArrayList<Review> mData;

    public FetchMovieReviewsTask(Context ctx, long id){
        super(ctx);
        movieId = id;
    }


    @Override
    public ArrayList<Review> loadInBackground() {

        URL reviewURL = NetworkUtils.buildURLReview(String.valueOf(movieId));
        try{
            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(reviewURL);

            ArrayList<Review> reviews = PopularMoviesJsonUtils.getReviewsFromJson(jsonResponse);
            return reviews;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onStartLoading() {
        if(mData != null){
            deliverResult(mData);
        }
        else
        {
            forceLoad();
        }
        super.onStartLoading();
    }

    @Override
    public void deliverResult(ArrayList<Review> data) {
        mData = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
    @Override
    public void onCanceled(ArrayList<Review> data) {
        super.onCanceled(data);
    }
}
