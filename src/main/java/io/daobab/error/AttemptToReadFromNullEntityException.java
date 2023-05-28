package io.daobab.error;

import io.daobab.model.Column;
import io.daobab.model.Entity;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class AttemptToReadFromNullEntityException extends DaobabException {

    private static final long serialVersionUID = 1L;


    public AttemptToReadFromNullEntityException() {
        super("Attempt to read from an entity which is null");
    }

    public AttemptToReadFromNullEntityException(Class<? extends Entity> clazz, String field) {
        super("Attempt to read from entity which is null. Entity:" + clazz.getSimpleName() + " field " + field);
    }

    public AttemptToReadFromNullEntityException(Column<?, ?, ?> column) {
        super("Attempt to read from an entity which is null. Entity:" + column.entityClass().getName() + " field " + column.getFieldName());
    }

}
