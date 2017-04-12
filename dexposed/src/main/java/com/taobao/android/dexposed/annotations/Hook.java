package com.taobao.android.dexposed.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者：zhangzhongping on 17/4/11 17:13
 * 邮箱：android_dy@163.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Hook {
    String Class() default "";
    String Name() default "";
    Class[] Type();
}