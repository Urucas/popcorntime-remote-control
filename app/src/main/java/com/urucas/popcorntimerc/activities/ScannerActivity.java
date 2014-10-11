package com.urucas.popcorntimerc.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;
import com.urucas.popcorntimerc.PopcornApplication;
import com.urucas.popcorntimerc.R;
import com.urucas.popcorntimerc.utils.Utils;

import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Urucas on 10/11/14.
 */
public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private static final String TAG = "Scanner Activity";
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        try {
            JSONObject data = new JSONObject(rawResult.getText());
            PopcornApplication.setSetting(PopcornApplication.PT_HOST, data.getString("ip").trim());
            PopcornApplication.setSetting(PopcornApplication.PT_PORT, data.getString("port").trim());
            PopcornApplication.setSetting(PopcornApplication.PT_USER, data.getString("user").trim());
            PopcornApplication.setSetting(PopcornApplication.PT_PASS, data.getString("pass").trim());
            setResult(RESULT_OK);

        }catch(Exception e){
            Utils.Toast(ScannerActivity.this, R.string.errorscanning);
            setResult(RESULT_CANCELED);

        }finally {
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }
}
