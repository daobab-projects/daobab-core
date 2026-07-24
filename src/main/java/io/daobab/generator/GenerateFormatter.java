package io.daobab.generator;

/**
 * String-case helpers turning database names ({@code UPPER_SNAKE_CASE}) into Java or TypeScript identifiers:
 * camelCase / camelCase-starting-lower / kebab-case conversions and simple capitalization utilities.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface GenerateFormatter {

    /**
     * Lower-cases the first character.
     */
    static String decapitalize(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        char[] c = string.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    /** Converts an {@code UPPER_SNAKE} / mixed name to {@code CamelCase}. */
    static String toCamelCase(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        String[] parts1 = s.split("_");

        for (String p : parts1) {
            String[] parts = p.toLowerCase().split("(?=\\p{Lu})"); // s.split("_");
            for (String part : parts) {
                sb.append(toProperCase(part));
            }
        }

        return sb.toString();
    }

    /** Converts to {@code camelCase} (first character lower). */
    static String toCamelCaseStartLower(String s) {
        if (s == null || s.isEmpty()) return s;
        return decapitalize(toCamelCase(s));
    }

    /** Converts an {@code UPPER_SNAKE} / mixed name to {@code kebab-case} (for TypeScript file names). */
    static String toTypeScriptCase(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        String[] parts1 = s.split("_");

        boolean firstpart = true;
        for (String p : parts1) {
            String[] parts = p.toLowerCase().split("(?=\\p{Lu})"); // s.split("_");
            for (String part : parts) {
                if (!firstpart) sb.append("-");
                firstpart = false;
                sb.append(part.toLowerCase());
            }
        }

        return sb.toString();
    }

    /** Upper-cases the first character and lower-cases the rest. */
    static String toProperCase(String s) {
        if (s == null || s.trim().isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    /** Upper-cases the first character, leaving the rest unchanged. */
    static String toUpperCaseFirstCharacter(String s) {
        if (s == null || s.trim().isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }


}
