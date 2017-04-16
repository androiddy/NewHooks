/*
 * Original work Copyright (c) 2005-2008, The Android Open Source Project
 * Modified work Copyright (c) 2013, rovo89 and Tungstwenty
 * Modified work Copyright (c) 2015, Alibaba Mobile Infrastructure (Android) Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taobao.android.dexposed;

import static com.taobao.android.dexposed.XposedHelpers.getIntField;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Application;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;

import com.taobao.android.dexposed.Hook22_23.Dexposed_Art;
import com.taobao.android.dexposed.Hook22_23.utils.HookInfo;
import com.taobao.android.dexposed.Hook22_23.utils.HookLog;
import com.taobao.android.dexposed.Hook22_23.utils.HookResult;
import com.taobao.android.dexposed.XC_MethodHook.MethodHookParam;
import com.taobao.android.dexposed.XC_MethodHook.Unhook;
import com.taobao.android.dexposed.XC_MethodHook.XC_MethodKeepHook;
import com.taobao.android.dexposed.XC_MethodReplacement.XC_MethodKeepReplacement;
import com.taobao.android.dexposed.XposedHelpers.InvocationTargetError;


public final class DexposedBridge {

    private static final int RUNTIME_UNKNOW = 0;
    private static final int RUNTIME_DALVIK = 1;
    private static final int RUNTIME_ART = 2;
    private static int runtime = RUNTIME_UNKNOW;
    public static String DALVIK = "dalvik";
    public static String ART = "art";
    private static String ART4_5 = "art4.4_5.0";
    private static boolean isLoad = false;
    private static final Object[] EMPTY_ARRAY = new Object[0];
    private static HookInfo hookInfo = new HookInfo();
    private static Dexposed_Art dexposed_art = new Dexposed_Art();
    public static final ClassLoader BOOTCLASSLOADER = ClassLoader.getSystemClassLoader();


    // built-in handlers
    private static final Map<Member, CopyOnWriteSortedSet<XC_MethodHook>> hookedMethodCallbacks
            = new HashMap<Member, CopyOnWriteSortedSet<XC_MethodHook>>();

    private static final ArrayList<XC_MethodHook.Unhook> allUnhookCallbacks = new ArrayList<XC_MethodHook.Unhook>();


    private static int getRuntime() {

        if (VERSION.SDK_INT >= 21) {
            return RUNTIME_ART;
        } else if (VERSION.SDK_INT >= 20) {
            String vm = System.getProperty("persist.sys.dalvik.vm.lib", "");
            if (vm.contains("art"))
                return RUNTIME_ART;
            else
                return RUNTIME_DALVIK;
        } else {
            return RUNTIME_DALVIK;
        }
    }

    /**
     * Writes a message to BASE_DIR/log/debug.log (needs to have chmod 777)
     *
     * @param text log message
     */
    private synchronized static void log(String text) {
        HookLog.d(text);
    }

    /**
     * Log the stack trace
     *
     * @param t The Throwable object for the stacktrace
     * @see DexposedBridge#log(String)
     */
    public synchronized static void log(Throwable t) {
        log(Log.getStackTraceString(t));
    }

    /**
     * Hook any method with the specified callback
     *
     * @param hookMethod The method to be hooked
     * @param callback
     */
    private static XC_MethodHook.Unhook hookMethod(Member hookMethod, XC_MethodHook callback) {
        if (!(hookMethod instanceof Method) && !(hookMethod instanceof Constructor<?>)) {
            throw new IllegalArgumentException("only methods and constructors can be hooked");
        }

        boolean newMethod = false;
        CopyOnWriteSortedSet<XC_MethodHook> callbacks;
        synchronized (hookedMethodCallbacks) {
            callbacks = hookedMethodCallbacks.get(hookMethod);
            if (callbacks == null) {
                callbacks = new CopyOnWriteSortedSet<XC_MethodHook>();
                hookedMethodCallbacks.put(hookMethod, callbacks);
                newMethod = true;
            }
        }
        callbacks.add(callback);
        if (newMethod) {
            Class<?> declaringClass = hookMethod.getDeclaringClass();
            if (runtime == RUNTIME_UNKNOW) runtime = getRuntime();
            int slot = (runtime == RUNTIME_DALVIK) ? (int) getIntField(hookMethod, "slot") : 0;

            Class<?>[] parameterTypes;
            Class<?> returnType;
            if (hookMethod instanceof Method) {
                parameterTypes = ((Method) hookMethod).getParameterTypes();
                returnType = ((Method) hookMethod).getReturnType();
            } else {
                parameterTypes = ((Constructor<?>) hookMethod).getParameterTypes();
                returnType = null;
            }

            AdditionalHookInfo additionalInfo = new AdditionalHookInfo(callbacks, parameterTypes, returnType);
            hookMethodNative(hookMethod, declaringClass, slot, additionalInfo);
        }
        return callback.new Unhook(hookMethod);
    }

    /**
     * Removes the callback for a hooked method
     *
     * @param hookMethod The method for which the callback should be removed
     * @param callback   The reference to the callback as specified in {@link #hookMethod}
     */
    public static void unhookMethod(Member hookMethod, XC_MethodHook callback) {
        CopyOnWriteSortedSet<XC_MethodHook> callbacks;
        synchronized (hookedMethodCallbacks) {
            callbacks = hookedMethodCallbacks.get(hookMethod);
            if (callbacks == null)
                return;
        }
        callbacks.remove(callback);
    }

    private static Set<XC_MethodHook.Unhook> hookAllMethods(Class<?> hookClass, String methodName, XC_MethodHook callback) {
        Set<XC_MethodHook.Unhook> unhooks = new HashSet<XC_MethodHook.Unhook>();
        for (Member method : hookClass.getDeclaredMethods())
            if (method.getName().equals(methodName))
                unhooks.add(hookMethod(method, callback));
        return unhooks;
    }

    protected static HookResult findAndHookMethod(Application application, String[] hookname, Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        HookResult result = new HookResult();
        try {
            if (parameterTypesAndCallback.length == 0 || !(parameterTypesAndCallback[parameterTypesAndCallback.length - 1] instanceof XC_MethodHook)) {
                result.setErrormsg("未定义回调接口");
                result.setHookSuccess(false);
                return result;
            }
            HookInfo hookInfo = DexposedBridge.canDexposed(application.getApplicationContext());
            if (hookInfo.isSupport()) {
                if (DexposedBridge.ART.equals(hookInfo.getModel())) {
                    result.setHookSuccess(hookInfo.getHook().findAndHookMethod(hookname, application.getClassLoader(), clazz.getName(), methodName,
                            FormattedClass((Class<?>) parameterTypesAndCallback[1], parameterTypesAndCallback),
                            ((Class) parameterTypesAndCallback[0]).getName()));
                    if (!result.isHookSuccess()) {
                        result.setErrormsg("未找到需要hook的方法或者类");
                    }
                    return result;
                } else {
                    XC_MethodHook callback = (XC_MethodHook) parameterTypesAndCallback[parameterTypesAndCallback.length - 1];
                    Method m = XposedHelpers.findMethodExact(clazz, methodName, DelParameterTypes(parameterTypesAndCallback));
                    XC_MethodHook.Unhook unhook = hookMethod(m, callback);
                    if (!(callback instanceof XC_MethodKeepHook
                            || callback instanceof XC_MethodKeepReplacement)) {
                        synchronized (allUnhookCallbacks) {
                            allUnhookCallbacks.add(unhook);
                        }
                    }
                    result.setHookSuccess(true);
                    return result;
                }
            }
            result.setErrormsg(hookInfo.getErrorMsg());
            result.setHookSuccess(false);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            result.setErrormsg(throwable);
            result.setHookSuccess(false);
        }
        return result;
    }

    //根据参数4分割并格式化方法的签名 用于art hook参数，dalvik Hook无需此方法
    //此方法为了用户在编写hook调用时更方便
    private static String FormattedClass(Class<?> returnValue, Object... objects) {
        List<String> list = new ArrayList<String>();
        for (int i = 2; i < objects.length; i++) {
            if (i != objects.length - 1) {
                list.add(ClassUtils.getClassString(((Class) objects[i]).getCanonicalName()));
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : list) {
            stringBuilder.append(string);
        }
        String returns = "V";
        if (!"void".equals(returnValue.getCanonicalName())) {
            String index1 = returnValue.getCanonicalName();
            returns = ClassUtils.getClassString(index1);
        }
        return "(".concat(stringBuilder.toString()).concat(")").concat(returns);
    }

    //将参数4去除返回值和代理类在参数4数组中的下标，保证原来框架的dalvik Hook代码可以正常运行
    //此方法为了用户在编写hook调用时更方便
    private static Object[] DelParameterTypes(Object[] parameterTypesAndCallback) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 2; i < parameterTypesAndCallback.length; i++) {
            list.add(parameterTypesAndCallback[i]);
        }
        Object[] obj = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            obj[i] = list.get(i);
        }
        return obj;
    }


    public static void unhookAllMethods() {
        synchronized (allUnhookCallbacks) {
            for (int i = 0; i < allUnhookCallbacks.size(); i++) {
                ((Unhook) allUnhookCallbacks.get(i)).unhook();
            }
            allUnhookCallbacks.clear();
        }
    }

    public static Set<XC_MethodHook.Unhook> hookAllConstructors(Class<?> hookClass, XC_MethodHook callback) {
        Set<XC_MethodHook.Unhook> unhooks = new HashSet<XC_MethodHook.Unhook>();
        for (Member constructor : hookClass.getDeclaredConstructors())
            unhooks.add(hookMethod(constructor, callback));
        return unhooks;
    }

    /**
     * This method is called as a replacement for hooked methods.
     */
    private static Object handleHookedMethod(Member method, int originalMethodId, Object additionalInfoObj,
                                             Object thisObject, Object[] args) throws Throwable {
        AdditionalHookInfo additionalInfo = (AdditionalHookInfo) additionalInfoObj;

        Object[] callbacksSnapshot = additionalInfo.callbacks.getSnapshot();
        final int callbacksLength = callbacksSnapshot.length;
        if (callbacksLength == 0) {
            try {
                return invokeOriginalMethodNative(method, originalMethodId, additionalInfo.parameterTypes,
                        additionalInfo.returnType, thisObject, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }

        MethodHookParam param = new MethodHookParam();
        param.method = method;
        param.Model = DALVIK;
        param.thisObject = thisObject;
        param.args = args;

        // call "before method" callbacks
        int beforeIdx = 0;
        do {
            try {
                param = ((XC_MethodHook) callbacksSnapshot[beforeIdx]).beforeHookedMethod(param);
            } catch (Throwable t) {
                log(t);
                // reset result (ignoring what the unexpectedly exiting callback did)
                param.setResult(null);
                param.returnEarly = false;
                continue;
            }

            if (param.returnEarly) {
                // skip remaining "before" callbacks and corresponding "after" callbacks
                beforeIdx++;
                break;
            }
        } while (++beforeIdx < callbacksLength);

        // call original method if not requested otherwise
        if (!param.returnEarly) {
            try {
                param.setResult(invokeOriginalMethodNative(method, originalMethodId,
                        additionalInfo.parameterTypes, additionalInfo.returnType, param.thisObject, param.args));
            } catch (InvocationTargetException e) {
                param.setThrowable(e.getCause());
            }
        }

        // call "after method" callbacks
        int afterIdx = beforeIdx - 1;
        do {
            Object lastResult = param.getResult();
            Throwable lastThrowable = param.getThrowable();

            try {
                ((XC_MethodHook) callbacksSnapshot[afterIdx]).afterHookedMethod(param);
            } catch (Throwable t) {
                DexposedBridge.log(t);

                // reset to last result (ignoring what the unexpectedly exiting callback did)
                if (lastThrowable == null)
                    param.setResult(lastResult);
                else
                    param.setThrowable(lastThrowable);
            }
        } while (--afterIdx >= 0);

        // return
        if (param.hasThrowable())
            throw param.getThrowable();
        else
            return param.getResult();
    }

    /**
     * Check device if can run dexposed, and load libs auto.
     */
    private synchronized static HookInfo canDexposed(Context context) {
        hookInfo = new HookInfo();
        if (!DeviceCheck.isDeviceSupport(context)) {
            hookInfo.setErrorMsg("当前手机不支持所有Hook模块");
            hookInfo.setSupport(false);
            return hookInfo;
        }
        return loadDexposedLib(context);
    }

    private static HookInfo loadDexposedLib(Context context) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= 21 && android.os.Build.VERSION.SDK_INT <= 24) {
                if (!isLoad) {
                    System.loadLibrary("dexposed_art");
                    init(android.os.Build.VERSION.SDK_INT);
                }
                hookInfo.setSupport(true);
                hookInfo.setHook(dexposed_art);
                hookInfo.setModel(ART);
            } else if ((android.os.Build.VERSION.SDK_INT >= 19 && android.os.Build.VERSION.SDK_INT < 21) && DeviceCheck.isArtMode()) {
                if (!isLoad) {
                    System.loadLibrary("dexposed_l");
                }
                hookInfo.setSupport(true);
                hookInfo.setModel(DALVIK);
            } else if (android.os.Build.VERSION.SDK_INT > 14) {
                if (!isLoad) {
                    System.loadLibrary("dexposed");
                }
                hookInfo.setSupport(true);
                hookInfo.setModel(DALVIK);
            } else {
                hookInfo.setSupport(false);
                return hookInfo;
            }
            isLoad = true;
            return hookInfo;
        } catch (Throwable e) {
            hookInfo.setErrorMsg(e);
            hookInfo.setSupport(false);
            return hookInfo;
        }
    }

    private native static Object invokeSuperNative(Object obj, Object[] args, Member method, Class<?> declaringClass,
                                                   Class<?>[] parameterTypes, Class<?> returnType, int slot)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException;

    public static native int findAndBackupAndHook(Class targetClass, String methodName, String methodSig,
                                                  Method hook, Method backup);

    private static native void init(int SDK_version);

    public static Object invokeSuper(Object obj, Member method, Object... args) throws NoSuchFieldException {

        try {
            if (runtime == RUNTIME_UNKNOW) runtime = getRuntime();

            int slot = 0;
            if (runtime == RUNTIME_DALVIK) {
                //get the super method slot
                Method m = XposedHelpers.findMethodExact(obj.getClass().getSuperclass(), method.getName(), ((Method) method).getParameterTypes());
                slot = (int) getIntField(m, "slot");
            }

            return invokeSuperNative(obj, args, method, method.getDeclaringClass(), ((Method) method).getParameterTypes(), ((Method) method).getReturnType(), slot);

        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (InvocationTargetException e) {
            throw new InvocationTargetError(e.getCause());
        }
    }

    /**
     * Intercept every call to the specified method and call a handler function instead.
     *
     * @param method The method to intercept
     */
    private native synchronized static void hookMethodNative(Member method, Class<?> declaringClass, int slot, Object additionalInfo);

    private native static Object invokeOriginalMethodNative(Member method, int methodId,
                                                            Class<?>[] parameterTypes, Class<?> returnType, Object thisObject, Object[] args)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;


    /**
     * Basically the same as {@link Method#invoke}, but calls the original method
     * as it was before the interception by Xposed. Also, access permissions are not checked.
     *
     * @param method     Method to be called
     * @param thisObject For non-static calls, the "this" pointer
     * @param args       Arguments for the method call as Object[] array
     * @return The result returned from the invoked method
     * @throws NullPointerException      if {@code receiver == null} for a non-static method
     * @throws IllegalAccessException    if this method is not accessible (see {@link AccessibleObject})
     * @throws IllegalArgumentException  if the number of arguments doesn't match the number of parameters, the receiver
     *                                   is incompatible with the declaring class, or an argument could not be unboxed
     *                                   or converted by a widening conversion to the corresponding parameter type
     * @throws InvocationTargetException if an exception was thrown by the invoked method
     */
    public static Object invokeOriginalMethod(Member method, Object thisObject, Object[] args)
            throws NullPointerException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (args == null) {
            args = EMPTY_ARRAY;
        }

        Class<?>[] parameterTypes;
        Class<?> returnType;
        if (method instanceof Method) {
            parameterTypes = ((Method) method).getParameterTypes();
            returnType = ((Method) method).getReturnType();

        } else if (method instanceof Constructor) {
            parameterTypes = ((Constructor<?>) method).getParameterTypes();
            returnType = null;
        } else {
            throw new IllegalArgumentException("method must be of type Method or Constructor");
        }

        return invokeOriginalMethodNative(method, 0, parameterTypes, returnType, thisObject, args);
    }

    public static class CopyOnWriteSortedSet<E> {
        private transient volatile Object[] elements = EMPTY_ARRAY;

        public synchronized boolean add(E e) {
            int index = indexOf(e);
            if (index >= 0)
                return false;

            Object[] newElements = new Object[elements.length + 1];
            System.arraycopy(elements, 0, newElements, 0, elements.length);
            newElements[elements.length] = e;
            Arrays.sort(newElements);
            elements = newElements;
            return true;
        }

        public synchronized boolean remove(E e) {
            int index = indexOf(e);
            if (index == -1)
                return false;

            Object[] newElements = new Object[elements.length - 1];
            System.arraycopy(elements, 0, newElements, 0, index);
            System.arraycopy(elements, index + 1, newElements, index, elements.length - index - 1);
            elements = newElements;
            return true;
        }

        public synchronized void clear() {
            elements = EMPTY_ARRAY;
        }

        private int indexOf(Object o) {
            for (int i = 0; i < elements.length; i++) {
                if (o.equals(elements[i]))
                    return i;
            }
            return -1;
        }

        public Object[] getSnapshot() {
            return elements;
        }
    }

    private static class AdditionalHookInfo {
        final CopyOnWriteSortedSet<XC_MethodHook> callbacks;
        final Class<?>[] parameterTypes;
        final Class<?> returnType;
        String shorty;

        private AdditionalHookInfo(CopyOnWriteSortedSet<XC_MethodHook> callbacks, Class<?>[] parameterTypes, Class<?> returnType) {
            this.callbacks = callbacks;
            this.parameterTypes = parameterTypes;
            this.returnType = returnType;

            StringBuilder sb = new StringBuilder(64);
            sb.append(Class2Shorty(returnType));
            for (Class<?> c : parameterTypes) {
                sb.append(Class2Shorty(c));
            }

            shorty = sb.toString();
        }

        String Class2Shorty(Class<?> cls) {
            if (cls.isPrimitive()) {
                return builtInMap.get(cls);
            } else
                return "L";
        }
    }

    private static Map<Class, String> builtInMap = new HashMap<Class, String>() {
        {
            put(Integer.TYPE, "I");
            put(Long.TYPE, "J");
            put(Double.TYPE, "D");
            put(Float.TYPE, "F");
            put(Boolean.TYPE, "Z");
            put(Character.TYPE, "C");
            put(Byte.TYPE, "B");
            put(Void.TYPE, "V");
            put(Short.TYPE, "S");
        }
    };
}
