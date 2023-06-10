package io.daobab.converter.duplicator.type;

import io.daobab.converter.duplicator.Duplicator;

public class ByteDuplicator extends Duplicator<Byte> {

    @Override
    public Byte duplicate(Byte obj) {
        return new Byte(obj.byteValue());
    }
}
