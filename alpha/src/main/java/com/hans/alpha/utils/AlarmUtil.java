package com.hans.alpha.utils;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.hans.alpha.android.Log;

/**
 * @author MaTianyu
 * @date 2015-08-22
 * modify by changelcai on 2016/1/23
 */
public class AlarmUtil {

    private static final String TAG = "MotherShip.AlarmUtil";

    /**
     * 开启定时器
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void startAlarmIntent(Context context, int triggerAtMillis, PendingIntent pendingIntent) {
        Log.d(TAG,"[startAlarmIntent]");
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP,triggerAtMillis, pendingIntent);
    }

    /**
     * 开启轮询定时器
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void startRepeatAlarmIntent(Context context, int triggerAtMillis, long intervalMillis, PendingIntent pendingIntent) {
        Log.d(TAG,"[startRepeatAlarmIntent]");
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,intervalMillis,triggerAtMillis, pendingIntent);
    }

    /**
     * 关闭定时器
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void stopAlarmIntent(Context context, PendingIntent pendingIntent) {
        Log.d(TAG,"[stopAlarmIntent]");
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }

    /**
     * 定时开启服务
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void startAlarmService(Context context, int triggerAtMillis, Class<?> cls, String action) {
        Log.d(TAG,"[startAlarmService]");
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        startAlarmIntent(context, triggerAtMillis,pendingIntent);
    }

    /**
     * 定时开启轮询服务
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void startRepeatAlarmService(Context context, int triggerAtMillis, long intervalMillis, Class<?> cls, String action) {
        Log.d(TAG,"[startRepeatAlarmService]");
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        startRepeatAlarmIntent(context, triggerAtMillis,intervalMillis,pendingIntent);
    }

    /**
     * 停止启服务
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void stopAlarmService(Context context, Class<?> cls, String action) {
        Log.d(TAG,"[stopAlarmService]");
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        stopAlarmIntent(context, pendingIntent);
    }

}
