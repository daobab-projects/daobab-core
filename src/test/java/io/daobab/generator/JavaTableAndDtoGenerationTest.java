package io.daobab.generator;

import io.daobab.generator.template.TemplateLanguage;
import io.daobab.model.DtoTable;
import io.daobab.model.Entity;
import org.junit.jupiter.api.Test;

import javax.tools.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Generates a Java entity (with the Entity suffix), its column interfaces and the DTO class,
 * compiles the generated sources and verifies the entity &lt;-&gt; DTO conversion end to end.
 */
class JavaTableAndDtoGenerationTest {

    private static final String TABLE_NAME = "model_item";
    private static final String BASE_PACKAGE = "gentest";

    @Test
    void generatesEntityWithDtoAndConverts() throws Exception {
        Path outDir = Files.createTempDirectory("daobab-gen-test");
        Path packageDir = outDir.resolve(BASE_PACKAGE);

        List<GenerateColumn> allColumns = new ArrayList<>();
        GenerateColumn id = column(allColumns, "MODEL_ITEM_ID", Integer.class);
        column(allColumns, "DESCRIPTION", String.class);
        column(allColumns, "CREATE_DATE_TIME", Timestamp.class);

        GenerateTable table = new GenerateTable();
        table.setTableName(TABLE_NAME);
        table.setType("TABLE");
        table.getColumnList().addAll(allColumns);
        table.addPrimaryKey(id);
        id.getColumnInTableOrCreate(TABLE_NAME).setPk(true);

        ColumnAnalysator.compileNames(allColumns);

        Writer writer = new Writer(TemplateLanguage.JAVA);
        writer.generateJavaTable(null, null, table, List.of(table), BASE_PACKAGE, packageDir.toString(), true, false);
        for (GenerateColumn c : allColumns) {
            writer.generateJavaColumn(null, null, c, packageDir.toString(), true);
        }

        Path entityFile = packageDir.resolve("table/ModelItemEntity.java");
        Path dtoFile = packageDir.resolve("dto/ModelItem.java");
        assertTrue(Files.exists(entityFile));
        assertTrue(Files.exists(dtoFile));

        String entitySource = Files.readString(entityFile);
        assertTrue(entitySource.contains("class ModelItemEntity extends DtoTable<ModelItemEntity, ModelItem>"), entitySource);
        assertTrue(entitySource.contains("public static ModelItemEntity fromDto(ModelItem dto)"), entitySource);
        assertTrue(entitySource.contains("public ModelItem toDto()"), entitySource);
        assertTrue(entitySource.contains("import gentest.dto.ModelItem;"), entitySource);

        Path classesDir = compile(outDir);

        try (URLClassLoader loader = new URLClassLoader(
                new URL[]{classesDir.toUri().toURL()}, Entity.class.getClassLoader())) {

            Class<?> entityClass = loader.loadClass("gentest.table.ModelItemEntity");
            Class<?> dtoClass = loader.loadClass("gentest.dto.ModelItem");
            assertTrue(DtoTable.class.isAssignableFrom(entityClass));

            Object entity = entityClass.getConstructor().newInstance();
            entity = entityClass.getMethod("setModelItemId", Integer.class).invoke(entity, 5);
            entity = entityClass.getMethod("setDescription", String.class).invoke(entity, "daobab");

            Object dto = entityClass.getMethod("toDto").invoke(entity);
            assertEquals(dtoClass, dto.getClass());
            assertEquals(5, dtoClass.getMethod("getModelItemId").invoke(dto));
            assertEquals("daobab", dtoClass.getMethod("getDescription").invoke(dto));

            Object entityBack = entityClass.getMethod("fromDto", dtoClass).invoke(null, dto);
            assertEquals(5, entityClass.getMethod("getModelItemId").invoke(entityBack));
            assertEquals("daobab", entityClass.getMethod("getDescription").invoke(entityBack));

            //DTO equality is based on the primary key
            Object builder = dtoClass.getMethod("builder").invoke(null);
            builder.getClass().getMethod("modelItemId", Integer.class).invoke(builder, 5);
            Object otherDto = builder.getClass().getMethod("build").invoke(builder);
            assertEquals(dto, otherDto);
        }
    }

    private GenerateColumn column(List<GenerateColumn> allColumns, String columnName, Class<?> fieldClass) {
        GenerateColumn column = new GenerateColumn();
        column.setColumnName(columnName);
        column.setFieldClass(fieldClass);
        String interfaceName = GenerateFormatter.toCamelCase(columnName);
        column.setFieldName(GenerateFormatter.decapitalize(interfaceName));
        column.setInterfaceName(interfaceName);
        column.setPackage(BASE_PACKAGE + ".column");
        column.addTableUsage(TABLE_NAME, "VARCHAR");
        column.getColumnInTableOrCreate(TABLE_NAME)
                .setColumnSize(10)
                .setNullable("0");
        allColumns.add(column);
        return column;
    }

    private Path compile(Path sourcesRoot) throws Exception {
        List<Path> files;
        try (Stream<Path> walk = Files.walk(sourcesRoot)) {
            files = walk.filter(p -> p.toString().endsWith(".java")).collect(Collectors.toList());
        }
        Path classesDir = Files.createDirectories(sourcesRoot.resolve("classes"));

        String daobabClasses = Paths.get(Entity.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            List<String> options = List.of("-classpath", daobabClasses, "-d", classesDir.toString());
            boolean success = Boolean.TRUE.equals(compiler.getTask(null, fileManager, diagnostics, options, null,
                    fileManager.getJavaFileObjectsFromPaths(files)).call());

            String messages = diagnostics.getDiagnostics().stream()
                    .map(Diagnostic::toString)
                    .collect(Collectors.joining(System.lineSeparator()));
            assertTrue(success, "compilation of the generated sources failed: " + messages);
        }
        return classesDir;
    }
}
