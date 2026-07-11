package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterOffsetDateTimeBased;

import java.time.OffsetDateTime;

public class StandardTypeConverterOffsetDateTime extends TypeConverterOffsetDateTimeBased<OffsetDateTime> {

    @Override
    public OffsetDateTime convertReadingTarget(OffsetDateTime from) {
        return from;
    }

    @Override
    public String convertWritingTarget(OffsetDateTime to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    @Override
    public Object convertWritingParameter(OffsetDateTime to) {
        //JDBC 4.2 drivers bind an OffsetDateTime directly for TIMESTAMP WITH TIME ZONE
        return to;
    }
}
