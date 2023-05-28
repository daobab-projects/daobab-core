package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

public class JsonFloatConverter extends JsonConverter<Float> {
    @Override
    public void toJson(StringBuilder sb, Float obj) {
        sb.append(numberToString(obj));
    }

    @Override
    public Float fromJson(String json) {
        return Float.parseFloat(json);
    }


    public static String numberToString(Float n) {

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
