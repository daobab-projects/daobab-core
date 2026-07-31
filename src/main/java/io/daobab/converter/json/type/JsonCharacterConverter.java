package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;
import io.daobab.error.DaobabException;

/**
 * JSON converter for {@link Character}: written as a quoted single-character JSON string, escaped
 * the same way as {@link JsonStringConverter} (so {@code '\n'} becomes {@code "\\n"}).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonCharacterConverter extends JsonConverter<Character> {

    @Override
    public void toJson(StringBuilder sb, Character obj) {
        sb.append(QUOTE);
        char ch = obj;
        if (ch == '"') {
            sb.append("\\\"");
        } else if (ch == '\\') {
            sb.append("\\\\");
        } else if (ch == '\n') {
            sb.append("\\n");
        } else if (ch == '\t') {
            sb.append("\\t");
        } else if (ch == '\r') {
            sb.append("\\r");
        } else if (ch == '\b') {
            sb.append("\\b");
        } else if (ch == '\f') {
            sb.append("\\f");
        } else if (ch > 127) {
            sb.append(String.format("\\u%04x", (int) ch));
        } else {
            sb.append(ch);
        }
        sb.append(QUOTE);
    }

    @Override
    public Character fromJson(String json) {
        if (json.isEmpty()) {
            throw new DaobabException("Cannot convert an empty value to Character");
        }
        if (json.length() == 1) {
            return json.charAt(0);
        }
        if (json.charAt(0) == '\\') {
            char ch = json.charAt(1);
            switch (ch) {
                case '"':
                case '\\':
                case '/':
                    return ch;
                case 'n':
                    return '\n';
                case 't':
                    return '\t';
                case 'r':
                    return '\r';
                case 'b':
                    return '\b';
                case 'f':
                    return '\f';
                case 'u':
                    return (char) Integer.parseInt(json.substring(2, 6), 16);
                default:
                    throw new DaobabException("Illegal escape sequence: " + json);
            }
        }
        throw new DaobabException("Cannot convert value '" + json + "' to Character");
    }
}
