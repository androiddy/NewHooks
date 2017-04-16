package com.taobao.android.dexposed.annotations;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

/**
 * 作者：zhangzhongping on 17/4/16 13:35
 * 邮箱：android_dy@163.com
 */
public class getHooks {
    public static void getHooks(Element element, ProcessingEnvironment processingEnv) {
        try {
            // 判断是否Class
            TypeElement typeElement = (TypeElement) element;
            Hooks hook = element.getAnnotation(Hooks.class);
            ClassName className = ClassName.get("com.taobao.android.dexposed", "XC_MethodHook");
            MethodSpec.Builder bindViewMethodSpecBuilder = MethodSpec.methodBuilder("HookMethod")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
            MethodSpec.Builder bindViewMethodSpecBuilders = MethodSpec.methodBuilder("OriginalHookMethod")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
            MethodSpec.Builder bind = MethodSpec.methodBuilder("setXC_MethodHook")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(className, "xc_methodhooks")
                    .addStatement("if(xc_methodhook == null){\n xc_methodhook = xc_methodhooks;\n}");
            String[] classes;
            int leng = 0;
            String typeindex = "param.args = new Object[]{";
            String tyoeindes = "OriginalHookMethod(";

            String tyoeindess = "OriginalHookMethod(";
            try {
                classes = hook.Type();
                leng = classes.length;
            } catch (MirroredTypesException mte) {
                List<? extends TypeMirror> classTypeMirror = mte.getTypeMirrors();
                leng = classTypeMirror.size();
            }
            String name = "";
            try {
                name = hook.returnVal();
            } catch (MirroredTypeException e) {
                TypeMirror classTypeMirror = e.getTypeMirror();
                name = classTypeMirror.toString();
            }

            for (int i = 0; i < leng; i++) {
                String ss = "object" + i;
                bindViewMethodSpecBuilder.addParameter(TypeName.OBJECT, ss);
                bindViewMethodSpecBuilders.addParameter(TypeName.OBJECT, ss);
                if (i == leng - 1) {
                    typeindex = typeindex.concat(ss + "};");
                    tyoeindes = tyoeindes.concat("param.args[" + i + "])");
                    tyoeindess = tyoeindess.concat(ss + ")");
                } else {
                    typeindex = typeindex.concat(ss + ",");
                    tyoeindess = tyoeindess.concat(ss + ",");
                    tyoeindes = tyoeindes.concat("param.args[" + i + "],");
                }
            }
            if (leng == 0) {
                typeindex = "param.args = null;";
                tyoeindes = tyoeindes.concat(")");
                tyoeindess = tyoeindess.concat(")");
            }
            TypeName namess = Utils.reval(name);
            bindViewMethodSpecBuilder.returns(namess);
            bindViewMethodSpecBuilders.returns(namess);
            FieldSpec.Builder fieldSpec = FieldSpec.builder(className, "xc_methodhook");
            fieldSpec.addModifiers(Modifier.PRIVATE, Modifier.STATIC);
            fieldSpec.initializer("null");
            bindViewMethodSpecBuilder.addStatement("" +
                    "if(xc_methodhook == null){\n" +
                    "     return " + tyoeindess + ";\n" +
                    "}\n" +
                    "try{\n" +
                    "XC_MethodHook.MethodHookParam param = new XC_MethodHook.MethodHookParam();\n" +
                    "param.Model = com.taobao.android.dexposed.DexposedBridge.ART;\n" +
                    "param.HookMethod = xc_methodhook;\n" +
                    typeindex +
                    "\nif (xc_methodhook instanceof com.taobao.android.dexposed.XC_MethodReplacement) {\n" +
                    "      param = ((com.taobao.android.dexposed.XC_MethodReplacement) xc_methodhook).replaceHookedMethod(param);\n" +
                    "      return (" + namess.toString() + ")param.getResult();\n" +
                    "  } else {\n" +
                    "       param = xc_methodhook.beforeHookedMethod(param);\n" +
                    "       param.setResult(" + tyoeindes + ");\n" +
                    "       param = xc_methodhook.afterHookedMethod(param);\n" +
                    "       return (" + namess.toString() + ")param.getResult();\n" +
                    " }\n" +
                    "}catch (Throwable e){\n" +
                    "   e.printStackTrace();\n" +
                    "}\n" +
                    "return " + tyoeindess + "");
            bindViewMethodSpecBuilders.addStatement("return " + Utils.revals(name));
            TypeSpec typeSpec = TypeSpec.classBuilder(element.getSimpleName() + "$Hook")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(bindViewMethodSpecBuilder.build())
                    .addMethod(bindViewMethodSpecBuilders.build())
                    .addMethod(bind.build())
                    .addField(fieldSpec.build())
                    .build();
            JavaFile javaFile = JavaFile.builder(Utils.getPackageName(typeElement), typeSpec).build();
            javaFile.writeTo(processingEnv.getFiler());
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
