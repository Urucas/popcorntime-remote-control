package com.urucas.popcorntimerc.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.urucas.popcorntimerc.PopcornApplication;
import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.activities.SplashActivity;
import com.urucas.popcorntimerc.model.Movie;

/**
 * Created by Urucas on 8/20/14.
 */
public class MovieDetailFragment extends android.support.v4.app.Fragment {

    private static final String TAG_NAME = "MovieDetailFragment";
    private View view;
    private Movie _movie;
    private ImageButton escBtt;
    private Button watchBtt;
    private TextView movieTitle, movieDesc;
    private ImageView poster;
    private Button trailerBtt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SplashActivity.clearVolumeKeys();

        view = inflater.inflate(R.layout.fragment_detail, container, false);

        escBtt = (ImageButton) view.findViewById(R.id.escBtt);
        escBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().escape();
            }
        });

        watchBtt = (Button) view.findViewById(R.id.watchBtt);
        watchBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().startStreaming();
            }
        });

        trailerBtt = (Button) view.findViewById(R.id.trailerBtt);
        trailerBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().playTrailer();
            }
        });

        movieTitle = (TextView) view.findViewById(R.id.movieTitle);
        movieTitle.setText(_movie.getTitle());

        movieDesc = (TextView) view.findViewById(R.id.movieDesc);
        movieDesc.setText(_movie.getDesc());

        poster = (ImageView) view.findViewById(R.id.poster);
        try {
            PopcornApplication.getImageLoader().displayImage(_movie.getPoster(), poster);

        }catch(Exception e){
            poster.setImageResource(R.drawable.posterholder);
        }

        return view;
    }

    public void setMovie(Movie movie) {
        _movie = movie;
    }
}
