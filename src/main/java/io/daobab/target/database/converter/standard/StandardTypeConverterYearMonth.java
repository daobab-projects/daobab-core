package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.time.YearMonth;

public class StandardTypeConverterYearMonth extends TypeConverterStringBased<YearMonth> {

    @Override
    public YearMonth convertReadingTarget(String from) {
        return from == null ? null : YearMonth.parse(from);
    }

    @Override
    public String convertWritingTarget(YearMonth to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    @Override
    public Object convertWritingParameter(YearMonth to) {
        return to == null ? null : to.toString();
    }
}
