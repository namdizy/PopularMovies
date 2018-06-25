package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.Models.Trailer;

import java.util.ArrayList;

/**
 * Created by Nnamdi on 1/15/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private ArrayList<Trailer> mTrailerData;

    private TrailerAdapterClickHandler mClickHandler;


    public interface TrailerAdapterClickHandler {
        void onClick(Trailer trailer);
    }

    public TrailerAdapter(TrailerAdapterClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        Trailer trailer = mTrailerData.get(position);

        holder.mTrailerTextName.setText(trailer.getName());

    }

    @Override
    public int getItemCount() {
        if(mTrailerData == null) return 0;
        return mTrailerData.size();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView mTrailerTextName;

        public TrailerAdapterViewHolder(View view){
            super(view);
            mTrailerTextName = view.findViewById(R.id.tv_trailer_title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Trailer trailerSelected = mTrailerData.get(adapterPosition);
            mClickHandler.onClick(trailerSelected);
        }
    }

    public void setTrailerData(ArrayList<Trailer> trailerData){
        mTrailerData = trailerData;
        notifyDataSetChanged();
    }
}
