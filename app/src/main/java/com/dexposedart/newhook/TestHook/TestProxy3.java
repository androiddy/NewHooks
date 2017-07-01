package com.dexposedart.newhook.TestHook;

import com.dexposedart.newhook.MainActivity;
import com.taobao.android.dexposed.XC_MethodReplacement;
import com.taobao.android.dexposed.annotations.Hooks;


/**
 * 作者：zhangzhongping on 17/4/3 23:29
 * 邮箱：android_dy@163.com
 * extends XC_MethodReplacement 是用来兼容dalvik虚拟机hook 实现方法是直接替换原方法的
 * extends XC_MethodHook 也是用来兼容dalvik虚拟机hook  两个实现方法是 一个是被hook方法执行前执行  一个是被hook方法执行后执行
 */

@Hooks(Class = "com.dexposedart.newhook.MainActivity", Name = "nimabi",
        Parameter = {"boolean[]"}, returnVal = "boolean", isStatic = false)
public class TestProxy3 extends XC_MethodReplacement {

    @Override
    public MethodHookParam replaceHookedMethod(MethodHookParam param) throws Throwable {
        MainActivity mainActivity = (MainActivity) param.thisObject;
        mainActivity.MainAct("MainActhook");
        //设置方法返回值 有返回值方法在继承XC_MethodReplacement的时候必须设置返回值
        param.setResult(false);
        return param;
    }
}
