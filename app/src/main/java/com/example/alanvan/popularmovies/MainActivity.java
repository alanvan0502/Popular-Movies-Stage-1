package com.example.alanvan.popularmovies;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.annotation.Nullable;
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

import com.example.alanvan.popularmovies.favorite_data.FavRepository;
import com.example.alanvan.popularmovies.favorite_data.database.FavEntry;
import com.example.alanvan.popularmovies.model.Movie;
import com.example.alanvan.popularmovies.utilities.InjectorUtils;
import com.example.alanvan.popularmovies.utilities.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler {

    public static final int NUM_PAGES = 3;
    private static final int NUM_COLUMNS = 2;
    private static final String SORT = "SORT";
    private static final String FAVORITE = "FAVORITE";
    private static final String RECYCLER_STATE = "RECYCLER_STATE";

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private boolean isSortPopularity = true;
    private boolean isFavoriteFilter = false;
    private FavRepository mFavRepository;
    private Parcelable savedRecyclerState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessage = findViewById(R.id.error_tv);

        mRecyclerView = findViewById(R.id.recycler_view_movies);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUM_COLUMNS);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mFavRepository = InjectorUtils.provideRepository(this);

        if (savedInstanceState != null) {
            isFavoriteFilter = savedInstanceState.getBoolean(FAVORITE);
            if (isFavoriteFilter) {
                loadFavorites();
            } else {
                isSortPopularity = savedInstanceState.getBoolean(SORT);
                loadFromInternet();
            }
            savedRecyclerState = savedInstanceState.getParcelable(RECYCLER_STATE);
            if (mRecyclerView.getLayoutManager() != null)
                mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerState);
        } else {
            Intent intent = getIntent();
            if (intent != null) {
                isFavoriteFilter = intent.getBooleanExtra(FAVORITE, false);
                isSortPopularity = intent.getBooleanExtra(SORT, true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFavoriteFilter) {
            loadFavorites();
        } else {
            loadFromInternet();
        }
    }

    private void loadFromInternet() {
        if (connectedToInternet(this)) {
            Log.d("INTERNET", "connected");
            new FetchMovieDataTask(mLoadingIndicator,
                    mMovieAdapter, mRecyclerView, savedRecyclerState, mErrorMessage).execute(isSortPopularity);
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
                isFavoriteFilter = false;
                new FetchMovieDataTask(mLoadingIndicator,
                        mMovieAdapter, mRecyclerView, savedRecyclerState, mErrorMessage).execute(true);
            } else
                showConnectionErrorMessage();
            return true;
        }

        if (id == R.id.action_sort_rating) {
            mMovieAdapter.setMovieData(null);
            if (connectedToInternet(this)) {
                isSortPopularity = false;
                isFavoriteFilter = false;
                new FetchMovieDataTask(mLoadingIndicator,
                        mMovieAdapter, mRecyclerView, savedRecyclerState, mErrorMessage).execute(false);
            } else
                showConnectionErrorMessage();
            return true;
        }

        if (id == R.id.action_get_favorites) {
            isFavoriteFilter = true;
            loadFavorites();
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadFavorites() {
        mFavRepository.getAllFavEntries().observe(this, new Observer<List<FavEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavEntry> favEntries) {
                if (favEntries != null) {
                    ArrayList<String> movieData = convertToMovieData(favEntries);
                    mMovieAdapter.setMovieData(movieData);
                }
            }
        });
    }

    private ArrayList<String> convertToMovieData(List<FavEntry> favEntries) {
        //TODO: Complete this!!!
        ArrayList<String> result = new ArrayList<>();
        for (FavEntry entry: favEntries) {
            Movie movie = entry.getMovie();
            String jsonString = JsonUtils.convertToJsonString(movie);
            result.add(jsonString);
        }
        Log.d("RESULTS", result.toString());
        return result;
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
        outState.putBoolean(FAVORITE, isFavoriteFilter);
        if (mRecyclerView.getLayoutManager() != null)
            outState.putParcelable(RECYCLER_STATE,
                    mRecyclerView.getLayoutManager().onSaveInstanceState());
    }


}
