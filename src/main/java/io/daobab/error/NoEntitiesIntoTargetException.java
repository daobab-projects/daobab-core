package io.daobab.error;

import io.daobab.target.Target;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NoEntitiesIntoTargetException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public NoEntitiesIntoTargetException(Target target) {
        super("No entities into target: " + target.getClass().getSimpleName());
    }

}
