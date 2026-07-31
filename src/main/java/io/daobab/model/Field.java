package io.daobab.model;


import io.daobab.converter.json.JsonConverter;

/**
 * A typed accessor to one value of an entity - the abstraction behind {@link Column}. It knows its field name
 * and type and reads/writes the value on an entity instance. {@link #transformTo(Entity)} re-homes the accessor
 * onto another entity carrying the same field, which is how cross-table join and where conditions are built.
 *
 * @param <E> the owning entity
 * @param <F> the field value type
 * @param <R> the relation the value is read from
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("rawtypes")
public interface Field<E extends Entity, F, R extends RelatedTo> {

    /**
     * The field (logical) name.
     */
    String getFieldName();


    /** The field value type. */
    @SuppressWarnings("rawtypes")
    Class getFieldClass();

    /** The element type when the field is a collection, or {@code null} otherwise. */
    @SuppressWarnings("rawtypes")
    default Class getInnerTypeClass() {
        return null;
    }

    /** The owning entity's class. */
    @SuppressWarnings("unchecked")
    default Class<E> entityClass() {
        return (Class<E>) getInstance().getClass();
    }

    /** Reads the value from the given entity. */
    F getValue(R entity);

    /** Writes the value into the given entity, returning it. */
    R setValue(R entity, F value);


    /** The entity instance this field is bound to. */
    E getInstance();

    default JsonConverter<F> getJsonConverter() {
        return null;
    }

    /** The value on the field's own {@link #getInstance() instance}, or {@code null} when there is none. */
    @SuppressWarnings("unchecked")
    default F getThisValue() {
        R te = (R) getInstance();
        if (te == null) return null;
        return getValue(te);
    }

    /** Reads the value from the given entity (alias of {@link #getValue(RelatedTo)}). */
    default F getValueOf(R value) {
        return this.getValue(value);
    }

    /** The same field re-homed onto another entity {@code entity} (used to compare columns across tables). */
    default <T extends Entity> Field<T, F, R> transformTo(T entity) {
        final String fn = getFieldName();
        return new Field<T, F, R>() {


            @Override
            public String getFieldName() {
                return fn;
            }

            @Override
            public Class<F> getFieldClass() {
                return this.getFieldClass();
            }

            @Override
            public Class<F> getInnerTypeClass() {
                return this.getInnerTypeClass();
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
            public T getInstance() {
                return entity;
            }
        };
    }

    /** Whether another field has the same field name. */
    default boolean equalsField(Field<?, ?, ?> another) {
        return getFieldName().equals(another.getFieldName());
    }


    /** Whether another field has the same entity class and field name. */
    default boolean equalsFieldAndEntity(Field<?, ?, ?> another) {
        return entityClass().equals(another.entityClass())
                && getFieldName().equals(another.getFieldName());
    }


}
