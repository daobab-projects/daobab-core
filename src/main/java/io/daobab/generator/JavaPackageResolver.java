package io.daobab.generator;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class JavaPackageResolver {

    static final List<String> forbiddenNames = Arrays.asList("abstract", "assert", "boolean", "break", "byte", "case",
            "catch", "char", "class", "const", "continue", "default",
            "double", "do", "else", "enum", "extends", "false",
            "final", "finally", "float", "for", "goto", "if",
            "implements", "import", "instanceof", "int", "interface", "long",
            "native", "new", "null", "package", "private", "protected",
            "public", "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws", "transient",
            "true", "try", "void", "volatile", "while",

            //Daobab KeyWords
            "sequenceName", "idGeneratorType", "sqlUpdate", "entity",
            "id", "entityrelationmap", "entitymap", "object", "class", "field",
            //Windows forbidden
            "con", "nul", "aux", "lst", "prn", "eof", "inp", "out"

    );

    private JavaPackageResolver() {
    }

    public static StringBuilder resolve(String jpackage, String catalog, String schema) {

        StringBuilder sb = new StringBuilder();
        sb.append(jpackage);

        StringBuilder properCatalog = resolveCatalog(catalog);
        StringBuilder properSchema = resolveSchema(schema);

        if (properCatalog.length() > 0) {
            sb.append(".").append(properCatalog);
        }

        if (properSchema.length() > 0) {
            sb.append(".").append(properSchema);
        }


        return sb;
    }

    public static StringBuilder resolveCatalog(String catalog) {
        StringBuilder sb = new StringBuilder();
        if (catalog != null && !catalog.trim().isEmpty() && !"%".equals(catalog)) {
            boolean forbidden = forbiddenNames.contains(catalog.toLowerCase(Locale.ROOT));
            sb.append(catalog.toLowerCase(Locale.ROOT));
            if (forbidden) {
                sb.append("_catalog");
            }
        }

        return sb;
    }

    public static StringBuilder resolveSchema(String schema) {
        StringBuilder sb = new StringBuilder();
        if (schema != null && !schema.trim().isEmpty() && !"%".equals(schema)) {
            boolean forbidden = forbiddenNames.contains(schema.toLowerCase(Locale.ROOT));
            sb.append(schema.toLowerCase(Locale.ROOT));
            if (forbidden) {
                sb.append("_schema");
            }
        }

        return sb;
    }


}
