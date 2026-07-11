package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.util.Locale;

public class StandardTypeConverterLocale extends TypeConverterStringBased<Locale> {

    @Override
    public Locale convertReadingTarget(String from) {
        return from == null ? null : Locale.forLanguageTag(from);
    }

    @Override
    public String convertWritingTarget(Locale to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toLanguageTag());
    }

    @Override
    public Object convertWritingParameter(Locale to) {
        return to == null ? null : to.toLanguageTag();
    }
}
