package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

/**
 * JSON converter for {@link Float}: written as a bare JSON number with trailing zeros trimmed
 * (see {@link #numberToString(Float)}) and parsed with {@link Float#parseFloat(String)}. {@code NaN} and the
 * infinities are written as {@code null}, since JSON has no token for them.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonFloatConverter extends JsonConverter<Float> {
    @Override
    public void toJson(StringBuilder sb, Float obj) {
        if (obj.isNaN() || obj.isInfinite()) {
            sb.append("null");
            return;
        }
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
