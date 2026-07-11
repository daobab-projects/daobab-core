package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.time.Period;

public class StandardTypeConverterPeriod extends TypeConverterStringBased<Period> {

    @Override
    public Period convertReadingTarget(String from) {
        return from == null ? null : Period.parse(from);
    }

    @Override
    public String convertWritingTarget(Period to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    @Override
    public Object convertWritingParameter(Period to) {
        return to == null ? null : to.toString();
    }
}
