package io.daobab.converter;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface TypeConverter<F, T> {

    T convertReadingTarget(F from);

    String convertWritingTarget(T to);


}
