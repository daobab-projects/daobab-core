package io.daobab.target.buffer.multi;

import io.daobab.target.Target;

public class TargetDuplicator extends SimpleMultiTarget {

    public TargetDuplicator(Target dataBaseTarget) {
        dataBaseTarget.getTables().forEach(entity -> register(false, entity.getEntityClass()));
    }
}
