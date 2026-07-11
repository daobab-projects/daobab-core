package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterIntegerBased;

import java.time.Month;

public class StandardTypeConverterMonth extends TypeConverterIntegerBased<Month> {

    @Override
    public Month convertReadingTarget(Integer from) {
        return from == null ? null : Month.of(from);
    }

    @Override
    public String convertWritingTarget(Month to) {
        return to == null ? "" : String.valueOf(to.getValue());
    }

    @Override
    public Object convertWritingParameter(Month to) {
        return to == null ? null : to.getValue();
    }
}
