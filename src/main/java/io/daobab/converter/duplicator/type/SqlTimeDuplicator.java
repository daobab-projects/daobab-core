package io.daobab.converter.duplicator.type;

import io.daobab.converter.duplicator.Duplicator;

import java.sql.Time;

public class SqlTimeDuplicator extends Duplicator<Time> {

    @Override
    public Time duplicate(Time obj) {
        return new Time(obj.getTime());
    }
}
