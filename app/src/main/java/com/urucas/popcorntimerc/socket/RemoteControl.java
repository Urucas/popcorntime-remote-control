package com.urucas.popcorntimerc.socket;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.ConnectionConfigurator;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.activities.SplashActivity;
import com.urucas.popcorntimerc.interfaces.JSONRPCCallback;
import com.urucas.popcorntimerc.interfaces.RemoteControlCallback;
import com.urucas.popcorntimerc.model.Movie;
import com.urucas.popcorntimerc.utils.Utils;

import org.json.JSONObject;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Urucas on 8/20/14.
 */
public class RemoteControl {

    private static final String TAG_NAME = "RemoteControl";

    private static String _ip, _port, _user, _pass;

    private JSONRPC2Session _jsonRPCSession;
    private Activity _activity;

    public RemoteControl(Activity activity, String ip, String port, String user, String pass) {
        _activity = activity;
        _ip = ip;
        _port = port;
        _user = user;
        _pass = pass;
    }

    private JSONRPC2Session getJsonRPCSession() throws MalformedURLException {
        if(_jsonRPCSession == null) {
            _jsonRPCSession = new JSONRPC2Session(new URL("http://" + _ip + ":" + _port));

            BasicAuthenticator auth = new BasicAuthenticator();
            auth.setCredentials(_user, _pass);

            _jsonRPCSession.setConnectionConfigurator(auth);
        }
        return _jsonRPCSession;
    }

    private static int requestID = 1;

    private class JSONRPCTask extends AsyncTask<String, String, JSONRPC2Response> {

        private Map<String, Object> params;
        private JSONRPC2Session session;
        private JSONRPCCallback callback;

        public JSONRPCTask(JSONRPC2Session session, JSONRPCCallback callback) {
            this.session = session;
            this.callback = callback;
        }

        public JSONRPCTask(JSONRPC2Session session, Map<String, Object> params, JSONRPCCallback callback){
            this.session = session;
            this.params  = params;
            this.callback = callback;
        }

        @Override
        protected JSONRPC2Response doInBackground(String... params1) {
            if(params == null) {
                params = new HashMap<String, Object>();
            }
            String method = params1[0];

            session.getOptions().setRequestContentType("application/json");
            JSONRPC2Request request = new JSONRPC2Request(method, params, requestID);
            JSONRPC2Response response = null;

            try {
                response = session.send(request);

            } catch (JSONRPC2SessionException e) {
            }
            requestID++;
            return response;
        }

        @Override
        protected void onPostExecute(JSONRPC2Response response) {
            if(response == null || response.getError() != null) {
                callback.onError();
                return;
            }
            Log.i("a", response.getResult().toString());

            JSONObject object = (JSONObject) response.getResult();
            callback.onSuccess(object);
        }

    }

    private class BasicAuthenticator implements ConnectionConfigurator {

        private String user;
        private String pass;

        public void setCredentials(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }

        @Override
        public void configure(HttpURLConnection connection) {

            byte[] encodedBytes = Base64.encode((user + ":" + pass).getBytes(), Base64.DEFAULT);
            // add custom HTTP header
            connection.addRequestProperty("Authorization", "Basic "+ new String(encodedBytes));
        }
    }

    /**
     * remote control events to emit
     */

    private void emit(String event){
        try {
            new JSONRPCTask(getJsonRPCSession(), new JSONRPCCallback(){

                @Override
                public void onSuccess(JSONObject jsonObject) {

                }

                @Override
                public void onError() {

                    _activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((SplashActivity)_activity).connectionError();
                        }
                    });
                }

            }).execute(event);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void toggleplay() {
        this.emit("toggleplaying");
    }

    public void fullscreen() {
        // toggle fullscreen only works on player view
        this.emit("togglefullscreen");
    }

    public void mute() {
        this.emit("togglemute");
    }

    public void escape() {
        this.emit("back");
    }

    public void moveRight() {
        this.emit("right");
    }

    public void moveLeft() {
        this.emit("left");
    }

    public void moveUp() {
        this.emit("up");
    }

    public void moveDown() {
        this.emit("down");
    }

    public void enter() {
        this.emit("enter");
    }

    public void showMoviesList() {
        this.emit("movieslist");
    }

    public void showSeriesList() {
        this.emit("showslist");
    }

    public void seasonUp() {
        this.emit("previousseason");
    }

    public void seasonDown() {
        this.emit("nextseason");
    }

    public void favourite() {
        this.emit("togglefavourite");
    }

    public void watched() {
        this.emit("togglewatched");
    }
}
