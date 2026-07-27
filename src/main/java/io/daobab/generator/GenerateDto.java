package io.daobab.generator;

import io.daobab.generator.template.GenKeys;
import io.daobab.generator.template.TemplateLanguage;
import io.daobab.generator.template.TemplateProvider;
import io.daobab.generator.template.TemplateType;

import java.util.List;
import java.util.stream.Collectors;

import static io.daobab.generator.GenerateFormatter.decapitalize;

/**
 * Builds the DTO class content and the entity's to/from DTO conversion methods. The Java DTO is a {@code record}
 * (its canonical constructor lets it be read straight from a query via {@code readRecord}), kept with getters and
 * a builder for backward compatibility; the Kotlin DTO is a {@code data class}. Equality is based on the single
 * primary key when there is one, otherwise on all the fields.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
class GenerateDto {

    private GenerateDto() {
    }

    /**
     * The full DTO class source for the given table (Java: immutable + builder, Kotlin: data class).
     */
    static String getDtoClassContent(GenerateTable table, String dtoPackage, String dtoName, TemplateLanguage language) {
        if (language == TemplateLanguage.KOTLIN) {
            return new Replacer()
                    .add(GenKeys.DTO_PACKAGE, dtoPackage)
                    .add(GenKeys.TYPE_IMPORTS, table.getTypeImports(TemplateLanguage.KOTLIN))
                    .add(GenKeys.DTO_FIELDS, getKotlinFields(table))
                    .add(GenKeys.DTO_EQUALS_HASHCODE, getKotlinEqualsHashCode(table, dtoName))
                    .add(GenKeys.DTO_NAME, dtoName)
                    .replaceAll(TemplateProvider.getTemplate(TemplateLanguage.KOTLIN, TemplateType.DTO_CLASS));
        }
        return new Replacer()
                .add(GenKeys.DTO_PACKAGE, dtoPackage)
                .add(GenKeys.TYPE_IMPORTS, table.getTypeImports(TemplateLanguage.JAVA))
                .add(GenKeys.DTO_RECORD_COMPONENTS, getRecordComponents(table))
                .add(GenKeys.DTO_GETTERS, getGetters(table))
                .add(GenKeys.DTO_EQUALS_HASHCODE, getEqualsHashCode(table, dtoName))
                .add(GenKeys.DTO_BUILDER_FIELDS, getBuilderFields(table))
                .add(GenKeys.DTO_BUILDER_METHODS, getBuilderMethods(table))
                .add(GenKeys.DTO_BUILDER_BUILD_ARGS, getBuildArgs(table))
                .add(GenKeys.DTO_NAME, dtoName)
                .replaceAll(TemplateProvider.getTemplate(TemplateLanguage.JAVA, TemplateType.DTO_CLASS));
    }

    /**
     * The entity part: the {@code fromDto} factory and the {@code toDto} override.
     */
    static String getConversionMethods(GenerateTable table, String entityName, String dtoName, TemplateLanguage language) {
        if (language == TemplateLanguage.KOTLIN) {
            return getKotlinConversionMethods(table, entityName, dtoName);
        }
        List<GenerateColumn> columns = table.getColumnList();
        StringBuilder sb = new StringBuilder();

        sb.append("\n\tpublic static ").append(entityName).append(" fromDto(").append(dtoName).append(" dto) {");
        sb.append("\n\t\treturn new ").append(entityName).append("()");
        for (int i = 0; i < columns.size(); i++) {
            String name = columns.get(i).getFinalFieldName();
            sb.append("\n\t\t\t\t.set").append(name).append("(dto.get").append(name).append("())");
        }
        sb.append(";");
        sb.append("\n\t}");
        sb.append("\n");
        sb.append("\n\t@Override");
        sb.append("\n\tpublic ").append(dtoName).append(" toDto() {");
        sb.append("\n\t\treturn ").append(dtoName).append(".builder()");
        for (GenerateColumn gc : columns) {
            String name = gc.getFinalFieldName();
            sb.append("\n\t\t\t\t.").append(decapitalize(name)).append("(get").append(name).append("())");
        }
        sb.append("\n\t\t\t\t.build();");
        sb.append("\n\t}");
        sb.append("\n");
        return sb.toString();
    }

    /** The Kotlin {@code fromDto}/{@code toDto} conversions (a companion-object factory and the override). */
    private static String getKotlinConversionMethods(GenerateTable table, String entityName, String dtoName) {
        List<GenerateColumn> columns = table.getColumnList();
        StringBuilder sb = new StringBuilder();

        sb.append("\n\tcompanion object {");
        sb.append("\n\t\t@JvmStatic");
        sb.append("\n\t\tfun fromDto(dto: ").append(dtoName).append("): ").append(entityName).append(" = ").append(entityName).append("()");
        for (GenerateColumn gc : columns) {
            sb.append("\n\t\t\t\t.set").append(gc.getFinalFieldName()).append("(dto.").append(fieldOf(gc)).append(")");
        }
        sb.append("\n\t}");
        sb.append("\n");
        sb.append("\n\toverride fun toDto(): ").append(dtoName).append(" = ").append(dtoName).append("(");
        for (int i = 0; i < columns.size(); i++) {
            GenerateColumn gc = columns.get(i);
            sb.append("\n\t\t\t").append(fieldOf(gc)).append(" = get").append(gc.getFinalFieldName()).append("()");
            if (i < columns.size() - 1) sb.append(",");
        }
        sb.append("\n\t)");
        sb.append("\n");
        return sb.toString();
    }

    /** The Kotlin data-class constructor properties (nullable ones default to {@code null}). */
    private static String getKotlinFields(GenerateTable table) {
        return table.getColumnList().stream()
                .map(gc -> "\tval " + fieldOf(gc) + ": " + kotlinTypeOf(gc)
                        + (isNullable(table, gc) ? "? = null" : ""))
                .collect(Collectors.joining(",\n"));
    }

    /**
     * A single primary key keeps the DTO equality on the key; otherwise the data class defaults
     * (all the fields) stay, matching the Java DTO behaviour.
     */
    private static String getKotlinEqualsHashCode(GenerateTable table, String dtoName) {
        List<GenerateColumn> primaryKeys = table.getPrimaryKeys();
        if (primaryKeys == null || primaryKeys.size() != 1) {
            return "";
        }
        GenerateColumn pk = primaryKeys.getFirst();
        String pkField = fieldOf(pk);
        String hashBody = isNullable(table, pk) ? pkField + "?.hashCode() ?: 0" : pkField + ".hashCode()";

        return " {" +
                "\n" +
                "\n\toverride fun hashCode() = " + hashBody +
                "\n" +
                "\n\toverride fun equals(other: Any?): Boolean {" +
                "\n\t\tif (this === other) return true" +
                "\n\t\tif (other == null || javaClass != other.javaClass) return false" +
                "\n\t\tother as " + dtoName +
                "\n\t\treturn " + pkField + " == other." + pkField +
                "\n\t}" +
                "\n}";
    }

    /** Whether the column is nullable in this table. */
    private static boolean isNullable(GenerateTable table, GenerateColumn gc) {
        return gc.getColumnInTableOrCreate(table.getTableName()).isNullable();
    }

    /** The Kotlin type of the column ({@code Integer} → {@code Int}, {@code byte[]} → {@code ByteArray}). */
    private static String kotlinTypeOf(GenerateColumn gc) {
        Class<?> fieldClass = gc.getFieldClass();
        if (Integer.class.equals(fieldClass)) {
            return "Int";
        }
        if (byte[].class.equals(fieldClass)) {
            return "ByteArray";
        }
        return fieldClass.getSimpleName();
    }

    /**
     * The record components of the Java DTO (one per column, in column order).
     */
    private static String getRecordComponents(GenerateTable table) {
        return table.getColumnList().stream()
                .map(gc -> "\t\t" + typeOf(gc) + " " + fieldOf(gc))
                .collect(Collectors.joining(",\n"));
    }

    /**
     * The field names passed to the record's canonical constructor from the builder's {@code build()}.
     */
    private static String getBuildArgs(GenerateTable table) {
        return table.getColumnList().stream()
                .map(GenerateDto::fieldOf)
                .collect(Collectors.joining(", "));
    }

    /** The getters of the Java DTO. */
    private static String getGetters(GenerateTable table) {
        return table.getColumnList().stream()
                .map(gc -> "\tpublic " + typeOf(gc) + " get" + gc.getFinalFieldName() + "() {"
                        + "\n\t\treturn " + fieldOf(gc) + ";"
                        + "\n\t}")
                .collect(Collectors.joining("\n\n"));
    }

    /**
     * The Java DTO {@code equals}/{@code hashCode} override on the single primary key. A composite key (or none)
     * returns an empty string, keeping the record's default all-component equality.
     */
    private static String getEqualsHashCode(GenerateTable table, String dtoName) {
        List<GenerateColumn> primaryKeys = table.getPrimaryKeys();
        boolean singlePk = primaryKeys != null && primaryKeys.size() == 1;
        if (!singlePk) {
            return "";
        }

        String pkField = fieldOf(primaryKeys.getFirst());
        return "\t@Override" +
                "\n\tpublic int hashCode() {" +
                "\n\t\treturn Objects.hashCode(" + pkField + ");" +
                "\n\t}" +
                "\n" +
                "\n\t@Override" +
                "\n\tpublic boolean equals(Object obj) {" +
                "\n\t\tif (this == obj) return true;" +
                "\n\t\tif (obj == null || getClass() != obj.getClass()) return false;" +
                "\n\t\t" + dtoName + " other = (" + dtoName + ") obj;" +
                "\n\t\treturn Objects.equals(" + pkField + ", other." + pkField + ");" +
                "\n\t}";
    }

    /** The private fields of the Java DTO builder. */
    private static String getBuilderFields(GenerateTable table) {
        return table.getColumnList().stream()
                .map(gc -> "\t\tprivate " + typeOf(gc) + " " + fieldOf(gc) + ";")
                .collect(Collectors.joining("\n"));
    }

    /** The fluent setter methods of the Java DTO builder. */
    private static String getBuilderMethods(GenerateTable table) {
        return table.getColumnList().stream()
                .map(gc -> "\t\tpublic Builder " + fieldOf(gc) + "(" + typeOf(gc) + " " + fieldOf(gc) + ") {"
                        + "\n\t\t\tthis." + fieldOf(gc) + " = " + fieldOf(gc) + ";"
                        + "\n\t\t\treturn this;"
                        + "\n\t\t}")
                .collect(Collectors.joining("\n\n"));
    }

    /** The simple name of the column's Java type. */
    private static String typeOf(GenerateColumn gc) {
        return gc.getFieldClass().getSimpleName();
    }

    /** The DTO field name (the final field name, decapitalized). */
    private static String fieldOf(GenerateColumn gc) {
        return decapitalize(gc.getFinalFieldName());
    }
}
