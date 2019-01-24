package com.example.alanvan.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alanvan.popularmovies.model.Movie;
import com.example.alanvan.popularmovies.utilities.JsonUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView mPosterIv;
    private TextView mReleaseDateTv;
    private TextView mRatingTv;
    private TextView mSynopsisTv;
    private TextView mDurationTv;
    private LinearLayout mDetailLayout;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        TextView mTitleTv = findViewById(R.id.detail_title_tv);
        mPosterIv = findViewById(R.id.detail_poster_iv);
        mReleaseDateTv = findViewById(R.id.release_date_tv);
        mRatingTv = findViewById(R.id.rating_tv);
        mSynopsisTv = findViewById(R.id.synopsis_tv);
        mDurationTv = findViewById(R.id.movie_duration_tv);
        mDetailLayout = findViewById(R.id.movie_detail_layout);
        mProgressBar = findViewById(R.id.pb_loading_indicator_detail);

        Intent intent = getIntent();
        Movie movie = null;
        if (intent.hasExtra(Movie.MOVIE_INFO)) {
            movie = intent.getParcelableExtra(Movie.MOVIE_INFO);
        }

        if (movie != null) {
            Log.d("MOVIE_ID", "" + movie.getId());
            new FetchMovieDetailDataTask(movie.getId(), mDurationTv, mDetailLayout, mProgressBar).execute();

            if (!movie.getTitle().equals("")) {
                mTitleTv.setText(movie.getTitle());
            }
            if (!movie.getReleaseDate().equals("")) {
                mReleaseDateTv.setText(movie.getReleaseDate());
            }
            if (!movie.getOverview().equals("")) {
                mSynopsisTv.setText(movie.getOverview());
            }
            if (movie.getVoteAverage() != -1) {
                String rating = Double.toString(movie.getVoteAverage());
                mRatingTv.setText(rating);
                mRatingTv.append(getString(R.string.rating_base_10));
            }

            Picasso.with(this)
                    .load(movie.getPosterPath())
                    .placeholder(R.mipmap.ic_movie_placeholder)
                    .error(R.mipmap.ic_movie_placeholder)
                    .into(mPosterIv);
        }
    }

    public void markFavorite(View view) {
        //TODO: implement this
        throw new RuntimeException("Not implemented yet");
    }
}
