package io.daobab.error;

import io.daobab.target.Target;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class NoEntitiesIntoTargetException extends DaobabException {

    private static final long serialVersionUID = 1L;


    public NoEntitiesIntoTargetException(Target target) {
        super("No entiies into target: " + target.getClass().getSimpleName());
    }


}
