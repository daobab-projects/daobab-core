package io.daobab.property;

public class PropertyReader {
    private PropertyReader() {
    }

    public static boolean readBooleanSmall(String key, String defaultValue) {
        String val = System.getProperty(key, defaultValue);
        return "true".equals(val);
    }

    public static <E extends Enum<E>> E readEnum(String key, Class<E> clazz, String defaultValue) {
        String val = System.getProperty(key, defaultValue);
        return Enum.valueOf(clazz, val);
    }
}
