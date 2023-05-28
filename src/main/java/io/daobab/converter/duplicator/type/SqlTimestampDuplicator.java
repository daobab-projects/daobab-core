package io.daobab.converter.duplicator.type;

import io.daobab.converter.duplicator.Duplicator;

import java.sql.Date;
import java.sql.Timestamp;

public class SqlTimestampDuplicator extends Duplicator<Timestamp> {

    @Override
    public Timestamp duplicate(Timestamp obj) {
        return new Timestamp(obj.getTime());
    }
}
