package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterIntegerBased;

import java.time.Year;

public class StandardTypeConverterYear extends TypeConverterIntegerBased<Year> {

    @Override
    public Year convertReadingTarget(Integer from) {
        return from == null ? null : Year.of(from);
    }

    @Override
    public String convertWritingTarget(Year to) {
        return to == null ? "" : String.valueOf(to.getValue());
    }

    @Override
    public Object convertWritingParameter(Year to) {
        return to == null ? null : to.getValue();
    }
}
