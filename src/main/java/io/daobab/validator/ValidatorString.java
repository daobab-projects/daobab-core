package io.daobab.validator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public interface ValidatorString {


    static boolean exists(String in) {
        return !(in == null || in.isEmpty());
    }

    static boolean isFilled(String in) {
        return !(in == null || in.isEmpty());
    }


}
