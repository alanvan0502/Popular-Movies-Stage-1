package com.example.alanvan.popularmovies;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alanvan.popularmovies.model.MovieDetail;
import com.example.alanvan.popularmovies.model.Review;
import com.example.alanvan.popularmovies.utilities.JsonUtils;
import com.example.alanvan.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

public class FetchMovieDetailDataTask extends AsyncTask<Void, Void, MovieDetail> {

    private WeakReference<TextView> mDurationTv;
    private WeakReference<ProgressBar> mProgressBar;
    private WeakReference<LinearLayout> mDetailLayout;
    private WeakReference<TrailerAdapter> mTrailerAdapter;
    private WeakReference<ReviewAdapter> mReviewAdapter;

    private int movieId;

    FetchMovieDetailDataTask
            (int movieId, TextView durationTv, LinearLayout detailLayout,
             ProgressBar progressBar, TrailerAdapter trailerAdapter, ReviewAdapter reviewAdapter) {
        mDurationTv = new WeakReference<>(durationTv);
        mProgressBar = new WeakReference<>(progressBar);
        mDetailLayout = new WeakReference<>(detailLayout);
        mTrailerAdapter = new WeakReference<>(trailerAdapter);
        mReviewAdapter = new WeakReference<>(reviewAdapter);
        this.movieId = movieId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressBar.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected MovieDetail doInBackground(Void... voids) {
        MovieDetail movieDetail = null;
        try {
            URL durationRequestUrl = NetworkUtils.buildDurationRequestlUrl(movieId);
            String durationResponse = NetworkUtils.getResponseFromHttpUrl(durationRequestUrl);
            int duration = JsonUtils.getDuration(durationResponse);

            URL videoKeysRequestUrl = NetworkUtils.buildVideoKeysRequestUrl(movieId);
            String videoLinkResponse = NetworkUtils.getResponseFromHttpUrl(videoKeysRequestUrl);
            List<String> videoKeys = JsonUtils.getVideoKeys(videoLinkResponse);

            URL reviewRequestUrl = NetworkUtils.buildReviewRequestUrl(movieId);
            String reviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);
            List<Review> reviews = JsonUtils.getReviews(reviewResponse);

            movieDetail = new MovieDetail(duration, videoKeys, reviews);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movieDetail;
    }

    @Override
    protected void onPostExecute(MovieDetail movieDetail) {
        super.onPostExecute(movieDetail);
        if (movieDetail != null) {
            if (movieDetail.getDuration() != 0) {
                String durationString = "" + movieDetail.getDuration() + " minutes";
                mDurationTv.get().setText(durationString);
            }
            mProgressBar.get().setVisibility(View.INVISIBLE);
            mDetailLayout.get().setVisibility(View.VISIBLE);
            mTrailerAdapter.get().setTrailerIdList(movieDetail.getVideoLink());
            mReviewAdapter.get().setReviewList(movieDetail.getReviews());
        }
    }
}
