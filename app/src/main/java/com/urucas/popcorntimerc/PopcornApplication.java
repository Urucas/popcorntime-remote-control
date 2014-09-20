package com.urucas.popcorntimerc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.urucas.popcorntimerc.socket.RemoteControl;

/**
 * Created by Urucas on 8/18/14.
 */

public class PopcornApplication extends Application {


    private static PopcornApplication _instance;
    private static RemoteControl _remotecontrol;
    private static SharedPreferences _preferences;

    public static String PT_IP = "pt-ip";
    public static String PT_PORT = "pt-port";
    public static String PT_USER = "pt-username";
    public static String PT_PASS = "pt-password";

    private static String PT_IP_DEFAULT = "192.168.0.102";
    private static String PT_PORT_DEFAULT = "8008";
    private static String PT_USER_DEFAULT = "popcorn";
    private static String PT_PASS_DEFAULT = "popcorn";

    public PopcornApplication() {
        super();
        _instance = this;
    }

    private static SharedPreferences getPreferences() {
        if(_preferences == null) {
            _preferences = PreferenceManager.getDefaultSharedPreferences(_instance.getApplicationContext());
        }
        return _preferences;
    }

    public static String getSetting(String key) {

        SharedPreferences pref = PopcornApplication.getPreferences();
        if(key.equals(PT_IP)) {
            return pref.getString(PT_IP, PT_IP_DEFAULT);
        }
        if(key.equals(PT_PORT)) {
            return pref.getString(PT_PORT, PT_PORT_DEFAULT);
        }
        if(key.equals(PT_USER)) {
            return pref.getString(PT_USER, PT_USER_DEFAULT);
        }
        if(key.equals(PT_PASS)) {
            return pref.getString(PT_PASS, PT_PASS_DEFAULT);
        }
        return null;
    }

    public static void setSetting(String key, String value) {
        SharedPreferences pref = PopcornApplication.getPreferences();
        if(key.equals(PT_IP)) {
            pref.edit().putString(PT_IP, value).commit();
        }
        if(key.equals(PT_PORT)) {
            pref.edit().putString(PT_PORT, value).commit();
        }
        if(key.equals(PT_USER)) {
            pref.edit().putString(PT_USER, value).commit();
        }
        if(key.equals(PT_PASS)) {
            pref.edit().putString(PT_PASS, value).commit();
        }
    }

    public static PopcornApplication getInstance() {
        return _instance;
    }
}
