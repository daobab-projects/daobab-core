package io.daobab.generator;

import io.daobab.model.CompositeColumns;
import io.daobab.model.PrimaryCompositeKey;
import io.daobab.model.PrimaryKey;

import java.util.List;

import static io.daobab.generator.SaveGenerated.saveGeneratedTo;

public class Writer implements DaobabClassGeneratorTemplates {


    int generatedColumnsCount = 0;
    int generatedTablesCount = 0;
    int generatedCompositesCount = 0;
    int generatedTargetsCount = 0;

    private static boolean isCompositeKeyNameWithSuffixFree(String tableNameWithSuffix, int counter, List<GenerateTable> allTables) {
        for (GenerateTable generateTable : allTables) {
            if (GenerateFormatter.toCamelCase(generateTable.getTableName()).equals(counter == 0 ? tableNameWithSuffix : tableNameWithSuffix + counter)) {
                return false;
            }
        }
        return true;
    }

    static String createCompositeKeyName(String tableName, List<GenerateTable> allTables) {
        String suffix = "Key";
        String tableNameWithKeySuffix = tableName + suffix;
        int counter = 0;
        while (!isCompositeKeyNameWithSuffixFree(tableNameWithKeySuffix, counter, allTables)) {
            counter++;
        }
        if (counter == 0) {
            return tableNameWithKeySuffix;
        }
        return tableNameWithKeySuffix + counter;
    }

    void generateCompositeKey(GenerateTable table, String path, boolean override) {

        String tableNameCamel = GenerateFormatter.toCamelCase(table.getTableName());

        Replacer replacer = new Replacer();

        replacer.add(GenKeys.COLUMN_IMPORTS, table.getColumnImport(tableNameCamel))
                .add(GenKeys.TABLE_PACKAGE, table.getJavaPackage())
                .add(GenKeys.COMPOSITE_KEY_COLUMN_TYPE_INTERFACES, table.getCompositeKeyInterfaces("E"))
                .add(GenKeys.COMPOSITE_KEY_COLUMN_INTERFACES, table.getCompositeKeyInterfaces2("E"))
                .add(GenKeys.COMPOSITE_KEY_METHOD, table.getCompositeMethod(table.getCompositeKeyName()))
                .add(GenKeys.COMPOSITE_NAME, table.getCompositeKeyName());
        saveGeneratedTo(replacer.replaceAll(COMPOSITE_KEY_TEMP), path, table.getCatalogName(), table.getSchemaName(), (table.isView() ? "view" : "table"), table.getCompositeKeyName(), FileType.JAVA, override);
        generatedCompositesCount++;
    }

    void generateTarget(String catalog, String schema, List<GenerateTable> tables, String javapackageName, String path, boolean override) {
        GenerateTarget target = new GenerateTarget();
        target.setSchemaName(schema);
        target.setCatalogName(catalog);
        target.setTableList(tables);

        StringBuilder javaPackage = JavaPackageResolver.resolve(javapackageName, catalog, schema);
        target.setJavaPackage(javaPackage.toString());

        Replacer replacer = new Replacer();

        replacer.add(GenKeys.TARGET_CLASS_NAME, target.getTargetClassName())
                .add(GenKeys.TAB_ARRAY, target.getTableArray())
                .add(GenKeys.TARGET_TABLES_INTERFACE, target.getTargetTablesInterfaceName())
                .add(GenKeys.TARGET_PACKAGE, target.getJavaPackage() + ";");

        saveGeneratedTo(replacer.replaceAll(targettemp), path, catalog, schema, null, target.getTargetClassName(), FileType.JAVA, override);
        generatedTargetsCount++;

        replacer.clear();

        replacer.add(GenKeys.TABLES_INTERFACE_NAME, target.getTargetTablesInterfaceName())
                .add(GenKeys.TAB_IMPORTS, target.getTableImports())
                .add(GenKeys.TABLES_INITIATED, target.getTablesInitiation())
                .add(GenKeys.TARGET_PACKAGE, target.getJavaPackage() + ";");

        saveGeneratedTo(replacer.replaceAll(TABLESINTERFACETEMP), path, catalog, schema, null, target.getTargetTablesInterfaceName(), FileType.JAVA, override);
    }

    void createTypeScriptTables(String catalog, String schema, List<GenerateTable> entities, String path, boolean override) {
        if (entities == null) return;

        for (GenerateTable entity : entities) {
            StringBuilder sb = new StringBuilder();
            List<GenerateColumn> ecolumns = entity.getColumnList();

            for (GenerateColumn column : ecolumns) {
                sb.append(" ")
                        .append(column.getFinalFieldName()).append(": ")
                        .append(TypeConverter.convertToTS(column.getFieldClass()))
                        .append(";\n");
            }

            String str = "\n\n" + DaobabClassGeneratorTemplates.typeScriptTabletemp.replaceAll(GenKeys.TABLE_CAMEL_NAME, GenerateFormatter.toCamelCase(entity.getTableName())).replaceAll(GenKeys.FIELDS, sb.toString()) + "\n\n";

            saveGeneratedTo(str, path, catalog, schema, "typescript", GenerateFormatter.toTypeScriptCase(entity.getTableName()), FileType.TYPESCRIPT, override);
        }
    }

