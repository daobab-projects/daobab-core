package io.daobab.converter.duplicator.type;

import io.daobab.converter.duplicator.Duplicator;

public class IntegerDuplicator extends Duplicator<Integer> {

    @Override
    public Integer duplicate(Integer obj) {
        return new Integer(obj.intValue());
    }
}
