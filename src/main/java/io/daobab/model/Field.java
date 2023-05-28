package io.daobab.model;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface Field<E extends Entity, F, R extends EntityRelation> {

    String getFieldName();

    default String getEntityName() {
        return getInstance().getEntityName();
    }

    Class getFieldClass();

    default Class getInnerTypeClass() {
        return null;
    }

    default Class<E> getEntityClass() {
        return (Class<E>) getInstance().getClass();
    }

    F getValue(R entity);

    void setValue(R entity, F value);


    E getInstance();

    default F getThisValue() {
        R te = (R) getInstance();
        if (te == null) return null;
        return getValue(te);
    }

    default F getValueOf(R value) {
        return this.getValue(value);
    }

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
            public void setValue(R entity, F value) {
                this.setValue(entity, value);
            }

            @Override
            public T getInstance() {
                return entity;
            }
        };
    }

    default boolean equalsField(Field<?,?,?> another) {
        return
                getEntityName().equals(another.getEntityName())
                        && getEntityClass().equals(another.getEntityClass())
                        && getFieldName().equals(another.getFieldName());
    }


}
