package com.taobao.android.dexposed;

import android.app.Application;

import com.taobao.android.dexposed.Hook22_23.utils.HookResult;
import com.taobao.android.dexposed.Hook22_23.utils.HookUtils;
import com.taobao.android.dexposed.annotations.Hook;
import com.taobao.android.dexposed.annotations.Hooks;
import java.util.ArrayList;
import java.util.List;


/**
 * 作者：zhangzhongping on 17/4/11 12:40
 * 邮箱：android_dy@163.com
 */
public class DalvikArt {

    /**
     * 参数1 = Application
     * 参数2 = Hook 或 Hooks注解类
     * <p>
     * 此方法可以用于Hook1个方法
     * <p>
     * <p>
     * 提示 Hook Activity类的非静态方法可能会oom  如果发现oom的情况请换hook点
     * <p>
     * 5.0 以上可能不支持hook系统函数
     *
     */
    public synchronized static HookResult findAndHookMethod(Application application, Class<?> arthook) {
        Hook hook = arthook.getAnnotation(Hook.class);
        Hooks hooks = arthook.getAnnotation(Hooks.class);
        HookResult hookResult = new HookResult();
        if (hook != null || hooks != null) {
            try {
                Class<?> clazz = Class.forName(hook == null ? hooks.Class() : hook.Class(), true, application.getClassLoader());
                String methodName = hook == null ? hooks.Name() : hook.Name();
                return DexposedBridge.findAndHookMethod(application, new String[]{"HookMethod", "OriginalHookMethod"}, clazz, methodName,
                        HookUtils.ClassJX(hook, hooks, arthook, application,true));
            } catch (Throwable e) {
                e.printStackTrace();
                hookResult.setErrormsg(e.getLocalizedMessage());
                hookResult.setHookSuccess(false);
                return hookResult;
            }
        }
        hookResult.setErrormsg("未定义 Hook 注解类");
        hookResult.setHookSuccess(false);
        return hookResult;
    }

    /**
     * 参数1 = Application
     * 参数2 = Hook 或 Hooks注解类数组
     * <p>
     * 此方法可以用于Hook多个方法
     * <p>
     * <p>
     * 提示 Hook Activity类的非静态方法可能会oom  如果发现oom的情况请换hook点
     * <p>
     * 5.0 以上可能不支持hook系统函数
     *
     */
    public synchronized static List<HookResult> findAndHookMethod(Application application, Class<?>... arthook) {
        List<HookResult> list = new ArrayList<>();
        for (Class<?> art : arthook) {
            list.add(findAndHookMethod(application, art));
        }
        return list;
    }
}
