package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

public class JsonDoubleConverter extends JsonConverter<Double> {
    @Override
    public void toJson(StringBuilder sb, Double obj) {
        sb.append(numberToString(obj));
    }

    @Override
    public Double fromJson(String json) {
        return Double.parseDouble(json);
    }


    public static String numberToString(Double n) {
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
