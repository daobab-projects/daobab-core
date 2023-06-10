package io.daobab.converter.duplicator.type;

import io.daobab.converter.duplicator.Duplicator;

public class ShortDuplicator extends Duplicator<Short> {

    @Override
    public Short duplicate(Short obj) {
        return new Short(obj.shortValue());
    }
}
