package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TargetDoesNotSupport extends DaobabException {

    public TargetDoesNotSupport() {
        super("Target does not support such operation");
    }

}
