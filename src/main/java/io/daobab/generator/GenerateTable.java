package io.daobab.generator;

import io.daobab.generator.template.GenKeys;
import io.daobab.generator.template.TemplateLanguage;
import io.daobab.generator.template.TemplateProvider;
import io.daobab.model.Composite;
import io.daobab.model.TableColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.daobab.generator.template.TemplateLanguage.JAVA;
import static io.daobab.generator.template.TemplateLanguage.KOTLIN;
import static io.daobab.generator.template.TemplateType.*;
import static java.lang.String.format;

/**
 * The per-table model of the runtime generator: the table name / schema / catalog, its primary-key columns, its
 * column list and any composite-key data. It renders the pieces of the generated entity - the column imports,
 * the implemented column interfaces, the {@code columns()} body ({@link TableColumn} builder chains), the
 * primary-key accessors and the composite-key methods - by filling the language templates.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
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

    /**
     * @param tableName  the table name
     * @param primaryKey the primary-key columns
     * @param allColumns the shared pool of columns (reused so the same column is one interface across tables)
     * @param columns    this table's columns
     */
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

    /**
     * The {@code import} statements for the column interfaces (skipping self-named and {@code java.lang} ones).
     */
    public String getColumnImport(String tableName, String endImport) {
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
                    .append(endImport)
                    .append("\n");
        }
        return sb.toString();
    }

    /** The {@code import} statements for the composite-key column interfaces. */
    public String getCompositeColumnImport(String tableName) {
        StringBuilder sb = new StringBuilder();
        for (GenerateColumn gc : getColumnList()) {
            if (gc.getFinalFieldName().equalsIgnoreCase(tableName)) {
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

    /** The {@code & Column<...>} intersection appended to the composite-key interface declaration (Java only). */
    public String getCompositeKeyInterfaces(Replacer replacer, String tableCamelName, TemplateLanguage language) {
        StringBuilder sb = new StringBuilder();

        if (language == JAVA) {
            for (GenerateColumn gc : getPrimaryKeys()) {
                sb.append(" & ")
                        .append(gc.getColumnInterfaceType(replacer, language, tableCamelName));
            }
        }
        return sb.toString();
    }

    /** The composite-key column interfaces plus the {@code Composite} marker, for the key type declaration. */
    public String getCompositeKeyInterfaces2(Replacer replacer, String tableCamelName, TemplateLanguage language) {
        StringBuilder sb = new StringBuilder();

        if (language == JAVA) {
            boolean atLeastOneColumnAdded = false;
            for (int i = 0; i < getPrimaryKeys().size(); i++) {
                GenerateColumn primKeyColumn = getPrimaryKeys().get(i);
                atLeastOneColumnAdded = true;
                sb.append(primKeyColumn.getColumnInterfaceType(replacer, language, tableCamelName));
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

    /** The list of column interfaces the entity implements (prefixed by the composite-key interface when needed). */
    public String getColumnInterfaces(Replacer replacer, TemplateLanguage language, String compositeKeyName, String tableCamelName, String entityName) {
        StringBuilder sb = new StringBuilder();

        if (getPrimaryKeys() != null && getPrimaryKeys().size() > 1) {
            sb.append(format("\t%s<%s>,%n", compositeKeyName, entityName));
        }

        for (int i = 0; i < getColumnList().size(); i++) {
            GenerateColumn gc = getColumnList().get(i);
            sb.append("\t");
            sb.append(gc.getColumnInterface(replacer, language, tableCamelName, entityName, this.getTableName()));
            if (i < getColumnList().size() - 1) sb.append(",\n");
        }

        if (getPrimaryKeys() != null) sb.append(",\n");

        return sb.toString();
    }

    /** The body of {@code columns()}: one {@link TableColumn} builder chain per column. */
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

    /** One column's {@link TableColumn} builder chain (with {@code .primaryKey()}/{@code .size()}/{@code .lob()}/...). */
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

        if ("yes".equalsIgnoreCase(generatedColumnInTable.getNullable())) {
            sb.append(".nullable()");
        }
        return sb;
    }

    /** The {@code colID()} accessor for a single-column primary key (empty when there is none). */
    public String getPkIdMethod(TemplateLanguage language) {
        if (getPrimaryKeys() == null || getPrimaryKeys().isEmpty()) return "";

        GenerateColumn pk = getPrimaryKeys().getFirst();

        Replacer replacer = new Replacer();

        String pkType = pk.getFieldClass().getSimpleName();

        if (KOTLIN.equals(language)) {
            pkType = pkType.equals("Integer") ? "Int" : pkType;
        }

        return replacer
                .add(GenKeys.TABLE_NAME, getEntityCamelName(language))
                .add(GenKeys.PK_TYPE_IMPORT, pkType)
                .add(GenKeys.TYPE_IMPORTS, getPkTypeSimpleName(language, pk))
                .add(GenKeys.INTERFACE_NAME, pk.getFinalFieldNameShortOrLong(tableName))
                .replaceAll(TemplateProvider.getTemplate(language, PK_COL_METHOD));
    }

    /** The simple name of the primary-key type ({@code Integer} rendered as {@code Int} in Kotlin). */
    String getPkTypeSimpleName(TemplateLanguage language, GenerateColumn pk) {
        if (language == KOTLIN) {
            String simpleName = pk.getFieldClass().getSimpleName();
            return simpleName.equals("Integer") ? "Int" : simpleName;
        } else {
            return pk.getFieldClass().getSimpleName();
        }
    }

    /** The {@code colCompositeId()} accessor for a composite primary key (empty when there is none). */
    public String getPkKeyMethod(String compositeKeyName, TemplateLanguage language) {
        if (getPrimaryKeys() == null || getPrimaryKeys().isEmpty()) return "";
        return new Replacer()
                .add(GenKeys.TABLE_NAME, getEntityCamelName(language))
                .add(GenKeys.COMPOSITE_KEY_METHOD, compositeKeyName)
                .replaceAll(TemplateProvider.getTemplate(language, COMPOSITE_PK_KEY_METHOD));
    }

    /** The composite-key columns method (empty when there is no primary key). */
    public String getCompositeMethod(String compositeKeyName, TemplateLanguage language) {
        if (getPrimaryKeys() == null) return "";

        return new Replacer()
                .add(GenKeys.COMPOSITE_NAME, compositeKeyName)
                .add(GenKeys.COMPOSITE_KEY_METHOD, getPrimaryKeys().stream().map(s -> getTableColumn(s, language)).collect(Collectors.joining(",\n")))
                .replaceAll(TemplateProvider.getTemplate(language, COMPOSITE_METHOD));
    }

    /** The table name. */
    public String getTableName() {
        return tableName;
    }

    /** Sets the table name. */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /** The schema name. */
    public String getSchemaName() {
        return schemaName;
    }

    /** Sets the schema name. */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /** The JDBC table type (e.g. {@code TABLE}, {@code VIEW}). */
    public String getType() {
        return type;
    }

    /** Sets the JDBC table type. */
    public void setType(String type) {
        this.type = type;
    }

    /** The table remarks/comment. */
    public String getRemarks() {
        return remarks;
    }

    /** Sets the table remarks/comment. */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /** The table's columns. */
    public List<GenerateColumn> getColumnList() {
        return columnList;
    }

    /** Replaces the table's columns. */
    public void setColumnList(List<GenerateColumn> columnList) {
        this.columnList = columnList;
    }


    /** The primary-key columns, or {@code null}. */
    public List<GenerateColumn> getPrimaryKeys() {
        return primaryKeys;
    }

    /** Sets the primary-key columns. */
    public void setPrimaryKeys(List<GenerateColumn> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    /** Adds a primary-key column. */
    public void addPrimaryKey(GenerateColumn pk) {
        if (this.primaryKeys == null) {
            this.primaryKeys = new ArrayList<>();
        }
        getPrimaryKeys().add(pk);
    }

    /** The package of the generated entity. */
    public String getJavaPackage() {
        return javaPackage;
    }

    /** Sets the package of the generated entity. */
    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    /** Whether the table is a database view. */
    public boolean isView() {
        return view;
    }

    /** Sets whether the table is a database view. */
    public void setView(boolean view) {
        this.view = view;
    }

    /** The generated composite-key class name. */
    public String getCompositeKeyName() {
        return compositeKeyName;
    }

    /** Sets the generated composite-key class name. */
    public void setCompositeKeyName(String compositeKeyName) {
        this.compositeKeyName = compositeKeyName;
    }

    /** The sub-tables whose composite keys this one inherits. */
    public List<GenerateTable> getInheritedSubCompositeKeys() {
        return inheritedSubCompositeKeys;
    }

    /** Sets the inherited composite-key sub-tables. */
    public void setInheritedSubCompositeKeys(List<GenerateTable> inheritedSubCompositeKeys) {
        this.inheritedSubCompositeKeys = inheritedSubCompositeKeys;
    }

    /** Whether this table's primary key contains all the given columns. */
    public boolean containsPrimaryKeyAllCollumns(List<GenerateColumn> columns) {
        return containsPrimaryKeyAllCollumns(getPrimaryKeys(), columns);
    }


    /** Whether {@code target} contains every column of {@code columns} (by column name). */
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

    /** The camel-case table name. */
    public String getCamelTableName() {
        return GenerateFormatter.toCamelCase(getTableName());
    }

    /**
     * Generated entity class name: the camel case table name with the 'Entity' suffix (Java and Kotlin).
     */
    public String getEntityCamelName(TemplateLanguage language) {
        String camelName = getCamelTableName();
        return language == JAVA || language == KOTLIN ? camelName + "Entity" : camelName;
    }

    /**
     * Generated DTO class name: the plain camel case table name.
     */
    public String getDtoName() {
        return getCamelTableName();
    }

    /** The catalog name. */
    public String getCatalogName() {
        return catalogName;
    }

    /** Sets the catalog name. */
    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }


    /** The distinct column value types needing an import (excluding {@code java.lang} and {@code byte[]}). */
    public Set<Class> getColumnTypes() {
        return columnList.stream()
                .map(GenerateColumn::getFieldClass)
                .filter(c -> !c.getName().startsWith("java.lang"))
                .filter(c -> !c.equals(byte[].class))
                .collect(Collectors.toSet());
    }

    /** The {@code import} statements for the column value types. */
    public String getTypeImports(TemplateLanguage language) {
        String endimport = language == JAVA ? ";" : "";
        return getColumnTypes().stream().map(c -> "import " + c.getName() + endimport).collect(Collectors.joining("\n"));
    }
}
