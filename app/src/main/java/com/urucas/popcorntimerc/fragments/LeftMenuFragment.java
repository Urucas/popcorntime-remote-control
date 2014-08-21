package com.urucas.popcorntimerc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

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

        popcornAppsSpinner = (Spinner) view.findViewById(R.id.popcornAppsSpinner);
        popcornAppsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _activity.selectPopcorn((String) parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        refreshSpinner(new ArrayList<String>());

        return view;
    }

    public void setActivity(SplashActivity activity) {
        _activity = activity;
    }

    public void refreshSpinner(ArrayList<String> popcornList) {
        if(popcornList.size() == 0){
            popcornList.add(getResources().getString(R.string.nopopcorns));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, popcornList);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        popcornAppsSpinner.setAdapter(spinnerArrayAdapter);
    }
}
