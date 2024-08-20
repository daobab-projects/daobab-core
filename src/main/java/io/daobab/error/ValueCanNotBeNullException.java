package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ValueCanNotBeNullException extends DaobabException {

    public ValueCanNotBeNullException() {
        super("Column related value can not be null here");
    }

}
