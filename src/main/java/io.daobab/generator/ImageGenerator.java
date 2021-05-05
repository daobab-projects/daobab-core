package io.daobab.generator;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.result.Entities;
import io.daobab.target.database.DaobabDataBaseMetaData;
import io.daobab.target.meta.MetaSpecificsTables;
import io.daobab.target.meta.table.MetaCatalog;
import io.daobab.target.meta.table.MetaColumn;
import io.daobab.target.meta.table.MetaSchema;
import io.daobab.target.meta.table.MetaTable;

import java.util.List;

import static io.daobab.generator.GenerateFormatter.toCamelCase;
import static io.daobab.statement.where.WhereAnd.and;


public class ImageGenerator implements MetaSpecificsTables {

    private String fileDirectoryPath;
    private String javaPackage;

    private boolean override = false;
    private boolean generateTypeScriptClasses = false;

    public void generate(DaobabDataBaseMetaData dataBaseMetaData) {
        dataBaseMetaData.getCatalogs().forEach(cat -> generateSchemas(dataBaseMetaData, cat));
        generateSchemas(dataBaseMetaData, null);
    }

    public void generateSchemas(DaobabDataBaseMetaData dataBaseMetaData, MetaCatalog catalog) {
        dataBaseMetaData.getSchemas()
                .select(tabMetaSchema)
                .where(and()
                        .ifTrue(catalog != null, w -> w.equal(tabMetaSchema.colCatalogName(), catalog)))
                .forEach(schema -> generateTables(dataBaseMetaData, catalog, schema));

//        generateTables(dataBaseMetaData,catalog,null);
//        generateTables(dataBaseMetaData,null,null);

    }

    public void generateTables(DaobabDataBaseMetaData dataBaseMetaData, MetaCatalog catalog, MetaSchema schema) {
        Entities<MetaTable> tables = dataBaseMetaData.getTables()
                .select(tabMetaTable)
                .where(and()
                        .ifTrue(schema != null, w -> w.equal(tabMetaTable.colSchemaName(), schema))
                        .ifTrue(catalog != null, w -> w.equal(tabMetaTable.colCatalogName(), catalog)))
                .findMany();


        Entities<MetaColumn> allColumns = dataBaseMetaData.getColumns().select(tabMetaColumn)
                .where(and()
                        .ifTrue(schema != null, w -> w.equal(tabMetaColumn.colSchemaName(), schema))
                        .ifTrue(catalog != null, w -> w.equal(tabMetaColumn.colCatalogName(), catalog)))
                .findMany();

        //setting camel names
        allColumns.forEach(column -> column.setCamelName(getFreeColumnName(allColumns, toCamelCase(column.getColumnName()), column.colCamelName())));

        for (MetaTable table : tables) {
            if (table.getCamelName() == null) continue;
//            String camel= toCamelCase(table.getTableName());
            table.setCamelName(getFreeName(tables, toCamelCase(table.getTableName()), table.colCamelName()));

            List<MetaColumn> columns = dataBaseMetaData.getColumns().select(tabMetaColumn)
                    .where(and()
                            .ifTrue(schema != null, w -> w.equal(tabMetaColumn.colSchemaName(), schema))
                            .ifTrue(catalog != null, w -> w.equal(tabMetaColumn.colCatalogName(), catalog))
                            .equal(tabMetaColumn.colTableName(), table))
                    .findMany();

            for (MetaColumn column : columns) {

            }
        }

    }

    private <E extends Entity, R extends EntityRelation<E>> String getFreeColumnName(List<E> list, String name, Column<E, String, R> column) {
        //jesli nazwa jest zabroniona - zmien
        //dodaj typ
        int counter = 0;
        while (isNameInUse(list, name, counter, column)) {
            counter++;
        }

        return counter == 0 ? name : name + counter;
    }

    private <E extends Entity, R extends EntityRelation<E>> String getFreeName(List<E> list, String name, Column<E, String, R> column) {
        int counter = 0;
        while (isNameInUse(list, name, counter, column)) {
            counter++;
        }

        return counter == 0 ? name : name + counter;
    }


    private <E extends Entity, R extends EntityRelation<E>> boolean isNameInUse(List<E> list, String name, int counter, Column<E, String, R> column) {
        for (E entity : list) {
            if (column.getValueOf((R) entity).equalsIgnoreCase(counter == 0 ? name : name + counter)) {
                return true;
            }
        }
        return false;
    }


    public String getFileDirectoryPath() {
        return fileDirectoryPath;
    }

    public void setFileDirectoryPath(String fileDirectoryPath) {
        this.fileDirectoryPath = fileDirectoryPath;
    }

    public String getJavaPackage() {
        return javaPackage;
    }

    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public boolean isGenerateTypeScriptClasses() {
        return generateTypeScriptClasses;
    }

    public void setGenerateTypeScriptClasses(boolean generateTypeScriptClasses) {
        this.generateTypeScriptClasses = generateTypeScriptClasses;
    }


//    private void generateColumn(MetaColumn column) {
//
//        Replacer replacer = new Replacer();
//
//        boolean columnAndTypeTheSameType = column.getFieldClass().getSimpleName().equalsIgnoreCase(column.getFinalFieldName());
//
//        if (byte[].class.equals(column.getFieldClass()) || columnAndTypeTheSameType) {
//            replacer.add(GenKeys.CLASS_FULL_NAME, "");
//        } else {
//            replacer.add(GenKeys.CLASS_FULL_NAME, "import " + column.getFieldClass().getName() + ";");
//        }
//        replacer.add(GenKeys.CLASS_SIMPLE_NAME, columnAndTypeTheSameType ? column.getFieldClass().getName() : column.getFieldClass().getSimpleName());
//        replacer.add(GenKeys.COLUMN_NAME, column.getColumnName());
//        replacer.add(GenKeys.INTERFACE_NAME, column.getInterfaceName());
//        replacer.add(GenKeys.FIELD_NAME, column.getFinalFieldName());
//        replacer.add(GenKeys.TABLES_AND_TYPE, column.getTableTypeDescription());
//        replacer.add(GenKeys.DB_TYPE, TypeConverter.getDbTypeName(column.getDatatype().getType()));
//        replacer.add(GenKeys.PACKAGE, getJavaPackage());
//
//        column.setAlreadyGenerated(true);
//
//        SaveGenerated.saveGeneratedTo(replacer.replateAll(coltemp), getFileDirectoryPath(), getSchema(), "column", column.getFinalFieldName(), FileType.JAVA, isOverride());
//
//    }

}
