package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_movies_data)
    TextView mMovieDisplayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String[] fakeMoviesData = {
                "Ad Astra - 2019-09-17 - 6",
                "1917 - 2019-12-10 - 8.1",
                "Birds of Prey (and the Fantabulous Emancipation of One Harley Quinn) - 2020-02-05 - 6.5",
                "Joker - 2019-10-02 - 8.3",
                "Jojo Rabbit - 2019-10-18 - 8.1",
                "Ford v Ferrari - 2019-11-13 - 7.7",
                "葉問4 - 2019-12-20 - 6",
                "Uncut Gems - 2019-11-14 - 7.4",
                "Terminator: Dark Fate - 2019-10-23 - 6.2",
                "기생충 - 2019-05-30 - 8.6",
        };
        for (String dummyMoviesData : fakeMoviesData) {
            mMovieDisplayTextView.append(dummyMoviesData + "\n\n\n");
        }
    }
}
