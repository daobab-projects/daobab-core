package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class TargetMandatoryException extends DaobabException {

    private static final long serialVersionUID = 1L;


    public TargetMandatoryException() {
        super("Target is mandatory");
    }


}
