package com.taobao.android.dexposed.Hook22_23;

import com.taobao.android.dexposed.DexposedBridge;
import com.taobao.android.dexposed.HookUtils;

import java.lang.reflect.Method;

/**
 * 作者：zhangzhongping on 17/4/10 23:28
 * 邮箱：android_dy@163.com
 */
public class Dexposed22_23 {

    private static final String TAG = "Dexposed22_23";

    public Dexposed22_23() {

    }

    public boolean findAndHookMethod(ClassLoader originClassLoader,String ... strings) {
        try {
            Class<?> hookItem = Class.forName(strings[strings.length-1]);
            if (strings[0] == null || strings[0].equals("")) {
                return false;
            }
            Class<?> clazz = Class.forName(strings[0], true, originClassLoader);
            Method hook = null;
            Method backup = null;
            for (Method method : hookItem.getDeclaredMethods()) {
                if (method.getName().equals("HookMethod")) {
                    hook = method;
                } else if (method.getName().equals("OriginalHookMethod")) {
                    backup = method;
                }
            }
            if (hook == null) {
                return false;
            }
            DexposedBridge.findAndBackupAndHook(clazz, strings[1], strings[2], hook, backup);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }



}
