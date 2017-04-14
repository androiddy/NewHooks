package com.dexposedart.newhook.TestHook;

import android.app.Application;

import com.taobao.android.dexposed.DalvikArt;
import com.taobao.android.dexposed.Hook22_23.utils.HookResult;

import java.util.List;


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

    public boolean[] StartFrameHook1(Application application) {
        List<HookResult> andHookMethods = DalvikArt.findAndHookMethod(application, TestProxy2.class,TestProxy3.class);
        return new boolean[]{andHookMethods.get(0).isHookSuccess(),andHookMethods.get(1).isHookSuccess()};
    }
}
