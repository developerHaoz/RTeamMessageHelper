package com.hans.alpha.android;

/**
 * the logger
 * Created by changelcai on 2016/1/22.
 * <p>建议上层统一使用此工具统一管理</p>
 */
public final class Log {

    /**
     * isPrint: print switch, true will print. false not print
     */
    public static boolean isPrint = true;
    private static String TAG = "MotherShip.Log";

    private Log() {}

    public static void setTag(String tag) {
        TAG = tag;
    }

    public static int i(Object o) {
        return isPrint && o != null ? android.util.Log.i(TAG, o.toString()) : -1;
    }

    public static int i(String m) {
        return isPrint && m != null ? android.util.Log.i(TAG, m) : -1;
    }

    /**
     * the custom Log
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param title The title you would like logged.
     * @param contents The content you would like logged.
     * @return
     * <p>建议使用此方法</p>
     */
    public static int v(String tag, String title, Object... contents) {
        return isPrint && title != null && contents != null? android.util.Log.v(tag, String.format(title,contents)) : -1;
    }

    public static int d(String tag, String title, Object... contents) {
        return isPrint && title != null && contents != null? android.util.Log.d(tag, String.format(title, contents)) : -1;
    }

    public static int i(String tag, String title, Object... contents) {
        return isPrint && title != null && contents != null? android.util.Log.i(tag, String.format(title, contents)) : -1;
    }

    public static int w(String tag, String title, Object... contents) {
        return isPrint && title != null && contents != null? android.util.Log.w(tag, String.format(title, contents)) : -1;
    }

    public static int e(String tag, String title, Object... contents) {
        return isPrint && title != null && contents != null? android.util.Log.e(tag, String.format(title, contents)) : -1;
    }

    /**
     * the system common log
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     * @return
     */
    public static int v(String tag, String msg) {
        return isPrint && msg != null ? android.util.Log.v(tag, msg) : -1;
    }

    public static int d(String tag, String msg) {
        return isPrint && msg != null ? android.util.Log.d(tag, msg) : -1;
    }

    public static int i(String tag, String msg) {
        return isPrint && msg != null ? android.util.Log.i(tag, msg) : -1;
    }

    public static int w(String tag, String msg) {
        return isPrint && msg != null ? android.util.Log.w(tag, msg) : -1;
    }

    public static int e(String tag, String msg) {
        return isPrint && msg != null ? android.util.Log.e(tag, msg) : -1;
    }

    /**
     * the system common log with object list
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     * @return
     */
    public static int v(String tag, Object... msg) {
        return isPrint ? android.util.Log.v(tag, getLogMessage(msg)) : -1;
    }

    public static int d(String tag, Object... msg) {
        return isPrint ? android.util.Log.d(tag, getLogMessage(msg)) : -1;
    }

    public static int i(String tag, Object... msg) {
        return isPrint ? android.util.Log.i(tag, getLogMessage(msg)) : -1;
    }

    public static int w(String tag, Object... msg) {
        return isPrint ? android.util.Log.w(tag, getLogMessage(msg)) : -1;
    }

    public static int e(String tag, Object... msg) {
        return isPrint ? android.util.Log.e(tag, getLogMessage(msg)) : -1;
    }

    private static String getLogMessage(Object... msg) {
        if (msg != null && msg.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object s : msg) {
                if (msg != null && s != null) {
                    sb.append(s.toString());
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * the system common log with Throwable
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     * @return
     */
    public static int v(String tag, String msg, Throwable tr) {
        return isPrint && msg != null ? android.util.Log.v(tag, msg, tr) : -1;
    }

    public static int d(String tag, String msg, Throwable tr) {
        return isPrint && msg != null ? android.util.Log.d(tag, msg, tr) : -1;
    }

    public static int i(String tag, String msg, Throwable tr) {
        return isPrint && msg != null ? android.util.Log.i(tag, msg, tr) : -1;
    }

    public static int w(String tag, String msg, Throwable tr) {
        return isPrint && msg != null ? android.util.Log.w(tag, msg, tr) : -1;
    }

    public static int e(String tag, String msg, Throwable tr) {
        return isPrint && msg != null ? android.util.Log.e(tag, msg, tr) : -1;
    }

    /**
     * the system common log with TAG use Object Tag
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     * @return
     */
    public static int v(Object tag, String msg) {
        return isPrint ? android.util.Log.v(tag.getClass().getSimpleName(), msg) : -1;
    }

    public static int d(Object tag, String msg) {
        return isPrint ? android.util.Log.d(tag.getClass().getSimpleName(), msg) : -1;
    }

    public static int i(Object tag, String msg) {
        return isPrint ? android.util.Log.i(tag.getClass().getSimpleName(), msg) : -1;
    }

    public static int w(Object tag, String msg) {
        return isPrint ? android.util.Log.w(tag.getClass().getSimpleName(), msg) : -1;
    }

    public static int e(Object tag, String msg) {
        return isPrint ? android.util.Log.e(tag.getClass().getSimpleName(), msg) : -1;
    }

}

