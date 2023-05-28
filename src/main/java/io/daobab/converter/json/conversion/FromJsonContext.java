package io.daobab.converter.json.conversion;

import io.daobab.error.DaobabException;

import java.util.Map;

class FromJsonContext {

    static final int NOTHING = 0;
    static final int KEY_OPENED = 1;
    static final int KEY_CLOSED = 2;
    static final int KEY_VAL_SEPARATOR = 3;
    static final int VAL_OPENED = 4;
    static final int VAL_CLOSED = 5;

    protected void putKeys(StringBuilder key, StringBuilder value, Map<String, String> map) {
        String keyString = key.toString();
        if (keyString.isEmpty()) {
            throw new DaobabException("Problem during json conversion. Cannot find a key");
        }
        String valueString = value.toString();
        map.put(keyString, valueString.equals("null") ? null : valueString);
    }
}
