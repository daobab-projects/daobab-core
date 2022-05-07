package io.daobab.generator;

import io.daobab.model.Composite;
import io.daobab.model.CompositeColumns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
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
    private boolean alreadyGenerated;

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
            boolean pkincolumns = false;
            List<GenerateColumn> pktoAdd = new ArrayList<>();
            for (GenerateColumn g : getColumnList()) {
                for (GenerateColumn pKey : primaryKey) {
                    if (g.getColumnName().equals(pKey.getColumnName())) {
                        pktoAdd.add(pKey);
                    }
                }

            }

            if (!pkincolumns) {
                getColumnList().addAll(pktoAdd);
            }
        }
    }

    public GenerateTable() {
    }


    public String toString() {
        StringBuilder primaryKeys = new StringBuilder();
        if (this.primaryKeys != null) {
            for (GenerateColumn pk : this.primaryKeys) {
                primaryKeys.append(pk.getFinalFieldNameShortOrLong(tableName));
            }
        }
        return "name:" + tableName + ",schema:" + schemaName + ",type:" + type + ",remarks:" + remarks + ", PK:" + (getPrimaryKeys() == null ? "NO" : primaryKeys.toString());
    }

    public String getColumnImport(String tableName) {
        StringBuilder sb = new StringBuilder();
        for (GenerateColumn gc : getColumnList()) {
            if (gc.getFinalFieldName().equalsIgnoreCase(tableName)) continue;
            sb.append("import ")
                    .append(gc.getPackage());

            sb.append(".").append(gc.getFinalFieldName())
                    .append(";")
                    .append("\n");
        }
        return sb.toString();
    }

    public String getCompositeColumnImport(String tableName) {
        StringBuilder sb = new StringBuilder();
        for (GenerateColumn gc : getColumnList()) {
            if (gc.getFinalFieldName().equalsIgnoreCase(tableName)) continue;
            sb.append("import ")
                    .append(gc.getPackage());

            sb.append(".").append(gc.getFinalFieldName())
                    .append(";")
                    .append("\n");
        }
        return sb.toString();
    }

    public String getCompositeKeyInterfaces(String tableCamelName) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < getPrimaryKeys().size(); i++) {
            GenerateColumn gc = getPrimaryKeys().get(i);
            sb.append(" & ");
            sb.append(gc.getColumnInterfaceType(tableCamelName));
        }
        return sb.toString();
    }

    public String getCompositeKeyInterfaces2(String tableCamelName) {
        StringBuilder sb = new StringBuilder();

        List<GenerateColumn> anotherCompositesColumns = new ArrayList<>();

//        for (int i = 0; i < getInheritedSubCompositeKeys().size(); i++) {
//            GenerateTable gt = getInheritedSubCompositeKeys().get(i);
//
//            anotherCompositesColumns.addAll(gt.getPrimaryKeys());
//            sb.append(" ");
//            sb.append(gt.getCompositeKeyName());
//            sb.append("<");
//            sb.append(tableCamelName);
//            sb.append(">,");
//        }

        boolean atLeastOneColumnAdded = false;
        for (int i = 0; i < getPrimaryKeys().size(); i++) {
            GenerateColumn primKeyColumn = getPrimaryKeys().get(i);
            boolean columnAlreadyImported = false;
            for (GenerateColumn targetgt : anotherCompositesColumns) {
                if (targetgt.getColumnName().equals(primKeyColumn.getColumnName())) {
                    columnAlreadyImported = true;
                    break;
                }
            }
            if (columnAlreadyImported) continue;
            atLeastOneColumnAdded = true;
            sb.append(" ");
            sb.append(primKeyColumn.getColumnInterfaceType(tableCamelName));
            if (i < getPrimaryKeys().size() - 1) sb.append(",");
        }

        if (!getInheritedSubCompositeKeys().isEmpty() && !atLeastOneColumnAdded) {
            sb.append(",");
        }

        sb.append(", ").append(Composite.class.getSimpleName());
        sb.append("<").append(tableCamelName).append(">");

        return sb.toString();
    }

    public String getColumnInterfaces(String compositeKeyName, String tableCamelName) {
        StringBuilder sb = new StringBuilder();

        if (getPrimaryKeys() != null && getPrimaryKeys().size() > 1) {
            sb.append("\t");

            sb.append(compositeKeyName)
                    .append("<")
                    .append(tableCamelName)
                    .append(">,\n");
        }

        for (int i = 0; i < getColumnList().size(); i++) {
            GenerateColumn gc = getColumnList().get(i);
            sb.append("\t");
            sb.append(gc.getColumnInterface(tableCamelName, this.getTableName()));
            if (i < getColumnList().size() - 1) sb.append(",\n");
        }

        if (getPrimaryKeys() != null) sb.append(",\n");

        return sb.toString();
    }

    public String getColumnMethods() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < getColumnList().size(); i++) {
            GenerateColumn gc = getColumnList().get(i);
            sb.append(getTableColumn(gc));
            if (i < getColumnList().size() - 1) sb.append(",");
            sb.append("\n");
        }
        return sb.toString();
    }

    public StringBuilder getTableColumn(GenerateColumn gc) {
        GeneratedColumnInTable generatedColumnInTable = gc.getColumnInTableOrCreate(this.getTableName());

        StringBuilder sb = new StringBuilder();
        sb.append("\t").append("\t").append("\t");
        sb.append("new TableColumn(col");
        sb.append(gc.getInterfaceName());
        sb.append("())");

        if (generatedColumnInTable.isPk()) {
            sb.append(".primaryKey()");
        }

        if (generatedColumnInTable.getColumnSize() == 2147483647) {
            sb.append(".lob()");
        } else if (generatedColumnInTable.getColumnSize() > 0) {
            sb.append(".size(");
            sb.append(generatedColumnInTable.getColumnSize());
            sb.append(")");
        }

        if (generatedColumnInTable.getDecimalDigits() != null && !generatedColumnInTable.getDecimalDigits().trim().isEmpty() && !"0".equals(generatedColumnInTable.getDecimalDigits())) {
            sb.append(".scale(");
            sb.append(generatedColumnInTable.getDecimalDigits());
            sb.append(")");
        }

        if ("yes".equalsIgnoreCase(generatedColumnInTable.getNullable())) {
            sb.append(".nullable()");
        }
        return sb;
    }

    public String getPkIdMethod() {
        if (getPrimaryKeys() == null) return "";
        String tableName = GenerateFormatter.toCamelCase(getTableName());
        return "@Override\n" +
                "\tpublic Column<" + tableName + "," + getPrimaryKeys().get(0).getFieldClass().getSimpleName() + "," + getPrimaryKeys().get(0).getFinalFieldNameShortOrLong(tableName) + "> colID() {\n" +
                "\t\treturn col" + getPrimaryKeys().get(0).getInterfaceName() + "();\n" +
                "\t}" +
                "\n" +
                "\n" +
                "\t@Override\n" +
                "\tpublic int hashCode() {\n" +
                "\t\treturn Objects.hashCode(getId());\n" +
                "\t}\n" +
                "\n" +
                "\t@Override\n" +
                "\tpublic boolean equals(Object obj) {\n" +
                "\t\tif (this == obj)return true;\n" +
                "\t\tif (obj == null)return false;\n" +
                "\t\tif (getClass() != obj.getClass())return false;\n" +
                "\t\tPrimaryKey<?,?,?> other = (PrimaryKey<?,?,?>) obj;\n" +
                "\t\treturn Objects.equals(getId(), other.getId());\n" +
                "\t}\n" +
                "\n";
    }


    public String getPkKeyMethod(String compositeKeyName) {
        if (getPrimaryKeys() == null) return "";
        String tableCamelName = GenerateFormatter.toCamelCase(getTableName());

        StringBuilder sb = new StringBuilder();
        sb.append("@Override\n" +
                "\tpublic " + CompositeColumns.class.getSimpleName() + "<" + compositeKeyName + "<" + tableCamelName + ">>" + " keyColumns() {\n" +
                "\t\treturn " + " composite" + compositeKeyName + "();\n\t\t}");

        return sb.toString();
    }


    public String getCompositeMethod(String compositeKeyName) {
        if (getPrimaryKeys() == null) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("default ").append(CompositeColumns.class.getSimpleName()).append("<").append(compositeKeyName).append("<E>>").append(" composite").append(compositeKeyName).append("() {\n").append("\treturn new ").append(CompositeColumns.class.getSimpleName()).append("<>(\n");

        for (int i = 0; i < getPrimaryKeys().size(); i++) {
            sb.append(getTableColumn(getPrimaryKeys().get(i)));
            if (i < getPrimaryKeys().size() - 1) sb.append(",\n");
        }
        sb.append(");\n\t\t}");

        return sb.toString();
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

    public boolean isAlreadyGenerated() {
        return alreadyGenerated;
    }

    public void setAlreadyGenerated(boolean alreadyGenerated) {
        this.alreadyGenerated = alreadyGenerated;
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
            for (GenerateColumn tabcol : target) {
                if (tabcol.getColumnName().equals(col.getColumnName())) {
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
