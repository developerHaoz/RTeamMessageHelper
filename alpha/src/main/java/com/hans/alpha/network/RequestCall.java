package com.hans.alpha.network;

import com.hans.alpha.android.Log;
import com.hans.alpha.network.callback.SceneCallback;
import com.hans.alpha.network.core.OkHttpCore;
import com.hans.alpha.network.request.OkHttpRequest;
import com.hans.alpha.utils.HandlerUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * create by changelcai on 2016/03/17
 * 请求网络对象
 * <p/>
 * Changed by Hans on 2016/7/29
 * 1.修复doScene中onResponse直接不是isSuccessful为false时候直接回调,没有判断当前ThreadMode
 * 而导致外部在调用的时候onError方法是在子线程而非UI线程导致的崩溃问题(Noted by YZW)
 * 2.将原本doSceneSync改成public
 * 3.删除doSceneSyn和doScene的原本设计思路;主要改变doScene的实现方式,直接将网络结果返回;以适应异步框架链式调用或者多个接口合并请求的请假
 * <p/>
 * Changed by Hans on 2016/8/13
 * 修改服了doSceneSyn在失败之后,没有抛出IOException的BUG
 */
public class RequestCall {

    private static final String TAG = "MotherShip.RequestCall";

    private OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;

    public RequestCall(OkHttpRequest request) {
        this.okHttpRequest = request;
    }

    private Call buildCall(SceneCallback callback) {
        buildRequest(callback);
        call = OkHttpCore.INSTANCE().getClient().newCall(request);
        return call;
    }

    private Request buildRequest(SceneCallback callback) {
        request = okHttpRequest.buildRequest(callback);
        return request;
    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public OkHttpRequest getOkHttpRequest() {
        return okHttpRequest;
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }


    /**
     * async,Anyway,the callback will be called in the other thread,not mainThread
     * 撸男，这里一定要注意上面的提示
     *
     * @param callback
     */
    public void doScene(final SceneCallback callback) {
        if (null == callback) {
            Log.e(TAG, "callback is null");
            return;
        }

        buildCall(callback);
        final Callback interCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "doScene onFailure:%s", e.getCause());
                deliverError(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "response.is not Successful code:%d thread:@%s", response.code(), Thread.currentThread().getId());
                    response.body().close();
                    //修复了 Response失败之后,没有判断当前ThreadMode的情况,而导致在子线程回调;从而导致外部调用时候,UI操作而奔溃的问题 Noted By YZW
                    deliverError(call, new IOException("response.is not Successful thread@" + Thread.currentThread().getId()));
                } else {
                    Log.d(TAG, "doScene is successful!!!");
                    final Object obj = callback.parseNetworkResponse(response);
                    response.body().close();
                    switch (callback.getHandleMode()) {
                        case SceneCallback.ThreadMode.MAIN_THREAD:
                            HandlerUtil.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onResponse(obj);
                                }
                            });
                            break;

                        case SceneCallback.ThreadMode.NORMAL_THREAD:
                            callback.onResponse(obj);
                            break;
                    }
                }
            }

            /**
             * 传递Error的方法 Added by Hans 2016/7/29
             * @param call
             * @param e
             */
            private void deliverError(final Call call, final Exception e) {
                switch (callback.getHandleMode()) {
                    case SceneCallback.ThreadMode.MAIN_THREAD:
                        HandlerUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onError(call, e);
                            }
                        });
                        break;

                    case SceneCallback.ThreadMode.NORMAL_THREAD:
                        callback.onError(call, e);
                        break;
                }
            }
        };
        if (null == call) {
            Log.e(TAG, "doSceneImpl err:%s", "call is null");
            throw new NullPointerException("call is null!");
        }
        call.enqueue(interCallback);
    }


    /**
     * sync,the callback will be called in the thread which invoke it
     * do not invoke it in the main Thread
     * 撸男，这里一定要注意上面的提示
     * <p/>
     * Changed by Hans 2016/7/29 删除原本的设计思路,同步执行直接返回执行结果;用来适应异步框架的调用
     */
    public Response doSceneSync() throws IOException {
        buildCall(null);
        if (null == call) {
            Log.e(TAG, "callback is null");
            return null;
        }
        Response response = call.execute();
        if (!response.isSuccessful()) {
            Log.d(TAG, "response.is not Successful code:%d thread:@%s", response.code(), Thread.currentThread().getId());
            response.body().close();
            throw new IOException("response.is not Successful thread@" + Thread.currentThread().getId());
        } else {
            Log.d(TAG, "doScene is successful!!!");
        }
        return response;
    }
}
