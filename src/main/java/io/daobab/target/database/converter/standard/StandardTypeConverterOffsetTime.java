package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterOffsetTimeBased;

import java.time.OffsetTime;

public class StandardTypeConverterOffsetTime extends TypeConverterOffsetTimeBased<OffsetTime> {

    @Override
    public OffsetTime convertReadingTarget(OffsetTime from) {
        return from;
    }

    @Override
    public String convertWritingTarget(OffsetTime to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    @Override
    public Object convertWritingParameter(OffsetTime to) {
        //JDBC 4.2 drivers bind an OffsetTime directly for TIME WITH TIME ZONE
        return to;
    }
}
