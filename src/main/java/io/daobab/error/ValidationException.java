package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String msg) {
        super(msg);
    }

}
