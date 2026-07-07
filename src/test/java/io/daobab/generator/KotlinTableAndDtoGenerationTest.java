package io.daobab.generator;

import io.daobab.generator.template.TemplateLanguage;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Generates a Kotlin entity (with the Entity suffix) and its DTO data class
 * and verifies the generated sources.
 */
class KotlinTableAndDtoGenerationTest {

    private static final String TABLE_NAME = "model_item";
    private static final String BASE_PACKAGE = "gentest";

    @Test
    void generatesKotlinEntityWithDto() throws Exception {
        Path outDir = Files.createTempDirectory("daobab-gen-kotlin-test");
        Path packageDir = outDir.resolve(BASE_PACKAGE);

        List<GenerateColumn> allColumns = new ArrayList<>();
        GenerateColumn id = column(allColumns, "MODEL_ITEM_ID", Integer.class, false);
        column(allColumns, "DESCRIPTION", String.class, true);
        column(allColumns, "CREATE_DATE_TIME", Timestamp.class, false);

        GenerateTable table = new GenerateTable();
        table.setTableName(TABLE_NAME);
        table.setType("TABLE");
        table.getColumnList().addAll(allColumns);
        table.addPrimaryKey(id);
        id.getColumnInTableOrCreate(TABLE_NAME).setPk(true);

        ColumnAnalysator.compileNames(allColumns);

        Writer writer = new Writer(TemplateLanguage.KOTLIN);
        writer.generateJavaTable(null, null, table, List.of(table), BASE_PACKAGE, packageDir.toString(), true, false);

        Path entityFile = packageDir.resolve("table/ModelItemEntity.kt");
        Path dtoFile = packageDir.resolve("dto/ModelItem.kt");
        assertTrue(Files.exists(entityFile));
        assertTrue(Files.exists(dtoFile));

        String entitySource = Files.readString(entityFile);
        assertTrue(entitySource.contains("class ModelItemEntity : DtoTable<ModelItemEntity, ModelItem>,"), entitySource);
        assertTrue(entitySource.contains("import gentest.dto.ModelItem"), entitySource);
        assertTrue(entitySource.contains("fun fromDto(dto: ModelItem): ModelItemEntity = ModelItemEntity()"), entitySource);
        assertTrue(entitySource.contains(".setModelItemId(dto.modelItemId)"), entitySource);
        assertTrue(entitySource.contains("override fun toDto(): ModelItem = ModelItem("), entitySource);
        assertTrue(entitySource.contains("description = getDescription()"), entitySource);

        String dtoSource = Files.readString(dtoFile);
        assertTrue(dtoSource.contains("package gentest.dto"), dtoSource);
        assertTrue(dtoSource.contains("data class ModelItem("), dtoSource);
        assertTrue(dtoSource.contains("val modelItemId: Int,"), dtoSource);
        assertTrue(dtoSource.contains("val description: String? = null,"), dtoSource);
        assertTrue(dtoSource.contains("val createDateTime: Timestamp"), dtoSource);
        assertTrue(dtoSource.contains("import java.sql.Timestamp"), dtoSource);
        //DTO equality on the primary key
        assertTrue(dtoSource.contains("override fun hashCode() = modelItemId.hashCode()"), dtoSource);
        assertTrue(dtoSource.contains("return modelItemId == other.modelItemId"), dtoSource);
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
}
