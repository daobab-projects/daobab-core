package io.daobab.property;

public class PropertyReader {

    public static final boolean readBooleanSmall(String key, String defaultValue) {
        String val = System.getProperty(key, defaultValue);
        return "true".equals(val);
    }
}
