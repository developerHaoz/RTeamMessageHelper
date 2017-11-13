package com.hans.alpha.assit;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

import com.hans.alpha.android.Log;

/**
 * 粘贴板助手类
 * create by changelcai on 2015/1/23
 */
public class ClipboardHelper {

    private static final String TAG = "MotherShip.ClipboardHelper";

    public static void copyToClipboardSupport(Context context, String text) {
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(text);
        Log.d(TAG,"[copyToClipboardSupport:%s]",text);
    }

    public static CharSequence getLatestTextSupport(Context context) {
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        CharSequence cs = clipboard.getText();
        Log.d(TAG,"[getLatestTextSupport]");
        return cs;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(null, text));
        Log.d(TAG,"[copyToClipboardSupport:%s]",text);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static int getItemCount(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = clipboard.getPrimaryClip();
        int count = data.getItemCount();
        Log.d(TAG,"[getItemCount:%d]",count);
        return count;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static String getText(Context context, int index) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > index) {
            String str = String.valueOf(clip.getItemAt(0).coerceToText(context));
            Log.d(TAG,"[getText]index:%d,text:%s",index,str);
            return str;
        }
        Log.d(TAG,"[getText]index:%d,text is null",index);
        return null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static String getLatestText(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            String str = String.valueOf(clip.getItemAt(0).coerceToText(context));
            return str;
        }
        Log.d(TAG,"[getLatestText]text is null");
        return null;
    }
}
