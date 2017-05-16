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
 * 作者：zhangzhongping on 17/4/16 13:33
 * 邮箱：android_dy@163.com
 */
public class getHook {

    public static void getHook(Element element, ProcessingEnvironment processingEnv) {
        try {
            TypeElement typeElement = (TypeElement) element;
            Hook hook = element.getAnnotation(Hook.class);
            ClassName className = ClassName.get("com.taobao.android.dexposed", "XC_MethodHook");
            MethodSpec.Builder bindViewMethodSpecBuilder = MethodSpec.methodBuilder("HookMethod")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC);
            MethodSpec.Builder bindViewMethodSpecBuilders = MethodSpec.methodBuilder("OriginalHookMethod")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC);
            MethodSpec.Builder bind = MethodSpec.methodBuilder("setXC_MethodHook")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .addParameter(className, "xc_methodhooks")
                    .addStatement("xc_methodhook = xc_methodhooks");
            Class[] classes = null;
            int leng;
            String typeindex = "param.args = new Object[]{";
            String tyoeindes = "OriginalHookMethod(";
            String tyoeindess = "OriginalHookMethod(";
            List<? extends TypeMirror> classTypeMirror = null;
            try {
                classes = hook.Parameter();
                leng = classes.length;
            } catch (MirroredTypesException mte) {
                classTypeMirror = mte.getTypeMirrors();
                leng = classTypeMirror.size();
            }
            String name;
            try {
                name = hook.returnVal().getName();
            } catch (MirroredTypeException e) {
                TypeMirror classTypeMirro = e.getTypeMirror();
                name = classTypeMirro.toString();
            }
            boolean isStatic = hook.isStatic();
            if (!isStatic) {
                bindViewMethodSpecBuilder.addParameter(TypeName.OBJECT, "thiz");
                bindViewMethodSpecBuilders.addParameter(TypeName.OBJECT, "thiz");
                tyoeindess = tyoeindess.concat("thiz,");
                tyoeindes = tyoeindes.concat("thiz,");
            }
            for (int i = 0; i < leng; i++) {
                String ss = "object" + i;
                if (classes != null && classes.length > 0) {
                    bindViewMethodSpecBuilder.addParameter(Utils.Parameter(classes[i].getName()), ss);
                    bindViewMethodSpecBuilders.addParameter(Utils.Parameter(classes[i].getName()), ss);
                } else {
                    bindViewMethodSpecBuilder.addParameter(Utils.Parameter(classTypeMirror.get(i).toString()), ss);
                    bindViewMethodSpecBuilders.addParameter(Utils.Parameter(classTypeMirror.get(i).toString()), ss);
                }
                if (i == leng - 1) {
                    typeindex = typeindex.concat(ss + "};");
                    tyoeindess = tyoeindess.concat(ss + ")");
                    if (classes != null && classes.length > 0) {
                        tyoeindes = tyoeindes.concat("(" + Utils.Parameter(classes[i].getName()).toString() + ")param.args[" + i + "])");
                    } else {
                        tyoeindes = tyoeindes.concat("(" + Utils.Parameter(classTypeMirror.get(i).toString()).toString() + ")param.args[" + i + "])");
                    }
                } else {
                    typeindex = typeindex.concat(ss + ",");
                    tyoeindess = tyoeindess.concat(ss + ",");
                    if (classes != null && classes.length > 0) {
                        tyoeindes = tyoeindes.concat("(" + Utils.Parameter(classes[i].getName()).toString() + ")param.args[" + i + "],");
                    } else {
                        tyoeindes = tyoeindes.concat("(" + Utils.Parameter(classTypeMirror.get(i).toString()).toString() + ")param.args[" + i + "],");
                    }
                }
            }
            if (leng == 0 ) {
                typeindex = "param.args = null;";
                tyoeindes = tyoeindes.concat(")").replace(",", "");
                tyoeindess = tyoeindess.concat(")").replace(",", "");
            }
            TypeName namess = Utils.Parameter(name);
            bindViewMethodSpecBuilder.returns(namess);
            bindViewMethodSpecBuilders.returns(namess);
            FieldSpec.Builder fieldSpec = FieldSpec.builder(className, "xc_methodhook");
            fieldSpec.addModifiers(Modifier.PRIVATE, Modifier.STATIC);
            fieldSpec.initializer("null");
            String ret = (String) Utils.ReturnValue(name);
            String aptindex = "return".equals(ret) ? tyoeindess.concat(";\nreturn;\n") : "return ".concat(tyoeindess).concat(";\n");
            String aptindex2 = "return".equals(ret) ? tyoeindess : "return ".concat(tyoeindess).concat("");
            String aptindex1 = "return".equals(ret) ? "return;\n" : "return (".concat(namess.toString()).concat(")param.getResult();\n");
            String yunindex = "return".equals(ret) ? tyoeindes.concat(";\n") : "param.setResult(".concat(tyoeindes).concat(");\n");
            bindViewMethodSpecBuilder.addStatement("" +
                    " if(xc_methodhook == null){\n" +
                    "" + aptindex + "" +
                    "}\n" +
                    "try{\n" +
                    "XC_MethodHook.MethodHookParam param = new XC_MethodHook.MethodHookParam();\n" +
                    "param.Model = com.taobao.android.dexposed.DexposedBridge.ART;\n" +
                    "param.HookMethod = xc_methodhook;\n" +
                    typeindex +
                    "\nif (xc_methodhook instanceof com.taobao.android.dexposed.XC_MethodReplacement) {\n" +
                    "      param = ((com.taobao.android.dexposed.XC_MethodReplacement) xc_methodhook).replaceHookedMethod(param);\n" +
                    "" + aptindex1 + "" +
                    "  } else {\n" +
                    "       param = xc_methodhook.beforeHookedMethod(param);\n" +
                    "" + yunindex + "" +
                    "       param = xc_methodhook.afterHookedMethod(param);\n" +
                    "" + aptindex1 + "" +
                    " }\n" +
                    "}catch (Throwable e){\n" +
                    "   e.printStackTrace();\n" +
                    "}\n" +
                    "" + aptindex2 + "");
            bindViewMethodSpecBuilders.addStatement("System.out.print(String.class);\n"+String.valueOf(Utils.ReturnValue(name)));
            TypeSpec typeSpec = TypeSpec.classBuilder(element.getSimpleName() + "$$Hook")
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
