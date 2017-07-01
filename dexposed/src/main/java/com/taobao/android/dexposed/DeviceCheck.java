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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

public class DeviceCheck {

    private static final String SELECT_RUNTIME_PROPERTY = "persist.sys.dalvik.vm.lib";
    private static final String LIB_DALVIK = "libdvm.so";
    private static final String LIB_ART = "libart.so";
    private static final String LIB_ART_D = "libartd.so";

    private static boolean isCheckedDeviceSupport = false;
    private static boolean isDeviceSupportable = false;

    private static boolean isDalvikMode() {
        String vmMode = getCurrentRuntimeValue();
        if ("Dalvik".equals(vmMode)) {
            return true;
        }
        return false;
    }

    public static boolean isArtMode() {
        String vmMode = getCurrentRuntimeValue();
        if ("ART".equals(vmMode)) {
            return true;
        }
        return false;
    }

    private static String getCurrentRuntimeValue() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            try {
                Method get = systemProperties.getMethod("get", String.class,
                        String.class);
                if (get == null) {
                    return "WTF?!";
                }
                try {
                    final String value = (String) get.invoke(systemProperties,
                            SELECT_RUNTIME_PROPERTY, "Dalvik");
                    if (LIB_DALVIK.equals(value)) {
                        return "Dalvik";
                    } else if (LIB_ART.equals(value)) {
                        return "ART";
                    } else if (LIB_ART_D.equals(value)) {
                        return "ART debug build";
                    }
                    return value;
                } catch (IllegalAccessException e) {
                    return "IllegalAccessException";
                } catch (IllegalArgumentException e) {
                    return "IllegalArgumentException";
                } catch (InvocationTargetException e) {
                    return "InvocationTargetException";
                }
            } catch (NoSuchMethodException e) {
                return "SystemProperties.get(String key, String def) method is not found";
            }
        } catch (ClassNotFoundException e) {
            return "SystemProperties class is not found";
        }
    }

    private static boolean isSupportSDKVersion() {
        if (android.os.Build.VERSION.SDK_INT >= 14 && android.os.Build.VERSION.SDK_INT < 20) {
            return true;
        } else if (android.os.Build.VERSION.SDK_INT == 10 || android.os.Build.VERSION.SDK_INT == 9) {
            return true;
        }
        return false;
    }

    private static boolean isX86CPU() {
        Process process = null;
        String abi = null;
        InputStreamReader ir = null;
        BufferedReader input = null;
        try {
            process = Runtime.getRuntime().exec("getprop ro.product.cpu.abi");
            ir = new InputStreamReader(process.getInputStream());
            input = new BufferedReader(ir);
            abi = input.readLine();
            if (abi.contains("x86")) {
                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                }
            }
            if (ir != null) {
                try {
                    ir.close();
                } catch (Exception e) {
                }
            }
            if (process != null) {
                try {
                    process.destroy();
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    public static String LoadCPU() {
        Process process = null;
        String abi = null;
        InputStreamReader ir = null;
        BufferedReader input = null;
        try {
            process = Runtime.getRuntime().exec("getprop ro.product.cpu.abi");
            ir = new InputStreamReader(process.getInputStream());
            input = new BufferedReader(ir);
            abi = input.readLine();
            return abi;
        } catch (Exception e) {
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                }
            }
            if (ir != null) {
                try {
                    ir.close();
                } catch (Exception e) {
                }
            }
            if (process != null) {
                try {
                    process.destroy();
                } catch (Exception e) {
                }
            }
        }
        return abi;
    }


    public static synchronized boolean isDeviceSupport() {
        try {
            if (isCheckedDeviceSupport) {
                return isDeviceSupportable;
            }
            if (!isX86CPU() && !isYunOS() && Build.VERSION.SDK_INT <= 21 && Build.VERSION.SDK_INT >= 15) {
                isDeviceSupportable = true;
            } else{
                isDeviceSupportable = isArt() && Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT <= 24;
            }
        } finally {
            isCheckedDeviceSupport = true;
        }
        return isDeviceSupportable;
    }

    private static boolean isArt() {
        return System.getProperty("java.vm.version").startsWith("2");
    }

    @SuppressLint("DefaultLocale")
    private static boolean isYunOS() {
        String s1 = null;
        String s2 = null;
        try {
            Method m = Class.forName("android.os.SystemProperties").getMethod(
                    "get", String.class);
            s1 = (String) m.invoke(null, "ro.yunos.version");
            s2 = (String) m.invoke(null, "java.vm.name");
        } catch (Throwable a) {
            a.printStackTrace();
        }
        if ((s2 != null && s2.toLowerCase().contains("lemur")) || (s1 != null && s1.trim().length() > 0)) {
            return true;
        } else {
            return false;
        }
    }
}
