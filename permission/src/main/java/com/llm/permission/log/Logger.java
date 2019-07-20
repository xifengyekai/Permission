package com.llm.permission.log;

import android.util.Log;

import com.llm.permission.BuildConfig;

import java.util.Locale;

/**
 * 日志工具
 *
 * @author v_luoliming01
 * @date 2019-06-27 14:30
 */
public final class Logger {
    /**
     * 日志默认tag
     */
    private static final String TAG = "PermissionLibrary";
    /**
     * 日志tag格式化字符窜
     */
    private static final String FORMAT_TAG = "%s->->->%s";
    /**
     * 日志开关
     * 默认与打包相关，debug包开启，release包关闭
     */
    private static boolean sDebug = BuildConfig.DEBUG;

    /**
     * 设置是否开启debug模式
     *
     * @param debug
     */
    public static void setDebug(boolean debug) {
        Logger.sDebug = debug;
    }


    public static void v(String message) {
        v(null, message);
    }

    public static void v(String tag, String message) {
        if (tag == null) {
            tag = "";
        }
        if (sDebug) {
            Log.v(String.format(Locale.CHINESE, FORMAT_TAG, TAG, tag), message);
        }
    }

    public static void i(String message) {
        i(null, message);
    }

    public static void i(String tag, String message) {
        if (tag == null) {
            tag = "";
        }
        if (sDebug) {
            Log.i(String.format(Locale.CHINESE, FORMAT_TAG, TAG, tag), message);
        }
    }

    public static void d(String message) {
        d(null, message);
    }

    public static void d(String tag, String message) {
        if (tag == null) {
            tag = "";
        }
        if (sDebug) {
            Log.d(String.format(Locale.CHINESE, FORMAT_TAG, TAG, tag), message);
        }
    }

    public static void w(String message) {
        w(null, message);
    }

    public static void w(String tag, String message) {
        if (tag == null) {
            tag = "";
        }
        if (sDebug) {
            Log.w(String.format(Locale.CHINESE, FORMAT_TAG, TAG, tag), message);
        }
    }

    public static void e(String message) {
        e(null, message);
    }

    public static void e(String tag, String message) {
        if (tag == null) {
            tag = "";
        }
        if (sDebug) {
            Log.e(String.format(Locale.CHINESE, FORMAT_TAG, TAG, tag), message);
        }
    }
}
