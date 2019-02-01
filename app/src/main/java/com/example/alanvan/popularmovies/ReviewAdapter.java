package com.example.alanvan.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alanvan.popularmovies.model.Review;

import java.util.List;

import static android.view.View.VISIBLE;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> mReviewList;

    ReviewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        TextView reviewText = viewHolder.reviewText;
        TextView reviewAuthor = viewHolder.reviewAuthor;
        Review review = mReviewList.get(position);

        reviewText.setText(review.getReviewText());
        reviewAuthor.setText(TextUtils.concat("-", review.getReviewAuthor(), "-"));
    }

    @Override
    public int getItemCount() {
        if (mReviewList == null) return 0;
        else {
            return mReviewList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView reviewSectionTitle;
        final TextView reviewText;
        final TextView reviewAuthor;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewSectionTitle = itemView.findViewById(R.id.review_section_title);
            reviewText = itemView.findViewById(R.id.review_text_tv);
            reviewAuthor = itemView.findViewById(R.id.review_author_tv);
        }
    }

    public void setReviewList(List<Review> reviewList) {
        mReviewList = reviewList;
        notifyDataSetChanged();
    }
}
