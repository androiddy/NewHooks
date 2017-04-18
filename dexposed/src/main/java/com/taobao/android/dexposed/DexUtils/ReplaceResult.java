package com.taobao.android.dexposed.DexUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

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
        this.errorMsg = writer.toString();
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
