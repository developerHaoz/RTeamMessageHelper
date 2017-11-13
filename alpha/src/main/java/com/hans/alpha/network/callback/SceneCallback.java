package com.hans.alpha.network.callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by changelcai on 2016/3/16.
 */
public abstract class SceneCallback<T> {

    public interface ThreadMode{
        int MAIN_THREAD = 1;
        int NORMAL_THREAD = 0;
    }

    public abstract T parseNetworkResponse(Response response) throws IOException;

    public void inProgress(float progress) {
    }

    public abstract int getHandleMode();

    public abstract void onError(Call call, Exception e);

    public abstract void onResponse(T response);

}
