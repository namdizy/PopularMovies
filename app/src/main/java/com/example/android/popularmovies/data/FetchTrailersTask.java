package com.example.android.popularmovies.data;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.popularmovies.Models.Trailer;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.example.android.popularmovies.utils.PopularMoviesJsonUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nnamdi on 1/15/2018.
 */

public class FetchTrailersTask extends AsyncTaskLoader<ArrayList<Trailer>> {

    private long movieId;
    private Context context;
    private ArrayList<Trailer> mData;


    public FetchTrailersTask(Context ctx, long id){
        super(ctx);
        context = ctx;
        movieId = id;
    }
    @Override
    public ArrayList<Trailer> loadInBackground() {
        ArrayList<Trailer> trailerArrayList;

        URL url = NetworkUtils.buildURLTrailer(String.valueOf(movieId));
        try{
            String trailersJsonString = NetworkUtils.getResponseFromHttpUrl(url);
            trailerArrayList = PopularMoviesJsonUtils.getTrailersFromJson(trailersJsonString);
            return trailerArrayList;
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
    public void deliverResult(ArrayList<Trailer> data) {
        mData = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
    @Override
    public void onCanceled(ArrayList<Trailer> data) {
        super.onCanceled(data);
    }
}
