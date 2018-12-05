package com.example.alanvan.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alanvan.popularmovies.model.Movie;
import com.example.alanvan.popularmovies.utilities.JsonUtils;
import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    private ImageView mPosterIv;
    private TextView mReleaseDateTv;
    private TextView mRatingTv;
    private TextView mSynopsisTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        TextView mTitleTv = findViewById(R.id.detail_title_tv);
        mPosterIv = findViewById(R.id.detail_poster_iv);
        mReleaseDateTv = findViewById(R.id.release_date_tv);
        mRatingTv = findViewById(R.id.rating_tv);
        mSynopsisTv = findViewById(R.id.synopsis_tv);

        Intent intent = getIntent();
        Movie movie = null;
        if (intent.hasExtra(Movie.MOVIE_INFO)) {
            movie = intent.getParcelableExtra(Movie.MOVIE_INFO);
        }

        if (movie != null) {

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
}
