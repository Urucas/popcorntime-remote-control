package com.urucas.popcorntimerc.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.fragments.ControlFragment;
import com.urucas.popcorntimerc.fragments.DownloadingFragment;
import com.urucas.popcorntimerc.fragments.MovieDetailFragment;
import com.urucas.popcorntimerc.fragments.PlayingFragment;
import com.urucas.popcorntimerc.interfaces.RemoteControlCallback;
import com.urucas.popcorntimerc.interfaces.VolumeKeysCallback;
import com.urucas.popcorntimerc.model.Movie;
import com.urucas.popcorntimerc.socket.RemoteControl;

import java.util.ArrayList;

public class SplashActivity extends ActionBarActivity {

    private static final String TAG_NAME = "SplashActivity";

    private Spinner socketsSpinner;
    private static RemoteControl remote;
    private ControlFragment controlFragment;
    private MovieDetailFragment movieFragment;
    private DownloadingFragment downloadingFragment;
    private PlayingFragment playingFragment;

    public static VolumeKeysCallback volumeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().setTitle(getResources().getString(R.string.title));

        clearVolumeKeys();

        socketsSpinner = (Spinner) findViewById(R.id.socketsSpinner);
        socketsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectPopcorn((String) parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        refreshSpinner(new ArrayList<String>());

        remote = new RemoteControl(SplashActivity.this, new RemoteControlCallback() {
            @Override
            public void onPopcornFound(ArrayList<String> popcornApps) {
                refreshSpinner(popcornApps);
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
                refreshSpinner(popcornApps);
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

    private void selectPopcorn(String localname) {
        if(remote.selectPopcornApp(localname)) {
           showControl();
        }
    }

    private void refreshSpinner(ArrayList<String> popcornList) {
        if(popcornList.size() == 0){
            popcornList.add(getResources().getString(R.string.nopopcorns));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, popcornList);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        socketsSpinner.setAdapter(spinnerArrayAdapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh){
            // search4sockets();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
