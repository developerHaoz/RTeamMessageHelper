package com.hans.alpha.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hans.alpha.android.Log;

/**
 * Created by changelcai on 2016/4/3.
 */
public class NetworkReceiver extends BroadcastReceiver {

    private static final String TAG = "MotherShip.NetworkReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Log.d(TAG, "网络状态已经改变");
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                String name = info.getTypeName();
                Log.d(TAG, "当前网络名称：" + name);
                listener.onNetworkStateChanged(name);
            } else {
                Log.d(TAG, "没有可用网络");
                listener.onNetworkStateChanged("");
                //doSomething()
            }
        }
    }

    NetworkListener listener;

    public void registerReceiver(Context context, NetworkListener listener) {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.PHONE_STATE");
            filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
            filter.setPriority(Integer.MAX_VALUE);
            context.registerReceiver(this, filter);
            this.listener = listener;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unRegisterReceiver(Context context) {
        try {
            context.unregisterReceiver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static interface NetworkListener {
        void onNetworkStateChanged(String name);
    }
}
