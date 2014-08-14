package com.urucas.popcorntimerc.activities;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.utils.Utils;

import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;


public class SplashActivity extends ActionBarActivity {

    private static final String TAG_NAME = "SplashActivity";
    private ImageButton playBtt, pauseBtt;
    private String myip;
    private static String port = "8006";

    private Socket socket;
    private HashMap<Socket, String> socketes = new HashMap<Socket, String>();
    private HashMap<String, Socket> connectedSockets = new HashMap<String, Socket>();
    private Spinner socketsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        playBtt = (ImageButton) findViewById(R.id.playBtt);
        playBtt.setVisibility(View.INVISIBLE);

        pauseBtt = (ImageButton) findViewById(R.id.pauseBtt);
        pauseBtt.setVisibility(View.INVISIBLE);

        socketsSpinner = (Spinner) findViewById(R.id.socketsSpinner);
        refreshSpinner();

        getSupportActionBar().setTitle(getResources().getString(R.string.title));

        // get current IP address
        myip = Utils.getIPAddress(true);

        // search for possible sockets
        search4sockets();
    }

    private void search4sockets() {

        if(!Utils.isWIFIConnected(SplashActivity.this)){
            Utils.Toast(SplashActivity.this, R.string.noconnection, Toast.LENGTH_SHORT);
            return;
        }
        for(int i = 0; i<256; i++) {
            String ip = "http://192.168.0." + String.valueOf(i);
            String socketip = ip + ":"+port;
            if(ip.equals(myip)) continue;
            if(connectedSockets.get(socket)!=null) continue;

            Log.i(TAG_NAME, socketip);
            Socket socket = createPossibleSocket(socketip);
            if(socket != null) socketes.put(socket, socketip);
        }
    }

    private Socket createPossibleSocket(String socketip) {
        try {
            final Socket socket = IO.socket(socketip);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sockectConnected(socketes.get(socket), socket);
                        }
                    });
                }
            });
            socket.on(Socket.EVENT_ERROR, new Emitter.Listener(){
                @Override
                public void call(Object... args) {
                    socket.disconnect();
                    socket.close();
                    socket.off();
                    socketes.remove(socket);
                }
            });
            socket.connect();
            return socket;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void sockectConnected(String ip, Socket socket) {
        if(socket == null) return;
        connectedSockets.put(ip, socket);
        refreshSpinner();
    }

    private ArrayList<String> getConnectedSocketes() {
        ArrayList<String> list = new ArrayList<String>();
        for(String ip: connectedSockets.keySet()){
            list.add(ip);
        }
        return list;
    }

    private void refreshSpinner() {
        ArrayList<String> socketslist = getConnectedSocketes();
        if(socketslist.size() == 0){
            socketslist.add(getResources().getString(R.string.nosockets));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, socketslist);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        socketsSpinner.setAdapter(spinnerArrayAdapter);
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
            search4sockets();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
