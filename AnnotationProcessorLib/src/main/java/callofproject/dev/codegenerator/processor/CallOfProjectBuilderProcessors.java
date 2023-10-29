package callofproject.dev.codegenerator.processor;

import callofproject.dev.codegenerator.annotation.CoPBuilder;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("callofproject.dev.codegenerator.annotation.CoPBuilder")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class CallOfProjectBuilderProcessors extends AbstractProcessor
{
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(CoPBuilder.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                generateBuilderClass(typeElement);
            }
        }
        return true;
    }

    private void generateBuilderClass(TypeElement typeElement) {
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        String className = typeElement.getSimpleName().toString();
        String builderClassName = className + "Builder";


        if (classExists(packageName, builderClassName)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, builderClassName + " already exists in package " + packageName);
            return;
        }

        try {
            JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(packageName + "." + builderClassName);
            try (PrintWriter writer = new PrintWriter(builderFile.openWriter())) {
                // Package declaration
                if (!packageName.isEmpty()) {
                    writer.println("package " + packageName + ";");
                    writer.println();
                }

                // Generate Person class with private fields
                writer.println("public class " + className + " {");
                for (Element enclosedElement : typeElement.getEnclosedElements()) {
                    if (enclosedElement.getKind() == ElementKind.FIELD) {
                        VariableElement field = (VariableElement) enclosedElement;
                        writer.println("    private " + field.asType() + " " + field.getSimpleName() + ";");
                    }
                }
                writer.println();

                // Generate Builder class
                writer.println("    public static class " + builderClassName + " {");
                writer.println("        private final " + className + " " + className.toLowerCase() + " = new " + className + "();");
                writer.println();

                // Generate setter methods
                for (Element enclosedElement : typeElement.getEnclosedElements()) {
                    if (enclosedElement.getKind() == ElementKind.FIELD) {
                        VariableElement field = (VariableElement) enclosedElement;
                        String fieldName = field.getSimpleName().toString();
                        String fieldType = field.asType().toString();
                        writer.println("        public " + builderClassName + " set" + capitalize(fieldName) + "(" + fieldType + " " + fieldName + ") {");
                        writer.println("            " + className.toLowerCase() + "." + fieldName + " = " + fieldName + ";");
                        writer.println("            return this;");
                        writer.println("        }");
                    }
                }
                writer.println();

                // Generate build method
                writer.println("        public " + className + " build() {");
                writer.println("            return " + className.toLowerCase() + ";");
                writer.println("        }");

                writer.println("    }");
                writer.println("}");
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

    private boolean classExists(String packageName, String className) {
        Elements elementUtils = processingEnv.getElementUtils();
        TypeElement typeElement = elementUtils.getTypeElement(packageName + "." + className);
        return typeElement != null;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}