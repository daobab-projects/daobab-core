package io.daobab.generator;

import io.daobab.generator.template.TemplateLanguage;
import io.daobab.model.DtoTable;
import io.daobab.model.Entity;
import io.daobab.processor.DaobabEntityProcessor;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Generates the annotated definition interface (the definitions-only generator mode),
 * feeds it to the daobab annotation processor and verifies the resulting entity and DTO.
 */
class JavaDefinitionGenerationTest {

    private static final String TABLE_NAME = "model_item";
    private static final String BASE_PACKAGE = "gentest";

    @Test
    void generatedDefinitionFeedsTheAnnotationProcessor() throws Exception {
        Path outDir = Files.createTempDirectory("daobab-gen-def-test");
        Path packageDir = outDir.resolve(BASE_PACKAGE);

        List<GenerateColumn> allColumns = new ArrayList<>();
        GenerateColumn id = column(allColumns, "MODEL_ITEM_ID", Integer.class, false);
        column(allColumns, "DESCRIPTION", String.class, true);
        column(allColumns, "CREATE_DATE_TIME", Timestamp.class, true);

        GenerateTable table = new GenerateTable();
        table.setTableName(TABLE_NAME);
        table.setType("TABLE");
        table.getColumnList().addAll(allColumns);
        table.addPrimaryKey(id);
        id.getColumnInTableOrCreate(TABLE_NAME).setPk(true);

        ColumnAnalysator.compileNames(allColumns);

        Writer writer = new Writer(TemplateLanguage.JAVA);
        writer.generateJavaDefinition(null, null, table, BASE_PACKAGE, packageDir.toString(), true, false);

        Path definitionFile = packageDir.resolve("definition/ModelItemDef.java");
        assertTrue(Files.exists(definitionFile));

        String definitionSource = Files.readString(definitionFile);
        assertTrue(definitionSource.contains("package gentest.definition;"), definitionSource);
        assertTrue(definitionSource.contains("@DaobabTable(tableName = \"model_item\""), definitionSource);
        assertTrue(definitionSource.contains("entityPackage = \"gentest.table\""), definitionSource);
        assertTrue(definitionSource.contains("columnPackage = \"gentest.column\""), definitionSource);
        assertTrue(definitionSource.contains("dtoPackage = \"gentest.dto\""), definitionSource);
        assertTrue(definitionSource.contains("public interface ModelItemDef {"), definitionSource);
        assertTrue(definitionSource.contains("@DaobabColumn(name = \"MODEL_ITEM_ID\", primaryKey = true, size = 10, notNull = true)"), definitionSource);
        assertTrue(definitionSource.contains("Integer modelItemId();"), definitionSource);
        assertTrue(definitionSource.contains("@DaobabColumn(name = \"DESCRIPTION\", size = 10)"), definitionSource);
        assertTrue(definitionSource.contains("Timestamp createDateTime();"), definitionSource);

        //the definition compiles with the annotation processor into the entity, columns and DTO
        Path classesDir = compileWithProcessor(outDir, definitionFile);

        try (URLClassLoader loader = new URLClassLoader(
                new URL[]{classesDir.toUri().toURL()}, Entity.class.getClassLoader())) {

            Class<?> entityClass = loader.loadClass("gentest.table.ModelItemEntity");
            Class<?> dtoClass = loader.loadClass("gentest.dto.ModelItem");
            loader.loadClass("gentest.column.ModelItemId");
            assertTrue(DtoTable.class.isAssignableFrom(entityClass));

            Object entity = entityClass.getConstructor().newInstance();
            entity = entityClass.getMethod("setModelItemId", Integer.class).invoke(entity, 9);
            entity = entityClass.getMethod("setDescription", String.class).invoke(entity, "generated");

            Object dto = entityClass.getMethod("toDto").invoke(entity);
            assertEquals(9, dtoClass.getMethod("getModelItemId").invoke(dto));

            Object entityBack = entityClass.getMethod("fromDto", dtoClass).invoke(null, dto);
            assertEquals("generated", entityClass.getMethod("getDescription").invoke(entityBack));
        }
    }

    private GenerateColumn column(List<GenerateColumn> allColumns, String columnName, Class<?> fieldClass, boolean nullable) {
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
                .setNullable(nullable ? "1" : "0");
        allColumns.add(column);
        return column;
    }

    private Path compileWithProcessor(Path workDir, Path definitionFile) throws Exception {
        Path classesDir = Files.createDirectories(workDir.resolve("classes"));
        Path generatedDir = Files.createDirectories(workDir.resolve("generated"));

        String daobabClasses = Paths.get(Entity.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            List<String> options = List.of(
                    "-classpath", daobabClasses,
                    "-d", classesDir.toString(),
                    "-s", generatedDir.toString(),
                    "-processor", DaobabEntityProcessor.class.getName());
            boolean success = Boolean.TRUE.equals(compiler.getTask(null, fileManager, diagnostics, options, null,
                    fileManager.getJavaFileObjectsFromPaths(List.of(definitionFile))).call());

            String messages = diagnostics.getDiagnostics().stream()
                    .map(Diagnostic::toString)
                    .collect(Collectors.joining(System.lineSeparator()));
            assertTrue(success, "compilation with the annotation processor failed: " + messages);
        }
        return classesDir;
    }
}
