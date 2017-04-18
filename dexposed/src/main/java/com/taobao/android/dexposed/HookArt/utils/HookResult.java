package com.taobao.android.dexposed.HookArt.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

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
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        errormsg.printStackTrace(printWriter);
        Throwable cause = errormsg.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.flush();
        printWriter.close();
        this.errormsg = writer.toString();
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
