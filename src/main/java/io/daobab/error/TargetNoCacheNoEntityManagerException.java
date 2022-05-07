package io.daobab.error;

import io.daobab.target.Target;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class TargetNoCacheNoEntityManagerException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public TargetNoCacheNoEntityManagerException(Target target) {
        super("Target " + target.getClass().getName() + " is neither DataBase nor InMemory buffer.");
    }

}
