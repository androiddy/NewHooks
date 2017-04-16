package com.dexposedart.newhook.TestHook;

import android.widget.Button;
import android.widget.Toast;

import com.dexposedart.newhook.App;
import com.taobao.android.dexposed.XC_MethodHook;
import com.taobao.android.dexposed.XC_MethodReplacement;
import com.taobao.android.dexposed.annotations.Hook;
import com.taobao.android.dexposed.annotations.HookMethod;
import com.taobao.android.dexposed.annotations.OriginalHookMethod;


/**
 * 作者：zhangzhongping on 17/4/3 23:29
 * 邮箱：android_dy@163.com
 * extends XC_MethodReplacement 是用来兼容dalvik虚拟机hook 实现方法是直接替换原方法的
 * extends XC_MethodHook 也是用来兼容dalvik虚拟机hook  两个实现方法是 一个是被hook方法执行前执行  一个是被hook方法执行后执行
 */
@Hook(Class = "com.dexposedart.newhook.MainActivity", Name = "test1", Type = {Button.class}, returnVal = int.class)
public class TestProxy2 extends XC_MethodReplacement {

    @Override
    public MethodHookParam replaceHookedMethod(MethodHookParam param) throws Throwable {
        Toast.makeText(App.getContext(), ((Button) param.args[0]).getText() + "->hook", 0).show();
        //设置方法返回值
        param.setResult(123);
        return param;
    }
}
