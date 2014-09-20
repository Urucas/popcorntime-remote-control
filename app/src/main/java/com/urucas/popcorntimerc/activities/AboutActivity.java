package com.urucas.popcorntimerc.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.utils.Utils;

/**
 * Created by Urucas on 9/20/14.
 */
public class AboutActivity extends ActionBarActivity {

    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        try {

            actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("");
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.back_bar);

            TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.title);
            title.setText(R.string.about);

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

        Button shareBtt = (Button) findViewById(R.id.shareBtt);
        shareBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        Button rateBtt = (Button) findViewById(R.id.rateBtt);
        rateBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate();
            }
        });

        Button contributeBtt = (Button) findViewById(R.id.contributeBtt);
        contributeBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contribute();
            }
        });

        Button feedbackBtt = (Button) findViewById(R.id.feedbackBtt);
        feedbackBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback();
            }
        });
    }

    private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.share_body));

        Intent openInChooser = Intent.createChooser(sharingIntent, getResources().getString(R.string.share_via));
        startActivity(openInChooser);
    }

    private void rate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.urucas.ptremotecontrol"));
        startActivity(intent);
    }

    private void contribute() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Urucas/popcorntime-remote-control/tree/json-rpc"));
        startActivity(intent);
    }

    private void feedback() {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");

        String email = "hello@urucas.com";

        String subject = getResources().getString(R.string.email_subject);
        String body = getResources().getString(R.string.email_body);

        String uriText = "mailto:" + Uri.encode(email) +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(body);
        Uri uri = Uri.parse(uriText);

        intent.setData(uri);

        String intentTitle = getResources().getString(R.string.email_send);

        startActivity(Intent.createChooser(intent, intentTitle));
    }
}
