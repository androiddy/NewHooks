package com.taobao.android.dexposed;

import android.app.Application;

import com.taobao.android.dexposed.Hook22_23.utils.HookResult;
import com.taobao.android.dexposed.Hook22_23.utils.HookUtils;
import com.taobao.android.dexposed.annotations.Hook;
import com.taobao.android.dexposed.annotations.HookMethod;
import com.taobao.android.dexposed.annotations.Hooks;
import com.taobao.android.dexposed.annotations.OriginalHookMethod;

import java.lang.reflect.Method;


/**
 * 作者：zhangzhongping on 17/4/11 12:40
 * 邮箱：android_dy@163.com
 */
public class DalvikArt {

    /**
     * 参数1 = Application
     * 参数2 = 需要hook的方法所在类
     * 参数3 = 需要hook的方法名
     * 参数4 = (art)hook代理类+hook的方法的返回值(无返回值写void.class即可)+hook的方法所有参数(参数.class 顺序一样即可)+(dalvik)hook回调
     * 参数4顺序不能变
     * <p>
     * <p>
     * 提示  Hook Activity类的非静态方法可能会oom  如果发现oom的情况请换hook点
     *
     * @author zhangzhongping
     * created at 17/4/11 02:38
     */
    @Deprecated
    public synchronized static HookResult findAndHookMethod(Application application, Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        return DexposedBridge.findAndHookMethod(application, new String[]{"HookMethod", "OriginalHookMethod"}, clazz, methodName, parameterTypesAndCallback);
    }

    /**
     * 参数1 = Application
     * 参数2 = Hook 或 Hooks注解类
     * <p>
     * <p>
     * 提示 Hook Activity类的非静态方法可能会oom  如果发现oom的情况请换hook点
     *
     * @author zhangzhongping
     * created at 17/4/12 21:50
     */
    public synchronized static HookResult findAndHookMethod(Application application, Class<?> arthook) {
        Hook hook = arthook.getAnnotation(Hook.class);
        Hooks hooks = arthook.getAnnotation(Hooks.class);
        HookMethod hookMethod1 = null;
        OriginalHookMethod originalHookMethod = null;
        HookResult hookResult = new HookResult();
        if (hook != null || hooks != null) {
            for (Method hookMethod : arthook.getDeclaredMethods()) {
                if (hookMethod1 == null) {
                    hookMethod1 = hookMethod.getAnnotation(HookMethod.class);
                }
                if (originalHookMethod == null) {
                    originalHookMethod = hookMethod.getAnnotation(OriginalHookMethod.class);
                }
            }
            if (hookMethod1 == null || originalHookMethod == null) {
                hookResult.setErrormsg("未定义 HookMethod | OriginalHookMethod 注解类");
                hookResult.setHookSuccess(false);
                return hookResult;
            }
            try {
                Class<?> clazz = Class.forName(hook == null ? hooks.Class() : hook.Class(), true, application.getClassLoader());
                String methodName = hook == null ? hooks.Name() : hook.Name();
                return DexposedBridge.findAndHookMethod(application, new String[]{hookMethod1.MethodName(), originalHookMethod.MethodName()}, clazz, methodName,
                        HookUtils.ClassJX(hook, hooks, arthook, application));
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
}
