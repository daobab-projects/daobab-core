package io.daobab.model.dummy;

import io.daobab.model.Column;
import io.daobab.model.Dual;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;

public interface DummyColumnTemplate {

    static Column<Dual, String, EntityRelation> dummyColumn(String name) {
        return createDummyColumn(new Dual(), String.class, name);
    }


    static <E extends Entity, F, R extends EntityRelation<E>> Column<E, F, R> createDummyColumn(E entity, Class<F> type, String name) {
        return new Column<E, F, R>() {

            private E dummyEntity = entity;
            private Class<F> dummyType = type;
            private String dummyName = name;

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
