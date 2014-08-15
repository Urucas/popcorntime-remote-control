package com.urucas.popcorntimerc.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.utils.Utils;

import org.json.JSONObject;

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
    private HashMap<String, String> connectedSocketsName = new HashMap<String, String>();
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
        socketsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSocket((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        refreshSpinner();

        getSupportActionBar().setTitle(getResources().getString(R.string.title));

        // get current IP address
        myip = Utils.getIPAddress(true);
        Log.i("myip", myip);

        // search for possible sockets
        // search4sockets();
    }

    private void selectSocket(String localname) {
        if(localname == null) return;

        String socketip = connectedSocketsName.get(localname);
        socket = connectedSockets.get(socketip);
        if(socket == null) return;

        playBtt.setVisibility(View.VISIBLE);
        playBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.emit("play", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                    }
                });
            }
        });

        pauseBtt.setVisibility(View.VISIBLE);
        pauseBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.emit("pause", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                    }
                });
            }
        });

    }

    private void search4sockets() {

        if(!Utils.isWIFIConnected(SplashActivity.this)){
            Utils.Toast(SplashActivity.this, R.string.noconnection, Toast.LENGTH_SHORT);
            return;
        }

        // set local ip search based on the mobile ip assigned
        String localip = "http://192.168." +
                myip.split("\\.")[2]
                +".";

        for(int i = 0; i<256; i++) {
            String ip = localip + String.valueOf(i);
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
                    Log.i(TAG_NAME, "socket connected");
                }
            });
            socket.on("jalou", new Emitter.Listener(){
                @Override
                public void call(Object... args) {
                    String localname = null;
                    try {
                        JSONObject json = (JSONObject)args[0];
                        localname = json.getString("name");

                    }catch(Exception e){
                        socket.disconnect();
                        socket.close();
                        socket.off();
                        socketes.remove(socket);
                        return;
                    }
                    if(localname != null) {
                        final String finalLocalname = localname;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sockectConnected(socketes.get(socket), socket, finalLocalname);
                            }
                        });
                    }
                }
            });
            socket.on("play error", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.Toast(SplashActivity.this, R.string.playernotfound);
                        }
                    });
                }
            });
            socket.on("pause error", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.Toast(SplashActivity.this, R.string.playernotfound);
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

    private void sockectConnected(String ip, Socket socket, String localname) {
        if(socket == null) return;
        connectedSocketsName.put(localname, ip);
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

    private ArrayList<String> getConnectdSocketsName() {
        ArrayList<String> list = new ArrayList<String>();
        for(String ip: connectedSocketsName.keySet()){
            list.add(ip);
        }
        return list;
    }

    private void refreshSpinner() {
        ArrayList<String> socketslist = getConnectdSocketsName();
        if(socketslist.size() == 0){
            socketslist.add(getResources().getString(R.string.nosockets));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, socketslist);

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
