package com.hans.alpha.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * create by changelcai on 2016/1/26
 * Handler工具类
 * <P>撸男，切忌这个Handler的Looper是主线程的，别乱来！！！</P>
 */
public class HandlerUtil {
    public static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable){
        HANDLER.post(runnable);
    }

    public static void runOnUiThreadDelay(Runnable runnable, long delayMillis){
        HANDLER.postDelayed(runnable,delayMillis);
    }

    /**
     * 没用时记得remove掉
     * @param runnable
     */
    public static void removeRunable(Runnable runnable){
        HANDLER.removeCallbacks(runnable);
    }

    public static boolean isMainThread(){
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
