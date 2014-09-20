package com.urucas.popcorntimerc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.activities.SplashActivity;

/**
 * Created by Urucas on 8/20/14.
 */
public class ControlFragment extends android.support.v4.app.Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_control, container, false);

        ImageButton leftBtt = (ImageButton) view.findViewById(R.id.leftBtt);
        ImageButton rigthBtt = (ImageButton) view.findViewById(R.id.rightBtt);
        ImageButton upBtt = (ImageButton) view.findViewById(R.id.upBtt);
        ImageButton downBtt = (ImageButton) view.findViewById(R.id.downBtt);
        ImageButton enterBtt = (ImageButton) view.findViewById(R.id.enterBtt);

        Button moviesBtt = (Button) view.findViewById(R.id.showMoviesBtt);
        Button seriesBtt = (Button) view.findViewById(R.id.showSeriesBtt);
        ImageButton backBtt = (ImageButton) view.findViewById(R.id.backBtt);

        ImageButton fullBtt = (ImageButton) view.findViewById(R.id.toggleFullscreenBtt);
        ImageButton seasonUpBtt = (ImageButton) view.findViewById(R.id.seasonUpBtt);
        ImageButton seasonDownBtt = (ImageButton) view.findViewById(R.id.seasonDownBtt);

        ImageButton favBtt = (ImageButton) view.findViewById(R.id.favouriteBtt);
        ImageButton watchedBtt = (ImageButton) view.findViewById(R.id.watchedBtt);

        ImageButton playBtt = (ImageButton) view.findViewById(R.id.playBtt);
        ImageButton muteBtt = (ImageButton) view.findViewById(R.id.muteBtt);

        leftBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().moveLeft();
            }
        });

        rigthBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().moveRight();
            }
        });

        upBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().moveUp();
            }
        });

        downBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().moveDown();
            }
        });

        enterBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().enter();
            }
        });

        moviesBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().showMoviesList();
            }
        });

        seriesBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().showSeriesList();
            }
        });

        backBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().escape();
            }
        });

        fullBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().fullscreen();
            }
        });

        seasonUpBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().seasonUp();
            }
        });

        seasonDownBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().seasonDown();
            }
        });

        favBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().favourite();
            }
        });

        watchedBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().watched();
            }
        });

        playBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().toggleplay();
            }
        });

        muteBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().mute();
            }
        });

        return view;
    }
}
