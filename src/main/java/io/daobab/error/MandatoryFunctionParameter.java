package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class MandatoryFunctionParameter extends DaobabException {

    private static final long serialVersionUID = 1L;

    public MandatoryFunctionParameter(String mode) {
        super("Function " + mode + " is missing a mandatory parameter.");
    }

}
