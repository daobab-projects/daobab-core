package io.daobab.generator;

import io.daobab.generator.template.TemplateLanguage;
import io.daobab.model.Column;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class DeveloperGenerator {

    private static final Writer writter = new Writer(TemplateLanguage.JAVA);

    public static void main(String[] sa) {
//        String column=DeveloperGenerator.getColumnInterfaceFor("pl.trst.test","FIRST_NAME",JdbcType.VARCHAR);

        String s = changeColumnFieldClass(null, String.class);
        System.out.println(s);
    }


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

//    public static String getColumnInterfaceFor(String packageName, String columnName, String fieldName, JdbcType type, Class fieldClass) {
//        Replacer replacer = new Replacer();
//
//        Class clazz = fieldClass == null ? typeConverter.convert(type.getType()) : fieldClass;
//        String finalFieldName = fieldName == null ? GenerateFormatter.toCamelCase(columnName) : fieldName;
//
//        boolean columnAndTypeTheSameType = clazz.getSimpleName().equalsIgnoreCase(finalFieldName);
//
//        if (byte[].class.equals(clazz) || columnAndTypeTheSameType) {
//            replacer.add(GenKeys.CLASS_FULL_NAME, "");
//        } else {
//            replacer.add(GenKeys.CLASS_FULL_NAME, "import " + clazz.getName() + ";");
//        }
//        replacer.add(GenKeys.CLASS_SIMPLE_NAME, columnAndTypeTheSameType ? clazz.getName() : clazz.getSimpleName());
//        replacer.add(GenKeys.COLUMN_NAME, columnName);
//        replacer.add(GenKeys.INTERFACE_NAME, GenerateFormatter.toCamelCase(columnName));
//        replacer.add(GenKeys.FIELD_NAME, GenerateFormatter.toCamelCase(columnName));
//        replacer.add(GenKeys.DB_TYPE, TypeConverter.getDbTypeName(type.getType()));
//        replacer.add(GenKeys.PACKAGE, packageName);
//
//        return replacer.replaceAll(COLUMN_TEMPLATE);
//    }
}
