package com.taobao.android.dexposed;

import android.app.Application;


/**
 * 作者：zhangzhongping on 17/4/11 12:40
 * 邮箱：android_dy@163.com
 */
public class DalvikArt {

    /**
     * 参数1 = Application
     * 参数2 = 需要hook的方法所在类
     * 参数3 = 需要hook的方法名
     * 参数4 = (art)hook代理类+hook的方法的返回值(无返回值写void.class即可)+hook的方法所有参数+(dalvik)hook回调
     * 参数4顺序不能变
     * <p>
     * <p>
     * 提示  6.0 Hook Activity类的非静态方法可能会oom  如果发现oom的情况请换hook点
     *
     * @author zhangzhongping
     * created at 17/4/11 02:38
     */
    public static HookResult findAndHookMethod(Application application, Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        return DexposedBridge.findAndHookMethod(application, clazz, methodName, parameterTypesAndCallback);
    }
}
