package com.dexposedart.newhook.TestHook;


import com.taobao.android.dexposed.Hook22_23.utils.HookLog;
import com.taobao.android.dexposed.XC_MethodHook;
import com.taobao.android.dexposed.annotations.Hook;


/**
 * 作者：zhangzhongping on 17/4/3 23:29
 * 邮箱：android_dy@163.com
 * extends XC_MethodReplacement 是用来兼容dalvik虚拟机hook 实现方法是直接替换原方法的
 * extends XC_MethodHook 也是用来兼容dalvik虚拟机hook  两个实现方法是 一个是被hook方法执行前执行  一个是被hook方法执行后执行
 * <p>
 * 如果art虚拟机不想直接替换原方法需要在@HookMethod注解方法里面调用@OriginalHookMethod注解方法
 * <p>
 * 如果art虚拟机想直接替换原方法 @HookMethod注解方法里面不能调用@OriginalHookMethod注解方法
 */
@Hook(Class = "com.dexposedart.newhook.MainActivity", Name = "test41")
public class TestProxy41 extends XC_MethodHook {

    @Override
    public void beforeHookedMethod(MethodHookParam param) throws Throwable {
        if (param.isEquals(this)) {
            HookLog.e("test41 beforeHookedMethod");
        }
    }

    @Override
    public void afterHookedMethod(MethodHookParam param) throws Throwable {
        if (param.isEquals(this)) {
            HookLog.e("test41 afterHookedMethod");
        }
    }
}
