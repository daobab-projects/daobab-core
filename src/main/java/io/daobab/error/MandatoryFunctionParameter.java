package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class MandatoryFunctionParameter extends DaobabException {

    public MandatoryFunctionParameter(String mode) {
        super("Function " + mode + " is missing a mandatory parameter.");
    }

}
