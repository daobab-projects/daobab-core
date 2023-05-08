package io.daobab.generator;


import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TableDescriptionGenerator {

    private static final String labelName = "Name";
    private static final String labelType = "Type";
    private static final String labelSize = "Size";
    private static final String labelDBName = "DBName";
    private static final String labelDBType = "DBType";
    private static final String labelDescription = "Description";

    private static final int NAME = 1;
    private static final int TYPE = 2;
    private static final int SIZE = 3;
    private static final int DBNAME = 4;
    private static final int DBTYPE = 5;
    private static final int DESCRIPTION = 6;
    private static final int NULLABLE = 7;

    public static String getTableDescription(GenerateTable table) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n\r    /**");
        sb.append("\r     * Comment:").append(table.getRemarks()).append("<br>");
        sb.append("\r     * <pre>");

        int maxSizeName = getMaxSizeFor(NAME, table);
        int maxSizeType = getMaxSizeFor(TYPE, table);
        int maxSizeSize = getMaxSizeFor(SIZE, table);
        int maxSizeDBName = getMaxSizeFor(DBNAME, table);
        int maxSizeDBType = getMaxSizeFor(DBTYPE, table);
        int maxSizeDescription = getMaxSizeFor(DESCRIPTION, table);
        int maxSizeNullable = getMaxSizeFor(NULLABLE, table);

        List<GenerateColumn> tableColumns = table.getColumnList();


        sb.append("\r     * <u>")
                .append(fillGaps(labelName, maxSizeName))
                .append(fillGaps(labelType, maxSizeType))
                .append(fillGaps(labelSize, maxSizeSize))
                .append(fillGaps(labelDBName, maxSizeDBName))
                .append(fillGaps(labelDBType, maxSizeDBType))
                .append(fillGaps(labelDescription, maxSizeDescription))
                .append("</u>");

        tableColumns.sort(new ComparatorByFinalFieldName());
        for (GenerateColumn c : tableColumns) {
            GeneratedColumnInTable g = c.getColumnInTable(table.getTableName());
            sb.append("\r     * ")
                    .append(fillGaps(g.isPk() ? (c.getFinalFieldName() + "(PK)") : c.getFinalFieldName(), maxSizeName))
                    .append(fillGaps(c.getFieldClass().getSimpleName(), maxSizeType))
                    .append(fillGaps(String.valueOf(g.getColumnSize()), maxSizeSize))
                    .append(fillGaps(c.getColumnName() == null ? "" : c.getColumnName(), maxSizeDBName))
                    .append(fillGaps(JDBCTypeConverter.getDataBaseTypeName(c.getDataType()), maxSizeDBType))
                    .append(fillGaps(g.getRemarks() == null ? "" : g.getRemarks(), maxSizeDescription));
        }

        sb.append("\r     * </pre>");
        sb.append("\r    */\n");

        return sb.toString();
    }

    private static StringBuilder fillGaps(String label, int maxSize) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        int labelSize = 0;
        if (label != null) {
            labelSize = label.length();
            sb.append(label);
        }
        for (int i = labelSize; i < maxSize; i++) {
            sb.append(" ");
        }
        sb.append(" ");
        return sb;
    }

    private static int getMaxSizeFor(int scenario, GenerateTable table) {
        int maxsize = 0;

        for (GenerateColumn c : table.getColumnList()) {
            if (c == null) continue;
            int currentSize = 0;
            GeneratedColumnInTable g = c.getColumnInTable(table.getTableName());
            switch (scenario) {
                case NAME:
                    currentSize = g.isPk() ? (c.getFinalFieldName() + "(PK)").length() : c.getFinalFieldName().length();
                    break;
                case TYPE:
                    currentSize = c.getFieldClass() == null ? 0 : c.getFieldClass().getSimpleName().length();
                    break;
                case SIZE:
                    currentSize = g == null ? 0 : String.valueOf(g.getColumnSize()).length();
                    break;
                case DBNAME:
                    currentSize = c.getColumnName() == null ? 0 : c.getColumnName().length();
                    break;
                case DBTYPE:
                    currentSize = JDBCTypeConverter.getDataBaseTypeName(c.getDataType()).length();
                    break;
                case DESCRIPTION:
                    currentSize = g == null || g.getRemarks() == null ? 0 : g.getRemarks().length();
                    break;
                case NULLABLE:
                    currentSize = g == null || g.getNullable() == null ? 0 : g.getNullable().length();
                    break;
            }

            if (currentSize > maxsize) maxsize = currentSize;
        }

        return maxsize;
    }
}
