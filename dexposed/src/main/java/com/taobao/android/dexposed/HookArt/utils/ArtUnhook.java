package com.taobao.android.dexposed.HookArt.utils;

import com.taobao.android.dexposed.XC_MethodHook;
import com.taobao.android.dexposed.callbacks.IXUnhook;
import java.lang.reflect.Method;

/**
 * 作者：zhangzhongping on 17/4/27 01:31
 * 邮箱：android_dy@163.com
 */
public class ArtUnhook implements IXUnhook {

    private Class<?> hook = null;

    public ArtUnhook(Class hook) {
        this.hook = hook;
    }

    @Override
    public void unhook() {
        try {
            Method methodism = hook.getDeclaredMethod("setXC_MethodHook", XC_MethodHook.class);
            if (methodism != null) {
                methodism.setAccessible(true);
                methodism.invoke(null, new Object[]{null});
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
