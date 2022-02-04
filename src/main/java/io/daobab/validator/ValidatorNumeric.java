package io.daobab.validator;


import io.daobab.error.ValidationException;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface ValidatorNumeric {


    static boolean isDigit(String str) {
        if (str == null) return true;
        String s = str.trim();
        for (int i = 0; i < s.length(); i++) {
            // If we find a non-digit character we return false.
            if (!Character.isDigit(s.charAt(i)) && (s.charAt(i) != '.') && (s.charAt(i) != '-'))
                return false;
        }
        return true;
    }


    static String validateNumber(String from) {
        if (!isDigit(from))
            throw new ValidationException("Numeric content not found. Value can not be converted: " + from);
        return from;
    }
}
