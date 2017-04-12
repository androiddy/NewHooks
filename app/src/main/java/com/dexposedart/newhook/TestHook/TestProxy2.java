package com.dexposedart.newhook.TestHook;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dexposedart.newhook.App;
import com.taobao.android.dexposed.XC_MethodReplacement;
import com.taobao.android.dexposed.annotations.Hook;
import com.taobao.android.dexposed.annotations.HookMethod;
import com.taobao.android.dexposed.annotations.OriginalHookMethod;


/**
 * 作者：zhangzhongping on 17/4/3 23:29
 * 邮箱：android_dy@163.com
 */
@Hook(Class = "com.dexposedart.newhook.MainActivity",
        Name = "test1", Type = {void.class, Button.class})
public class TestProxy2 extends XC_MethodReplacement {

    @HookMethod(MethodName = "HookMethods")
    public static Object HookMethods(Button view) {
        Toast.makeText(App.getContext(), view.getText() + "->hook", 0).show();
        return new Object();
    }

    @OriginalHookMethod(MethodName = "OriginalHookMethods")
    public static Object OriginalHookMethods(Button sequence) {
        return new Object();
    }

    @Override
    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
        return HookMethods((Button) param.args[0]);
    }
}
