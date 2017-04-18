package com.taobao.android.dexposed.HookArt.utils;


import com.taobao.android.dexposed.HookArt.Dexposed_Art;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 作者：zhangzhongping on 17/4/10 23:29
 * 邮箱：android_dy@163.com
 */
public class HookInfo {

    private boolean isSupport;

    private Dexposed_Art hook;

    private String Model;

    private String errorMsg;

    public void setErrorMsg(Throwable errorMsg) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        errorMsg.printStackTrace(printWriter);
        Throwable cause = errorMsg.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.flush();
        printWriter.close();
        this.errorMsg = writer.toString();
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
