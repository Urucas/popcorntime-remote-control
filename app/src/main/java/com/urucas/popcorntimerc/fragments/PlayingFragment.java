package com.urucas.popcorntimerc.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.urucas.popcorntimerc.PopcornApplication;
import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.activities.SplashActivity;
import com.urucas.popcorntimerc.interfaces.VolumeKeysCallback;
import com.urucas.popcorntimerc.model.Movie;

/**
 * Created by Urucas on 8/20/14.
 */
public class PlayingFragment extends android.support.v4.app.Fragment {

    private View view;
    private Movie _movie;
    private TextView movieTitle;
    private TextView movieDesc;
    private ImageView poster;
    private ImageButton playBtt;
    private ImageButton pauseBtt;
    private ImageButton muteBtt;
    private ImageButton fullscreenBtt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_playing, container, false);

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

        playBtt = (ImageButton) view.findViewById(R.id.playBtt);
        playBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().play();
            }
        });


        pauseBtt = (ImageButton) view.findViewById(R.id.pauseBtt);
        pauseBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().pause();
            }
        });

        muteBtt = (ImageButton) view.findViewById(R.id.muteBtt);
        muteBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().mute();
            }
        });

        fullscreenBtt = (ImageButton) view.findViewById(R.id.fullscreenBtt);
        fullscreenBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().fullscreen();
            }
        });

        SplashActivity.setVolumeKeys(new VolumeKeysCallback() {
            @Override
            public void onVolumeUp() {
                SplashActivity.getRemoteControl().volumeUp();
            }

            @Override
            public void onVolumeDown() {
                SplashActivity.getRemoteControl().volumeDown();
            }
        });

        return view;
    }

    public void setMovie(Movie movie) {
        _movie = movie;
    }
}
