package com.hans.alpha.network.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by changelcai on 2016/3/16.
 */
public abstract class StringCallback extends SceneCallback<String> {

    public final static String TAG = "MotherShip.StringCallback";

    @Override
    public String parseNetworkResponse(Response response) throws IOException {
//        Log.d(TAG,"parseNetworkResponse:%s",response.toString());
        return response.body().string();
    }

}
