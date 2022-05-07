package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 8963724980254353803L;

    public ValidationException(String msg) {
        super(msg);
    }

}
