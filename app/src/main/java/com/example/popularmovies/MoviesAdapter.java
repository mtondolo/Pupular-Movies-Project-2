package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.popularmovies.Utils.NetworkUtils;
import com.example.popularmovies.model.MovieEntry;

import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    //private List<Movie> mMovies;

    private List<MovieEntry> mMovieEntries;
    private final Context mContext;

    final private MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(int id);
    }

    public MoviesAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder holder, int position) {
        MovieEntry movieEntry = mMovieEntries.get(position);

        Glide.with(holder.itemView.getContext())
                .load(String.valueOf(NetworkUtils.buildPosterUrl(movieEntry.getMoviePoster())))
                .placeholder(R.color.colorPrimary)
                .centerCrop()
                .into(holder.mMoviesImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovieEntries == null) {
            return 0;
        }
        return mMovieEntries.size();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        final ImageView mMoviesImageView;

        MoviesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mMoviesImageView = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieEntry movieEntry = mMovieEntries.get(adapterPosition);
            int movieId = movieEntry.getId();
            mClickHandler.onClick(movieId);
        }
    }

    void setMovies(List<MovieEntry> movieEntries) {
        mMovieEntries = movieEntries;
        notifyDataSetChanged();
    }
}
