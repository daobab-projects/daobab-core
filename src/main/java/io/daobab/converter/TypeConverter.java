package io.daobab.converter;


public interface TypeConverter<T> {

    T convertReadingTarget(String from);

    String convertWritingTarget(T to);
}
