package com.example.alanvan.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alanvan.popularmovies.model.Movie;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler {

    public static final int NUM_PAGES = 3;
    private static final int NUM_COLUMNS = 2;
    private static final String SORT = "SORT";
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private boolean isSortPopularity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessage = findViewById(R.id.error_tv);

        mRecyclerView = findViewById(R.id.recycler_view_movies);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, NUM_COLUMNS);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this, this);

        mRecyclerView.setAdapter(mMovieAdapter);

        if (savedInstanceState != null) {
            isSortPopularity = savedInstanceState.getBoolean(SORT);
        }

        if (connectedToInternet(this)) {
            Log.d("INTERNET", "connected");
            new FetchMovieDataTask(mLoadingIndicator,
                    mMovieAdapter, mRecyclerView, mErrorMessage).execute(isSortPopularity);
        } else {
            showConnectionErrorMessage();
        }
    }

    private boolean connectedToInternet(Context ctx) {
        ConnectivityManager conMgr =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        return (i != null && i.isConnected());
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     * @param movie The movie object clicked
     */
    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(Movie.MOVIE_INFO, movie);
        startActivity(intent);
    }

    /**
     * Inflate the menu
     * @param menu, menu object to be inflated
     * @return true if the menu has been properly inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort, menu);
        return true;
    }

    /**
     * This method starts the background network task to fetch movie data. It depends on which
     * sort setting is selected.
     * @param item, the menu item selected
     * @return true if a menu item is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_popularity) {
            mMovieAdapter.setMovieData(null);
            if (connectedToInternet(this)) {
                isSortPopularity = true;
                new FetchMovieDataTask(mLoadingIndicator,
                        mMovieAdapter, mRecyclerView, mErrorMessage).execute(true);
            } else
                showConnectionErrorMessage();
            return true;
        }

        if (id == R.id.action_sort_rating) {
            mMovieAdapter.setMovieData(null);
            if (connectedToInternet(this)) {
                isSortPopularity = false;
                new FetchMovieDataTask(mLoadingIndicator,
                        mMovieAdapter, mRecyclerView, mErrorMessage).execute(false);
            } else
                showConnectionErrorMessage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showConnectionErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorMessage.setText(getString(R.string.network_connection_error));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SORT, isSortPopularity);
    }
}
