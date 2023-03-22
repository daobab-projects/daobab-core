package io.daobab.generator;

import io.daobab.generator.template.GenKeys;
import io.daobab.generator.template.TemplateLanguage;
import io.daobab.generator.template.TemplateProvider;
import io.daobab.model.CompositeColumns;
import io.daobab.model.PrimaryCompositeKey;
import io.daobab.model.PrimaryKey;

import java.util.List;
import java.util.Objects;

import static io.daobab.generator.SaveGenerated.saveGeneratedTo;
import static io.daobab.generator.template.TemplateLanguage.KOTLIN;
import static io.daobab.generator.template.TemplateLanguage.TYPE_SCRIPT;
import static io.daobab.generator.template.TemplateType.*;

public class Writer {


    int generatedColumnsCount = 0;
    int generatedTablesCount = 0;
    int generatedCompositesCount = 0;
    int generatedTargetsCount = 0;

    private TemplateLanguage language;

    public Writer(TemplateLanguage language) {
        this.language = language;
    }

    public void setLanguage(TemplateLanguage language) {
        this.language = language;
    }

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

    void generateCompositeKey(TemplateLanguage language, GenerateTable table, String path, boolean override) {
        if (Objects.requireNonNull(language) == TemplateLanguage.JAVA) {
            generateJavaCompositeKey(table, path, override);
        }
    }

    void generateJavaCompositeKey(GenerateTable table, String path, boolean override) {

        String tableNameCamel = GenerateFormatter.toCamelCase(table.getTableName());

        Replacer replacer = new Replacer();

        replacer.add(GenKeys.COLUMN_IMPORTS, table.getColumnImport(tableNameCamel))
                .add(GenKeys.TABLE_PACKAGE, table.getJavaPackage())
                .add(GenKeys.COMPOSITE_KEY_COLUMN_TYPE_INTERFACES, table.getCompositeKeyInterfaces("E", language))
                .add(GenKeys.COMPOSITE_KEY_COLUMN_INTERFACES, table.getCompositeKeyInterfaces2("E", language))
                .add(GenKeys.COMPOSITE_KEY_METHOD, table.getCompositeMethod(table.getCompositeKeyName(), language))
                .add(GenKeys.COMPOSITE_NAME, table.getCompositeKeyName());
        saveGeneratedTo(replacer.replaceAll(TemplateProvider.getTemplate(language, COMPOSITE_KEY_TEMP)), path, table.getCatalogName(), table.getSchemaName(), (table.isView() ? "view" : "table"), table.getCompositeKeyName(), language, override);
        generatedCompositesCount++;
    }

    void generateJavaTarget(String catalog, String schema, List<GenerateTable> tables, String javaPackageName, String path, boolean override) {
        GenerateTarget target = new GenerateTarget();
        target.setSchemaName(schema);
        target.setCatalogName(catalog);
        target.setTableList(tables);

        StringBuilder javaPackage = JavaPackageResolver.resolve(javaPackageName, catalog, schema);
        target.setJavaPackage(javaPackage.toString());

        Replacer replacer = new Replacer();

        replacer.add(GenKeys.TARGET_CLASS_NAME, target.getTargetClassName())
                .add(GenKeys.TAB_ARRAY, target.getTableArray())
                .add(GenKeys.TARGET_TABLES_INTERFACE, target.getTargetTablesInterfaceName())
                .add(GenKeys.TARGET_PACKAGE, target.getJavaPackage() + ";");

        saveGeneratedTo(replacer.replaceAll(TemplateProvider.getTemplate(language, DATA_BASE_TARGET_CLASS)), path, catalog, schema, null, target.getTargetClassName(), language, override);
        generatedTargetsCount++;

        replacer.clear();

        replacer.add(GenKeys.TABLES_INTERFACE_NAME, target.getTargetTablesInterfaceName())
                .add(GenKeys.TAB_IMPORTS, target.getTableImports())
                .add(GenKeys.TABLES_INITIATED, target.getTablesInitiation(language))
                .add(GenKeys.TARGET_PACKAGE, target.getJavaPackage() + ";");

        saveGeneratedTo(replacer.replaceAll(TemplateProvider.getTemplate(language, DATABASE_TABLES_INTERFACE)), path, catalog, schema, null, target.getTargetTablesInterfaceName(), language, override);
    }

    void createTypeScriptTables(String catalog, String schema, List<GenerateTable> entities, String path, boolean override) {
        if (entities == null) return;

        for (GenerateTable entity : entities) {
            StringBuilder sb = new StringBuilder();
            List<GenerateColumn> ecolumns = entity.getColumnList();

            for (GenerateColumn column : ecolumns) {
                sb.append(" ")
                        .append(column.getFinalFieldName()).append(": ")
                        .append(JDBCTypeConverter.convertToTS(column.getFieldClass()))
                        .append(";\n");
            }

            String str = "\n\n" + TemplateProvider.getTemplate(TYPE_SCRIPT, TABLE_CLASS).replaceAll(GenKeys.TABLE_CAMEL_NAME, GenerateFormatter.toCamelCase(entity.getTableName())).replaceAll(GenKeys.FIELDS, sb.toString()) + "\n\n";

            saveGeneratedTo(str, path, catalog, schema, "typescript", GenerateFormatter.toTypeScriptCase(entity.getTableName()), TYPE_SCRIPT, override);
        }
    }

