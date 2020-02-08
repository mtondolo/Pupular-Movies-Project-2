package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {
    private String[] mMoviesData;

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
        String movieForThisDataPosition = mMoviesData[position];
        holder.mMoviesTextView.setText(movieForThisDataPosition);
    }

    @Override
    public int getItemCount() {
        if (mMoviesData == null) {
            return 0;
        }
        return mMoviesData.length;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mMoviesTextView;

        public MoviesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mMoviesTextView = itemView.findViewById(R.id.tv_movies_data);
        }
    }

    void setMoviesData(String[] moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }
}
