package io.daobab.converter.json.type;

import io.daobab.converter.JsonConverter;

public class JsonDoubleConverter implements JsonConverter<Double> {
    @Override
    public String toJson(Double obj) {
        return numberToString(obj);
    }

    @Override
    public Double fromJson(String json) {
        return Double.parseDouble(json);
    }


    public static String numberToString(Double n) {
//        if (n == null) {
//            throw new JSONException("Null pointer");
//        }
        //testValidity(n);

        // Shave off trailing zeros and decimal point, if possible.

        String s = n.toString();
        if (s.indexOf('.') > 0 && s.indexOf('e') < 0 && s.indexOf('E') < 0) {
            while (s.endsWith("0")) {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }
}
