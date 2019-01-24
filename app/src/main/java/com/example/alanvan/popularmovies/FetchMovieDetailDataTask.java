package com.example.alanvan.popularmovies;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alanvan.popularmovies.model.MovieDetail;
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

    private int movieId;

    FetchMovieDetailDataTask
            (int movieId, TextView durationTv, LinearLayout detailLayout, ProgressBar progressBar) {
        mDurationTv = new WeakReference<>(durationTv);
        mProgressBar = new WeakReference<>(progressBar);
        mDetailLayout = new WeakReference<>(detailLayout);
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

            movieDetail = new MovieDetail(duration, videoKeys);
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
        }
    }
}
