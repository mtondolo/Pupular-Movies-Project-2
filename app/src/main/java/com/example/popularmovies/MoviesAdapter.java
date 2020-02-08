package com.example.popularmovies;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;

public class MoviesAdapter {
    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_movies_data)
        TextView mMoviewTextView;

        public MoviesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
