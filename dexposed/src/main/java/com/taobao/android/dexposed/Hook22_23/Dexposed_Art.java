package com.taobao.android.dexposed.Hook22_23;


import com.taobao.android.dexposed.DexposedBridge;
import com.taobao.android.dexposed.Hook22_23.utils.HookLog;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 作者：zhangzhongping on 17/4/10 23:28
 * 邮箱：android_dy@163.com
 */
public class Dexposed_Art {

    private static final String TAG = "dexposed_art";

    public Dexposed_Art() {

    }

    public boolean findAndHookMethod(String[] hookname, ClassLoader originClassLoader, String... strings) {
        try {
            Class<?> hookItem = Class.forName(strings[strings.length - 1]);
            if (strings[0] == null || strings[0].equals("")) {
                HookLog.e(strings[0]);
                return false;
            }
            Class<?> clazz = Class.forName(strings[0], true, originClassLoader);
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
            int andBackupAndHook = DexposedBridge.findAndBackupAndHook(clazz, strings[1], strings[2], hook, backup);
            return andBackupAndHook == 1;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

}
