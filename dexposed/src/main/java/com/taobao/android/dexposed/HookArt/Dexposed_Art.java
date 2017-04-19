package com.taobao.android.dexposed.HookArt;


import com.taobao.android.dexposed.DexposedBridge;
import com.taobao.android.dexposed.HookArt.utils.HookLog;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 作者：zhangzhongping on 17/4/10 23:28
 * 邮箱：android_dy@163.com
 */
public class Dexposed_Art {

    public boolean findAndHookMethod(String[] hookname, Object... strings) {
        try {
            Class<?> hookItem = Class.forName((String) strings[strings.length - 1]);
            if (strings[0] == null || strings[0].equals("")) {
                return false;
            }
            Class<?> clazz = (Class<?>) strings[0];
            if (Modifier.isAbstract(clazz.getModifiers())) {
                HookLog.e("Hook may fail for abstract class: " + strings[0]);
            }
            Method hook = null;
            Method backup = null;
            for (Method method : hookItem.getDeclaredMethods()) {
                if (method.getName().equals(hookname[0])) {
                    hook = method;
                } else if (method.getName().equals(hookname[1])) {
                    backup = method;
                }
            }
            if (hook == null) {
                return false;
            }
            int andBackupAndHook = DexposedBridge.findAndBackupAndHook(clazz, (String) strings[1],
                    (String) strings[2], hook, backup);
            return andBackupAndHook == 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

}
