package com.example.alanvan.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.alanvan.popularmovies.model.Movie;
import com.example.alanvan.popularmovies.utilities.JsonUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<String> mTrailerIdList;
    private final Context context;

    /*
     * On-click handler to facilitate an Activity in interfacing with the RecyclerView
     */
    private final TrailerOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages
     */
    public interface TrailerOnClickHandler {
        void onClick(String trailerId);
    }

    /**
     * Creates a TrailerAdapter
     * @param context, the application context
     * @param clickHandler, the clickHandler for the adapter
     */
    TrailerAdapter(Context context, TrailerOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     * @param viewGroup, the ViewGroup that these ViewHolders are contained within.
     * @param viewType,  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                   can use this viewType integer to provide a different layout. See
     *                   {@link RecyclerView.Adapter#getItemViewType(int)}
     * @return a new ViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trailer_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        TextView trailerNumber = viewHolder.trailerNumber;
        int number = position + 1;
        trailerNumber.setText(MessageFormat.format("Trailer No. {0}", number));
    }

    @Override
    public int getItemCount() {
        if (mTrailerIdList == null) return 0;
        else {
            return mTrailerIdList.size();
        }
    }

    /**
     * Cache of the children views of a trailer item
     */
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        final ImageButton playButton;
        final TextView trailerNumber;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            playButton = itemView.findViewById(R.id.playButton);
            trailerNumber = itemView.findViewById(R.id.trailer_number);
            playButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String trailerId = mTrailerIdList.get(adapterPosition);
            mClickHandler.onClick(trailerId);
        }
    }

    /**
     * This method sets the trailer id list to a TrailerAdapter. This is handy when we get new data
     * from the web but don't want to create a new Trailer to display it
     * @param trailerIdList, the trailer id data to be processed
     */
    public void setTrailerIdList(List<String> trailerIdList) {
        mTrailerIdList = trailerIdList;
        notifyDataSetChanged();
    }
}
