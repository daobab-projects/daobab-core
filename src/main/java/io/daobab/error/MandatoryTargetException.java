package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MandatoryTargetException extends DaobabException {

    public MandatoryTargetException() {
        super("Target is mandatory");
    }

}
