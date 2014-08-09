package com.urucas.popcorntimerc.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.utils.Utils;

import java.net.URISyntaxException;


public class SplashActivity extends ActionBarActivity {

    private static final String TAG_NAME = "SplashActivity";
    private Socket socket;
    private ImageButton playBtt, pauseBtt;
    private String intra;
    private static String port = "8006";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        playBtt = (ImageButton) findViewById(R.id.playBtt);
        pauseBtt = (ImageButton) findViewById(R.id.pauseBtt);

        getSupportActionBar().setTitle(getResources().getString(R.string.title));

        init();
    }

    public void init() {

        try {
            socket = IO.socket("http://192.168.0.100:8006");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i(TAG_NAME, "connected");
                }
            });

            socket.connect();

            playBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    socket.emit("play");
                }
            });

            pauseBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    socket.emit("pause");
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
