package com.urucas.popcorntimerc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.urucas.popcorntimerc.PopcornApplication;
import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.activities.SplashActivity;

import java.util.ArrayList;

/**
 * Created by Urucas on 8/20/14.
 */
public class LeftMenuFragment extends android.support.v4.app.Fragment {

    private View view;

    private SplashActivity _activity;
    private Spinner popcornAppsSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_leftmenu, container, false);

        EditText ptPort = (EditText) view.findViewById(R.id.ptPort);
        ptPort.setText(PopcornApplication.getSetting(PopcornApplication.PT_PORT));

        EditText ptUser = (EditText) view.findViewById(R.id.ptUser);
        ptUser.setText(PopcornApplication.getSetting(PopcornApplication.PT_USER));

        EditText ptPass = (EditText) view.findViewById(R.id.ptPass);
        ptPass.setText(PopcornApplication.getSetting(PopcornApplication.PT_PASS));

        return view;
    }

    public void setActivity(SplashActivity activity) {
        _activity = activity;
    }

}
