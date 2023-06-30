package io.daobab.model;

import io.daobab.query.marker.ColumnOrQuery;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface Column<E extends Entity, F, R extends RelatedTo> extends Field<E, F, R>, ColumnOrQuery<E, F, R> {

    String getColumnName();

    @Override
    default <T extends Entity> Column<T, F, R> transformTo(T entity) {
        final String cn = getColumnName();
        final String fn = getFieldName();
        final Class<F> fieldClazz = getFieldClass();
        final F value = getValue((R) entity);
        return new Column<T, F, R>() {

            @Override
            public String getColumnName() {
                return cn;
            }

            @Override
            public String getFieldName() {
                return fn;
            }

            @Override
            public Class<F> getFieldClass() {
                return fieldClazz;
            }

            @Override
            public F getValue(R entity) {
                return value;
            }

            @Override
            public R setValue(R entity, F value) {
                return this.setValue(entity, value);
            }

            @Override
            public T getInstance() {
                return entity;
            }
        };
    }

    default Column<E, F, R> transformTo(String newName) {
        final String cn = getColumnName();
        final String fn = newName;
        final E entity = getInstance();
        final Class<F> fieldClazz = getFieldClass();
        return new Column<E, F, R>() {

            @Override
            public String getColumnName() {
                return cn;
            }

            @Override
            public String getFieldName() {
                return fn;
            }

            @Override
            public Class<F> getFieldClass() {
                return fieldClazz;
            }

            @Override
            public F getValue(R entity) {
                return this.getValue(entity);
            }

            @Override
            public R setValue(R entity, F value) {
                return this.setValue(entity, value);
            }

            @Override
            public E getInstance() {
                return entity;
            }
        };
    }

    default boolean equalsColumn(Column<?, ?, ?> another) {
        return
                equalsField(another)
                        && getColumnName().equals(another.getColumnName());

    }


}
