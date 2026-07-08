package io.daobab.generator;

import io.daobab.generator.template.GenKeys;
import io.daobab.generator.template.TemplateLanguage;
import io.daobab.generator.template.TemplateProvider;
import io.daobab.generator.template.TemplateType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.daobab.generator.GenerateFormatter.decapitalize;

/**
 * Builds the annotated definition interface for a table - the input for the daobab annotation processor.
 * Instead of ready entities, the generator may produce such definitions only and leave
 * the entity, column and DTO generation to the compilation.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
class GenerateDefinition {

    private static final int LOB_SIZE = 2147483647;

    private GenerateDefinition() {
    }

    static String getDefinitionContent(GenerateTable table, String tableName, String definitionPackage, String definitionName,
                                       String entityPackage, String columnPackage, String dtoPackage) {
        return new Replacer()
                .add(GenKeys.TABLE_PACKAGE, definitionPackage)
                .add(GenKeys.TYPE_IMPORTS, table.getTypeImports(TemplateLanguage.JAVA))
                .add(GenKeys.TABLE_NAME, tableName)
                .add(GenKeys.DEFINITION_TABLE_ATTRIBUTES, getTableAttributes(entityPackage, columnPackage, dtoPackage))
                .add(GenKeys.DEFINITION_METHODS, getColumnMethods(table))
                .add(GenKeys.DEFINITION_NAME, definitionName)
                .replaceAll(TemplateProvider.getTemplate(TemplateLanguage.JAVA, TemplateType.DEFINITION_INTERFACE));
    }

    private static String getTableAttributes(String entityPackage, String columnPackage, String dtoPackage) {
        return ",\n\t\tentityPackage = \"" + entityPackage + "\"," +
                "\n\t\tcolumnPackage = \"" + columnPackage + "\"," +
                "\n\t\tdtoPackage = \"" + dtoPackage + "\"";
    }

    private static String getColumnMethods(GenerateTable table) {
        //the annotation processor does not support composite primary keys
        boolean markPrimaryKey = table.getPrimaryKeys() != null && table.getPrimaryKeys().size() == 1;
        if (table.getPrimaryKeys() != null && table.getPrimaryKeys().size() > 1) {
            System.out.println("Warning: table " + table.getTableName() + " has a composite primary key,"
                    + " which the annotation processor does not support. The definition is generated without the primary key markers.");
        }

        return table.getColumnList().stream()
                .map(gc -> getColumnMethod(table, gc, markPrimaryKey))
                .collect(Collectors.joining("\n\n"));
    }

    private static String getColumnMethod(GenerateTable table, GenerateColumn gc, boolean markPrimaryKey) {
        GeneratedColumnInTable git = gc.getColumnInTableOrCreate(table.getTableName());

        List<String> attributes = new ArrayList<>();
        attributes.add("name = \"" + gc.getColumnName() + "\"");
        if (markPrimaryKey && git.isPk()) {
            attributes.add("primaryKey = true");
        }
        if (git.getColumnSize() == LOB_SIZE) {
            attributes.add("lob = true");
        } else if (git.getColumnSize() > 0) {
            attributes.add("size = " + git.getColumnSize());
        }
        String decimalDigits = git.getDecimalDigits();
        if (decimalDigits != null && !decimalDigits.trim().isEmpty() && !"0".equals(decimalDigits.trim())) {
            attributes.add("scale = " + decimalDigits.trim());
        }
        if ("0".equals(git.getNullable())) {
            attributes.add("notNull = true");
        }

        String type = gc.getFieldClass().getSimpleName();
        return "\t@DaobabColumn(" + String.join(", ", attributes) + ")" +
                "\n\t" + type + " " + decapitalize(gc.getFinalFieldName()) + "();";
    }
}
