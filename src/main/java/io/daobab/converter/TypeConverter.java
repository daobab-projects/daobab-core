package io.daobab.converter;


public interface TypeConverter<F, T> {

    F convertReadingTarget(T from);

    String convertWritingTarget(F to);


}
