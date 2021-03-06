package com.hans.alpha.network.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class BitmapCallback extends SceneCallback<Bitmap>
{
    @Override
    public Bitmap parseNetworkResponse(Response response) throws IOException
    {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }

}
