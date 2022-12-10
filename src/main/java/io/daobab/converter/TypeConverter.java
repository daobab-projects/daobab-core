package io.daobab.converter;


public interface TypeConverter<F, T> {

    T convertReadingTarget(F from);

    String convertWritingTarget(T to);


}
