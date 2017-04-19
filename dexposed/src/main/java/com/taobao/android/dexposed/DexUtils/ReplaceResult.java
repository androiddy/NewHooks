package com.taobao.android.dexposed.DexUtils;

import android.util.Log;

/**
 * 作者：zhangzhongping on 17/4/17 19:27
 * 邮箱：android_dy@163.com
 */
public class ReplaceResult {

    private String errorMsg = "Success";

    private boolean isSuccess = false;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setErrorMsg(Throwable errormsg) {
        this.errorMsg = Log.getStackTraceString(errormsg);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
