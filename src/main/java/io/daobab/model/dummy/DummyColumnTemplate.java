package io.daobab.model.dummy;

import io.daobab.model.Column;
import io.daobab.model.Dual;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;

import java.util.Date;

@SuppressWarnings({"rawtypes", "unchecked"})
public interface DummyColumnTemplate {

    static Column<Dual, String, RelatedTo> dummyColumn(String name) {
        return createDummyColumn(new Dual(), String.class, name);
    }

    static Column<Dual, Date, RelatedTo> dummyDateColumn(String name) {
        return createDummyColumn(new Dual(), Date.class, name);
    }

    static <E extends Entity> Column<E, Date, RelatedTo> dummyEntityDateColumn(E entity, String name) {
        return createDummyColumn(entity, Date.class, name);
    }

    static <F> Column<Dual, F, RelatedTo> dummyColumn(String name, Class<F> clazz) {
        return createDummyColumn(new Dual(), clazz, name);
    }


    static <E extends Entity, F, R extends RelatedTo<E>> Column<E, F, R> createDummyColumn(E entity, Class<F> type, String name) {
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
            public Object getValue(RelatedTo entity) {
                return dummyEntity;
            }

            @Override
            public RelatedTo setValue(RelatedTo entity, Object value) {
                return entity;
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
