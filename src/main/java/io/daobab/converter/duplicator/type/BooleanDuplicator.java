package io.daobab.converter.duplicator.type;

import io.daobab.converter.duplicator.Duplicator;

import java.math.BigInteger;

public class BooleanDuplicator extends Duplicator<Boolean> {

    @Override
    public Boolean duplicate(Boolean obj) {
        return new Boolean(obj);
    }
}
