package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class TargetDoesNotSupport extends DaobabException {

    private static final long serialVersionUID = 1L;

    public TargetDoesNotSupport() {
        super("Target does not support such operation");
    }

}
