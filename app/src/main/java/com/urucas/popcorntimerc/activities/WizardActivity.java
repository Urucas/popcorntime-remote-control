package com.urucas.popcorntimerc.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.urucas.popcorntimerc.R;

/**
 * Created by Urucas on 9/21/14.
 */
public class WizardActivity extends ActionBarActivity {

    private android.support.v7.app.ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        try {

            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("");
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.back_bar);

            TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.title);
            title.setText(R.string.help);

            ImageButton backBtt = (ImageButton) actionBar.getCustomView().findViewById(R.id.backBtt);
            backBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }catch(Exception e) {
            e.printStackTrace();
        }

    }

}
