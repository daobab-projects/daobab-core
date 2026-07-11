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
 *     <li>a column interface per definition method (reused when a matching interface already exists,
 *     or disambiguated with a type suffix when a column of the same name but a different type shows up),</li>
 *     <li>the entity class (named with the 'Entity' suffix) extending {@code DtoTable}, implementing
 *     the column interfaces and, when a primary key column is marked, the {@code PrimaryKey} interface,</li>
 *     <li>an immutable DTO class with a builder, connected to the entity by the
 *     {@code toDto()}/{@code fromDto(dto)} conversion methods.</li>
 * </ul>
 * The processor is based purely on the JDK annotation processing API - no third party libraries.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class DaobabEntityProcessor extends AbstractProcessor {

    /**
     * Column interfaces generated in this compilation: fully qualified name -&gt; field type.
     * Lets many definitions share one column interface and disambiguates the type conflicts.
     */
    private final Map<String, String> generatedColumns = new HashMap<>();

    /**
     * Column interface usage collected across all the definitions of the round: usage key -&gt; usages.
     * Feeds the documentation table generated above each {@code col...()} method.
     */
    private final Map<String, List<ColumnUsage>> columnUsage = new HashMap<>();

    private static String stripDefinitionSuffix(String definitionName) {
        if (definitionName.endsWith("Definition")) {
            return definitionName.substring(0, definitionName.length() - "Definition".length());
        }
        if (definitionName.endsWith("Def")) {
            return definitionName.substring(0, definitionName.length() - "Def".length());
        }
        return definitionName;
    }

    private static String stripEntitySuffix(String entityName) {
        if (entityName.endsWith("Entity") && entityName.length() > "Entity".length()) {
            return entityName.substring(0, entityName.length() - "Entity".length());
        }
        return entityName;
    }

    private static String capitalize(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private static String decapitalize(String name) {
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
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

    /**
     * The disambiguating suffix appended to a column name on a type clash, mirroring the generator:
     * {@code java.lang.Integer -> TypeInteger}, {@code byte[] -> TypeByteArray}.
     */
    private static String typeSuffix(String fieldType) {
        if (fieldType.endsWith("[]")) {
            return "Type" + capitalize(simpleName(fieldType.substring(0, fieldType.length() - 2))) + "Array";
        }
        return "Type" + simpleName(fieldType);
    }

    private static String simpleName(String fqcn) {
        int dot = fqcn.lastIndexOf('.');
        return dot < 0 ? fqcn : fqcn.substring(dot + 1);
    }

    private static String usageKey(String columnPackage, String fieldName, String fieldType) {
        return columnPackage + "|" + fieldName + "|" + fieldType;
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

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<EntityContext> contexts = new ArrayList<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(DaobabTable.class)) {
            if (element.getKind() != ElementKind.INTERFACE) {
                error(element, "@DaobabTable may annotate an interface only");
                continue;
            }
            EntityContext context = prepare((TypeElement) element);
            if (context != null) {
                contexts.add(context);
            }
        }

        //collect the column usage across all the definitions first, so each column interface can be
        //documented with every table it appears in, together with its type and its size
        columnUsage.clear();
        for (EntityContext context : contexts) {
            for (ColumnModel column : context.columns) {
                columnUsage.computeIfAbsent(usageKey(context.columnPackage, column.fieldName, column.fieldType),
                                k -> new ArrayList<>())
                        .add(new ColumnUsage(context.tableName, column.fieldType, column.size, column.notNull, column.lob));
            }
        }

        for (EntityContext context : contexts) {
            try {
                writeSources(context);
            } catch (IOException e) {
                error(context.definition, "Cannot write a generated source: " + e.getMessage());
            }
        }
        return true;
    }

    /**
     * Reads and validates a definition. Returns the context to generate from, or {@code null} when the
     * definition is invalid (an error is already reported) and has to be skipped.
     */
    private EntityContext prepare(TypeElement definition) {
        DaobabTable table = definition.getAnnotation(DaobabTable.class);

        String definitionPackage = processingEnv.getElementUtils().getPackageOf(definition).getQualifiedName().toString();
        String definitionName = definition.getSimpleName().toString();

        String entityName = table.entityName().isEmpty() ? stripDefinitionSuffix(definitionName) + "Entity" : table.entityName();
        String entityPackage = table.entityPackage().isEmpty() ? definitionPackage : table.entityPackage();
        String columnPackage = table.columnPackage().isEmpty() ? definitionPackage + ".column" : table.columnPackage();
        String tableName = table.tableName().isEmpty() ? toUpperSnakeCase(stripEntitySuffix(entityName)) : table.tableName();

        boolean generateDto = table.generateDto();
        String dtoName = table.dtoName().isEmpty() ? stripDefinitionSuffix(definitionName) : table.dtoName();
        String dtoPackage = table.dtoPackage().isEmpty() ? entityPackage : table.dtoPackage();

        if (entityName.equals(definitionName) && entityPackage.equals(definitionPackage)) {
            error(definition, "The generated entity would collide with its definition."
                    + " Rename the definition (e.g. " + definitionName + "Def) or set the entityName/entityPackage attribute.");
            return null;
        }

        if (generateDto && dtoName.equals(definitionName) && dtoPackage.equals(definitionPackage)) {
            error(definition, "The generated DTO would collide with its definition."
                    + " Rename the definition (e.g. " + definitionName + "Def) or set the dtoName/dtoPackage attribute.");
            return null;
        }

        if (generateDto && dtoName.equals(entityName) && dtoPackage.equals(entityPackage)) {
            error(definition, "The generated DTO would collide with the generated entity."
                    + " Set the dtoName or dtoPackage attribute.");
            return null;
        }

        List<ColumnModel> columns = readColumns(definition);
        if (columns == null) {
            return null;
        }
        if (columns.isEmpty()) {
            error(definition, "The definition has no column methods");
            return null;
        }
        if (columns.stream().filter(c -> c.primaryKey).count() > 1) {
            error(definition, "Composite primary keys are not supported: mark at most one column with primaryKey = true");
            return null;
        }

        EntityContext context = new EntityContext();
        context.definition = definition;
        context.entityPackage = entityPackage;
        context.columnPackage = columnPackage;
        context.entityName = entityName;
        context.tableName = tableName;
        context.columns = columns;
        context.generateDto = generateDto;
        context.dtoPackage = dtoPackage;
        context.dtoName = dtoName;
        return context;
    }

    private void writeSources(EntityContext context) throws IOException {
        for (ColumnModel column : context.columns) {
            if (!ensureColumnInterface(context.definition, context.columnPackage, column)) {
                return;
            }
        }

        if (context.generateDto) {
            writeDto(context.definition, context.dtoPackage, context.dtoName, context.columns);
        }

        writeEntity(context.definition, context.entityPackage, context.columnPackage, context.entityName,
                context.tableName, context.columns, context.generateDto, context.dtoPackage, context.dtoName);
    }

    /**
     * Resolves the column interface for the given column, generating it unless a matching one
     * already exists. Column interfaces are shared between entities by their simple name, so when a
     * column of the same name but a different type shows up, the field name is disambiguated with a
     * type suffix (e.g. {@code TitleTypeInteger}) - the same rule the generator applies - instead of
     * failing the compilation. The resolved name is written back into {@code column.fieldName}, so the
     * entity and the DTO pick it up.
     *
     * @return false when the compilation should fail
     */
    private boolean ensureColumnInterface(TypeElement definition, String columnPackage, ColumnModel column) throws IOException {
        String baseName = column.fieldName;
        List<String> candidates = List.of(baseName, baseName + typeSuffix(column.fieldType));

        for (String candidate : candidates) {
            String fqcn = columnPackage + "." + candidate;

            String alreadyGeneratedType = generatedColumns.get(fqcn);
            if (alreadyGeneratedType != null) {
                if (alreadyGeneratedType.equals(column.fieldType)) {
                    column.fieldName = candidate;
                    return true;
                }
                continue; //the name is taken by another type, fall back to the type-qualified candidate
            }

            TypeElement existing = processingEnv.getElementUtils().getTypeElement(fqcn);
            if (existing != null) {
                String existingType = getterTypeOf(existing, candidate);
                if (existingType != null && !existingType.equals(column.fieldType)) {
                    continue; //a hand-written interface of another type, fall back to the type-qualified candidate
                }
                generatedColumns.put(fqcn, column.fieldType);
                column.fieldName = candidate;
                return true;
            }

            column.fieldName = candidate;
            String doc = columnDoc(candidate, columnUsage.get(usageKey(columnPackage, baseName, column.fieldType)));
            writeColumnInterface(definition, columnPackage, column, doc);
            generatedColumns.put(fqcn, column.fieldType);
            return true;
        }

        error(definition, "Column " + baseName + " of the type " + column.fieldType
                + " conflicts with an already generated column of the same name, and the type-qualified"
                + " name " + baseName + typeSuffix(column.fieldType) + " is taken by yet another type");
        return false;
    }

    /**
     * The Javadoc table rendered above a {@code col...()} method: every table the column appears in,
     * with its type and its size. Returns an empty string when there is no collected usage.
     */
    private String columnDoc(String interfaceName, List<ColumnUsage> usages) {
        if (usages == null || usages.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\t/**\n");
        sb.append("\t * Column <b>").append(interfaceName).append("</b> occurrences across the generated tables:\n");
        sb.append("\t * <table>\n");
        sb.append("\t * <caption>column usage</caption>\n");
        sb.append("\t * <tr><th>Table</th><th>Type</th><th>Size</th><th>Not null</th></tr>\n");
        for (ColumnUsage usage : usages) {
            sb.append("\t * <tr><td>").append(usage.tableName)
                    .append("</td><td>").append(simpleName(usage.fieldType))
                    .append("</td><td>").append(usage.lob ? "LOB" : (usage.size > 0 ? Integer.toString(usage.size) : "-"))
                    .append("</td><td>").append(usage.notNull)
                    .append("</td></tr>\n");
        }
        sb.append("\t * </table>\n");
        sb.append("\t */\n");
        return sb.toString();
    }

    private String getterTypeOf(TypeElement columnInterface, String fieldName) {
        for (ExecutableElement method : ElementFilter.methodsIn(columnInterface.getEnclosedElements())) {
            if (method.getSimpleName().contentEquals("get" + fieldName) && method.getParameters().isEmpty()) {
                return boxedTypeName(method.getReturnType());
            }
        }
        return null;
    }

    private void writeColumnInterface(TypeElement definition, String columnPackage, ColumnModel column, String doc) throws IOException {
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
        sb.append(doc);
        sb.append("\t@SuppressWarnings({\"rawtypes\", \"unchecked\"})\n");
        sb.append("\tdefault Column<E, ").append(type).append(", ").append(name).append("> col").append(name).append("() {\n");
        sb.append("\t\treturn DaobabCache.getColumn(\"").append(name).append("\", \"").append(column.columnName)
                .append("\", (Table<?>) this, ").append(type).append(".class);\n");
        sb.append("\t}\n");
        sb.append("}\n");

        writeSource(columnPackage + "." + name, sb.toString(), definition);
    }

    private void writeEntity(TypeElement definition, String entityPackage, String columnPackage,
                             String entityName, String tableName, List<ColumnModel> columns,
                             boolean generateDto, String dtoPackage, String dtoName) throws IOException {

        ColumnModel pk = columns.stream().filter(c -> c.primaryKey).findFirst().orElse(null);

        //the column interfaces are imported by their simple names, so a DTO of the same name has to stay fully qualified
        boolean dtoNameCollides = columns.stream().anyMatch(c -> c.fieldName.equals(dtoName));
        String dtoRef = dtoNameCollides ? dtoPackage + "." + dtoName : dtoName;

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(entityPackage).append(";\n\n");
        sb.append("import io.daobab.creation.DaobabCache;\n");
        sb.append("import io.daobab.model.*;\n");
        for (ColumnModel column : columns) {
            sb.append("import ").append(columnPackage).append(".").append(column.fieldName).append(";\n");
        }
        if (generateDto && !dtoNameCollides && !dtoPackage.equals(entityPackage)) {
            sb.append("import ").append(dtoPackage).append(".").append(dtoName).append(";\n");
        }
        sb.append("import java.util.*;\n\n");

        sb.append("@SuppressWarnings({\"rawtypes\", \"unused\"})\n");
        sb.append("@TableInformation(name = \"").append(tableName).append("\")\n");
        sb.append("public class ").append(entityName).append(" extends ");
        if (generateDto) {
            sb.append("DtoTable<").append(entityName).append(", ").append(dtoRef).append(">");
        } else {
            sb.append("Table<").append(entityName).append(">");
        }
        sb.append(" implements\n");
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

        if (generateDto) {
            sb.append("\tpublic static ").append(entityName).append(" fromDto(").append(dtoRef).append(" dto) {\n");
            sb.append("\t\treturn new ").append(entityName).append("()");
            for (ColumnModel column : columns) {
                sb.append("\n\t\t\t\t.set").append(column.fieldName).append("(dto.get").append(column.fieldName).append("())");
            }
            sb.append(";\n\t}\n\n");

            sb.append("\t@Override\n");
            sb.append("\tpublic ").append(dtoRef).append(" toDto() {\n");
            sb.append("\t\treturn ").append(dtoRef).append(".builder()");
            for (ColumnModel column : columns) {
                sb.append("\n\t\t\t\t.").append(decapitalize(column.fieldName)).append("(get").append(column.fieldName).append("())");
            }
            sb.append("\n\t\t\t\t.build();\n\t}\n\n");
        }

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

    /**
     * The immutable DTO counterpart of the entity: final fields, getters, a builder
     * and equality based on the primary key (or on all the fields when there is no primary key).
     */
    private void writeDto(TypeElement definition, String dtoPackage, String dtoName, List<ColumnModel> columns) throws IOException {
        ColumnModel pk = columns.stream().filter(c -> c.primaryKey).findFirst().orElse(null);

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(dtoPackage).append(";\n\n");
        sb.append("import java.util.Objects;\n\n");
        sb.append("public final class ").append(dtoName).append(" {\n\n");

        for (ColumnModel column : columns) {
            sb.append("\tprivate final ").append(column.fieldType).append(" ").append(decapitalize(column.fieldName)).append(";\n");
        }
        sb.append("\n");

        sb.append("\tprivate ").append(dtoName).append("(Builder builder) {\n");
        for (ColumnModel column : columns) {
            String field = decapitalize(column.fieldName);
            sb.append("\t\tthis.").append(field).append(" = builder.").append(field).append(";\n");
        }
        sb.append("\t}\n\n");

        sb.append("\tpublic static Builder builder() {\n");
        sb.append("\t\treturn new Builder();\n");
        sb.append("\t}\n\n");

        for (ColumnModel column : columns) {
            sb.append("\tpublic ").append(column.fieldType).append(" get").append(column.fieldName).append("() {\n");
            sb.append("\t\treturn ").append(decapitalize(column.fieldName)).append(";\n");
            sb.append("\t}\n\n");
        }

        String hashBody;
        String equalsBody;
        if (pk != null) {
            String pkField = decapitalize(pk.fieldName);
            hashBody = "Objects.hashCode(" + pkField + ")";
            equalsBody = "Objects.equals(" + pkField + ", other." + pkField + ")";
        } else {
            StringBuilder hash = new StringBuilder("Objects.hash(");
            StringBuilder equals = new StringBuilder();
            for (int i = 0; i < columns.size(); i++) {
                String field = decapitalize(columns.get(i).fieldName);
                hash.append(field);
                equals.append("Objects.equals(").append(field).append(", other.").append(field).append(")");
                if (i < columns.size() - 1) {
                    hash.append(", ");
                    equals.append("\n\t\t\t\t&& ");
                }
            }
            hash.append(")");
            hashBody = hash.toString();
            equalsBody = equals.toString();
        }

        sb.append("\t@Override\n");
        sb.append("\tpublic int hashCode() {\n");
        sb.append("\t\treturn ").append(hashBody).append(";\n");
        sb.append("\t}\n\n");
        sb.append("\t@Override\n");
        sb.append("\tpublic boolean equals(Object obj) {\n");
        sb.append("\t\tif (this == obj) return true;\n");
        sb.append("\t\tif (obj == null || getClass() != obj.getClass()) return false;\n");
        sb.append("\t\t").append(dtoName).append(" other = (").append(dtoName).append(") obj;\n");
        sb.append("\t\treturn ").append(equalsBody).append(";\n");
        sb.append("\t}\n\n");

        sb.append("\tpublic static final class Builder {\n\n");
        for (ColumnModel column : columns) {
            sb.append("\t\tprivate ").append(column.fieldType).append(" ").append(decapitalize(column.fieldName)).append(";\n");
        }
        sb.append("\n\t\tprivate Builder() {\n\t\t}\n\n");
        for (ColumnModel column : columns) {
            String field = decapitalize(column.fieldName);
            sb.append("\t\tpublic Builder ").append(field).append("(").append(column.fieldType).append(" ").append(field).append(") {\n");
            sb.append("\t\t\tthis.").append(field).append(" = ").append(field).append(";\n");
            sb.append("\t\t\treturn this;\n");
            sb.append("\t\t}\n\n");
        }
        sb.append("\t\tpublic ").append(dtoName).append(" build() {\n");
        sb.append("\t\t\treturn new ").append(dtoName).append("(this);\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n");
        sb.append("}\n");

        writeSource(dtoPackage + "." + dtoName, sb.toString(), definition);
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

    /**
     * One usage of a column interface by a table: the source for a documentation table row.
     */
    private static final class ColumnUsage {
        final String tableName;
        final String fieldType;
        final int size;
        final boolean notNull;
        final boolean lob;

        ColumnUsage(String tableName, String fieldType, int size, boolean notNull, boolean lob) {
            this.tableName = tableName;
            this.fieldType = fieldType;
            this.size = size;
            this.notNull = notNull;
            this.lob = lob;
        }
    }

    /**
     * A prepared and validated definition, ready to generate the sources from.
     */
    private static final class EntityContext {
        TypeElement definition;
        String entityPackage;
        String columnPackage;
        String entityName;
        String tableName;
        List<ColumnModel> columns;
        boolean generateDto;
        String dtoPackage;
        String dtoName;
    }
}
