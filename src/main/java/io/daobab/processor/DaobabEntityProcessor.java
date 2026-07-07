package io.daobab.processor;

import io.daobab.annotation.DaobabColumn;
import io.daobab.annotation.DaobabTable;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Generates daobab entities out of the {@link DaobabTable} definitions during the compilation.
 * <p>
 * For every definition interface the processor generates:
 * <ul>
 *     <li>a column interface per definition method (skipped when a matching interface already exists),</li>
 *     <li>the entity class extending {@code Table}, implementing the column interfaces
 *     and, when a primary key column is marked, the {@code PrimaryKey} interface.</li>
 * </ul>
 * The processor is based purely on the JDK annotation processing API - no third party libraries.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class DaobabEntityProcessor extends AbstractProcessor {

    /**
     * Column interfaces generated in this compilation: fully qualified name -&gt; field type.
     * Lets many definitions share one column interface and detects the type conflicts.
     */
    private final Map<String, String> generatedColumns = new HashMap<>();

    private static String stripDefinitionSuffix(String definitionName) {
        if (definitionName.endsWith("Definition")) {
            return definitionName.substring(0, definitionName.length() - "Definition".length());
        }
        if (definitionName.endsWith("Def")) {
            return definitionName.substring(0, definitionName.length() - "Def".length());
        }
        return definitionName;
    }

    private static String capitalize(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private static String toUpperSnakeCase(String name) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append('_');
            }
            sb.append(Character.toUpperCase(c));
        }
        return sb.toString();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(DaobabTable.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(DaobabTable.class)) {
            if (element.getKind() != ElementKind.INTERFACE) {
                error(element, "@DaobabTable may annotate an interface only");
                continue;
            }
            try {
                generateEntity((TypeElement) element);
            } catch (IOException e) {
                error(element, "Cannot write a generated source: " + e.getMessage());
            }
        }
        return true;
    }

    private void generateEntity(TypeElement definition) throws IOException {
        DaobabTable table = definition.getAnnotation(DaobabTable.class);

        String definitionPackage = processingEnv.getElementUtils().getPackageOf(definition).getQualifiedName().toString();
        String definitionName = definition.getSimpleName().toString();

        String entityName = table.entityName().isEmpty() ? stripDefinitionSuffix(definitionName) : table.entityName();
        String entityPackage = table.entityPackage().isEmpty() ? definitionPackage : table.entityPackage();
        String columnPackage = table.columnPackage().isEmpty() ? definitionPackage + ".column" : table.columnPackage();
        String tableName = table.tableName().isEmpty() ? toUpperSnakeCase(entityName) : table.tableName();

        if (entityName.equals(definitionName) && entityPackage.equals(definitionPackage)) {
            error(definition, "The generated entity would collide with its definition."
                    + " Rename the definition (e.g. " + definitionName + "Def) or set the entityName/entityPackage attribute.");
            return;
        }

        List<ColumnModel> columns = readColumns(definition);
        if (columns == null) {
            return;
        }
        if (columns.isEmpty()) {
            error(definition, "The definition has no column methods");
            return;
        }
        if (columns.stream().filter(c -> c.primaryKey).count() > 1) {
            error(definition, "Composite primary keys are not supported: mark at most one column with primaryKey = true");
            return;
        }

        for (ColumnModel column : columns) {
            if (!ensureColumnInterface(definition, columnPackage, column)) {
                return;
            }
        }

        writeEntity(definition, entityPackage, columnPackage, entityName, tableName, columns);
    }

    /**
     * Every abstract, parameterless method of the definition describes one column.
     */
    private List<ColumnModel> readColumns(TypeElement definition) {
        List<ColumnModel> columns = new ArrayList<>();

        for (ExecutableElement method : ElementFilter.methodsIn(definition.getEnclosedElements())) {
            if (method.getModifiers().contains(Modifier.DEFAULT) || method.getModifiers().contains(Modifier.STATIC)) {
                continue;
            }
            if (!method.getParameters().isEmpty() || method.getReturnType().getKind() == TypeKind.VOID) {
                error(method, "A column method has to be parameterless and has to return the column type");
                return null;
            }

            DaobabColumn settings = method.getAnnotation(DaobabColumn.class);
            String methodName = method.getSimpleName().toString();

            ColumnModel column = new ColumnModel();
            column.fieldName = capitalize(methodName);
            column.fieldType = boxedTypeName(method.getReturnType());
            if (settings == null) {
                column.columnName = toUpperSnakeCase(methodName);
            } else {
                column.columnName = settings.name().isEmpty() ? toUpperSnakeCase(methodName) : settings.name();
                column.primaryKey = settings.primaryKey();
                column.size = settings.size();
                column.precision = settings.precision();
                column.scale = settings.scale();
                column.notNull = settings.notNull();
                column.unique = settings.unique();
                column.lob = settings.lob();
            }
            columns.add(column);
        }
        return columns;
    }

    /**
     * Generates the column interface unless a matching one already exists.
     * Column interfaces are shared between the entities, so a type conflict is an error.
     *
     * @return false when the compilation should fail
     */
    private boolean ensureColumnInterface(TypeElement definition, String columnPackage, ColumnModel column) throws IOException {
        String fqcn = columnPackage + "." + column.fieldName;

        String alreadyGeneratedType = generatedColumns.get(fqcn);
        if (alreadyGeneratedType != null) {
            if (!alreadyGeneratedType.equals(column.fieldType)) {
                error(definition, "Column " + column.fieldName + " is already generated with the type " + alreadyGeneratedType
                        + ", the type " + column.fieldType + " conflicts with it");
                return false;
            }
            return true;
        }

        TypeElement existing = processingEnv.getElementUtils().getTypeElement(fqcn);
        if (existing != null) {
            String existingType = getterTypeOf(existing, column.fieldName);
            if (existingType != null && !existingType.equals(column.fieldType)) {
                error(definition, "The existing column interface " + fqcn + " keeps the type " + existingType
                        + ", the type " + column.fieldType + " conflicts with it");
                return false;
            }
            generatedColumns.put(fqcn, column.fieldType);
            return true;
        }

        writeColumnInterface(definition, columnPackage, column);
        generatedColumns.put(fqcn, column.fieldType);
        return true;
    }

    private String getterTypeOf(TypeElement columnInterface, String fieldName) {
        for (ExecutableElement method : ElementFilter.methodsIn(columnInterface.getEnclosedElements())) {
            if (method.getSimpleName().contentEquals("get" + fieldName) && method.getParameters().isEmpty()) {
                return boxedTypeName(method.getReturnType());
            }
        }
        return null;
    }

    private void writeColumnInterface(TypeElement definition, String columnPackage, ColumnModel column) throws IOException {
        String name = column.fieldName;
        String type = column.fieldType;

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(columnPackage).append(";\n\n");
        sb.append("import io.daobab.creation.DaobabCache;\n");
        sb.append("import io.daobab.model.*;\n\n");
        sb.append("@SuppressWarnings(\"unused\")\n");
        sb.append("public interface ").append(name).append("<E extends Entity> extends RelatedTo<E>, MapHandler<E> {\n\n");
        sb.append("\tdefault ").append(type).append(" get").append(name).append("() {\n");
        sb.append("\t\treturn readParam(\"").append(name).append("\");\n");
        sb.append("\t}\n\n");
        sb.append("\tdefault E set").append(name).append("(").append(type).append(" val) {\n");
        sb.append("\t\treturn storeParam(\"").append(name).append("\", val);\n");
        sb.append("\t}\n\n");
        sb.append("\t@SuppressWarnings({\"rawtypes\", \"unchecked\"})\n");
        sb.append("\tdefault Column<E, ").append(type).append(", ").append(name).append("> col").append(name).append("() {\n");
        sb.append("\t\treturn DaobabCache.getColumn(\"").append(name).append("\", \"").append(column.columnName)
                .append("\", (Table<?>) this, ").append(type).append(".class);\n");
        sb.append("\t}\n");
        sb.append("}\n");

        writeSource(columnPackage + "." + name, sb.toString(), definition);
    }

    private void writeEntity(TypeElement definition, String entityPackage, String columnPackage,
                             String entityName, String tableName, List<ColumnModel> columns) throws IOException {

        ColumnModel pk = columns.stream().filter(c -> c.primaryKey).findFirst().orElse(null);

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(entityPackage).append(";\n\n");
        sb.append("import io.daobab.creation.DaobabCache;\n");
        sb.append("import io.daobab.model.*;\n");
        for (ColumnModel column : columns) {
            sb.append("import ").append(columnPackage).append(".").append(column.fieldName).append(";\n");
        }
        sb.append("import java.util.*;\n\n");

        sb.append("@SuppressWarnings({\"rawtypes\", \"unused\"})\n");
        sb.append("@TableInformation(name = \"").append(tableName).append("\")\n");
        sb.append("public class ").append(entityName).append(" extends Table<").append(entityName).append("> implements\n");
        for (ColumnModel column : columns) {
            sb.append("\t\t").append(column.fieldName).append("<").append(entityName).append(">,\n");
        }
        if (pk != null) {
            sb.append("\t\tPrimaryKey<").append(entityName).append(", ").append(pk.fieldType).append(", ").append(pk.fieldName).append("> {\n\n");
        } else {
            //no primary key: close the implements list on the last column interface
            sb.setLength(sb.length() - 2);
            sb.append(" {\n\n");
        }

        sb.append("\tpublic ").append(entityName).append("() {\n\t\tsuper();\n\t}\n\n");
        sb.append("\tpublic ").append(entityName).append("(Map<String, Object> parameters) {\n\t\tsuper(parameters);\n\t}\n\n");

        sb.append("\t@Override\n");
        sb.append("\tpublic List<TableColumn> columns() {\n");
        sb.append("\t\treturn DaobabCache.getTableColumns(this,\n");
        sb.append("\t\t\t\t() -> Arrays.asList(\n");
        for (int i = 0; i < columns.size(); i++) {
            ColumnModel column = columns.get(i);
            sb.append("\t\t\t\t\t\tnew TableColumn(col").append(column.fieldName).append("())");
            if (column.primaryKey) sb.append(".primaryKey()");
            if (column.size > 0) sb.append(".size(").append(column.size).append(")");
            if (column.precision > 0) sb.append(".precision(").append(column.precision).append(")");
            if (column.scale > 0) sb.append(".scale(").append(column.scale).append(")");
            if (column.notNull) sb.append(".notNull()");
            if (column.unique) sb.append(".unique()");
            if (column.lob) sb.append(".lob()");
            sb.append(i < columns.size() - 1 ? ",\n" : "\n");
        }
        sb.append("\t\t\t\t));\n");
        sb.append("\t}\n");

        if (pk != null) {
            sb.append("\n\t@Override\n");
            sb.append("\tpublic Column<").append(entityName).append(", ").append(pk.fieldType).append(", ").append(pk.fieldName).append("> colID() {\n");
            sb.append("\t\treturn col").append(pk.fieldName).append("();\n");
            sb.append("\t}\n");
            sb.append("\n\t@Override\n");
            sb.append("\tpublic int hashCode() {\n");
            sb.append("\t\treturn Objects.hashCode(getId());\n");
            sb.append("\t}\n");
            sb.append("\n\t@Override\n");
            sb.append("\tpublic boolean equals(Object obj) {\n");
            sb.append("\t\tif (this == obj) return true;\n");
            sb.append("\t\tif (obj == null) return false;\n");
            sb.append("\t\tif (getClass() != obj.getClass()) return false;\n");
            sb.append("\t\tPrimaryKey<?, ?, ?> other = (PrimaryKey<?, ?, ?>) obj;\n");
            sb.append("\t\treturn Objects.equals(getId(), other.getId());\n");
            sb.append("\t}\n");
        }
        sb.append("}\n");

        writeSource(entityPackage + "." + entityName, sb.toString(), definition);
    }

    private void writeSource(String fqcn, String content, Element originatingElement) throws IOException {
        JavaFileObject file = processingEnv.getFiler().createSourceFile(fqcn, originatingElement);
        try (Writer writer = file.openWriter()) {
            writer.write(content);
        }
    }

    /**
     * Returns the boxed, erased type name, ready to be used in the generated source and in a class literal.
     */
    private String boxedTypeName(TypeMirror type) {
        if (type.getKind().isPrimitive()) {
            return processingEnv.getTypeUtils().boxedClass((PrimitiveType) type).getQualifiedName().toString();
        }
        return processingEnv.getTypeUtils().erasure(type).toString();
    }

    private void error(Element element, String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    private static final class ColumnModel {
        String fieldName;
        String columnName;
        String fieldType;
        boolean primaryKey;
        int size;
        int precision;
        int scale;
        boolean notNull;
        boolean unique;
        boolean lob;
    }
}
