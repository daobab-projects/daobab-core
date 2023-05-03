package io.daobab.error;

import io.daobab.model.Entity;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class AttemptToWriteIntoNullEntityException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public AttemptToWriteIntoNullEntityException() {
        super("Attempt to write into null entity.");
    }

    public AttemptToWriteIntoNullEntityException(Class<? extends Entity> clazz, String field) {
        super("Attempt to write into null entity:" + clazz.getSimpleName() + " field " + field);
    }

}
