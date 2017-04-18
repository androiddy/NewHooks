package com.taobao.android.dexposed.annotations;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * 作者：zhangzhongping on 17/4/16 13:34
 * 邮箱：android_dy@163.com
 */
public class Utils {

    private static Elements elementUtils;

    private static final Map<String, TypeName> abbreviationMap = new HashMap<String, TypeName>();

    private static final Map<String, String> abbreviationMaps = new HashMap<String, String>();

    public static void Inits() {
        abbreviationMaps.put("int", "1");
        abbreviationMaps.put("boolean", "false");
        abbreviationMaps.put("float", "1");
        abbreviationMaps.put("long", "1");
        abbreviationMaps.put("short", "1");
        abbreviationMaps.put("byte", "1");
        abbreviationMaps.put("double", "1");
        abbreviationMaps.put("char", "1");
        abbreviationMaps.put("void", "void");
        abbreviationMaps.put("int[]", "new int[]{1}");
        abbreviationMaps.put("boolean[]", "new boolean[]{false}");
        abbreviationMaps.put("float[]", "new float[]{1}");
        abbreviationMaps.put("long[]", "new long[]{1}");
        abbreviationMaps.put("short[]", "new short[]{1}");
        abbreviationMaps.put("byte[]", "new byte[]{1}");
        abbreviationMaps.put("double[]", "new double[]{1}");
        abbreviationMaps.put("char[]", "new char[]{1}");
        abbreviationMaps.put("java.lang.Object", "new Object()");
        abbreviationMaps.put("java.lang.String", "new String()");
        abbreviationMaps.put("java.lang.Void", "null");
        abbreviationMaps.put("java.lang.Boolean", "new Boolean(false)");
        abbreviationMaps.put("java.lang.Byte", "new Byte(\"1\")");
        abbreviationMaps.put("java.lang.Short", "new Short(\"1\")");
        abbreviationMaps.put("java.lang.Integer", "new Integer(1)");
        abbreviationMaps.put("java.lang.Long", "new Long(1)");
        abbreviationMaps.put("java.lang.Character", "new Character((char) 1)");
        abbreviationMaps.put("java.lang.Float", "new Float(1)");
        abbreviationMaps.put("java.lang.Double", "new Double(1)");
        abbreviationMaps.put("java.lang.Void[]", "null");
        abbreviationMaps.put("java.lang.Boolean[]", "new Boolean[]{true}");
        abbreviationMaps.put("java.lang.Byte[]", "new Byte[]{1}");
        abbreviationMaps.put("java.lang.Short[]", "new Short[]{1}");
        abbreviationMaps.put("java.lang.Integer[]", "new Integer[]{1}");
        abbreviationMaps.put("java.lang.Long[]", "new Long[]{(long)1}");
        abbreviationMaps.put("java.lang.Character[]", "new Character[]{1}");
        abbreviationMaps.put("java.lang.Float[]", "new Float[]{(float)1}");
        abbreviationMaps.put("java.lang.Double[]", "new Double[]{(double) 1}");
        abbreviationMaps.put("java.lang.String[]", "new String[]{\"\"}");
        abbreviationMaps.put("java.lang.Object[]", "new Object[]{new Object()}");
    }

    public static void Init() {
        abbreviationMap.put("int", TypeName.INT);
        abbreviationMap.put("boolean", TypeName.BOOLEAN);
        abbreviationMap.put("float", TypeName.FLOAT);
        abbreviationMap.put("long", TypeName.LONG);
        abbreviationMap.put("short", TypeName.SHORT);
        abbreviationMap.put("byte", TypeName.BYTE);
        abbreviationMap.put("double", TypeName.DOUBLE);
        abbreviationMap.put("char", TypeName.CHAR);
        abbreviationMap.put("void", TypeName.OBJECT);
        abbreviationMap.put("java.lang.Object", TypeName.OBJECT);
        abbreviationMap.put("int[]", ClassName.get((Type)int[].class));
        abbreviationMap.put("boolean[]", ClassName.get((Type)boolean[].class));
        abbreviationMap.put("float[]", ClassName.get((Type)float[].class));
        abbreviationMap.put("long[]", ClassName.get((Type)long[].class));
        abbreviationMap.put("short[]",ClassName.get((Type)short[].class));
        abbreviationMap.put("byte[]", ClassName.get((Type)byte[].class));
        abbreviationMap.put("double[]", ClassName.get((Type)double[].class));
        abbreviationMap.put("char[]", ClassName.get((Type)char[].class));
        abbreviationMap.put("java.lang.String", ClassName.get("java.lang", "String"));
        abbreviationMap.put("java.lang.Void", ClassName.get("java.lang", "Void"));
        abbreviationMap.put("java.lang.Boolean", ClassName.get("java.lang", "Boolean"));
        abbreviationMap.put("java.lang.Byte", ClassName.get("java.lang", "Byte"));
        abbreviationMap.put("java.lang.Short", ClassName.get("java.lang", "Short"));
        abbreviationMap.put("java.lang.Integer", ClassName.get("java.lang", "Integer"));
        abbreviationMap.put("java.lang.Long", ClassName.get("java.lang", "Long"));
        abbreviationMap.put("java.lang.Character", ClassName.get("java.lang", "Character"));
        abbreviationMap.put("java.lang.Float", ClassName.get("java.lang", "Float"));
        abbreviationMap.put("java.lang.Double", ClassName.get("java.lang", "Double"));
        abbreviationMap.put("java.lang.Object[]",ClassName.get((Type)Object[].class));
        abbreviationMap.put("java.lang.String[]", ClassName.get((Type)String[].class));
        abbreviationMap.put("java.lang.Void[]", ClassName.get((Type)Void[].class));
        abbreviationMap.put("java.lang.Boolean[]", ClassName.get((Type)Boolean[].class));
        abbreviationMap.put("java.lang.Byte[]", ClassName.get((Type)Byte[].class));
        abbreviationMap.put("java.lang.Short[]", ClassName.get((Type)Short[].class));
        abbreviationMap.put("java.lang.Integer[]", ClassName.get((Type)Integer[].class));
        abbreviationMap.put("java.lang.Long[]", ClassName.get((Type)Long[].class));
        abbreviationMap.put("java.lang.Character[]", ClassName.get((Type)Character[].class));
        abbreviationMap.put("java.lang.Float[]", ClassName.get((Type)Float[].class));
        abbreviationMap.put("java.lang.Double[]", ClassName.get((Type)Double[].class));
    }

    public static TypeName reval(String name) {
        if (abbreviationMap.get(name) != null) {
            return abbreviationMap.get(name);
        }
        return TypeName.OBJECT;
    }

    public static Object revals(String name) {
        if (abbreviationMaps.get(name)!=null) {
            if (abbreviationMaps.get(name).equals("void")) {
                return "new Object()";
            }
            return abbreviationMaps.get(name);
        }
        return "new Object()";
    }

    public static String getPackageName(TypeElement typeElement) {
        return getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
    }

    public static void setElementUtils(Elements elementUtils) {
        Utils.elementUtils = elementUtils;
    }

    public static Elements getElementUtils() {
        return elementUtils;
    }
}
