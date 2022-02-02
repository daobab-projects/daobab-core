package io.daobab.error;

import io.daobab.target.buffer.multi.MultiEntity;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class SourceTargetUnderBufferedTargetMissed extends DaobabException {

    private static final long serialVersionUID = 1L;

    public SourceTargetUnderBufferedTargetMissed(Class<? extends MultiEntity> clazz) {
        super("MultiEntity must have source target specified. " + clazz + " has not.");
    }

}
