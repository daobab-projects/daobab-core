package io.daobab.error;

import io.daobab.model.Field;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class AttemptToSetNullValueInWrongWay extends DaobabException {

    private static final long serialVersionUID = 1L;


    public AttemptToSetNullValueInWrongWay() {
        super("Attempt to set null into field.");
    }

    public AttemptToSetNullValueInWrongWay(Field<?, ?, ?> field) {
        super("Attempt to set null into field " + field.getFieldName() + " of entity:" + field.getEntityName() + ". Use SetNull method instead.");
    }

}
