package io.daobab.generator;

import io.daobab.model.Column;
import io.daobab.target.database.JdbcType;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static io.daobab.generator.DaobabClassGeneratorTemplates.COLUMN_TEMPLATE;

public class DeveloperGenerator {

    private static Writter writter = new Writter();

    public static void main(String[] sa) {
//        String column=DeveloperGenerator.getColumnInterfaceFor("pl.trst.test","FIRST_NAME",JdbcType.VARCHAR);

        String s = changeColumnFieldClass(null, String.class);
        System.out.println(s);
    }

    public static String getColumnInterfaceFor(String packagename, String columnName, JdbcType type) {
        return getColumnInterfaceFor(packagename, columnName, null, type, null);
    }

//    public static String getTableFor(String packagename, String tableName, List<Column> pk, Column... columns) {
//
//        GenerateColumn[] gcArray = new GenerateColumn[columns.length];
//        for (int i = 0; i < columns.length; i++) {
//            gcArray[i] = toGc(columns[i]);
//        }
//
//        List<GenerateColumn> gclist = null;
//        if (pk != null) {
//            gclist = new ArrayList<>();
//            for (Column p : pk) {
//                gclist.add(toGc(p));
//            }
//        }
//
//        GenerateTable gt = new GenerateTable(tableName, gclist, new LinkedList<>(), gcArray);
//        gt.setJavaPackage(packagename);
//
//        return generateTable(gt, packagename);
//
//    }

//    public static String getTableFor(String packagename, String tableName, Column... columns) {
//        return getTableFor(packagename, tableName, null, columns);
//    }

    public static <F> String changeColumnFieldClass(Column<?, F, ?> column, Class<F> newFieldClass) {
        StringBuilder sb = new StringBuilder();
        String data = "";
        try {
            data = (String) Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getData(DataFlavor.stringFlavor);

            int pointerToMethod = data.indexOf("getFieldClass()");

            sb.append(data.substring(0, pointerToMethod).replaceFirst("import " + column.getFieldClass().getName(), "import " + newFieldClass.getName()));
            sb.append(data.substring(pointerToMethod).replaceFirst(column.getFieldClass().getSimpleName() + ".class", newFieldClass.getSimpleName() + ".class"));
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

//    private static String generateTable(GenerateTable table, String packagename) {
//
//        Replacer replacer = new Replacer();
//
//        String tableName = table.getTableName();
//        String tableNameCamel = GenerateFormatter.toCamelCase(table.getTableName());
//        String compositeKeyName = DaobabGenerator.createCompositeKeyName(tableNameCamel, Arrays.asList(table));
//        boolean pkExist = table.getPrimaryKeys() != null;
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(packagename);
//        if (table.getSchemaName() != null && !table.getSchemaName().trim().isEmpty()) {
//            sb.append(".").append(table.getSchemaName().trim().toLowerCase());
//        }
//
//        sb.append(table.isView() ? ".view" : ".table");
//
//        table.setJavaPackage(sb.toString());
//
//        replacer.add(GenKeys.PK_IMPORT, pkExist ? "import " + PrimaryKey.class.getName() + ";" : "");
//        if (pkExist) {
//            if (table.getPrimaryKeys().size() == 1) {
//                replacer.add(GenKeys.PK_TYPE_IMPORT, "import " + table.getPrimaryKeys().get(0).getFieldClass().getName() + ";\n");
//            } else {
//                //todo multikey
//                replacer.add(GenKeys.PK_TYPE_IMPORT, "import " + table.getPrimaryKeys().get(0).getFieldClass().getName() + ";\n");
//            }
//        }
//
//        replacer.add(GenKeys.COLUMN_IMPORTS, table.getColumnImport(tableNameCamel));
//        replacer.add(GenKeys.COLUMN_INTERFACES, table.getColumnInterfaces(compositeKeyName, tableNameCamel));
//        replacer.add(GenKeys.TABLE_NAME, tableName);
//        replacer.add(GenKeys.TABLE_CAMEL_NAME, tableNameCamel);
//        replacer.add(GenKeys.COLUMN_METHODS, table.getColumnMethods());
//        replacer.add(GenKeys.PK_ID_METHOD, pkExist ? table.getPkIdMethod() : "");
//        replacer.add(GenKeys.TABLE_PACKAGE, table.getJavaPackage());
//        if (pkExist) {
//            if (table.getPrimaryKeys().size() == 1) {
//                replacer.add(GenKeys.PK_INTERFACE, PrimaryKey.class.getSimpleName() + "<" + tableNameCamel + "," + table.getPrimaryKeys().get(0).getFieldClass().getSimpleName() + "," + table.getPrimaryKeys().get(0).getFinalFieldNameShortOrLong(tableNameCamel) + ">");
//            } else {
//                replacer.add(GenKeys.PK_INTERFACE, PrimaryCompositeKey.class.getSimpleName() + "<" + tableNameCamel + "," + table.getPrimaryKeys().get(0).getFieldClass().getSimpleName() + "," + table.getPrimaryKeys().get(0).getFinalFieldNameShortOrLong(tableNameCamel) + ">");
//            }
//        } else {
//            replacer.add(GenKeys.PK_INTERFACE, "");
//        }
//
//        table.setAlreadyGenerated(true);
//
//        return replacer.replaceAll(DaobabGenerator.tabtemp);
//
//    }

    private static GenerateColumn toGc(Column column) {
        GenerateColumn rv = new GenerateColumn(column.getColumnName(), column.getFieldClass());
        rv.setFieldName(column.getFieldName());
        rv.setInterfaceName(column.getClass().getSimpleName());
        rv.setPackage(column.getClass().getPackage().toString());
        return rv;
    }

    public static String getColumnInterfaceFor(String packagename, String columnName, String fieldName, JdbcType type, Class fieldClass) {
        Replacer replacer = new Replacer();

        Class clazz = fieldClass == null ? TypeConverter.convert(type.getType()) : fieldClass;
        String finalFieldName = fieldName == null ? GenerateFormatter.toCamelCase(columnName) : fieldName;

        boolean columnAndTypeTheSameType = clazz.getSimpleName().equalsIgnoreCase(finalFieldName);

        if (byte[].class.equals(clazz) || columnAndTypeTheSameType) {
            replacer.add(GenKeys.CLASS_FULL_NAME, "");
        } else {
            replacer.add(GenKeys.CLASS_FULL_NAME, "import " + clazz.getName() + ";");
        }
        replacer.add(GenKeys.CLASS_SIMPLE_NAME, columnAndTypeTheSameType ? clazz.getName() : clazz.getSimpleName());
        replacer.add(GenKeys.COLUMN_NAME, columnName);
        replacer.add(GenKeys.INTERFACE_NAME, GenerateFormatter.toCamelCase(columnName));
        replacer.add(GenKeys.FIELD_NAME, GenerateFormatter.toCamelCase(columnName));
        replacer.add(GenKeys.DB_TYPE, TypeConverter.getDbTypeName(type.getType()));
        replacer.add(GenKeys.PACKAGE, packagename);

        return replacer.replaceAll(COLUMN_TEMPLATE);
    }
}
