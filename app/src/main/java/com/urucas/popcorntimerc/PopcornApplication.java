package com.urucas.popcorntimerc;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.urucas.popcorntimerc.socket.RemoteControl;

/**
 * Created by Urucas on 8/18/14.
 */

public class PopcornApplication extends Application {

    private static ImageLoader _imageloader;
    private static PopcornApplication _instance;
    private static RemoteControl _remotecontrol;

    public PopcornApplication() {
        super();
        _instance = this;
    }

    public static ImageLoader getImageLoader() {
        if(_imageloader == null) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(_instance.getApplicationContext()).build();
            ImageLoader.getInstance().init(config);
            _imageloader = ImageLoader.getInstance();
        }
        return _imageloader;
    }

    public static PopcornApplication getInstance() {
        return _instance;
    }
}
