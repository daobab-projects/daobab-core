package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class ValueCanNotBeNullException extends DaobabException {

    private static final long serialVersionUID = 1L;

    public ValueCanNotBeNullException() {
        super("Column related value can not be null here");
    }

}
