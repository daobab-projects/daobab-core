package io.daobab.processor;

import io.daobab.annotation.DaobabColumn;
import io.daobab.annotation.DaobabDataBase;
import io.daobab.annotation.DaobabTable;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
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
 *     or disambiguated with a type suffix when a column of the same name but a different type shows up);
 *     when the column declares a {@code typeConverterClass}, its {@code col...()} wires that
 *     {@code DatabaseTypeConverter} into the column - validated to match the field type,</li>
 *     <li>the entity class (named with the 'Entity' suffix) extending {@code DtoTable}, implementing
 *     the column interfaces and, when a primary key column is marked, the {@code PrimaryKey} interface;
 *     several marked columns form a composite primary key: a {@code XxxKey} interface grouping the key
 *     columns is generated next to the entity, which implements it together with
 *     {@code PrimaryCompositeKey} - matching the generator's composite key output,</li>
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

    private static final String CONVERTER_INTERFACE = "io.daobab.target.database.converter.type.DatabaseTypeConverter";

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

    private static String nameCell(ColumnModel c) {
        return c.primaryKey ? c.fieldName + "(PK)" : c.fieldName;
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
     * The type converter pinned to each generated column interface: fully qualified name -&gt; converter
     * FQN (absent when the column declares no converter). A shared column interface carries one converter,
     * so definitions reusing it must agree on it.
     */
    private final Map<String, String> generatedColumnConverters = new HashMap<>();

    private static String sizeCell(ColumnModel c) {
        return c.lob ? "LOB" : (c.size > 0 ? Integer.toString(c.size) : "-");
    }

    /**
     * Pads a cell to the column width the way the generator's {@code TableDescriptionGenerator} does:
     * a leading space, the value, right padding, a trailing space.
     */
    private static String pad(String value, int width) {
        if (value == null) {
            value = "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(' ').append(value);
        for (int i = value.length(); i < width; i++) {
            sb.append(' ');
        }
        sb.append(' ');
        return sb.toString();
    }

    /**
     * The {@code TableColumn} builder chain describing one column - shared by the entity's
     * {@code columns()} body and by the composite key's column group.
     */
    private static String tableColumnChain(ColumnModel column) {
        StringBuilder sb = new StringBuilder();
        sb.append("new TableColumn(col").append(column.fieldName).append("())");
        if (column.primaryKey) sb.append(".primaryKey()");
        if (column.size > 0) sb.append(".size(").append(column.size).append(")");
        if (column.precision > 0) sb.append(".precision(").append(column.precision).append(")");
        if (column.scale > 0) sb.append(".scale(").append(column.scale).append(")");
        if (column.notNull) sb.append(".notNull()");
        if (column.unique) sb.append(".unique()");
        if (column.lob) sb.append(".lob()");
        return sb.toString();
    }

    private void writeSources(EntityContext context) throws IOException {
        for (ColumnModel column : context.columns) {
            if (!ensureColumnInterface(context.definition, context.columnPackage, column)) {
                return;
            }
        }

        //after the column names are resolved, so the key interface uses the disambiguated names
        if (context.compositeKeyName != null) {
            writeCompositeKey(context);
        }

        if (context.generateDto) {
            writeDto(context.definition, context.dtoPackage, context.dtoName, context.columns);
        }

        writeEntity(context.definition, context.entityPackage, context.columnPackage, context.entityName,
                context.tableName, context.columns, context.generateDto, context.dtoPackage, context.dtoName,
                context.compositeKeyName);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(DaobabTable.class.getName(), DaobabDataBase.class.getName());
    }

    /**
     * A free name for the composite key interface: the entity base name with the 'Key' suffix, plus a
     * numeric counter when the name is already taken - the same naming rule the generator applies.
     */
    private String compositeKeyName(EntityContext context, Set<String> reservedNames) {
        String base = stripEntitySuffix(context.entityName) + "Key";
        String candidate = base;
        int counter = 0;
        while (reservedNames.contains(context.entityPackage + "." + candidate)
                || processingEnv.getElementUtils().getTypeElement(context.entityPackage + "." + candidate) != null) {
            counter++;
            candidate = base + counter;
        }
        return candidate;
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
            column.returnType = method.getReturnType();
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

                TypeMirror converter = typeConverterMirror(settings);
                if (converter != null && !isConverterSentinel(converter)) {
                    column.converterMirror = converter;
                    column.converterType = processingEnv.getTypeUtils().erasure(converter).toString();
                }
            }
            columns.add(column);
        }
        return columns;
    }

    /**
     * Resolves the fully qualified name of the entity generated out of a {@link DaobabTable}
     * definition, applying the same defaulting rules as {@link #prepare(TypeElement)}. Shared by
     * the entity generation and by the {@link DaobabDataBase} interface assembly, so both agree on
     * where every entity ends up.
     */
    private EntityRef resolveEntityRef(TypeElement definition) {
        DaobabTable table = definition.getAnnotation(DaobabTable.class);
        String definitionPackage = processingEnv.getElementUtils().getPackageOf(definition).getQualifiedName().toString();
        String definitionName = definition.getSimpleName().toString();

        String entityName = table.entityName().isEmpty() ? stripDefinitionSuffix(definitionName) + "Entity" : table.entityName();
        String entityPackage = table.entityPackage().isEmpty() ? definitionPackage : table.entityPackage();
        return new EntityRef(entityPackage, entityName);
    }

    /**
     * Generates the database interface of a {@link DaobabDataBase}: an interface named after the
     * database with the 'Tables' suffix, exposing an initialized field per entity (e.g.
     * {@code BookEntity tabBook = new BookEntity();}) - the compile time counterpart of the
     * hand written {@code MetaDataTables} and of the {@code Tables} interface emitted by the generator.
     */
    private void writeTablesInterface(TypeElement configElement, Map<String, List<TypeElement>> definitionsByPackage) throws IOException {
        DaobabDataBase db = configElement.getAnnotation(DaobabDataBase.class);

        String elementPackage = processingEnv.getElementUtils().getPackageOf(configElement).getQualifiedName().toString();
        String targetPackage = db.targetPackage().isEmpty() ? elementPackage : db.targetPackage();
        String interfaceName = db.name() + "Tables";

        //the tables listed explicitly, in the given order...
        List<TypeElement> definitions = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (TypeMirror mirror : tableMirrors(db)) {
            Element definition = processingEnv.getTypeUtils().asElement(mirror);
            if (definition == null || definition.getKind() != ElementKind.INTERFACE) {
                error(configElement, "@DaobabDataBase.tables must list interfaces annotated with @DaobabTable");
                return;
            }
            if (definition.getAnnotation(DaobabTable.class) == null) {
                error(configElement, "The table " + ((TypeElement) definition).getQualifiedName()
                        + " referenced by @DaobabDataBase is not annotated with @DaobabTable");
                return;
            }
            if (seen.add(((TypeElement) definition).getQualifiedName().toString())) {
                definitions.add((TypeElement) definition);
            }
        }

        //...plus every @DaobabTable definition of the scanned package, added alphabetically for a
        //stable output and skipping the ones already listed explicitly
        if (!db.tablesPackage().isEmpty()) {
            List<TypeElement> fromPackage = definitionsInPackage(definitionsByPackage, db.tablesPackage());
            if (fromPackage.isEmpty()) {
                error(configElement, "@DaobabDataBase.tablesPackage \"" + db.tablesPackage()
                        + "\" holds no @DaobabTable definition in this compilation");
                return;
            }
            addNew(definitions, seen, fromPackage);
        } else if (definitions.isEmpty()) {
            //neither tables nor tablesPackage was given: fall back to the annotated element's own package,
            //and record it - it is an implicit default the user did not spell out
            List<TypeElement> fromPackage = definitionsInPackage(definitionsByPackage, elementPackage);
            if (fromPackage.isEmpty()) {
                error(configElement, "@DaobabDataBase \"" + db.name() + "\" declares neither tables nor tablesPackage,"
                        + " and no @DaobabTable definition was found in the current package \"" + elementPackage + "\"");
                return;
            }
            List<String> names = fromPackage.stream().map(d -> d.getSimpleName().toString()).toList();
            note(configElement, "@DaobabDataBase \"" + db.name() + "\" declares neither tables nor tablesPackage;"
                    + " defaulting to the current package \"" + elementPackage + "\" ("
                    + names.size() + " table(s): " + String.join(", ", names) + ")");
            addNew(definitions, seen, fromPackage);
        }

        if (definitions.isEmpty()) {
            error(configElement, "@DaobabDataBase requires at least one table: set tables or tablesPackage");
            return;
        }

        List<EntityRef> refs = new ArrayList<>();
        for (TypeElement definition : definitions) {
            refs.add(resolveEntityRef(definition));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(targetPackage).append(";\n\n");
        sb.append("import io.daobab.query.base.QueryWhisperer;\n");
        Set<String> imports = new LinkedHashSet<>();
        for (EntityRef ref : refs) {
            if (!ref.entityPackage.equals(targetPackage)) {
                imports.add(ref.entityPackage + "." + ref.entityName);
            }
        }
        for (String imp : imports) {
            sb.append("import ").append(imp).append(";\n");
        }
        sb.append("\n");

        sb.append("public interface ").append(interfaceName).append(" extends QueryWhisperer {\n\n");
        for (int i = 0; i < definitions.size(); i++) {
            TypeElement definition = definitions.get(i);
            EntityRef ref = refs.get(i);
            DaobabTable table = definition.getAnnotation(DaobabTable.class);
            String tableName = table.tableName().isEmpty()
                    ? toUpperSnakeCase(stripEntitySuffix(ref.entityName)) : table.tableName();
            String fieldName = "tab" + stripEntitySuffix(ref.entityName);

            sb.append(tableDoc(tableName, readColumns(definition)));
            sb.append("\t").append(ref.entityName).append(" ").append(fieldName)
                    .append(" = new ").append(ref.entityName).append("();\n\n");
        }
        sb.append("}\n");

        writeSource(targetPackage + "." + interfaceName, sb.toString(), configElement);
    }

    /**
     * The Javadoc placed above an initialized table field of a {@link DaobabDataBase} interface: the
     * schema of the table rendered as an aligned {@code <pre>} block (Name / Type / Size / DBName),
     * mirroring the documentation the generator emits above its {@code tab...} fields. DBType and the
     * remarks are omitted because a definition, unlike live JDBC metadata, does not carry them.
     */
    private String tableDoc(String tableName, List<ColumnModel> columns) {
        if (columns == null || columns.isEmpty()) {
            return "";
        }
        List<ColumnModel> sorted = new ArrayList<>(columns);
        sorted.sort(Comparator.comparing(c -> c.fieldName));

        int wName = "Name".length();
        int wType = "Type".length();
        int wSize = "Size".length();
        int wDbName = "DBName".length();
        for (ColumnModel c : sorted) {
            wName = Math.max(wName, nameCell(c).length());
            wType = Math.max(wType, simpleName(c.fieldType).length());
            wSize = Math.max(wSize, sizeCell(c).length());
            wDbName = Math.max(wDbName, c.columnName.length());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\t/**\n");
        sb.append("\t * Table <b>").append(tableName).append("</b>:\n");
        sb.append("\t * <pre>\n");
        sb.append("\t * <u>").append(pad("Name", wName)).append(pad("Type", wType))
                .append(pad("Size", wSize)).append(pad("DBName", wDbName)).append("</u>\n");
        for (ColumnModel c : sorted) {
            sb.append("\t * ").append(pad(nameCell(c), wName)).append(pad(simpleName(c.fieldType), wType))
                    .append(pad(sizeCell(c), wSize)).append(pad(c.columnName, wDbName)).append("\n");
        }
        sb.append("\t * </pre>\n");
        sb.append("\t */\n");
        return sb.toString();
    }

    /**
     * The {@link DaobabTable} definitions collected in the given package during this round, sorted by
     * simple name for a stable, reproducible output.
     */
    private List<TypeElement> definitionsInPackage(Map<String, List<TypeElement>> definitionsByPackage, String pkg) {
        return definitionsByPackage.getOrDefault(pkg, List.of()).stream()
                .sorted(Comparator.comparing(d -> d.getSimpleName().toString()))
                .toList();
    }

    /**
     * Appends the definitions not already collected (deduplicated by fully qualified name).
     */
    private void addNew(List<TypeElement> target, Set<String> seen, List<TypeElement> toAdd) {
        for (TypeElement definition : toAdd) {
            if (seen.add(definition.getQualifiedName().toString())) {
                target.add(definition);
            }
        }
    }

    /**
     * Reads the {@code tables()} class array of a {@link DaobabDataBase}. Class valued annotation
     * members are not available as {@code Class} objects during the annotation processing - accessing
     * them throws {@link MirroredTypesException} carrying the {@link TypeMirror}s instead.
     */
    private List<? extends TypeMirror> tableMirrors(DaobabDataBase db) {
        try {
            db.tables();
            return List.of();
        } catch (MirroredTypesException e) {
            return e.getTypeMirrors();
        }
    }

    /**
     * Reads and validates a definition. Returns the context to generate from, or {@code null} when the
     * definition is invalid (an error is already reported) and has to be skipped.
     */
    private EntityContext prepare(TypeElement definition) {
        DaobabTable table = definition.getAnnotation(DaobabTable.class);

        String definitionPackage = processingEnv.getElementUtils().getPackageOf(definition).getQualifiedName().toString();
        String definitionName = definition.getSimpleName().toString();

        EntityRef entityRef = resolveEntityRef(definition);
        String entityName = entityRef.entityName;
        String entityPackage = entityRef.entityPackage;
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
        for (ColumnModel column : columns) {
            if (column.converterMirror != null && !validateConverter(definition, column)) {
                return null;
            }
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

    /**
     * Reads the {@code typeConverterClass()} of a {@link DaobabColumn}. A class-valued annotation member is
     * not available as a {@code Class} during annotation processing - accessing it throws
     * {@link MirroredTypeException} carrying the {@link TypeMirror} instead.
     */
    private TypeMirror typeConverterMirror(DaobabColumn settings) {
        try {
            settings.typeConverterClass();
            return null;
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
    }

    /**
     * Whether the mirror is the annotation's default - the raw converter interface, meaning "no converter".
     */
    private boolean isConverterSentinel(TypeMirror converter) {
        return processingEnv.getTypeUtils().erasure(converter).toString().equals(CONVERTER_INTERFACE);
    }

    /**
     * The Daobab column type a converter produces - the {@code T} of {@code DatabaseTypeConverter<F, T>} -
     * resolved through the converter's type hierarchy (so an intermediate base such as
     * {@code TypeConverterIntegerBased<T>} is followed to {@code DatabaseTypeConverter<Integer, T>}).
     * Returns {@code null} when it cannot be determined (e.g. the converter binds {@code T} to a type
     * variable rather than a concrete type).
     */
    private TypeMirror converterColumnType(TypeMirror converter) {
        var types = processingEnv.getTypeUtils();
        TypeElement dtc = processingEnv.getElementUtils().getTypeElement(CONVERTER_INTERFACE);
        if (dtc == null) {
            return null;
        }
        TypeMirror dtcErasure = types.erasure(dtc.asType());

        Deque<TypeMirror> queue = new ArrayDeque<>();
        queue.add(converter);
        Set<String> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            TypeMirror current = queue.poll();
            if (!(current instanceof DeclaredType declared)) {
                continue;
            }
            if (types.isSameType(types.erasure(current), dtcErasure)) {
                List<? extends TypeMirror> args = declared.getTypeArguments();
                if (args.size() != 2) {
                    return null;
                }
                TypeMirror columnType = args.get(1);
                return columnType.getKind() == TypeKind.TYPEVAR ? null : columnType;
            }
            if (visited.add(types.erasure(current).toString())) {
                queue.addAll(types.directSupertypes(current));
            }
        }
        return null;
    }

    /**
     * Validates that a column's declared converter produces the column's own type: the {@code T} of
     * {@code DatabaseTypeConverter<F, T>} has to match the annotated method's return type. Reports an error
     * and returns {@code false} on a mismatch (e.g. an {@code Integer} converter on a {@code LocalDateTime}
     * column). A converter whose column type cannot be resolved is left alone.
     */
    private boolean validateConverter(TypeElement definition, ColumnModel column) {
        TypeMirror columnType = converterColumnType(column.converterMirror);
        if (columnType == null) {
            return true;
        }
        var types = processingEnv.getTypeUtils();
        TypeMirror fieldType = column.returnType;
        if (fieldType.getKind().isPrimitive()) {
            fieldType = types.boxedClass((PrimitiveType) fieldType).asType();
        }
        if (!types.isSameType(types.erasure(columnType), types.erasure(fieldType))) {
            error(definition, "The type converter " + column.converterType + " converts to "
                    + simpleName(types.erasure(columnType).toString()) + ", but the column " + column.fieldName
                    + " is of type " + simpleName(boxedTypeName(column.returnType))
                    + ". The converter's column type must match the annotated field type.");
            return false;
        }
        return true;
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
                    //one shared column interface carries one converter: the definitions reusing it must agree
                    String existingConverter = generatedColumnConverters.get(fqcn);
                    if (!Objects.equals(existingConverter, column.converterType)) {
                        error(definition, "Column " + candidate + " is shared, but its type converter differs:"
                                + " already generated with " + (existingConverter == null ? "none" : existingConverter)
                                + ", now requested with " + (column.converterType == null ? "none" : column.converterType)
                                + ". A shared column interface must declare the same converter everywhere.");
                        return false;
                    }
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
            if (column.converterType != null) {
                generatedColumnConverters.put(fqcn, column.converterType);
            }
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
        if (column.converterType == null) {
            sb.append("\t\treturn DaobabCache.getColumn(\"").append(name).append("\", \"").append(column.columnName)
                    .append("\", (Table<?>) this, ").append(type).append(".class);\n");
        } else {
            //pin the declared converter to the column, so DatabaseConverterManager uses it in preference
            //to the automatically resolved one (the col() yields a Column whose getColumnTypeConverter() returns it)
            sb.append("\t\treturn DaobabCache.getColumnWithConverter(\"").append(name).append("\", \"").append(column.columnName)
                    .append("\", (Table<?>) this, ").append(type).append(".class, ").append(column.converterType).append(".class);\n");
        }
        sb.append("\t}\n");
        sb.append("}\n");

        writeSource(columnPackage + "." + name, sb.toString(), definition);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<EntityContext> contexts = new ArrayList<>();
        //all the @DaobabTable definitions of the round indexed by package, so a @DaobabDataBase can
        //pick up a whole package by name instead of listing every definition class
        Map<String, List<TypeElement>> definitionsByPackage = new LinkedHashMap<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(DaobabTable.class)) {
            if (element.getKind() != ElementKind.INTERFACE) {
                error(element, "@DaobabTable may annotate an interface only");
                continue;
            }
            TypeElement definition = (TypeElement) element;
            String definitionPackage = processingEnv.getElementUtils().getPackageOf(definition).getQualifiedName().toString();
            definitionsByPackage.computeIfAbsent(definitionPackage, k -> new ArrayList<>()).add(definition);
            EntityContext context = prepare(definition);
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

        //name the composite key interface of every definition with a multi-column primary key,
        //avoiding every type this compilation generates or compiles
        Set<String> reservedNames = new HashSet<>();
        for (EntityContext context : contexts) {
            reservedNames.add(context.entityPackage + "." + context.entityName);
            reservedNames.add(context.definition.getQualifiedName().toString());
            if (context.generateDto) {
                reservedNames.add(context.dtoPackage + "." + context.dtoName);
            }
        }
        for (EntityContext context : contexts) {
            if (context.columns.stream().filter(c -> c.primaryKey).count() > 1) {
                context.compositeKeyName = compositeKeyName(context, reservedNames);
                reservedNames.add(context.entityPackage + "." + context.compositeKeyName);
            }
        }

        for (EntityContext context : contexts) {
            try {
                writeSources(context);
            } catch (IOException e) {
                error(context.definition, "Cannot write a generated source: " + e.getMessage());
            }
        }

        //assemble the database interfaces gathering every entity of a @DaobabDataBase into one place
        for (Element element : roundEnv.getElementsAnnotatedWith(DaobabDataBase.class)) {
            if (element.getKind() != ElementKind.INTERFACE && element.getKind() != ElementKind.CLASS) {
                error(element, "@DaobabDataBase may annotate a type only");
                continue;
            }
            try {
                writeTablesInterface((TypeElement) element, definitionsByPackage);
            } catch (IOException e) {
                error(element, "Cannot write a generated source: " + e.getMessage());
            }
        }
        return true;
    }

    /**
     * The composite key interface of a definition with a multi-column primary key - the counterpart of
     * the {@code XxxKey} interface the generator emits. It extends every key column interface (so its
     * default method reaches the {@code col...()} accessors) plus the {@code Composite} marker, and
     * groups the key columns in {@code compositeXxxKey()}; the entity implements it and returns the
     * group from {@code colCompositeId()}.
     */
    private void writeCompositeKey(EntityContext context) throws IOException {
        String keyName = context.compositeKeyName;
        List<ColumnModel> keyColumns = context.columns.stream().filter(c -> c.primaryKey).toList();

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(context.entityPackage).append(";\n\n");
        for (ColumnModel column : keyColumns) {
            sb.append("import ").append(context.columnPackage).append(".").append(column.fieldName).append(";\n");
        }
        sb.append("import io.daobab.model.*;\n\n");

        sb.append("@SuppressWarnings({\"rawtypes\", \"unused\"})\n");
        sb.append("public interface ").append(keyName).append("<E extends Entity");
        for (ColumnModel column : keyColumns) {
            sb.append(" & ").append(column.fieldName).append("<E>");
        }
        sb.append("> extends\n");
        for (ColumnModel column : keyColumns) {
            sb.append("\t\t").append(column.fieldName).append("<E>,\n");
        }
        sb.append("\t\tComposite<E> {\n\n");

        sb.append("\tdefault CompositeColumns<").append(keyName).append("<E>> composite").append(keyName).append("() {\n");
        sb.append("\t\treturn new CompositeColumns<>(\n");
        for (int i = 0; i < keyColumns.size(); i++) {
            sb.append("\t\t\t\t").append(tableColumnChain(keyColumns.get(i)));
            sb.append(i < keyColumns.size() - 1 ? ",\n" : ");\n");
        }
        sb.append("\t}\n");
        sb.append("}\n");

        writeSource(context.entityPackage + "." + keyName, sb.toString(), context.definition);
    }

    private void writeEntity(TypeElement definition, String entityPackage, String columnPackage,
                             String entityName, String tableName, List<ColumnModel> columns,
                             boolean generateDto, String dtoPackage, String dtoName,
                             String compositeKeyName) throws IOException {

        List<ColumnModel> pkColumns = columns.stream().filter(c -> c.primaryKey).toList();
        ColumnModel pk = pkColumns.size() == 1 ? pkColumns.get(0) : null;
        boolean compositePk = pkColumns.size() > 1;

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
        if (compositePk) {
            //the composite key interface leads the list, the way the generator wires it
            sb.append("\t\t").append(compositeKeyName).append("<").append(entityName).append(">,\n");
        }
        for (ColumnModel column : columns) {
            sb.append("\t\t").append(column.fieldName).append("<").append(entityName).append(">,\n");
        }
        if (pk != null) {
            sb.append("\t\tPrimaryKey<").append(entityName).append(", ").append(pk.fieldType).append(", ").append(pk.fieldName).append("> {\n\n");
        } else if (compositePk) {
            sb.append("\t\tPrimaryCompositeKey<").append(entityName).append(", ").append(compositeKeyName)
                    .append("<").append(entityName).append(">> {\n\n");
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
            sb.append("\t\t\t\t\t\t").append(tableColumnChain(columns.get(i)));
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
        } else if (compositePk) {
            sb.append("\n\t@Override\n");
            sb.append("\tpublic CompositeColumns<").append(compositeKeyName).append("<").append(entityName).append(">> colCompositeId() {\n");
            sb.append("\t\treturn composite").append(compositeKeyName).append("();\n");
            sb.append("\t}\n");
            //equality on the whole composite key, mirroring the single-column primary key above
            sb.append("\n\t@Override\n");
            sb.append("\tpublic int hashCode() {\n");
            sb.append("\t\treturn Objects.hash(");
            for (int i = 0; i < pkColumns.size(); i++) {
                sb.append("get").append(pkColumns.get(i).fieldName).append("()");
                if (i < pkColumns.size() - 1) sb.append(", ");
            }
            sb.append(");\n");
            sb.append("\t}\n");
            sb.append("\n\t@Override\n");
            sb.append("\tpublic boolean equals(Object obj) {\n");
            sb.append("\t\tif (this == obj) return true;\n");
            sb.append("\t\tif (obj == null) return false;\n");
            sb.append("\t\tif (getClass() != obj.getClass()) return false;\n");
            sb.append("\t\t").append(entityName).append(" other = (").append(entityName).append(") obj;\n");
            sb.append("\t\treturn ");
            for (int i = 0; i < pkColumns.size(); i++) {
                ColumnModel keyColumn = pkColumns.get(i);
                sb.append("Objects.equals(get").append(keyColumn.fieldName).append("(), other.get").append(keyColumn.fieldName).append("())");
                if (i < pkColumns.size() - 1) sb.append("\n\t\t\t\t&& ");
            }
            sb.append(";\n");
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
        //equality on the single primary key; a composite key (or none) falls back to all the fields, like the generator
        List<ColumnModel> pkColumns = columns.stream().filter(c -> c.primaryKey).toList();
        ColumnModel pk = pkColumns.size() == 1 ? pkColumns.get(0) : null;

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

    private void note(Element element, String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message, element);
    }

    /**
     * The fully qualified name of an entity generated out of a definition: where a
     * {@link DaobabDataBase} field points and what the entity generation emits.
     */
    private static final class EntityRef {
        final String entityPackage;
        final String entityName;

        EntityRef(String entityPackage, String entityName) {
            this.entityPackage = entityPackage;
            this.entityName = entityName;
        }
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
        /**
         * The FQN of the {@code DatabaseTypeConverter} pinned to the column, or {@code null} when none is declared.
         */
        String converterType;
        /**
         * The converter's type mirror, for validating it against the field type; {@code null} when none is declared.
         */
        TypeMirror converterMirror;
        /**
         * The method's declared return type, for boxing and converter validation.
         */
        TypeMirror returnType;
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
        /**
         * The composite key interface name; {@code null} unless the primary key spans several columns.
         */
        String compositeKeyName;
    }
}
