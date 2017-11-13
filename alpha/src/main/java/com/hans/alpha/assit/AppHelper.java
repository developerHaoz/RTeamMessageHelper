package com.hans.alpha.assit;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;

import com.hans.alpha.android.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * create by changelcai on 2015/1/23
 * <p/>
 * change by hans on 2016/7/20 添加获取版本名字的方法
 */
public class AppHelper {

    private static final String TAG = "MotherShip.AppHelper";

    /**
     * 调用系统分享
     */
    public static void shareToOtherApp(Context context, String title, String content, String dialogTitle) {
        Log.d(TAG, "[shareToOtherApp]");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, dialogTitle));
    }

    /**
     * 调用系统分享,默认只过滤掉蓝牙。
     */
    public static void shareToOtherAppByFilter(Context context, String title, String content, String dialogTitle, String[] filters) {
        Log.d(TAG, "[shareToOtherAppByFilter]");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        if (!resInfo.isEmpty()) {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            for (ResolveInfo info : resInfo) {
                Intent targeted = new Intent(Intent.ACTION_SEND);
                targeted.setType("text/plain");
                ActivityInfo activityInfo = info.activityInfo;

                boolean bool = false;
                // activityInfo.packageName, activityInfo.name, etc.
                for (String str : filters) {
                    if (activityInfo.packageName.contains(str) || activityInfo.name.contains(str)) {
                        bool = true;
                        break;
                    }
                }
                if (bool) {
                    continue;
                }
                targeted.setPackage(activityInfo.packageName);
                targeted.putExtra(Intent.EXTRA_SUBJECT, title);
                targeted.putExtra(Intent.EXTRA_TEXT, content);
                targeted.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                targetedShareIntents.add(targeted);
            }
            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), dialogTitle);
            if (chooserIntent == null) {
                return;
            }
            // A Parcelable[] of Intent or LabeledIntent objects as set with
            // putExtra(String, Parcelable[]) of additional activities to place
            // a the front of the list of choices, when shown to the user with a
            // ACTION_CHOOSER.
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

            try {
                context.startActivity(chooserIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Log.d(TAG, "Can't find share component to share");
            }
        }
    }


    /**
     * need < uses-permission android:name =“android.permission.GET_TASKS” />
     * 判断是否前台运行
     */
    public static boolean isRunningForeground(Context context) {
        Log.d(TAG, "[isRunningForeground]");
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName componentName = taskList.get(0).topActivity;
            if (componentName != null && componentName.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取App包信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        Log.d(TAG, "[getPackageInfo]");
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    /**
     * 获取App的版本号
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi;
                try {
                    pi = pm.getPackageInfo(context.getPackageName(), 0);
                    if (pi != null) {
                        return pi.versionCode;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /**
     * 获取App的版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = packageManager.getPackageInfo(context.getPackageName(),
                    0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pi != null)
            return pi.versionName;
        return "";
    }

}
