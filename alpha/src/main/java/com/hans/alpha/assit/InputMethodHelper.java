package com.hans.alpha.assit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hans.alpha.android.Log;

/**
 * @author MaTianyu(http://litesuits.com) on 2015-06-01
 * modify by changelcai on 2016/1/26
 * 软键盘相关工具类
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class InputMethodHelper {

    private static final String TAG = "MotherShip.InputMethodHelper";

    public static void toggleSoftInput(Context context) {
        Log.d(TAG,"[toggleSoftInput]");
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean showSoftInput(View view) {
        Log.d(TAG,"[showSoftInput]");
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static boolean showSoftInput(Activity activity) {
        Log.d(TAG,"[showSoftInput],activity:%s",activity.getComponentName());
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            return imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
        return false;
    }

    public static boolean hideSoftInput(View view) {
        Log.d(TAG,"[hideSoftInput]");
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean hideSoftInput(Activity activity) {
        Log.d(TAG,"[hideSoftInput],activity:%s",activity.getComponentName());
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
        return false;
    }

    public static boolean isActive(Context context) {
        Log.d(TAG,"[isActive]");
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean bool = imm.isActive();
        Log.d(TAG,"isActive:%s",bool);
        return bool;
    }

  /*  private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d(tag, "网络状态已经改变");
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if(info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    Log.d(tag, "当前网络名称：" + name);
                    //doSomething()
                } else {
                    Log.d(tag, "没有可用网络");
                    //doSomething()
                }
            }
        }
    };
*/
}
