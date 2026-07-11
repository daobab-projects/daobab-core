package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

public class StandardTypeConverterCharacter extends TypeConverterStringBased<Character> {

    @Override
    public Character convertReadingTarget(String from) {
        if (from == null || from.isEmpty()) {
            return null;
        }
        return from.charAt(0);
    }

    @Override
    public String convertWritingTarget(Character to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : String.valueOf(to));
    }

    @Override
    public Object convertWritingParameter(Character to) {
        return to == null ? null : String.valueOf(to);
    }
}
