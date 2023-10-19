package ru.otus.java.annotations;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes("ru.otus.java.annotations.CustomToStringAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class CustomToStringAnnotationProcessor extends AbstractProcessor {
    private boolean isToStringMethodAlreadyPresent(Element annotatedElement) {
        for (Element element : annotatedElement.getEnclosedElements()) {
            if (element.getSimpleName().toString().equals("toString") && element.getKind() == ElementKind.METHOD) {
                return true;
            }
        }
        return false;
    }
    private void generateSourceWithCustomToStringMethod(Element annotatedElement) throws IOException {
        String className = ((TypeElement) annotatedElement).getQualifiedName().toString();
        String classSimpleName = annotatedElement.getSimpleName().toString();
        String packageName = ((PackageElement) annotatedElement.getEnclosingElement()).getQualifiedName().toString();
        String classWithCustomToStringMethodSimpleName = classSimpleName + "WithCustomToString";

        if (isToStringMethodAlreadyPresent(annotatedElement)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                    "Method toString is already present in class " + className);
            return;
        }

        JavaFileObject builderFile = processingEnv.getFiler()
               .createSourceFile(classWithCustomToStringMethodSimpleName);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package " + packageName + ";\n");
            out.println("public class " + classWithCustomToStringMethodSimpleName +
                    " extends " + classSimpleName + " {");
            out.println("\t@Override");
            out.println("\tpublic String toString() {");
            out.println("\t\treturn \"" + classWithCustomToStringMethodSimpleName + " {\\n\" +");
            for (Element annotatedElementChild : annotatedElement.getEnclosedElements()) {
                if (annotatedElementChild.getKind().isField() &&
                        (annotatedElementChild.getModifiers().contains(Modifier.PUBLIC) ||
                                annotatedElementChild.getModifiers().contains(Modifier.PROTECTED))) {
                    out.println("\t\t\t\"\\t" + annotatedElementChild.getSimpleName().toString() + " = \\\"\" + "
                        + annotatedElementChild.getSimpleName().toString() + " + \"\\\"\\n\" +");
                }
            }
            out.println("\t\t\t\"}\";");
            out.println("\t}");
            out.println("}");
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements
                    = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element annotatedElement : annotatedElements) {
                try {
                    generateSourceWithCustomToStringMethod(annotatedElement);
                } catch (IOException e) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                    return false;
                }
            }
        }

        return true;
    }
}
