package com.dexposedart.newhook.TestHook;

import com.taobao.android.dexposed.XC_MethodReplacement;
import com.taobao.android.dexposed.annotations.Hook;
import com.taobao.android.dexposed.annotations.HookMethod;
import com.taobao.android.dexposed.annotations.OriginalHookMethod;


/**
 * 作者：zhangzhongping on 17/4/3 23:29
 * 邮箱：android_dy@163.com
 */
@Hook(Class = "com.dexposedart.newhook.MainActivity",
        Name = "test111", Type = {String.class})
public class TestProxy3 extends XC_MethodReplacement {

    @HookMethod(MethodName = "HookMethods")
    public static Object HookMethods() {
        return OriginalHookMethods() + "Hook";
    }

    @OriginalHookMethod(MethodName = "OriginalHookMethods")
    public static Object OriginalHookMethods() {
        return new Object();
    }

    @Override
    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
        return HookMethods();
    }
}
