package com.taobao.android.dexposed.HookArt.utils;

import android.util.Log;


/**
 * 作者：zhangzhongping on 17/4/11 02:47
 * 邮箱：android_dy@163.com
 */
public class HookResult {

    private String type;

    private String errormsg = "Success";

    private boolean hookSuccess = false;

    public void setType(String type) {
        this.type = type;
    }

    public void setErrormsg(Throwable errormsg) {
        this.errormsg = Log.getStackTraceString(errormsg);
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public void setHookSuccess(boolean hookSuccess) {
        this.hookSuccess = hookSuccess;
    }

    public String getType() {
        return type;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public boolean isHookSuccess() {
        return hookSuccess;
    }

}
