package com.taobao.android.dexposed.HookArt.utils;

import android.util.Log;

/**
 * 作者：zhangzhongping on 17/4/14 19:13
 * 邮箱：android_dy@163.com
 */
public class HookLog {

    public static boolean isShow = true;

    public static String TAG = "NewHooks";

    public static void e(String msg) {
        if (isShow) {
            Log.e(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (isShow) {
            Log.d(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (isShow) {
            Log.w(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (isShow) {
            Log.i(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (isShow) {
            Log.v(TAG, msg);
        }
    }
}
