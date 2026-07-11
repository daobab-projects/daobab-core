package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.time.MonthDay;

public class StandardTypeConverterMonthDay extends TypeConverterStringBased<MonthDay> {

    @Override
    public MonthDay convertReadingTarget(String from) {
        return from == null ? null : MonthDay.parse(from);
    }

    @Override
    public String convertWritingTarget(MonthDay to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    @Override
    public Object convertWritingParameter(MonthDay to) {
        return to == null ? null : to.toString();
    }
}
