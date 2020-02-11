package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.Utils.NetworkUtils;
import com.example.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private List<Movie> mMovies;

    public MoviesAdapter(List<Movie> movies) {
        mMovies = movies;
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        MoviesAdapterViewHolder viewHolder = new MoviesAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder holder, int position) {

        Movie movie = mMovies.get(position);
        URL posterUrl = NetworkUtils.buildPosterUrl(movie.getMoviePoster());
        Picasso.get()
                .load(String.valueOf(posterUrl))
                .placeholder(R.color.colorPrimary)
                .error(R.color.colorPrimary)
                .into(holder.mMoviesImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) {
            return 0;
        }
        return mMovies.size();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMoviesImageView;

        public MoviesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mMoviesImageView = itemView.findViewById(R.id.iv_movie_poster);
        }
    }

    void setMoviesData(List<Movie> moviesData) {
        mMovies = moviesData;
        notifyDataSetChanged();
    }
}
