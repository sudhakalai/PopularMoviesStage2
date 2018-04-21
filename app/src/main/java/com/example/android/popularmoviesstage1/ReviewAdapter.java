package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    ArrayList<MovieReview> mReviews = new ArrayList<>();

    ReviewAdapter(ArrayList<MovieReview> reviews){

        mReviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_review_item, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

        holder.review = mReviews.get(position);

        String author = holder.review.getAuthorName();
        String review = holder.review.getReview();

        holder.bind(author, review);

    }

    @Override
    public int getItemCount() {
        if(mReviews == null){
            return 0;
        }else{
            return mReviews.size();
        }
    }

    public void setReviewList(ArrayList<MovieReview> list){
        this.mReviews = list;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView authorTv;
        private TextView reviewTv;
        private Context context;
        MovieReview review;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            authorTv = itemView.findViewById(R.id.author);
            reviewTv = itemView.findViewById(R.id.review);
        }

        public void bind(String author, String review){
            authorTv.setText(author);
            reviewTv.setText(review);
        }
    }
}
