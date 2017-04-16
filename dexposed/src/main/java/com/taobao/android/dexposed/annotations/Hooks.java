package com.taobao.android.dexposed.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class = 需要hook的类
 * Name = 需要hook的方法名
 * Type = 需要hook方法的参数(如 : java.long.String)
 * returnVal = 需要hook方法的返回值（如 : java.long.String , void , int , int[]）
 * 此注释是为了方便hook一些无法用.class获取参数的方法设计
 * （例如编写一些第三方应用的注入插件 一些自定义参数我们在我们的工程中无法获取.class参数 直接只需要设置参数的类字符串即可）
 * 作者：zhangzhongping on 17/4/11 17:13
 * 邮箱：android_dy@163.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Hooks {
    String Class();

    String Name();

    String returnVal();

    String[] Type() default {};
}