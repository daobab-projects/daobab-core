package io.daobab.generator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface GenerateFormatter {

    static String decapitalize(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }
        char[] c = string.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

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

    static String toCamelCaseStartLower(String s) {
        if (s == null || s.isEmpty()) return s;
        return decapitalize(toCamelCase(s));
    }

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

    static String toProperCase(String s) {
        if (s == null || s.trim().isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    static String toUpperCaseFirstCharacter(String s) {
        if (s == null || s.trim().isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }


}
