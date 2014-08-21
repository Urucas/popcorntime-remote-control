package com.urucas.popcorntimerc.activities;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.fragments.ControlFragment;
import com.urucas.popcorntimerc.fragments.DownloadingFragment;
import com.urucas.popcorntimerc.fragments.LeftMenuFragment;
import com.urucas.popcorntimerc.fragments.MovieDetailFragment;
import com.urucas.popcorntimerc.fragments.PlayingFragment;
import com.urucas.popcorntimerc.interfaces.RemoteControlCallback;
import com.urucas.popcorntimerc.interfaces.VolumeKeysCallback;
import com.urucas.popcorntimerc.model.Movie;
import com.urucas.popcorntimerc.socket.RemoteControl;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class SplashActivity extends SlidingFragmentActivity{

    private static final String TAG_NAME = "SplashActivity";

    private static RemoteControl remote;
    private ControlFragment controlFragment;
    private MovieDetailFragment movieFragment;
    private DownloadingFragment downloadingFragment;
    private PlayingFragment playingFragment;

    public static VolumeKeysCallback volumeCallback;
    private SlidingMenu sm;
    private LeftMenuFragment leftMenuFragment;
    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        setBehindContentView(R.layout.frame_leftmenu);

        sm = getSlidingMenu();
        sm.setMode(SlidingMenu.LEFT);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        setSlidingActionBarEnabled(false);

        leftMenuFragment = new LeftMenuFragment();
        leftMenuFragment.setActivity(SplashActivity.this);

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.frame_leftmenu, leftMenuFragment)
            .commit();

        clearVolumeKeys();

        try {

            actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("");
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.main_bar);

            ImageButton menuBtt = (ImageButton) actionBar.getCustomView().findViewById(R.id.menuBtt);
            menuBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle();
                }
            });

        }catch(Exception e) {
            Log.d(TAG_NAME, e.getMessage());
            e.printStackTrace();
        }

        remote = new RemoteControl(SplashActivity.this, new RemoteControlCallback() {
            @Override
            public void onPopcornFound(ArrayList<String> popcornApps) {
                leftMenuFragment.refreshSpinner(popcornApps);
            }

            @Override
            public void onMovieDetail(Movie movie) {
                showMovieDetail(movie);
            }

            @Override
            public void onControlRequest() {
                showControl();
            }

            @Override
            public void onDownloading(Movie movie) {
                showDownloading(movie);
            }

            @Override
            public void onPlaying(Movie movie) {
                showPlaying(movie);
            }

            @Override
            public void onPopCornDisconected(ArrayList<String> popcornApps) {
                leftMenuFragment.refreshSpinner(popcornApps);
            }
        });
        remote.search4Popcorns();

    }

    public static void clearVolumeKeys() {
        volumeCallback = new VolumeKeysCallback(){
            @Override
            public void onVolumeUp() { }

            @Override
            public void onVolumeDown() { }
        };
    }

    public static void setVolumeKeys(VolumeKeysCallback callback) {
        volumeCallback = callback;
    }

    public static RemoteControl getRemoteControl() {
        return remote;
    }

    public void showControl() {

        controlFragment = new ControlFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, controlFragment)
                .commit();

    }

    public void showMovieDetail(Movie movie) {

        movieFragment = new MovieDetailFragment();
        movieFragment.setMovie(movie);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, movieFragment)
                .commit();
    }

    public void showDownloading(Movie movie) {

        downloadingFragment = new DownloadingFragment();
        downloadingFragment.setMovie(movie);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, downloadingFragment)
                .commit();
    }

    public void showPlaying(Movie movie) {

        playingFragment = new PlayingFragment();
        playingFragment.setMovie(movie);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, playingFragment)
                .commit();
    }

    public void selectPopcorn(String localname) {

        if(remote.selectPopcornApp(localname)) {
           showControl();
        }

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    volumeCallback.onVolumeDown();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    volumeCallback.onVolumeUp();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

}
