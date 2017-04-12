package com.dexposedart.newhook.TestHook;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import com.dexposedart.newhook.App;
import com.dexposedart.newhook.MainActivity;
import com.taobao.android.dexposed.DalvikArt;
import com.taobao.android.dexposed.HookResult;
import com.taobao.android.dexposed.XC_MethodReplacement;


/**
 * 作者：zhangzhongping on 17/4/7 13:31
 * 邮箱：android_dy@163.com
 */
public class TestHookMeanager {

    public static TestHookMeanager testHookMeanager;

    public static TestHookMeanager getTestHookMeanager() {
        if (testHookMeanager == null) {
            testHookMeanager = new TestHookMeanager();
        }
        return testHookMeanager;
    }

    public boolean StartFrameHook(Application application) {
/*        HookResult andHookMethod = DalvikArt.findAndHookMethod(application,
                MainActivity.class, "Toasts", TestProxy1.class, void.class, Boolean[].class,
                String.class, View[].class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        Toast.makeText(App.getContext(), param.args[1] + " Hook1", 0).show();
                        return null;
                    }
                });*/
        HookResult andHookMethods = DalvikArt.findAndHookMethod(application, TestProxy1.class);
        return andHookMethods.hookSuccess;
    }

    public boolean StartFrameHook1(Application application) {
        HookResult andHookMethods = DalvikArt.findAndHookMethod(application, TestProxy2.class);
        return andHookMethods.hookSuccess;
    }

    public boolean StartFrameHook2(Application application) {
        HookResult andHookMethods = DalvikArt.findAndHookMethod(application, TestProxy3.class);
        return andHookMethods.hookSuccess;
    }
}
