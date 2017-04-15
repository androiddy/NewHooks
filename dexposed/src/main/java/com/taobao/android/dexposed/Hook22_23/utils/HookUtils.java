package com.taobao.android.dexposed.Hook22_23.utils;


import android.app.Application;

import com.taobao.android.dexposed.XC_MethodHook;
import com.taobao.android.dexposed.annotations.Hook;
import com.taobao.android.dexposed.annotations.Hooks;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：zhangzhongping on 17/4/6 15:21
 * 邮箱：android_dy@163.com
 */
public class HookUtils {

    private static final Map<String, Class> abbreviationMap = new HashMap<String, Class>();

    static {
        abbreviationMap.put("int", int.class);
        abbreviationMap.put("boolean", boolean.class);
        abbreviationMap.put("float", float.class);
        abbreviationMap.put("long", long.class);
        abbreviationMap.put("short", short.class);
        abbreviationMap.put("byte", byte.class);
        abbreviationMap.put("double", double.class);
        abbreviationMap.put("char", char.class);
        abbreviationMap.put("void", void.class);
        abbreviationMap.put("int[]", int.class);
        abbreviationMap.put("boolean[]", boolean[].class);
        abbreviationMap.put("float[]", float[].class);
        abbreviationMap.put("long[]", long[].class);
        abbreviationMap.put("short[]", short[].class);
        abbreviationMap.put("byte[]", byte[].class);
        abbreviationMap.put("double[]", double[].class);
        abbreviationMap.put("char[]", char[].class);
    }

    public static Object[] ClassJX(Hook hook, Hooks hooks, Class arthook, Application application, boolean isTest) throws Throwable {
        XC_MethodHook xc_methodReplacement = (XC_MethodHook) arthook.newInstance();
        Class<?> clazz = Class.forName(hook == null ? hooks.Class() : hook.Class(), true, application.getClassLoader());
        String methodName = hook == null ? hooks.Name() : hook.Name();
        Class[] type = hook == null ? ClassLoad(hooks.Type(), application) : hook.Type();
        Object[] obj = new Object[type.length + 1];
        Class type1 = clazz.getDeclaredMethod(methodName, type).getReturnType();
        String c = type1.getName();
        Class retvar;
        if (abbreviationMap.get(c) != null) {
            retvar = abbreviationMap.get(c);
        } else {
            retvar = Class.forName(c, true, application.getClassLoader());
        }
        System.arraycopy(type, 0, obj, 1, type.length);
        obj[0] = retvar;
        Object[] objects = new Object[obj.length + 2];
        System.arraycopy(obj, 0, objects, 1, obj.length);
        objects[0] = isTest ?
                AdapterCallBackList(type.length, arthook,
                        xc_methodReplacement) : arthook;
        objects[objects.length - 1] = xc_methodReplacement;
        return objects;
    }


    private static Class[] ClassLoad(String[] strings, Application application) throws Throwable {
        Class[] classes = new Class[strings.length];
        for (int i = 0; i < strings.length; i++) {
            if (abbreviationMap.get(strings[i]) != null) {
                classes[i] = abbreviationMap.get(strings[i]);
            } else {
                classes[i] = Class.forName(strings[i], true, application.getClassLoader());
            }
        }
        return classes;
    }

    private static Class<?> AdapterCallBackList(int leng, Class mng, XC_MethodHook xc_methodHook) throws Throwable {
        if (leng > 8) {
            throw new RuntimeException("需要hook的方法参数大于8");
        }
        Class<?> obj = getAptHookClass(mng);
        setXc_methodhook(obj, xc_methodHook);
        return obj;
    }

    private static Class<?> getAptHookClass(Class<?> cl) throws ClassNotFoundException {
        String classname = cl.getName().concat("$Hook");
        Class<?> aClass = Class.forName(classname);
        return aClass;
    }

    private static void setXc_methodhook(Class xc_methodhook, XC_MethodHook xc_methodHook) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Method xc_methodhook1 = xc_methodhook.getDeclaredMethod("setXC_MethodHook",XC_MethodHook.class);
        if (xc_methodhook1 != null) {
            xc_methodhook1.setAccessible(true);
            xc_methodhook1.invoke(null, xc_methodHook);
        }else{
            throw new RuntimeException("内部错误");
        }
    }
}
