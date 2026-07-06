package io.daobab.converter;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface TypeConverter<F, T> {

    T convertReadingTarget(F from);

    String convertWritingTarget(T to);

    /**
     * Converts a value into an object which may be bound to a PreparedStatement parameter.
     * By default the value is returned as is - override whenever the value needs
     * a conversion before binding (enums, java.time types not supported by the driver etc.)
     */
    default Object convertWritingParameter(T to) {
        return to;
    }

}
