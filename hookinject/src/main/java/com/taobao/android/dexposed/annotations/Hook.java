package com.taobao.android.dexposed.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class = 需要hook的类
 * Name = 需要hook的方法名
 * Type = 需要hook方法的参数(如 :String.class)
 * 作者：zhangzhongping on 17/4/11 17:13
 * 邮箱：android_dy@163.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Hook {
    String Class();

    String Name();

    Class<?> returnVal();

    Class[] Type() default {};
}