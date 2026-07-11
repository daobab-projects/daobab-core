package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterIntegerBased;

import java.time.DayOfWeek;

public class StandardTypeConverterDayOfWeek extends TypeConverterIntegerBased<DayOfWeek> {

    @Override
    public DayOfWeek convertReadingTarget(Integer from) {
        return from == null ? null : DayOfWeek.of(from);
    }

    @Override
    public String convertWritingTarget(DayOfWeek to) {
        return to == null ? "" : String.valueOf(to.getValue());
    }

    @Override
    public Object convertWritingParameter(DayOfWeek to) {
        return to == null ? null : to.getValue();
    }
}
