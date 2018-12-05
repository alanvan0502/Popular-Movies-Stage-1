package com.example.alanvan.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.alanvan.popularmovies.model.Movie;
import com.example.alanvan.popularmovies.utilities.JsonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ArrayList<String> mMovieData;
    private final Context context;

    /*
     * On-click handler to facilitate an Activity in interfacing with the RecyclerView
     */
    private final MovieAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    /**
     * Creates a MovieAdapter
     * @param context, the application context
     * @param clickHandler, the clickHandler for the adapter
     */
    MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     * @param viewGroup, the ViewGroup that these ViewHolders are contained within.
     * @param viewType,  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                   can use this viewType integer to provide a different layout. See
     *                   {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     * @return a new ViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Movie movie = JsonUtils.convertToMovieObject(mMovieData.get(position));

        if (movie != null) {
            Picasso.with(context)
                    .load(movie.getPosterPath())
                    .placeholder(R.mipmap.ic_movie_placeholder)
                    .error(R.mipmap.ic_movie_placeholder)
                    .into(viewHolder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null) return 0;
        else {
            Log.d("Movie data length", ""+mMovieData.size());
            return mMovieData.size();
        }
    }

    /**
     * Cache of the children views for a movie list item
     */
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        final ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String movieJsonString = mMovieData.get(adapterPosition);
            Movie movie = JsonUtils.convertToMovieObject(movieJsonString);
            mClickHandler.onClick(movie);
        }
    }

    /**
     * This method sets the movie data to a MovieAdapter. This is handy when we get new data
     * from the web but don't want to create a new MovieAdapter to display it
     * @param movieData, the movie data to be processed
     */
    public void setMovieData(ArrayList<String> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
