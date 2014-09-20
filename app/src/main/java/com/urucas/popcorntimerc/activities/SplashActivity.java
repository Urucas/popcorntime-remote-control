package com.urucas.popcorntimerc.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.urucas.popcorntimerc.PopcornApplication;
import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.fragments.ControlFragment;
import com.urucas.popcorntimerc.fragments.LeftMenuFragment;
import com.urucas.popcorntimerc.socket.RemoteControl;
import com.urucas.popcorntimerc.utils.Utils;


public class SplashActivity extends SlidingFragmentActivity{

    private static final String TAG_NAME = "SplashActivity";

    private static RemoteControl remote;
    private ControlFragment controlFragment;
    private SlidingMenu sm;
    private LeftMenuFragment leftMenuFragment;
    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        setBehindContentView(R.layout.frame_leftmenu);

        sm = getSlidingMenu();
        sm.setMode(SlidingMenu.LEFT);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        setSlidingActionBarEnabled(false);

        leftMenuFragment = new LeftMenuFragment();
        leftMenuFragment.setActivity(SplashActivity.this);

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.frame_leftmenu, leftMenuFragment)
            .commit();

        try {

            actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("");
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.main_bar);

            ImageButton menuBtt = (ImageButton) actionBar.getCustomView().findViewById(R.id.menuBtt);
            menuBtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle();
                }
            });

        }catch(Exception e) {
            Log.d(TAG_NAME, e.getMessage());
            e.printStackTrace();
        }

        remote = new RemoteControl(
                SplashActivity.this,
                PopcornApplication.getSetting(PopcornApplication.PT_IP),
                PopcornApplication.getSetting(PopcornApplication.PT_PORT),
                PopcornApplication.getSetting(PopcornApplication.PT_USER),
                PopcornApplication.getSetting(PopcornApplication.PT_PASS)
        );
        showControl();
    }

    public static RemoteControl getRemoteControl() {
        return remote;
    }

    public void showControl() {

        controlFragment = new ControlFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, controlFragment)
                .commit();

    }

    public void connectionError() {
        Utils.Toast(SplashActivity.this, "Error conecting to Popcorn app");
    }

}
