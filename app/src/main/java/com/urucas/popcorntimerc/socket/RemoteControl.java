package com.urucas.popcorntimerc.socket;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.interfaces.RemoteControlCallback;
import com.urucas.popcorntimerc.model.Movie;
import com.urucas.popcorntimerc.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Urucas on 8/20/14.
 */
public class RemoteControl {

    private static final String TAG_NAME = "RemoteControl";
    private static String port = "8006";
    private final RemoteControlCallback _callback;
    private final Activity _activity;

    private Socket socket;
    private HashMap<Socket, String> socketes = new HashMap<Socket, String>();
    private HashMap<String, Socket> connectedSockets = new HashMap<String, Socket>();
    private HashMap<String, String> connectedSocketsName = new HashMap<String, String>();

    private Movie movie;

    private String _myip;

    public RemoteControl(Activity activity, RemoteControlCallback callback) {
        super();
        _activity = activity;
        _callback = callback;
        _myip = Utils.getIPAddress(true);
    }

    public void search4Popcorns() {

        if(!Utils.isWIFIConnected(_activity)){
            Utils.Toast(_activity, R.string.noconnection, Toast.LENGTH_SHORT);
            return;
        }

        // set local ip search based on the mobile ip assigned
        String localip = "http://192.168." +
                _myip.split("\\.")[2]
                +".";

        for(int i = 0; i<256; i++) {
            String ip = localip + String.valueOf(i);
            String socketip = ip + ":"+port;
            if(ip.equals(_myip)) continue;
            if(connectedSockets.get(socket)!=null) continue;

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

            socket.on("my name is", new Emitter.Listener(){
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
                        sockectConnected(socketes.get(socket), socket, finalLocalname);
                        _activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _callback.onPopcornFound(getConnectdSocketsName());
                            }
                        });
                    }
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

            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i(TAG_NAME, "socket disconnected");
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

        socket.on("show movie detail", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject json = (JSONObject) args[0];
                try {
                    movie = new Movie();
                    movie.setPoster(json.getString("poster"));
                    movie.setTitle(json.getString("title"));
                    movie.setDesc(json.getString("desc"));
                    _activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _callback.onMovieDetail(movie);
                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        socket.on("show control", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                _activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _callback.onControlRequest();
                    }
                });
            }
        });

        socket.on("show downloading", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject json = (JSONObject) args[0];
                try {
                    movie = new Movie();
                    movie.setPoster(json.getString("poster"));
                    movie.setTitle(json.getString("title"));
                    movie.setDesc(json.getString("desc"));
                    _activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _callback.onDownloading(movie);
                        }
                    });
                }catch(Exception e){}
            }
        });

        socket.on("show playing", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject json = (JSONObject) args[0];
                try {
                    movie = new Movie();
                    movie.setPoster(json.getString("poster"));
                    movie.setTitle(json.getString("title"));
                    movie.setDesc(json.getString("desc"));
                    _activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _callback.onPlaying(movie);
                        }
                    });
                }catch(Exception e){}
            }
        });
    }

    public boolean selectPopcornApp(String localname) {

        String socketip = connectedSocketsName.get(localname);
        socket = connectedSockets.get(socketip);
        if(socket == null) return false;

        return true;
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

    /**
     * remote control events to emit
     */

    public void play() {
        socket.emit("play");
    }

    public void pause() {
        socket.emit("pause");
    }

    public void fullscreen() {
        socket.emit("fullscreen");
    }

    public void mute() {
        socket.emit("mute");
    }

    public void escape() {
        socket.emit("press esc");
    }

    public void moveRight() {
        socket.emit("move right");
    }

    public void moveLeft() {
        socket.emit("move left");
    }

    public void moveUp() {
        socket.emit("move up");
    }

    public void moveDown() {
        socket.emit("move down");
    }

    public void enter() {
        socket.emit("press enter");
    }

    public void startStreaming() {
        socket.emit("start streaming");
    }

    public void cancelStreaming() {
        socket.emit("cancel streaming");
    }

    public void volumeUp() {
        socket.emit("volume up");
    }

    public void volumeDown() {
        socket.emit("volume down");
    }
}
