package io.daobab.validator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
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
