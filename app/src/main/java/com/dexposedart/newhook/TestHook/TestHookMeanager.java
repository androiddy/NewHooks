package com.dexposedart.newhook.TestHook;

import android.app.Application;

import com.taobao.android.dexposed.DalvikArt;
import com.taobao.android.dexposed.HookResult;


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
        HookResult andHookMethods = DalvikArt.findAndHookMethod(application, TestProxy1.class);
        return andHookMethods.isHookSuccess();
    }

    public boolean StartFrameHook1(Application application) {
        HookResult andHookMethods = DalvikArt.findAndHookMethod(application, TestProxy2.class);
        return andHookMethods.isHookSuccess();
    }

    public boolean StartFrameHook2(Application application) {
        HookResult andHookMethods = DalvikArt.findAndHookMethod(application, TestProxy3.class);
        return andHookMethods.isHookSuccess();
    }
}
