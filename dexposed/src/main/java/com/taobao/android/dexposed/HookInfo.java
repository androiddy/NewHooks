package com.taobao.android.dexposed;

import com.taobao.android.dexposed.Hook22_23.Dexposed22_23;

/**
 * 作者：zhangzhongping on 17/4/10 23:29
 * 邮箱：android_dy@163.com
 */
public class HookInfo {

    private boolean isSupport;

    private Dexposed22_23 hook;

    private String Model;

    private String errorMsg;

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

    public void setHook(Dexposed22_23 hook) {
        this.hook = hook;
    }


    public Dexposed22_23 getHook() {
        return hook;
    }
}
