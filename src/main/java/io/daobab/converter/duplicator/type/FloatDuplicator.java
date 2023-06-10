package io.daobab.converter.duplicator.type;

import io.daobab.converter.duplicator.Duplicator;

public class FloatDuplicator extends Duplicator<Float> {

    @Override
    public Float duplicate(Float obj) {
        return new Float(obj.floatValue());
    }
}
