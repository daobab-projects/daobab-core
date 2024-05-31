package io.daobab.property;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class PropertyReader {
    private PropertyReader() {

        DaobabPropertyGenerator dd = new DaobabPropertyGenerator();
        dd.getOverride();
    }

    public static boolean readBooleanSmall(String key, String defaultValue) {
        String val = System.getProperty(key, defaultValue);
        return "true".equals(val);
    }

    public static Boolean readBoolean(String key) {
        String val = System.getProperty(key);
        if (val == null || val.isEmpty()) {
            return null;
        }
        return "true".equals(val);
    }

    public static <E extends Enum<E>> E readEnum(String key, Class<E> clazz, String defaultValue) {
        String val = System.getProperty(key, defaultValue);
        return Enum.valueOf(clazz, val);
    }
}
