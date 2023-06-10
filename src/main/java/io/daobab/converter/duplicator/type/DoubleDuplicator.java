package io.daobab.converter.duplicator.type;

import io.daobab.converter.duplicator.Duplicator;

import java.util.Date;

public class DoubleDuplicator extends Duplicator<Double> {

    @Override
    public Double duplicate(Double obj) {
        return new Double(obj.doubleValue());
    }
}
