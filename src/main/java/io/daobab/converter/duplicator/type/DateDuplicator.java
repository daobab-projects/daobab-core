package io.daobab.converter.duplicator.type;

import io.daobab.converter.duplicator.Duplicator;

import java.util.Date;

public class DateDuplicator extends Duplicator<Date> {

    @Override
    public Date duplicate(Date obj) {
        return new Date(obj.getTime());
    }
}
