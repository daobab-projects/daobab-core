package io.daobab.target.database;


import io.daobab.creation.TypeIntroducer;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;

public abstract class DatabaseTypeIntroducer<T, D> extends TypeIntroducer<T> implements DatabaseTypeConverter<D, T> {


}
