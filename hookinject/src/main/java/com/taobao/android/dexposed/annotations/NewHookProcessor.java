package com.taobao.android.dexposed.annotations;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Created by zhy on 16/4/22.
 */
@AutoService(Processor.class)
public class NewHookProcessor extends AbstractProcessor {
    private Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<String>(2);
        annotataions.add(Hook.class.getCanonicalName());
        annotataions.add(Hooks.class.getCanonicalName());
        return annotataions;
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Hook.class);
        for (Element element : elements) {
            getHook(element);
        }
        Set<? extends Element> elementss = roundEnvironment.getElementsAnnotatedWith(Hooks.class);
        for (Element element : elementss) {
            getHooks(element);
        }
        return false;
    }

    private String getPackageName(TypeElement typeElement) {
        return elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    public void getHook(Element element) {
        try {
            // 判断是否Class
            TypeElement typeElement = (TypeElement) element;
            Hook hook = element.getAnnotation(Hook.class);
            ClassName className = ClassName.get("com.taobao.android.dexposed", "XC_MethodHook");
            MethodSpec.Builder bindViewMethodSpecBuilder = MethodSpec.methodBuilder("HookMethod")
                    .addException(Throwable.class)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.OBJECT);
            MethodSpec.Builder bindViewMethodSpecBuilders = MethodSpec.methodBuilder("OriginalHookMethod")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.OBJECT);
            MethodSpec.Builder bind = MethodSpec.methodBuilder("setXC_MethodHook")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(className, "xc_methodhooks")
                    .addStatement("if(xc_methodhook == null){\n xc_methodhook = xc_methodhooks;\n}\n");
            Class[] classes;
            int leng = 0;
            String typeindex = "param.args = new Object[]{";
            String tyoeindes = "OriginalHookMethod(";
            try {
                classes = hook.Type();
                leng = classes.length;
            } catch (MirroredTypesException mte) {
                List<? extends TypeMirror> classTypeMirror = mte.getTypeMirrors();
                leng = classTypeMirror.size();
            }

            for (int i = 0; i < leng; i++) {
                String ss = "object" + i;
                bindViewMethodSpecBuilder.addParameter(TypeName.OBJECT, ss);
                bindViewMethodSpecBuilders.addParameter(TypeName.OBJECT, ss);
                if (i == leng - 1) {
                    typeindex = typeindex.concat(ss + "};");
                    tyoeindes = tyoeindes.concat("param.args[" + i + "])");
                } else {
                    typeindex = typeindex.concat(ss + ",");
                    tyoeindes = tyoeindes.concat("param.args[" + i + "],");
                }
            }
            if (leng == 0) {
                typeindex = "param.args = null;";
                tyoeindes = tyoeindes.concat(")");
            }
            FieldSpec.Builder fieldSpec = FieldSpec.builder(className, "xc_methodhook");
            fieldSpec.addModifiers(Modifier.PRIVATE, Modifier.STATIC);
            fieldSpec.initializer("null");
            bindViewMethodSpecBuilder.addStatement("" +
                    "Object object = new Object();\n" +
                    "if(xc_methodhook == null){\n" +
                    "       return object;\n" +
                    "}\n" +
                    "try{\n" +
                    "XC_MethodHook.MethodHookParam param = new XC_MethodHook.MethodHookParam();\n" +
                    "param.Model = com.taobao.android.dexposed.DexposedBridge.ART;\n" +
                    "param.HookMethod = xc_methodhook;\n" +
                    typeindex +
                    "\nif (xc_methodhook instanceof com.taobao.android.dexposed.XC_MethodReplacement) {\n" +
                    "      ((com.taobao.android.dexposed.XC_MethodReplacement) xc_methodhook).replaceHookedMethod(param);\n" +
                    "      object = param.getResult();\n" +
                    "  } else {\n" +
                    "       xc_methodhook.beforeHookedMethod(param);\n" +
                    "       param.setResult(" + tyoeindes + ");\n" +
                    "       xc_methodhook.afterHookedMethod(param);\n" +
                    "       object = param.getResult();\n" +
                    " }\n" +
                    "}catch (Throwable e){\n" +
                    "   e.printStackTrace();\n" +
                    "}\n" +
                    "return object");
            bindViewMethodSpecBuilders.addStatement("return new Object()");
            TypeSpec typeSpec = TypeSpec.classBuilder(element.getSimpleName() + "$Hook")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(bindViewMethodSpecBuilder.build())
                    .addMethod(bindViewMethodSpecBuilders.build())
                    .addMethod(bind.build())
                    .addField(fieldSpec.build())
                    .build();
            JavaFile javaFile = JavaFile.builder(getPackageName(typeElement), typeSpec).build();

            javaFile.writeTo(processingEnv.getFiler());
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void getHooks(Element element) {
        try {
            TypeElement typeElement = (TypeElement) element;
            Hooks hook = element.getAnnotation(Hooks.class);
            ClassName className = ClassName.get("com.taobao.android.dexposed", "XC_MethodHook");
            MethodSpec.Builder bindViewMethodSpecBuilder = MethodSpec.methodBuilder("HookMethod")
                    .addException(Throwable.class)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.OBJECT);
            MethodSpec.Builder bindViewMethodSpecBuilders = MethodSpec.methodBuilder("OriginalHookMethod")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.OBJECT);

            MethodSpec.Builder bind = MethodSpec.methodBuilder("setXC_MethodHook")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(className, "xc_methodhooks")
                    .addStatement("if(xc_methodhook == null){\n xc_methodhook = xc_methodhooks;\n}");
            String[] classes;
            int leng = 0;
            String typeindex = "param.args = new Object[]{";
            String tyoeindes = "OriginalHookMethod(";
            try {
                classes = hook.Type();
                leng = classes.length;
            } catch (MirroredTypesException mte) {
                List<? extends TypeMirror> classTypeMirror = mte.getTypeMirrors();
                leng = classTypeMirror.size();
            }
            for (int i = 0; i < leng; i++) {
                String ss = "object" + i;
                bindViewMethodSpecBuilder.addParameter(TypeName.OBJECT, ss);
                bindViewMethodSpecBuilders.addParameter(TypeName.OBJECT, ss);
                if (i == leng - 1) {
                    typeindex = typeindex.concat(ss + "};");
                    tyoeindes = tyoeindes.concat("param.args[" + i + "])");
                } else {
                    typeindex = typeindex.concat(ss + ",");
                    tyoeindes = tyoeindes.concat("param.args[" + i + "],");
                }
            }
            if (leng == 0) {
                typeindex = "param.args = null;";
                tyoeindes = tyoeindes.concat(")");
            }
            FieldSpec.Builder fieldSpec = FieldSpec.builder(className, "xc_methodhook");
            fieldSpec.addModifiers(Modifier.PRIVATE, Modifier.STATIC);
            fieldSpec.initializer("null");
            bindViewMethodSpecBuilder.addStatement("" +
                    "Object object = new Object();\n" +
                    "if(xc_methodhook == null){\n" +
                    "       return object;\n" +
                    "}\n" +
                    "try{\n" +
                    "XC_MethodHook.MethodHookParam param = new XC_MethodHook.MethodHookParam();\n" +
                    "param.Model = com.taobao.android.dexposed.DexposedBridge.ART;\n" +
                    "param.HookMethod = xc_methodhook;\n" +
                    typeindex +
                    "\nif (xc_methodhook instanceof com.taobao.android.dexposed.XC_MethodReplacement) {\n" +
                    "      ((com.taobao.android.dexposed.XC_MethodReplacement) xc_methodhook).replaceHookedMethod(param);\n" +
                    "      object = param.getResult();\n" +
                    "  } else {\n" +
                    "       xc_methodhook.beforeHookedMethod(param);\n" +
                    "       param.setResult(" + tyoeindes + ");\n" +
                    "       xc_methodhook.afterHookedMethod(param);\n" +
                    "       object = param.getResult();\n" +
                    " }\n" +
                    "}catch (Throwable e){\n" +
                    "   e.printStackTrace();\n" +
                    "}\n" +
                    "return object");
            bindViewMethodSpecBuilders.addStatement("return new Object()");
            TypeSpec typeSpec = TypeSpec.classBuilder(element.getSimpleName() + "$Hook")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(bindViewMethodSpecBuilder.build())
                    .addMethod(bindViewMethodSpecBuilders.build())
                    .addMethod(bind.build())
                    .addField(fieldSpec.build())
                    .build();
            JavaFile javaFile = JavaFile.builder(getPackageName(typeElement), typeSpec).build();

            javaFile.writeTo(processingEnv.getFiler());
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
