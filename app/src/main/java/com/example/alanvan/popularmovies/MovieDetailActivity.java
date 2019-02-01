package com.example.alanvan.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alanvan.popularmovies.favorite_data.FavRepository;
import com.example.alanvan.popularmovies.favorite_data.database.FavEntry;
import com.example.alanvan.popularmovies.model.Movie;
import com.example.alanvan.popularmovies.utilities.InjectorUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerOnClickHandler {

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    private ImageView mPosterIv;
    private TextView mReleaseDateTv;
    private TextView mRatingTv;
    private TextView mSynopsisTv;
    private TextView mDurationTv;
    private LinearLayout mDetailLayout;
    private ProgressBar mProgressBar;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mRecyclerViewTrailer;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView mRecyclerViewReviews;
    private ImageView mFavorite;
    private Button mFavoriteButton;

    private FavRepository mRepository;
    private Movie mMovie;

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
        mFavoriteButton = findViewById(R.id.mark_favorite);

        //Setup trailer recyclerview
        mRecyclerViewTrailer = findViewById(R.id.recycler_view_trailers);
        RecyclerView.LayoutManager layoutManagerTrailer =
                new LinearLayoutManager(this);
        mRecyclerViewTrailer.setLayoutManager(layoutManagerTrailer);
        mRecyclerViewTrailer.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this, this);
        mRecyclerViewTrailer.setAdapter(mTrailerAdapter);

        //Setup review recyclerview
        mRecyclerViewReviews = findViewById(R.id.recycler_view_reviews);
        RecyclerView.LayoutManager layoutManagerReview =
                new LinearLayoutManager(this);
        mRecyclerViewReviews.setLayoutManager(layoutManagerReview);
        mRecyclerViewReviews.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mRecyclerViewReviews.setAdapter(mReviewAdapter);

        Intent intent = getIntent();

        if (intent.hasExtra(Movie.MOVIE_INFO)) {
            mMovie = intent.getParcelableExtra(Movie.MOVIE_INFO);
        }

        if (mMovie != null) {
            new FetchMovieDetailDataTask(mMovie.getId(),
                    mDurationTv, mDetailLayout, mProgressBar, mTrailerAdapter, mReviewAdapter).execute();

            if (!mMovie.getTitle().equals("")) {
                mTitleTv.setText(mMovie.getTitle());
            }
            if (!mMovie.getReleaseDate().equals("")) {
                mReleaseDateTv.setText(mMovie.getReleaseDate());
            }
            if (!mMovie.getOverview().equals("")) {
                mSynopsisTv.setText(mMovie.getOverview());
            }
            if (mMovie.getVoteAverage() != -1) {
                String rating = Double.toString(mMovie.getVoteAverage());
                mRatingTv.setText(rating);
                mRatingTv.append(getString(R.string.rating_base_10));
            }

            Picasso.with(this)
                    .load(mMovie.getPosterPath())
                    .placeholder(R.mipmap.ic_movie_placeholder)
                    .error(R.mipmap.ic_movie_placeholder)
                    .into(mPosterIv);
        }

        mFavorite = findViewById(R.id.favorite);
        mRepository = InjectorUtils.provideRepository(this);

        setupViewModel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }

    private void setupViewModel() {
        FavoriteViewModel viewModel
                = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        viewModel.getAllFavoriteEntries().observe(this, new Observer<List<FavEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavEntry> favEntries) {
                if (favEntries != null)
                    for (FavEntry entry: favEntries) {
                        if (entry.getMovieId() == mMovie.getId()) {
                            mFavorite.setVisibility(View.VISIBLE);
                            mFavoriteButton.setText(getString(R.string.unmark_favorite));
                        }
                    }
            }
        });
    }

    public void markFavorite(final View view) {
        if (mMovie != null) {
            if (mFavorite.getVisibility() == View.INVISIBLE) {
                final FavEntry entry = new FavEntry(mMovie, mMovie.getId());
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mRepository.insertFavEntry(entry);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mFavoriteButton.setText(getString(R.string.unmark_favorite));
                            }
                        });
                    }
                });
            } else {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mRepository.deleteFavEntry(mMovie.getId());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mFavorite.setVisibility(View.INVISIBLE);
                                mFavoriteButton.setText(getString(R.string.mark_as_favorite_button_text));
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public void onClick(String trailerId) {
        openYouTube(trailerId);
    }

    public void openYouTube(String trailerId) {
        // Get the URL text.
        String url = YOUTUBE_BASE_URL + trailerId;

        // Parse the URI and create the intent.
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        // Find an activity to hand the intent and start that activity.
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this!");
        }
    }
}
