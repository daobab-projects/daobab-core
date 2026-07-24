package io.daobab.model;

import io.daobab.query.marker.ColumnOrQuery;

/**
 * A named database column of an entity: a {@link Field} that additionally knows its {@link #getColumnName()
 * database column name} and can stand as a query expression ({@link ColumnOrQuery}). The shared column
 * interfaces the generator and the annotation processor emit extend this - the same interface being reused
 * across every entity that has a column of that name and type is the central Daobab idea.
 *
 * @param <E> the owning entity
 * @param <F> the field value type
 * @param <R> the relation the value is read from
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("rawtypes")
public interface Column<E extends Entity, F, R extends RelatedTo> extends Field<E, F, R>, ColumnOrQuery<E, F, R> {

    /**
     * The database column name.
     */
    String getColumnName();

    /** This column re-homed onto {@code entity}, carrying its current value - to compare columns across tables. */
    @SuppressWarnings("unchecked")
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

    /** Whether another column has the same database column name. */
    default boolean equalsColumn(Column<?, ?, ?> another) {
        return getColumnName().equals(another.getColumnName());
    }


}
