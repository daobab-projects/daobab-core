package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.time.Duration;

public class StandardTypeConverterDuration extends TypeConverterStringBased<Duration> {

    @Override
    public Duration convertReadingTarget(String from) {
        return from == null ? null : Duration.parse(from);
    }

    @Override
    public String convertWritingTarget(Duration to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    @Override
    public Object convertWritingParameter(Duration to) {
        return to == null ? null : to.toString();
    }
}
