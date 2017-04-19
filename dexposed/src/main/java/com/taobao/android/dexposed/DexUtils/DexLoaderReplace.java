package com.taobao.android.dexposed.DexUtils;

import android.app.Application;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by bruce on 11/14/15.
 */
public class DexLoaderReplace {

    public static synchronized ReplaceResult injectAboveEqualApiLevel14(Application application, String dexPath) {
        ReplaceResult replaceResult = new ReplaceResult();
        if (android.os.Build.VERSION.SDK_INT >= 15) {
            try {
                PathClassLoader pathClassLoader = (PathClassLoader) application.getClassLoader();
                DexClassLoader dexClassLoader = new DexClassLoader(dexPath, application.getCacheDir().getPath(), application.getApplicationInfo().nativeLibraryDir, pathClassLoader);
                Object pathList = getPathList(pathClassLoader);
                setField(pathList, pathList.getClass(), "dexElements", getDexElements(getPathList(dexClassLoader)));
                replaceResult.setSuccess(true);
            } catch (Throwable e) {
                e.printStackTrace();
                replaceResult.setErrorMsg(e);
                replaceResult.setSuccess(false);
            }
        } else {
            replaceResult.setErrorMsg("当前系统版本小于 4.1");
            replaceResult.setSuccess(false);
        }
        return replaceResult;
    }

    private static Object getPathList(Object baseDexClassLoader)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        return getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }


    private static Object getDexElements(Object paramObject)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        return getField(paramObject, paramObject.getClass(), "dexElements");
    }


    private static Object getField(Object obj, Class<?> cl, String field)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }


    private static void setField(Object obj, Class<?> cl, String field, Object value)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        localField.set(obj, value);
    }
}
