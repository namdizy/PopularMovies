package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.Models.Movies;
import com.example.android.popularmovies.data.FetchMoviesTask;
import com.example.android.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Movies>>,
        MovieAdapter.MovieAdapterOnclickHandler {

    private static final String TAG = "MainActivity";


    private static final String FETCH_POPULAR_MOVIES = "popular_movies";
    private static final String FETCH_TOP_RATED_MOVIES = "top_rated_movies";
    private static final String FETCH_FAVORITE_MOVIES = "favorite_movies";
    public static final String MOVIE_EXTRA = "movie";
    public static final String FILTER_EXTRA = "filter";

    private static final int ID_MOVIE_LOADER = 44;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    ProgressBar mLoadingIndicator;
    TextView mErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = findViewById(R.id.recyclerview_movies);

        mErrorMessage = findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String filterByPopularFlag = pref.getString(getString(R.string.filter_movies_flag), null);
        if(filterByPopularFlag == null){
            filterByPopularFlag = FETCH_POPULAR_MOVIES;
        }

        getLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
    }


    private void loadMovieData(){
        boolean checkConnection = NetworkUtils.checkConnection(this);
        if(checkConnection){
            getLoaderManager().restartLoader(ID_MOVIE_LOADER, null, this);
        }else{
            showErrorMessage();
        }

    }

    @Override
    public void onClick(Movies movie) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String filterByPopularFlag = pref.getString(getString(R.string.filter_movies_flag), null);
        if(filterByPopularFlag == null){
            filterByPopularFlag = FETCH_POPULAR_MOVIES;
        }

        Intent intent = new Intent(this, DetailsPageActivity.class);
        intent.putExtra(MOVIE_EXTRA, movie);
        intent.putExtra(FILTER_EXTRA, filterByPopularFlag);
        startActivity(intent);
    }

    private void showMoviesView(){
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<ArrayList<Movies>> onCreateLoader(int i, Bundle bundle) {
        showMoviesView();
        mLoadingIndicator.setVisibility(View.VISIBLE);
        if(NetworkUtils.checkConnection(this)) {
            return new FetchMoviesTask(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movies>> loader, ArrayList<Movies> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (data != null) {
            showMoviesView();
            mMovieAdapter.setMoviesData(data);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movies>> loader) {
        mMovieAdapter.setMoviesData(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();

        if(item.getItemId() == R.id.action_load_popular_movies) {
            editor.putString(getString(R.string.filter_movies_flag), FETCH_POPULAR_MOVIES);
            editor.apply();
        }
        else if(item.getItemId() == R.id.action_load_top_rated)     {
            editor.putString(getString(R.string.filter_movies_flag), FETCH_TOP_RATED_MOVIES);
            editor.apply();
        }
        else if(item.getItemId() == R.id.action_favorite_movies){
            editor.putString(getString(R.string.filter_movies_flag), FETCH_FAVORITE_MOVIES);
            editor.apply();
        }
        loadMovieData();
        return super.onOptionsItemSelected(item);
    }

}