    void generateJavaColumn(String catalog, String schema, GenerateColumn column, String path, boolean override) {

        Replacer replacer = new Replacer();


        replacer.add(GenKeys.CLASS_SIMPLE_NAME, getCorrectClassSimpleNameForLanguage(replacer, language, column))
                .add(GenKeys.COLUMN_NAME, column.getColumnName())
                .add(GenKeys.INTERFACE_NAME, column.getInterfaceName())
                .add(GenKeys.FIELD_NAME, column.getFinalFieldName())
                .add(GenKeys.TABLES_AND_TYPE, column.getTableTypeDescription())
                .add(GenKeys.DB_TYPE, JDBCTypeConverter.getDataBaseTypeName(column.getDataType()))
                .add(GenKeys.PACKAGE, column.getPackage());

        column.setAlreadyGenerated(true);

        saveGeneratedTo(replacer.replaceAll(TemplateProvider.getTemplate(language, COLUMN_INTERFACE)), path, catalog, schema, "column", column.getFinalFieldName(), language, override);
        generatedColumnsCount++;
    }

    private String getCorrectClassSimpleNameForLanguage(Replacer replacer, TemplateLanguage language, GenerateColumn column) {
        if (language == KOTLIN) {
            String longName = column.getFieldClass().getName();
            String shortName = column.getFieldClass().getSimpleName();

            if (column.getFieldClass().equals(Integer.class)) {
                longName = "";
                shortName = "Int";
            } else if (byte[].class.equals(column.getFieldClass())) {
                longName = "";
                shortName = "ByteArray";
            }

            boolean columnAndTypeTheSameType = shortName.equalsIgnoreCase(column.getFinalFieldName());

            if (byte[].class.equals(column.getFieldClass()) || columnAndTypeTheSameType) {
                replacer.add(GenKeys.CLASS_FULL_NAME, "");
            } else if (column.getFieldClass().getName().startsWith("java.lang.")) {
                replacer.add(GenKeys.CLASS_FULL_NAME, "");
            } else {
                replacer.add(GenKeys.CLASS_FULL_NAME, "import " + longName + ";");
            }

            return columnAndTypeTheSameType && !longName.isEmpty() ? longName : shortName;

        } else {
            boolean columnAndTypeTheSameType = column.getFieldClass().getSimpleName().equalsIgnoreCase(column.getFinalFieldName());

            if (byte[].class.equals(column.getFieldClass()) || columnAndTypeTheSameType) {
                replacer.add(GenKeys.CLASS_FULL_NAME, "");
            } else if (column.getFieldClass().getName().startsWith("java.lang.")) {
                replacer.add(GenKeys.CLASS_FULL_NAME, "");
            } else {
                replacer.add(GenKeys.CLASS_FULL_NAME, "import " + column.getFieldClass().getName() + ";");
            }

            return columnAndTypeTheSameType ? column.getFieldClass().getName() : column.getFieldClass().getSimpleName();
        }
    }

    void generateJavaTable(String catalog, String schema, GenerateTable table, List<GenerateTable> allTables, String javaackage, String path, boolean override, boolean schemaIntoTable) {
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
                .add(GenKeys.COLUMN_METHODS, table.getColumnMethods(language))
                .add(GenKeys.TABLE_PACKAGE, table.getJavaPackage());
        if (pkExist) {
            if (table.getPrimaryKeys().size() == 1) {
                GenerateColumn pkCol = table.getPrimaryKeys().get(0);
                String pkColSimpleName = table.getPkTypeSimpleName(language, pkCol);
                if (language == KOTLIN) {
                    replacer.add(GenKeys.PK_INTERFACE, PrimaryKey.class.getSimpleName() + "<" + tableNameCamel + "," + pkColSimpleName + "," + table.getPrimaryKeys().get(0).getFinalFieldNameShortOrLong(tableNameCamel) + "<*>>");
                    replacer.add(GenKeys.PK_ID_METHOD, table.getPkIdMethod(language));
                } else {
                    //Java
                    replacer.add(GenKeys.PK_INTERFACE, PrimaryKey.class.getSimpleName() + "<" + tableNameCamel + "," + pkColSimpleName + "," + table.getPrimaryKeys().get(0).getFinalFieldNameShortOrLong(tableNameCamel) + ">");
                    replacer.add(GenKeys.PK_ID_METHOD, table.getPkIdMethod(language));
                }

            } else {
                replacer.add(GenKeys.PK_INTERFACE, PrimaryCompositeKey.class.getSimpleName() + "<" + tableNameCamel + "," + compositeKeyName + "<" + tableNameCamel + ">>");
                replacer.add(GenKeys.PK_ID_METHOD, table.getPkKeyMethod(compositeKeyName, language));

//                generateCompositeKey(compositeKeyName, table);
                table.setCompositeKeyName(compositeKeyName);
            }
        } else {
            replacer.add(GenKeys.PK_INTERFACE, "")
                    .add(GenKeys.PK_ID_METHOD, "");
        }

        table.setAlreadyGenerated(true);

        saveGeneratedTo(replacer.replaceAll(TemplateProvider.getTemplate(language, TABLE_CLASS)), path, catalog, schema, (table.isView() ? "view" : "table"), tableNameCamel, language, override);
        generatedTablesCount++;

    }

}
