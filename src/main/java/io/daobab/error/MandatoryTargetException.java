package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MandatoryTargetException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public MandatoryTargetException() {
        super("Target is mandatory");
    }

}
