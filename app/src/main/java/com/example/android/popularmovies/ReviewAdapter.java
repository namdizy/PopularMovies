package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.Models.Review;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Nnamdi on 1/15/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private ArrayList<Review> mReviewData;

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        Review review = mReviewData.get(position);

        holder.mContentTextView.setText(review.getContent());
        holder.mAuthorTextView.setText(review.getAuthor());
    }

    @Override
    public int getItemCount() {
        if(mReviewData == null){ return 0;}
        return mReviewData.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView mAuthorTextView;
        private TextView mContentTextView;

        public ReviewAdapterViewHolder(View view){
            super(view);
            mAuthorTextView = view.findViewById(R.id.tv_review_author);
            mContentTextView = view.findViewById(R.id.tv_review_content);

        }
    }

    public void setReviewData(ArrayList<Review> data){
        mReviewData = data;
        notifyDataSetChanged();
    }
}
