package com.example.alanvan.popularmovies;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alanvan.popularmovies.utilities.JsonUtils;
import com.example.alanvan.popularmovies.utilities.NetworkUtils;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Background AsyncTask Class to fetch movie data
 */
public class FetchMovieDataTask extends AsyncTask<Boolean, Void, ArrayList<String>> {

    // These are WeakReferences to prevent "leaky context" -- weak references
    // enable the activity to be garbage collected if it is not needed.
    private WeakReference<ProgressBar> mLoadingIndicator;
    private WeakReference<MovieAdapter> mMovieAdapter;
    private WeakReference<RecyclerView> mRecyclerView;
    private WeakReference<TextView> mErrorMessage;
    private Parcelable savedRecyclerState;

    // Constructor, provides references to the views in MainActivity
    FetchMovieDataTask(ProgressBar loadingIndicator,
                       MovieAdapter movieAdapter,
                       RecyclerView recyclerView,
                       Parcelable savedRecyclerState, TextView errorMessage) {
        this.mLoadingIndicator = new WeakReference<>(loadingIndicator);
        this.mMovieAdapter = new WeakReference<>(movieAdapter);
        this.mRecyclerView = new WeakReference<>(recyclerView);
        this.mErrorMessage = new WeakReference<>(errorMessage);
        this.savedRecyclerState = savedRecyclerState;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoadingIndicator.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<String> doInBackground(Boolean... params) {
        Boolean byPopularity = params[0];
        ArrayList<String> movieData = new ArrayList<>();

        for (int i = 1; i <= MainActivity.NUM_PAGES; i++) {
            try {
                URL requestURL = NetworkUtils.buildUrl(byPopularity, i);
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestURL);
                String[] movieDataArray = JsonUtils.getMovieData(jsonResponse);
                movieData.addAll(Arrays.asList(movieDataArray));
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        return movieData;
    }

    @Override
    protected void onPostExecute(ArrayList<String> movieData) {
        if (!movieData.isEmpty()) {
            showMovieDataView();
            mMovieAdapter.get().setMovieData(movieData);
            if (savedRecyclerState != null) {
                mRecyclerView.get().getLayoutManager().onRestoreInstanceState(savedRecyclerState);
            }
            Log.d("MOVIE DATA", movieData.toString());
        } else {
            showErrorMessage();
        }
        mLoadingIndicator.get().setVisibility(View.INVISIBLE);
    }

    /**
     * This method shows the movie data view
     */
    private void showMovieDataView() {
        mErrorMessage.get().setVisibility(View.INVISIBLE);
        mRecyclerView.get().setVisibility(View.VISIBLE);
    }

    /**
     * Show error message if loading fails
     */
    private void showErrorMessage() {
        mRecyclerView.get().setVisibility(View.INVISIBLE);
        mErrorMessage.get().setVisibility(View.VISIBLE);
    }
}
