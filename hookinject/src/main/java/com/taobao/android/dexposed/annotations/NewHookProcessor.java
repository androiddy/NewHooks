package com.taobao.android.dexposed.annotations;

import com.google.auto.service.AutoService;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Created by zhy on 16/4/22.
 */
@AutoService(Processor.class)
public class NewHookProcessor extends AbstractProcessor {


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Utils.Init();
        Utils.Inits();
        Set<String> annotataions = new LinkedHashSet<String>(2);
        annotataions.add(Hook.class.getCanonicalName());
        annotataions.add(Hooks.class.getCanonicalName());
        return annotataions;
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Hook.class);
        for (Element element : elements) {
            getHook.getHook(element,processingEnv);
        }
        Set<? extends Element> elementss = roundEnvironment.getElementsAnnotatedWith(Hooks.class);
        for (Element element : elementss) {
            getHooks.getHooks(element,processingEnv);
        }
        return false;
    }



    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Utils.setElementUtils(processingEnv.getElementUtils());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

}
