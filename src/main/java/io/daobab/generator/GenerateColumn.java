package io.daobab.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class GenerateColumn extends HashMap<String, GeneratedColumnInTable> {

    private final List<TableAndType> tables = new ArrayList<>();
    private String columnName;
    private int dataType;
    private Class fieldClass;
    private String interfaceName;
    private String javaPackage;
    private String fieldName;
    private String finalFieldName;
    private boolean alreadyGenerated;


    public GenerateColumn() {
    }

    public GenerateColumn(String columnName, Class<?> fieldClass) {
        setColumnName(columnName.replace("\\s", "_").replace("#", "_"));
        setFieldClass(fieldClass);
    }

    public void addTableUsage(String tableName, String jdbcType) {
        tables.add(new TableAndType(tableName, jdbcType));
    }

    @Override
    public String toString() {
        return "name:" + columnName + ",type:" + dataType;
    }


    public String getColumnInterface(String tableCamelName, String tableRealName) {

        GeneratedColumnInTable g = getColumnInTable(tableRealName);
        if (g == null) {
            System.out.println("table " + tableRealName + " has no data");
        }

        return getFinalFieldNameShortOrLong(tableCamelName) +
                "<" +
                tableCamelName +
                ">";
    }

    public String getColumnInterfaceType(String tableRealName) {

        return getFinalFieldNameShortOrLong(tableRealName) +
                "<" +
                tableRealName +
                ">";
    }

    public String getFinalFieldNameShortOrLong(String tableCamelName) {
        if (getFinalFieldName().equalsIgnoreCase(tableCamelName)) {
            return getPackage() + "." + getFinalFieldName();
        } else {
            return getFinalFieldName();
        }
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public void setDataType(String dataType, JDBCTypeConverter typeConverter) {
        if (dataType == null) return;
        this.dataType = Integer.parseInt(dataType);
        setFieldClass(typeConverter.convert(JDBCTypeConverter.UNKNOWN_TABLE, this));
    }

    public Class getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(Class fieldClass) {
        this.fieldClass = fieldClass;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getPackage() {
        return javaPackage;
    }

    public void setPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isAlreadyGenerated() {
        return alreadyGenerated;
    }

    public void setAlreadyGenerated(boolean alreadyGenerated) {
        this.alreadyGenerated = alreadyGenerated;
    }


    public String getFinalFieldName() {
        return finalFieldName;
    }

    public void setFinalFieldName(String finalFieldName) {
        this.finalFieldName = finalFieldName;
        this.interfaceName = finalFieldName;
    }

    public GeneratedColumnInTable getColumnInTable(String tableName) {
        return get(tableName);
    }

    public GeneratedColumnInTable getColumnInTableOrCreate(String tableName) {
        return this.computeIfAbsent(tableName, k -> new GeneratedColumnInTable());
    }

    public String getTableTypeDescription() {
        if (tables.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("    /**\n");
        for (TableAndType tableAndType : tables) {
            sb.append("     * ").append(tableAndType.table).append(": ").append(tableAndType.type).append("\n");
        }
        sb.append("     */\n");

        return sb.toString();
    }

    private static class TableAndType {
        private final String table;
        private final String type;

        private TableAndType(String table, String type) {
            this.table = table;
            this.type = type;
        }
    }

}
