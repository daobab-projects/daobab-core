package io.daobab.generator;

import io.daobab.generator.template.GenKeys;
import io.daobab.generator.template.TemplateLanguage;
import io.daobab.generator.template.TemplateProvider;
import io.daobab.model.Composite;
import io.daobab.model.TableColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.daobab.generator.template.TemplateLanguage.JAVA;
import static io.daobab.generator.template.TemplateLanguage.KOTLIN;
import static io.daobab.generator.template.TemplateType.*;
import static java.lang.String.format;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class GenerateTable {

    private String tableName;
    private String schemaName;
    private String catalogName;
    private String type;
    private String remarks;
    private List<GenerateColumn> primaryKeys;
    private String compositeKeyName;
    private List<GenerateTable> inheritedSubCompositeKeys = new ArrayList<>();
    private List<GenerateColumn> columnList = new ArrayList<>();

    private String javaPackage;

    private boolean view = false;

    public GenerateTable(String tableName, List<GenerateColumn> primaryKey, List<GenerateColumn> allColumns, GenerateColumn... columns) {
        setTableName(tableName);
        if (primaryKey != null) setPrimaryKeys(primaryKey);

        if (columns != null) {
            for (GenerateColumn tableColumn : columns) {
                boolean foundcolumn = false;
                for (GenerateColumn gc : allColumns) {
                    if (gc.getColumnName().equals(tableColumn.getColumnName())) {
                        getColumnList().add(gc);
                        foundcolumn = true;
                        break;
                    }
                }
                if (!foundcolumn) {
                    getColumnList().add(tableColumn);
                    allColumns.add(tableColumn);
                }
            }
        }

        if (columns != null) setColumnList(Arrays.asList(columns));
        if (primaryKey != null && columns == null) {
            setColumnList(primaryKey);
        }
        if (primaryKey != null && getColumnList() != null && !getColumnList().isEmpty()) {
            boolean pkPresent = false;
            List<GenerateColumn> pktoAdd = new ArrayList<>();
            for (GenerateColumn g : getColumnList()) {
                for (GenerateColumn pKey : primaryKey) {
                    if (g.getColumnName().equals(pKey.getColumnName())) {
                        pktoAdd.add(pKey);
                    }
                }
            }

            if (!pkPresent) {
                getColumnList().addAll(pktoAdd);
            }
        }
    }

    public GenerateTable() {
    }


    public String toString() {
        StringBuilder primaryKeysSB = new StringBuilder();
        if (this.primaryKeys != null) {
            for (GenerateColumn pk : this.primaryKeys) {
                primaryKeysSB.append(pk.getFinalFieldNameShortOrLong(tableName));
            }
        }
        return "name:" + tableName + ",schema:" + schemaName + ",type:" + type + ",remarks:" + remarks + ", PK:" + (getPrimaryKeys() == null ? "NO" : primaryKeysSB.toString());
    }

    public String getColumnImport(String tableName) {
        StringBuilder sb = new StringBuilder();
        for (GenerateColumn gc : getColumnList()) {
            if (gc.getFinalFieldName().equalsIgnoreCase(tableName)
                    //java.lang is always imported
                    || (gc.getPackage().startsWith("java.lang.") && gc.getPackage().lastIndexOf(".") == "java.lang.".lastIndexOf("."))
            ) {
                continue;
            }
            sb.append("import ")
                    .append(gc.getPackage())
                    .append(".").append(gc.getFinalFieldName())
                    .append(";")
                    .append("\n");
        }
        return sb.toString();
    }

    public String getCompositeColumnImport(String tableName) {
        StringBuilder sb = new StringBuilder();
        for (GenerateColumn gc : getColumnList()) {
            if (gc.getFinalFieldName().equalsIgnoreCase(tableName)){
                continue;
            }
            sb.append("import ")
                    .append(gc.getPackage())
                    .append(".").append(gc.getFinalFieldName())
                    .append(";")
                    .append("\n");
        }
        return sb.toString();
    }

    public String getCompositeKeyInterfaces(String tableCamelName, TemplateLanguage language) {
        StringBuilder sb = new StringBuilder();

        if (language == JAVA) {
            for (GenerateColumn gc : getPrimaryKeys()) {
                sb.append(" & ");
                sb.append(gc.getColumnInterfaceType(tableCamelName));
            }
        }
        return sb.toString();
    }

    public String getCompositeKeyInterfaces2(Replacer replacer, String tableCamelName, TemplateLanguage language) {
        StringBuilder sb = new StringBuilder();

        if (language == JAVA) {
            boolean atLeastOneColumnAdded = false;
            for (int i = 0; i < getPrimaryKeys().size(); i++) {
                GenerateColumn primKeyColumn = getPrimaryKeys().get(i);
                atLeastOneColumnAdded = true;
                sb.append(" ");
                sb.append(primKeyColumn.getColumnInterfaceType(tableCamelName));
                if (i < getPrimaryKeys().size() - 1) sb.append(",");
            }

            if (!getInheritedSubCompositeKeys().isEmpty() && !atLeastOneColumnAdded) {
                sb.append(",");
            }

            sb.append(format(", %s<%s>", Composite.class.getSimpleName(), tableCamelName));
        } else if (language == KOTLIN) {
            boolean atLeastOneColumnAdded = false;
            for (int i = 0; i < getPrimaryKeys().size(); i++) {
                GenerateColumn primKeyColumn = getPrimaryKeys().get(i);
                GeneratedColumnInTable git = primKeyColumn.getColumnInTableOrCreate(tableName);
                atLeastOneColumnAdded = true;
                sb.append(" ")
                        .append(primKeyColumn.getFinalFieldNameShortOrLong(tableCamelName))
                        .append("<")
                        .append(tableCamelName)
                        .append(", ")
                        .append(primKeyColumn.getCorrectClassSimpleNameForLanguage(replacer, language))
                        .append(">")
                        .append(git.isNullable() ? "?" : "");
                if (i < getPrimaryKeys().size() - 1) sb.append(",");
            }

            if (!getInheritedSubCompositeKeys().isEmpty() && !atLeastOneColumnAdded) {
                sb.append(",");
            }

            sb.append(format(", %s<%s>", Composite.class.getSimpleName(), tableCamelName));
        }


        return sb.toString();
    }

    public String getColumnInterfaces(Replacer replacer, TemplateLanguage language, String compositeKeyName, String tableCamelName) {
        StringBuilder sb = new StringBuilder();

        if (getPrimaryKeys() != null && getPrimaryKeys().size() > 1) {
            sb.append(format("\t%s<%s>,%n", compositeKeyName, tableCamelName));
        }

        for (int i = 0; i < getColumnList().size(); i++) {
            GenerateColumn gc = getColumnList().get(i);
            sb.append("\t");
            sb.append(gc.getColumnInterface(replacer, language, tableCamelName, this.getTableName()));
            if (i < getColumnList().size() - 1) sb.append(",\n");
        }

        if (getPrimaryKeys() != null) sb.append(",\n");

        return sb.toString();
    }

    public String getColumnMethods(TemplateLanguage language) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < getColumnList().size(); i++) {
            GenerateColumn gc = getColumnList().get(i);
            sb.append(getTableColumn(gc, language));
            if (i < getColumnList().size() - 1) sb.append(",");
            sb.append("\n");
        }
        return sb.toString();
    }

    public StringBuilder getTableColumn(GenerateColumn gc, TemplateLanguage language) {
        GeneratedColumnInTable generatedColumnInTable = gc.getColumnInTableOrCreate(this.getTableName());

        StringBuilder sb = new StringBuilder();
        sb.append("\t").append("\t").append("\t");
        if (language == JAVA) {
            sb.append("new ");
        }

        sb.append(TableColumn.class.getSimpleName()).append(format("(col%s())", gc.getInterfaceName()));

        if (generatedColumnInTable.isPk()) {
            sb.append(".primaryKey()");
        }

        if (generatedColumnInTable.getColumnSize() == 2147483647) {
            sb.append(".lob()");
        } else if (generatedColumnInTable.getColumnSize() > 0) {
            sb.append(format(".size(%s)", generatedColumnInTable.getColumnSize()));
        }

        if (generatedColumnInTable.getDecimalDigits() != null && !generatedColumnInTable.getDecimalDigits().trim().isEmpty() && !"0".equals(generatedColumnInTable.getDecimalDigits())) {
            sb.append(format(".scale(%s)", generatedColumnInTable.getDecimalDigits()));
        }

        if ("yes" .equalsIgnoreCase(generatedColumnInTable.getNullable())) {
            sb.append(".nullable()");
        }
        return sb;
    }


    public String getPkIdMethod(TemplateLanguage language) {
        if (getPrimaryKeys() == null || getPrimaryKeys().isEmpty()) return "";

        GenerateColumn pk = getPrimaryKeys().get(0);

        Replacer replacer = new Replacer();

        return replacer
                .add(GenKeys.TABLE_NAME, GenerateFormatter.toCamelCase(getTableName()))
                .add(GenKeys.PK_TYPE_IMPORT, getPkTypeSimpleName(language, pk))
                .add(GenKeys.INTERFACE_NAME, pk.getFinalFieldNameShortOrLong(tableName))
                .add(GenKeys.CLASS_SIMPLE_NAME, pk.getCorrectClassSimpleNameForLanguage(replacer, language))
                .add(GenKeys.INTERFACE_NAME, pk.getInterfaceName())
                .replaceAll(TemplateProvider.getTemplate(language, PK_COL_METHOD));
    }

    String getPkTypeSimpleName(TemplateLanguage language, GenerateColumn pk) {
        if (language == KOTLIN) {
            String simpleName = pk.getFieldClass().getSimpleName();
            return simpleName.equals("Integer") ? "Int" : simpleName;
        } else {
            return pk.getFieldClass().getSimpleName();
        }
    }

    public String getPkKeyMethod(String compositeKeyName, TemplateLanguage language) {
        if (getPrimaryKeys() == null || getPrimaryKeys().isEmpty()) return "";
        return new Replacer()
                .add(GenKeys.TABLE_NAME, GenerateFormatter.toCamelCase(getTableName()))
                .add(GenKeys.COMPOSITE_KEY_METHOD, compositeKeyName)
                .replaceAll(TemplateProvider.getTemplate(language, COMPOSITE_PK_KEY_METHOD));
    }

    public String getCompositeMethod(String compositeKeyName, TemplateLanguage language) {
        if (getPrimaryKeys() == null) return "";

        return new Replacer()
                .add(GenKeys.COMPOSITE_NAME, compositeKeyName)
                .add(GenKeys.COMPOSITE_KEY_METHOD, getPrimaryKeys().stream().map(s -> getTableColumn(s, language)).collect(Collectors.joining(",\n")))
                .replaceAll(TemplateProvider.getTemplate(language, COMPOSITE_METHOD));
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<GenerateColumn> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<GenerateColumn> columnList) {
        this.columnList = columnList;
    }


    public List<GenerateColumn> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<GenerateColumn> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public void addPrimaryKey(GenerateColumn pk) {
        if (this.primaryKeys == null) {
            this.primaryKeys = new ArrayList<>();
        }
        getPrimaryKeys().add(pk);
    }

    public String getJavaPackage() {
        return javaPackage;
    }

    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public String getCompositeKeyName() {
        return compositeKeyName;
    }

    public void setCompositeKeyName(String compositeKeyName) {
        this.compositeKeyName = compositeKeyName;
    }

    public List<GenerateTable> getInheritedSubCompositeKeys() {
        return inheritedSubCompositeKeys;
    }

    public void setInheritedSubCompositeKeys(List<GenerateTable> inheritedSubCompositeKeys) {
        this.inheritedSubCompositeKeys = inheritedSubCompositeKeys;
    }

    public boolean containsPrimaryKeyAllCollumns(List<GenerateColumn> columns) {
        return containsPrimaryKeyAllCollumns(getPrimaryKeys(), columns);
    }


    public boolean containsPrimaryKeyAllCollumns(List<GenerateColumn> target, List<GenerateColumn> columns) {
        if (target == null || target.size() < columns.size()) {
            return false;
        }
        for (GenerateColumn col : columns) {
            boolean contains = false;
            for (GenerateColumn tabCol : target) {
                if (tabCol.getColumnName().equals(col.getColumnName())) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                return false;
            }
        }
        return true;
    }

    public String getCamelTableName() {
        return GenerateFormatter.toCamelCase(getTableName());
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }
}
