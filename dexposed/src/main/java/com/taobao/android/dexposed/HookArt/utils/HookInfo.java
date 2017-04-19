package com.taobao.android.dexposed.HookArt.utils;


import android.util.Log;

import com.taobao.android.dexposed.HookArt.Dexposed_Art;

/**
 * 作者：zhangzhongping on 17/4/10 23:29
 * 邮箱：android_dy@163.com
 */
public class HookInfo {

    private boolean isSupport = false;

    private Dexposed_Art hook;

    private String Model;

    private String errorMsg = "Null";

    public void setErrorMsg(Throwable errorMsg) {
        this.errorMsg = Log.getStackTraceString(errorMsg);
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public boolean isSupport() {
        return isSupport;
    }

    public void setSupport(boolean support) {
        isSupport = support;
    }

    public void setHook(Dexposed_Art hook) {
        this.hook = hook;
    }

    public Dexposed_Art getHook() {
        return hook;
    }
}
