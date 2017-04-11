package com.dexposedart.newhook.TestHook;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dexposedart.newhook.App;


/**
 * 作者：zhangzhongping on 17/4/3 23:29
 * 邮箱：android_dy@163.com
 */
public class TestProxy1 {

    //必备方法方法名不能改  参数根据hook的方法参数修改
    public static Object HookMethod(boolean[] ss,String s, View v) {
        Toast.makeText(App.getContext(),v.getClass()+"Hook",0).show();
        return OriginalHookMethod(ss,s,v);
    }

    //必备方法方法名不能改  参数根据hook的方法参数修改
    public static Object OriginalHookMethod(boolean[] ss,String s, View v) {
        return new Object();
    }
}
