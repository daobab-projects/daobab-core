package io.daobab.model.dummy;

import io.daobab.model.Column;
import io.daobab.model.Dual;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;

import java.util.Date;

@SuppressWarnings({"rawtypes", "unchecked"})
public interface DummyColumnTemplate {

    static Column<Dual, String, EntityRelation> dummyColumn(String name) {
        return createDummyColumn(new Dual(), String.class, name);
    }

    static Column<Dual, Date, EntityRelation> dummyDateColumn(String name) {
        return createDummyColumn(new Dual(), Date.class, name);
    }

    static <E extends Entity> Column<E, Date, EntityRelation> dummyEntityDateColumn(E entity, String name) {
        return createDummyColumn(entity, Date.class, name);
    }

    static <F> Column<Dual, F, EntityRelation> dummyColumn(String name, Class<F> clazz) {
        return createDummyColumn(new Dual(), clazz, name);
    }


    static <E extends Entity, F, R extends EntityRelation<E>> Column<E, F, R> createDummyColumn(E entity, Class<F> type, String name) {
        return new Column<E, F, R>() {

            private final E dummyEntity = entity;
            private final Class<F> dummyType = type;
            private final String dummyName = name;

            @Override
            public String getFieldName() {
                return dummyName;
            }

            @Override
            public Class getFieldClass() {
                return dummyType;
            }

            @Override
            public Object getValue(EntityRelation entity) {
                return dummyEntity;
            }

            @Override
            public void setValue(EntityRelation entity, Object value) {

            }

            @Override
            public E getInstance() {
                return dummyEntity;
            }

            @Override
            public String getColumnName() {
                return dummyName;
            }
        };
    }
}
