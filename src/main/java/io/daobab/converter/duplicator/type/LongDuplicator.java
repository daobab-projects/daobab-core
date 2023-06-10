package io.daobab.converter.duplicator.type;

import io.daobab.converter.duplicator.Duplicator;

public class LongDuplicator extends Duplicator<Long> {

    @Override
    public Long duplicate(Long obj) {
        return new Long(obj.longValue());
    }
}
