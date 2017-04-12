package com.taobao.android.dexposed;

/**
 * 作者：zhangzhongping on 17/4/11 02:47
 * 邮箱：android_dy@163.com
 */
public class HookResult {

    private String type;

    private String errormsg = "NULL";

    private boolean hookSuccess = false;

    private XC_MethodHook.Unhook unhook;

    public void setType(String type) {
        this.type = type;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public void setHookSuccess(boolean hookSuccess) {
        this.hookSuccess = hookSuccess;
    }

    public void setUnhook(XC_MethodHook.Unhook unhook) {
        this.unhook = unhook;
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

    public XC_MethodHook.Unhook getUnhook() {
        return unhook;
    }
}
