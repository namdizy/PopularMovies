package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.Models.Movies;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nnamdi on 11/26/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList<Movies> mMoviesData;

    private final MovieAdapterOnclickHandler mClickHandler;


    public interface MovieAdapterOnclickHandler{
        void onClick(Movies movie);
    }


    public MovieAdapter(MovieAdapterOnclickHandler clickHandler){
        mClickHandler = clickHandler;
    }
    @Override
    public int getItemCount() {
        if(mMoviesData == null) return 0;
        return mMoviesData.size();
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Movies movie = mMoviesData.get(position);

        URL url = NetworkUtils.buildImageURL(movie.getPosterPath());

        Context context = movieAdapterViewHolder.mMoviePosterImageView.getContext();

        Picasso.with(context).load(url.toString()).into(movieAdapterViewHolder.mMoviePosterImageView);
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView mMoviePosterImageView;

        public MovieAdapterViewHolder(View view){
            super(view);

            mMoviePosterImageView = view.findViewById(R.id.imv_movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movies movieSelected = mMoviesData.get(adapterPosition);
            mClickHandler.onClick(movieSelected);
        }
    }

    public void setMoviesData(ArrayList<Movies> moviesData){
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }
}
