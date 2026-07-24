package io.daobab.generator;

import io.daobab.generator.template.GenKeys;
import io.daobab.generator.template.TemplateLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.daobab.generator.template.TemplateLanguage.KOTLIN;

/**
 * The per-column model of the runtime generator - the counterpart of the annotation processor's
 * {@code ColumnModel}. Being a {@code Map<tableName, GeneratedColumnInTable>}, it maps every table the column
 * appears in to that table's metadata; on top of that it carries the column name, the JDBC data type, the
 * resolved Java {@link #getFieldClass() field class}, the shared interface name / package and the disambiguated
 * {@link #getFinalFieldName() final field name}. It also renders the column interface reference for a target
 * language and the HTML Javadoc usage table.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
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

    /**
     * @param columnName the database column name (whitespace and {@code #} normalized to {@code _})
     * @param fieldClass the Java type of the column
     */
    public GenerateColumn(String columnName, Class<?> fieldClass) {
        setColumnName(columnName.replace("\\s", "_").replace("#", "_"));
        setFieldClass(fieldClass);
    }

    /**
     * Records that the column appears in {@code tableName} with the given JDBC type name (for the doc table).
     */
    public void addTableUsage(String tableName, String jdbcType) {
        tables.add(new TableAndType(tableName, jdbcType));
    }

    @Override
    public String toString() {
        return "name:" + columnName + ",type:" + dataType;
    }


    /**
     * The column interface reference for an entity (e.g. {@code Title<BookEntity>}), language-aware: Kotlin adds
     * the value type and its nullability.
     */
    public String getColumnInterface(Replacer replacer, TemplateLanguage language, String tableCamelName, String entityName, String tableRealName) {

        GeneratedColumnInTable g = getColumnInTable(tableRealName);
        if (g == null) {
            System.out.println("table " + tableRealName + " has no data");
            return "error";
        }
        String type = getCorrectClassSimpleNameForLanguage(replacer, language);

        if (TemplateLanguage.JAVA.equals(language)) {
            return getFinalFieldNameShortOrLong(tableCamelName) + "<" + entityName + ">";
        } else if (TemplateLanguage.KOTLIN.equals(language)) {
            return getFinalFieldNameShortOrLong(tableCamelName) + "<" + entityName + ", " + type + ("1".equalsIgnoreCase(g.getNullable()) ? "?" : "") + ">";
        } else {
            throw new RuntimeException("Unknown language: " + language);
        }
    }

    /** The column interface reference typed by the table itself (used when building composite keys). */
    public String getColumnInterfaceType(Replacer replacer, TemplateLanguage language, String tableRealName) {

        if (TemplateLanguage.JAVA.equals(language)) {
            return getFinalFieldNameShortOrLong(tableRealName) + "<" + tableRealName + ">";
        }

        return getFinalFieldNameShortOrLong(tableRealName) +
                "<" + tableRealName + ", " + getCorrectClassSimpleNameForLanguage(replacer, language) + ">";
    }

    /** The final field name, fully qualified with its package when it collides with the table name. */
    public String getFinalFieldNameShortOrLong(String tableCamelName) {
        if (getFinalFieldName().equalsIgnoreCase(tableCamelName)) {
            return getPackage() + "." + getFinalFieldName();
        } else {
            return getFinalFieldName();
        }
    }

    /** The database column name. */
    public String getColumnName() {
        return columnName;
    }

    /** Sets the database column name. */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /** The JDBC data type ({@link java.sql.Types}). */
    public int getDataType() {
        return dataType;
    }

    /** Sets the JDBC data type. */
    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    /** Sets the JDBC data type from its string form and resolves the field class through the converter. */
    public void setDataType(String dataType, JDBCTypeConverter typeConverter) {
        if (dataType == null) return;
        this.dataType = Integer.parseInt(dataType);
        setFieldClass(typeConverter.convert(JDBCTypeConverter.UNKNOWN_TABLE, this));
    }

    /** The Java type of the column. */
    public Class getFieldClass() {
        return fieldClass;
    }

    /** Sets the Java type of the column. */
    public void setFieldClass(Class fieldClass) {
        this.fieldClass = fieldClass;
    }

    /** The generated column interface name. */
    public String getInterfaceName() {
        return interfaceName;
    }

    /** Sets the generated column interface name. */
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    /** The package of the generated column interface. */
    public String getPackage() {
        return javaPackage;
    }

    /** Sets the package of the generated column interface. */
    public void setPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    /** The field (logical) name. */
    public String getFieldName() {
        return fieldName;
    }

    /** Sets the field name. */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /** Whether the column interface has already been generated in this run. */
    public boolean isAlreadyGenerated() {
        return alreadyGenerated;
    }

    /** Marks the column interface as already generated. */
    public void setAlreadyGenerated(boolean alreadyGenerated) {
        this.alreadyGenerated = alreadyGenerated;
    }


    /** The disambiguated final field name (also used as the interface name). */
    public String getFinalFieldName() {
        return finalFieldName;
    }

    /** Sets the final field name (and the interface name to match). */
    public void setFinalFieldName(String finalFieldName) {
        this.finalFieldName = finalFieldName;
        this.interfaceName = finalFieldName;
    }

    /** The column's metadata in the given table, or {@code null}. */
    public GeneratedColumnInTable getColumnInTable(String tableName) {
        return get(tableName);
    }

    /** The column's metadata in the given table, creating an empty one when absent. */
    public GeneratedColumnInTable getColumnInTableOrCreate(String tableName) {
        return this.computeIfAbsent(tableName, k -> new GeneratedColumnInTable());
    }

    /** The HTML Javadoc table listing every table the column appears in (Table / Type / Size / Nullable). */
    public String getTableTypeDescription() {
        if (tables.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();

        sb.append("\t/**\n");
        sb.append("\t * Column <b>").append(columnName).append("</b> occurrences across the generated tables:\n");
        sb.append("\t * <table>\n");
        sb.append("\t * <caption>column usage</caption>\n");
        sb.append("\t * <tr><th>Table</th><th>Type</th><th>Size</th><th>Nullable</th></tr>\n");
        for (TableAndType tableAndType : tables) {
            GeneratedColumnInTable git = get(tableAndType.table);
            sb.append("\t * <tr><td>").append(tableAndType.table)
                    .append("</td><td>").append(tableAndType.type)
                    .append("</td><td>").append(git.getColumnSize())
                    .append("</td><td>").append("1".equals(git.getNullable()))
                    .append("</td></tr>\n");
        }
        sb.append("\t * </table>\n");
        sb.append("\t */");

        return sb.toString();
    }

    /**
     * The value type name for the target language, registering the needed {@code import} (or none for
     * {@code java.lang} types and for a type whose name matches the field name) into the replacer under
     * {@link GenKeys#CLASS_FULL_NAME}. Kotlin maps {@code Integer} to {@code Int} and {@code byte[]} to
     * {@code ByteArray}.
     */
    public String getCorrectClassSimpleNameForLanguage(Replacer replacer, TemplateLanguage language) {
        if (language == KOTLIN) {
            String longName = getFieldClass().getName();
            String shortName = getFieldClass().getSimpleName();

            if (getFieldClass().equals(Integer.class)) {
                longName = "";
                shortName = "Int";
            } else if (byte[].class.equals(getFieldClass())) {
                longName = "";
                shortName = "ByteArray";
            }

            boolean columnAndTypeTheSameType = shortName.equalsIgnoreCase(getFinalFieldName());

            if (byte[].class.equals(getFieldClass()) || columnAndTypeTheSameType) {
                replacer.add(GenKeys.CLASS_FULL_NAME, "");
            } else if (getFieldClass().getName().contains("java.lang.")) {
                replacer.add(GenKeys.CLASS_FULL_NAME, "");
            } else {
                replacer.add(GenKeys.CLASS_FULL_NAME, "import " + longName);
            }

            return columnAndTypeTheSameType && !longName.isEmpty() ? longName : shortName;

        } else {
            boolean columnAndTypeTheSameType = getFieldClass().getSimpleName().equalsIgnoreCase(getFinalFieldName());

            if (byte[].class.equals(getFieldClass()) || columnAndTypeTheSameType) {
                replacer.add(GenKeys.CLASS_FULL_NAME, "");
            } else if (getFieldClass().getName().startsWith("java.lang.")) {
                replacer.add(GenKeys.CLASS_FULL_NAME, "");
            } else {
                replacer.add(GenKeys.CLASS_FULL_NAME, "import " + getFieldClass().getName() + ";");
            }

            return columnAndTypeTheSameType ? getFieldClass().getName() : getFieldClass().getSimpleName();
        }
    }

    /** One usage of the column by a table: a row of the {@link #getTableTypeDescription() doc table}. */
    private static class TableAndType {
        private final String table;
        private final String type;

        private TableAndType(String table, String type) {
            this.table = table;
            this.type = type;
        }
    }


}
