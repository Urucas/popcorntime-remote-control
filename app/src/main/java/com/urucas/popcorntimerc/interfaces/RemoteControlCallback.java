package com.urucas.popcorntimerc.interfaces;

import com.urucas.popcorntimerc.model.Movie;

import java.util.ArrayList;

/**
 * Created by Urucas on 8/20/14.
 */
public interface RemoteControlCallback {

    public void onPopcornFound(ArrayList<String> socketsName);

    public void onMovieDetail(Movie movie);

    public void onControlRequest();

    public void onDownloading(Movie movie);

    public void onPlaying(Movie movie);
}
