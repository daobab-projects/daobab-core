package io.daobab.error;

import io.daobab.target.Target;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TargetNotTransactional extends DaobabException {

    private static final long serialVersionUID = 1L;

    public TargetNotTransactional(Target target) {
        super("Untransactional target: " + target.getClass().getName());
    }

}
