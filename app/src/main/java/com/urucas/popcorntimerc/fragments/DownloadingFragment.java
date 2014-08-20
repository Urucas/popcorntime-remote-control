package com.urucas.popcorntimerc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.urucas.popcorntimerc.PopcornApplication;
import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.activities.SplashActivity;
import com.urucas.popcorntimerc.model.Movie;

/**
 * Created by Urucas on 8/20/14.
 */
public class DownloadingFragment extends android.support.v4.app.Fragment {

    private View view;
    private Button cancelBtt;
    private Movie _movie;
    private TextView movieTitle;
    private TextView movieDesc;
    private ImageView poster;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SplashActivity.clearVolumeKeys();

        view = inflater.inflate(R.layout.fragment_downloading, container, false);

        cancelBtt = (Button) view.findViewById(R.id.cancelBtt);
        cancelBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.getRemoteControl().cancelStreaming();
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
