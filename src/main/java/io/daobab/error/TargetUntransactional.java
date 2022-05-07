package io.daobab.error;

import io.daobab.target.Target;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class TargetUntransactional extends DaobabException {

    private static final long serialVersionUID = 1L;

    public TargetUntransactional(Target target) {
        super("Untransactional target: " + target.getClass().getName());
    }

}
