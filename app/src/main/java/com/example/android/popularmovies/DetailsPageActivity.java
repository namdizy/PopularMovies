package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.Models.Movies;

import com.example.android.popularmovies.Models.Review;
import com.example.android.popularmovies.Models.Trailer;
import com.example.android.popularmovies.data.FetchMovieReviewsTask;
import com.example.android.popularmovies.data.FetchTrailersTask;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class DetailsPageActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterClickHandler
{

    private ImageView dMoviePoster;
    private TextView dReleaseDate;
    private TextView dSynopsis;
    private TextView dVoteAveage;
    private Button dFavorite;
    private TextView dTitleBar;

    private Movies movie;

    private static final String TAG = "DetailsActivity";
    private static final String FAVORITE_MOVIES = "favorite_movies";
    public static final String MOVIE_EXTRA = "movie";
    public static final String FILTER_EXTRA = "filter";

    private final static int ID_REVIEWS_LOADER = 45;
    private final static int ID_TRAILERS_LOADER = 50;
    private long movieId;

    private RecyclerView mTrailersRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mReviewsRecyclerView;
    private ReviewAdapter mReviewAdapter;

    public static String[] mProjections = {
            MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID,
            MovieContract.FavoriteMoviesEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.FavoriteMoviesEntry.COLUMN_OVERVIEW,
            MovieContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH,
            MovieContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE,
            MovieContract.FavoriteMoviesEntry.COLUMN_VOTE_AVERAGE
    };

    private LoaderManager.LoaderCallbacks<ArrayList<Review>> fetchReviewsLoader =
            new LoaderManager.LoaderCallbacks<ArrayList<Review>>() {
                @Override
                public Loader<ArrayList<Review>> onCreateLoader(int i, Bundle bundle) {
                    if(NetworkUtils.checkConnection(getBaseContext())){
                        return new FetchMovieReviewsTask(DetailsPageActivity.this, movieId);
                    }
                    return null;
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<Review>> loader,
                                           ArrayList<Review> data) {
                    if(data != null){
                        mReviewAdapter.setReviewData(data);
                    }
                }

                @Override
                public void onLoaderReset(Loader<ArrayList<Review>> loader) {
                    mReviewAdapter.setReviewData(null);
                }
            };

    private LoaderManager.LoaderCallbacks<ArrayList<Trailer>> fetchTrailerLoader =
            new LoaderManager.LoaderCallbacks<ArrayList<Trailer>>() {
        @Override
        public Loader<ArrayList<Trailer>> onCreateLoader(int i, Bundle bundle) {
            if(NetworkUtils.checkConnection(getBaseContext())){
                return new FetchTrailersTask(DetailsPageActivity.this, movieId);
            }
            return null;

        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Trailer>> loader,
                                   ArrayList<Trailer> data) {
            if (data != null) {
                mTrailerAdapter.setTrailerData(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {
            mTrailerAdapter.setTrailerData(null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);

        dMoviePoster = findViewById(R.id.imv_details_page_poster);
        dReleaseDate =  findViewById(R.id.tv_details_page_release_date);
        dSynopsis = findViewById(R.id.tv_details_page_synopsis);
        dVoteAveage =  findViewById(R.id.tv_details_page_vote_average);

        dTitleBar = findViewById(R.id.tv_details_title_header);

        mTrailersRecyclerView = findViewById(R.id.recyclerView_trailer);
        mReviewsRecyclerView = findViewById(R.id.recyclerView_reviews);


        dFavorite = findViewById(R.id.btn_favorite);

        movie = getIntent().getExtras().getParcelable(MOVIE_EXTRA);
        String filter = getIntent().getExtras().getString(FILTER_EXTRA);

        if(filter != FAVORITE_MOVIES){
            dFavorite = findViewById(R.id.btn_favorite);
        }

        RecyclerView.LayoutManager trailerLayoutManager = new GridLayoutManager(this, 1);
        mTrailersRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailerAdapter);


        RecyclerView.LayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
        mReviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mReviewsRecyclerView.setAdapter(mReviewAdapter);

        URL url = NetworkUtils.buildImageURL(movie.getPosterPath());

        Picasso.with(this).load(url.toString()).into(dMoviePoster);
        dTitleBar.setText(movie.getOriginalTitle());
        dReleaseDate.setText(movie.getReleaseDate());
        dSynopsis.setText(movie.getOverview());
        dVoteAveage.setText(String.valueOf(movie.getVoteAverage()) + "/10");

        movieId = movie.getId();

        getLoaderManager().initLoader(ID_REVIEWS_LOADER, null, fetchReviewsLoader);
        getLoaderManager().initLoader(ID_TRAILERS_LOADER, null, fetchTrailerLoader);

    }



    public void handleFavoriteBtnClick(View arg){

        ContentResolver contentResolver = getContentResolver();
        boolean isInDB = checkIfInDB(contentResolver, movie.getId());
        if(!isInDB){
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID, movie.getId());
            contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
            contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
            contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            contentResolver.insert(MovieContract.FavoriteMoviesEntry.CONTENT_URI, contentValues);
        }


    }

    public boolean checkIfInDB(ContentResolver resolver, long movieId){

        Uri uri = MovieContract.FavoriteMoviesEntry.buildMovieUri(movieId);
        Cursor cursor =resolver.query(uri, mProjections, null, null, null );
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    @Override
    public void onClick(Trailer trailer) {
        Uri uri = Uri.parse(NetworkUtils.buildYoutubeURL(trailer.getKey()).toString());

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (intent.resolveActivity(this.getPackageManager()) != null) {
            this.startActivity(intent);
        }

    }
}
