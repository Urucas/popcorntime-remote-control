package com.urucas.popcorntimerc.fragments;

import android.app.Fragment;
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
    private ImageButton leftBtt;
    private ImageButton rigthBtt;
    private ImageButton upBtt;
    private ImageButton downBtt;
    private ImageButton enterBtt;
    private Button moviesBtt;
    private Button seriesBtt;
    private ImageButton backBtt;
    private ImageButton fullBtt;
    private ImageButton seasonUpBtt;
    private ImageButton seasonDownBtt;
    private ImageButton favBtt;
    private ImageButton watchedBtt;
    private ImageButton playBtt;
    private ImageButton muteBtt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SplashActivity.clearVolumeKeys();

        view = inflater.inflate(R.layout.fragment_control, container, false);

        leftBtt = (ImageButton) view.findViewById(R.id.leftBtt);
        rigthBtt = (ImageButton) view.findViewById(R.id.rightBtt);
        upBtt = (ImageButton) view.findViewById(R.id.upBtt);
        downBtt = (ImageButton) view.findViewById(R.id.downBtt);
        enterBtt = (ImageButton) view.findViewById(R.id.enterBtt);

        moviesBtt = (Button) view.findViewById(R.id.showMoviesBtt);
        seriesBtt = (Button) view.findViewById(R.id.showSeriesBtt);
        backBtt = (ImageButton) view.findViewById(R.id.backBtt);

        fullBtt = (ImageButton) view.findViewById(R.id.toggleFullscreenBtt);
        seasonUpBtt = (ImageButton) view.findViewById(R.id.seasonUpBtt);
        seasonDownBtt = (ImageButton) view.findViewById(R.id.seasonDownBtt);

        favBtt = (ImageButton) view.findViewById(R.id.favouriteBtt);
        watchedBtt = (ImageButton) view.findViewById(R.id.watchedBtt);

        playBtt = (ImageButton) view.findViewById(R.id.playBtt);
        muteBtt = (ImageButton) view.findViewById(R.id.muteBtt);

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
