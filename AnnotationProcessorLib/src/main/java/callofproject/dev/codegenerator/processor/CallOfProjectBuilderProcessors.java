package callofproject.dev.codegenerator.processor;

import callofproject.dev.codegenerator.annotation.CoPBuilder;
import com.google.auto.service.AutoService;
import org.openjdk.javax.lang.model.type.ExecutableType;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
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
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        var annotatedElements = roundEnv.getElementsAnnotatedWith(CoPBuilder.class);
        var elementList = annotatedElements.stream().toList();

        if (elementList.isEmpty())
            return false;

        var types = elementList.stream().map(i -> (TypeElement)i).toList();
        var className = types.get(0).getQualifiedName().toString();

        try
        {
            writeFile(className, types);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return true;
    }

    private void writeFile(String className, List<TypeElement> types) throws IOException
    {
        var classNameStartIdx = className.lastIndexOf('.');
        var packageName = "";

        if (classNameStartIdx > 0)
            packageName = className.substring(0, classNameStartIdx);

        var builderClassName = className + " " + "Builder";
        var simpleBuilderClassName = builderClassName.substring(classNameStartIdx + 1);

        JavaFileObject builderClass =processingEnv.getFiler().createSourceFile(builderClassName);
        var simpleClassName = className.substring(classNameStartIdx + 1);

        try(var printWriter = new PrintWriter(builderClass.openWriter()))
        {
            if (!(packageName.isBlank() && packageName.isEmpty()))
            {
                printWriter.print("package ");
                printWriter.print(packageName);
                printWriter.println(";");
                printWriter.println();
            }

            printWriter.print("public class ");
            printWriter.print(simpleBuilderClassName);
            printWriter.print("{");
            printWriter.println();

            printWriter.print("    private ");
            printWriter.print("var");
            printWriter.print(" callOfProjectObject = new ");
            printWriter.print(simpleClassName);
            printWriter.println("();");
            printWriter.println();

            printWriter.print("    public ");
            printWriter.print(simpleClassName);
            printWriter.println(" build() {");
            printWriter.println("        return object;");
            printWriter.println("    }");
            printWriter.println();


            for (TypeElement element : types)
            {
                for (Element enclosedElement : element.getEnclosedElements()) {
                    if (enclosedElement.getKind() == ElementKind.FIELD) {
                        VariableElement fieldElement = (VariableElement) enclosedElement;

                        var fieldName = fieldElement.getSimpleName().toString();
                        var fieldType = fieldElement.asType().toString();

                        
                    }
                }
            }

        }

    }
}
