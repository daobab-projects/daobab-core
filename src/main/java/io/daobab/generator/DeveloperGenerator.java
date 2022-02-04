package io.daobab.generator;

import io.daobab.model.Column;
import io.daobab.target.database.connection.JdbcType;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static io.daobab.generator.DaobabClassGeneratorTemplates.COLUMN_TEMPLATE;

public class DeveloperGenerator {

    private static final Writter writter = new Writter();

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
