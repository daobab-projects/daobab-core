package io.daobab.error;

import io.daobab.target.Target;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TargetHasNoEntitiesException extends DaobabException {

    public TargetHasNoEntitiesException(Target target) {
        super("No entities into target: " + target.getClass().getSimpleName());
    }

}
