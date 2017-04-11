package com.dexposedart.newhook;

import android.app.Application;
import android.content.Context;

import com.dexposedart.newhook.TestHook.TestHookMeanager;

/**
 * 作者：zhangzhongping on 17/4/11 01:19
 * 邮箱：android_dy@163.com
 */
public class App extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        TestHookMeanager.getTestHookMeanager().StartFrameHook(this);
    }

    public static Context getContext() {
        return context;
    }
}
