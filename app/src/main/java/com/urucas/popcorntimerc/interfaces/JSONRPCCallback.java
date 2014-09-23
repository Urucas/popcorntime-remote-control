package com.urucas.popcorntimerc.interfaces;

import org.json.JSONObject;

/**
 * Created by Urucas on 9/20/14.
 */
public interface JSONRPCCallback {

    public void onSuccess(JSONObject jsonObject);
    public void onSuccess();
    public void onError(int error); // int resource of the error message
}