    void generateColumn(String catalog, String schema, GenerateColumn column, String path, boolean override) {

        Replacer replacer = new Replacer();

        boolean columnAndTypeTheSameType = column.getFieldClass().getSimpleName().equalsIgnoreCase(column.getFinalFieldName());

        if (byte[].class.equals(column.getFieldClass()) || columnAndTypeTheSameType) {
            replacer.add(GenKeys.CLASS_FULL_NAME, "");
        } else {
            replacer.add(GenKeys.CLASS_FULL_NAME, "import " + column.getFieldClass().getName() + ";");
        }
        replacer.add(GenKeys.CLASS_SIMPLE_NAME, columnAndTypeTheSameType ? column.getFieldClass().getName() : column.getFieldClass().getSimpleName())
                .add(GenKeys.COLUMN_NAME, column.getColumnName())
                .add(GenKeys.INTERFACE_NAME, column.getInterfaceName())
                .add(GenKeys.FIELD_NAME, column.getFinalFieldName())
                .add(GenKeys.TABLES_AND_TYPE, column.getTableTypeDescription())
                .add(GenKeys.DB_TYPE, TypeConverter.getDataBaseTypeName(column.getDataType()))
                .add(GenKeys.PACKAGE, column.getPackage());

        column.setAlreadyGenerated(true);

        saveGeneratedTo(replacer.replaceAll(COLUMN_TEMPLATE), path, catalog, schema, "column", column.getFinalFieldName(), FileType.JAVA, override);
        generatedColumnsCount++;
    }

    void generateTable(String catalog, String schema, GenerateTable table, List<GenerateTable> allTables, String javaackage, String path, boolean override, boolean schemaIntoTable) {
        String tableName = table.getTableName();
        String tableNameCamel = GenerateFormatter.toCamelCase(table.getTableName());
        boolean pkExist = table.getPrimaryKeys() != null;
        String compositeKeyName = createCompositeKeyName(tableNameCamel, allTables);

        StringBuilder javaPackage = JavaPackageResolver.resolve(javaackage, catalog, schema);
        javaPackage.append(table.isView() ? ".view" : ".table");

        table.setJavaPackage(javaPackage.toString());

        Replacer replacer = new Replacer();


        if (pkExist) {
            if (table.getPrimaryKeys().size() == 1) {
                replacer.add(GenKeys.PK_IMPORT, "import " + PrimaryKey.class.getName() + ";")
                        .add(GenKeys.PK_TYPE_IMPORT, "import " + table.getPrimaryKeys().get(0).getFieldClass().getName() + ";\n");
            } else {
                replacer.add(GenKeys.PK_IMPORT, "import " + PrimaryCompositeKey.class.getName() + ";\n" + "import " + CompositeColumns.class.getName() + ";")
                        .add(GenKeys.PK_TYPE_IMPORT, "");
            }
        } else {
            replacer.add(GenKeys.PK_IMPORT, "")
                    .add(GenKeys.PK_TYPE_IMPORT, "");
        }

        replacer.add(GenKeys.COLUMN_IMPORTS, table.getColumnImport(tableNameCamel))
                .add(GenKeys.COLUMN_INTERFACES, table.getColumnInterfaces(compositeKeyName, tableNameCamel))
                .add(GenKeys.TABLE_NAME, schemaIntoTable ? (schema + "." + tableName) : tableName)
                .add(GenKeys.TABLE_CAMEL_NAME, tableNameCamel)
                .add(GenKeys.COLUMN_METHODS, table.getColumnMethods())
                .add(GenKeys.TABLE_PACKAGE, table.getJavaPackage());
        if (pkExist) {
            if (table.getPrimaryKeys().size() == 1) {
                replacer.add(GenKeys.PK_INTERFACE, PrimaryKey.class.getSimpleName() + "<" + tableNameCamel + "," + table.getPrimaryKeys().get(0).getFieldClass().getSimpleName() + "," + table.getPrimaryKeys().get(0).getFinalFieldNameShortOrLong(tableNameCamel) + ">");
                replacer.add(GenKeys.PK_ID_METHOD, table.getPkIdMethod());
            } else {
                replacer.add(GenKeys.PK_INTERFACE, PrimaryCompositeKey.class.getSimpleName() + "<" + tableNameCamel + "," + compositeKeyName + "<" + tableNameCamel + ">>");
                replacer.add(GenKeys.PK_ID_METHOD, table.getPkKeyMethod(compositeKeyName));

//                generateCompositeKey(compositeKeyName, table);
                table.setCompositeKeyName(compositeKeyName);
            }
        } else {
            replacer.add(GenKeys.PK_INTERFACE, "")
                    .add(GenKeys.PK_ID_METHOD, "");
        }

        table.setAlreadyGenerated(true);

        saveGeneratedTo(replacer.replaceAll(tabtemp), path, catalog, schema, (table.isView() ? "view" : "table"), tableNameCamel, FileType.JAVA, override);
        generatedTablesCount++;

    }

}
