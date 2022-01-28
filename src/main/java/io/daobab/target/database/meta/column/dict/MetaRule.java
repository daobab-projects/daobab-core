package io.daobab.target.database.meta.column.dict;

public enum MetaRule {

    NO_ACTION,
    CASCADE,
    SET_NULL,
    RESTRICT,
    SET_DEFAULT;

    public static MetaRule ofString(String str) {
        if (str == null) return null;
        if (str.endsWith("NoAction")) return NO_ACTION;
        if (str.endsWith("KeyCascade")) return CASCADE;
        if (str.endsWith("KeySetNull")) return SET_NULL;
        if (str.endsWith("Restrict")) return RESTRICT;
        if (str.endsWith("SetDefault")) return SET_DEFAULT;
        return null;
    }
}
